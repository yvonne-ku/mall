
package com.noinch.mall.biz.customer.user.application.service.impl;

import cn.hutool.core.util.StrUtil;
import com.noinch.mall.biz.customer.user.domain.dto.GeetestRespDTO;
import com.noinch.mall.biz.customer.user.domain.service.GeetestService;
import com.noinch.mall.biz.customer.user.domain.aggregate.CustomerUser;
import com.noinch.mall.biz.customer.user.domain.dp.CustomerUserAccount;
import com.noinch.mall.biz.customer.user.domain.dp.CustomerUserName;
import com.noinch.mall.biz.customer.user.domain.dp.CustomerUserPassword;
import com.noinch.mall.biz.customer.user.domain.dp.CustomerUserPhone;
import com.noinch.mall.biz.customer.user.domain.repository.CustomerUserRepository;
import com.noinch.mall.springboot.starter.convention.errorcode.BaseErrorCode;
import com.noinch.mall.springboot.starter.convention.exception.ClientException;
import lombok.AllArgsConstructor;
import com.noinch.mall.biz.customer.user.application.req.UserLoginCommand;
import com.noinch.mall.biz.customer.user.application.req.UserRegisterCommand;
import com.noinch.mall.biz.customer.user.application.req.UserVerifyCodeCommand;
import com.noinch.mall.biz.customer.user.application.resp.UserLoginRespDTO;
import com.noinch.mall.biz.customer.user.application.resp.UserRegisterRespDTO;
import com.noinch.mall.biz.customer.user.application.service.CustomerUserService;
import com.noinch.mall.springboot.starter.cache.DistributedCache;
import com.noinch.mall.springboot.starter.designpattern.strategy.AbstractStrategyChoose;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * C 端用户接口
*
*/
@Slf4j
@Service
@AllArgsConstructor
public class CustomerUserServiceImpl implements CustomerUserService {

    private final AbstractStrategyChoose abstractStrategyChoose;
    private final DistributedCache distributedCache;
    private final GeetestService geetestService;
    private final CustomerUserRepository customerUserRepository;

    @Override
    public void verifyCodeSend(UserVerifyCodeCommand requestParam) {
        String mark = "send_verify_code_" + requestParam.getType() + "_by_" + requestParam.getPlatform();
        abstractStrategyChoose.chooseAndExecute(mark, requestParam);
    }
    
    @Override
    public UserRegisterRespDTO register(UserRegisterCommand requestParam) {
        // 1. 极验验证码二次验证
        boolean geetestResult = geetestService.verifyGeetest(
                requestParam.getCaptchaId(),
                requestParam.getLotNumber(),
                requestParam.getPassToken(),
                requestParam.getGenTime(),
                requestParam.getCaptchaOutput()
        );
        if (!geetestResult) {
            throw new ClientException(BaseErrorCode.GEETEST_ERROR);
        }

        // 2. 用户名唯一性
        if (customerUserRepository.findByUsername(requestParam.getUsername())) {
            throw new ClientException(BaseErrorCode.USER_NAME_EXIST_ERROR);
        }
        // 3. 手机号唯一性
        if (customerUserRepository.findByPhone(requestParam.getPhone())) {
            throw new ClientException(BaseErrorCode.PHONE_EXIST_ERROR);
        }
        // 4. 验证码校验
        String mark = "check_verify_code_register_by_" + requestParam.getPlatform();
        if (! (Boolean) abstractStrategyChoose.chooseAndExecuteResp(mark, requestParam)) {
            throw new ClientException(BaseErrorCode.PHONE_VERIFY_CODE_ERROR);
        }
        // 5. 注册到数据库
        CustomerUser customerUser = CustomerUser.builder()
                .username(new CustomerUserName(requestParam.getUsername()))
                .phone(new CustomerUserPhone(requestParam.getPhone()))
                .password(new CustomerUserPassword(requestParam.getPassword()))
                .account(new CustomerUserAccount(generateByUuid()))
                .build();
        CustomerUser res = customerUserRepository.register(customerUser);
        // 5. 返回注册自动生成的 customerUserId 和 account
        return UserRegisterRespDTO.builder()
                .customerUserId(res.getCustomerUserId())
                .account(res.getAccount())
                .build();
    }
    
    @Override
    public UserLoginRespDTO login(UserLoginCommand requestParam) {
        // 1. 极验验证码二次验证
        boolean geetestResult = geetestService.verifyGeetest(
                requestParam.getCaptchaId(),
                requestParam.getLotNumber(),
                requestParam.getPassToken(),
                requestParam.getGenTime(),
                requestParam.getCaptchaOutput()
                );
        if (!geetestResult) {
            throw new ClientException(BaseErrorCode.GEETEST_ERROR);
        }

        // 2. 登录方式验证，成功会返回 accessToken，存入 distributedCache 方便 checkLogin
        String mark = "login_by_" + requestParam.getType();
        UserLoginRespDTO userLoginRespDTO = abstractStrategyChoose.chooseAndExecuteResp(mark, requestParam);
        distributedCache.put("accessToken:" + userLoginRespDTO.getAccessToken(), userLoginRespDTO, 3000, TimeUnit.MINUTES);
        return userLoginRespDTO;
    }
    
    @Override
    public UserLoginRespDTO checkLogin(String accessToken) {
        return distributedCache.get("accessToken:" + accessToken, UserLoginRespDTO.class);
    }
    
    @Override
    public void logout(String accessToken) {
        if (StrUtil.isNotBlank(accessToken)) {
            distributedCache.delete("accessToken:" + accessToken);
        }
    }

    private String generateByUuid() {
        // 取前8位，加上时间戳后6位
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String uuidPart = uuid.substring(0, 8);
        String timePart = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("HHmmss")
        );
        return "ACC" + uuidPart + timePart;
    }
}

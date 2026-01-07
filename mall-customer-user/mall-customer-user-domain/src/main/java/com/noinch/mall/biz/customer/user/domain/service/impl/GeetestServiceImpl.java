package com.noinch.mall.biz.customer.user.domain.service.impl;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.noinch.mall.biz.customer.user.domain.service.GeetestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * 极验验证码服务（基于官方SDK重写）
 */
@Slf4j
@Service
@RequiredArgsConstructor
class GeetestServiceImpl implements GeetestService {

    /**
     * 极验验证码服务域名
     */
    private static final String domain = "http://gcaptcha4.geetest.com";

    @Value("${geetest.captcha-id}")
    private String captchaId = "";

    @Value("${geetest.captcha-key}")
    private String captchaKey = "";

    @Override
    public boolean verifyGeetest(String captchaId, String lotNumber, String passToken, String genTime, String captchaOutput) {

        // 1.生成签名
        String signToken = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, captchaKey).hmacHex(lotNumber);

        // 2.上传校验参数到极验二次验证接口, 校验用户验证状态
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("lot_number", lotNumber);
        queryParams.add("captcha_output", captchaOutput);
        queryParams.add("pass_token", passToken);
        queryParams.add("gen_time", genTime);
        queryParams.add("sign_token", signToken);
        String url = String.format(domain + "/validate" + "?captcha_id=%s", captchaId);
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        JSONObject jsonObject = new JSONObject();
        try {
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(queryParams, headers);
            ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
            String resBody = response.getBody();
            jsonObject = new JSONObject(resBody);
        } catch (Exception e){
            jsonObject.put("result","fail");
            jsonObject.put("reason","request geetest api fail");
        }

        return jsonObject.getString("result").equals("success");
    }
}
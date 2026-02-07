package com.noinch.mall.biz.pay.infrastructure.handler;


import cn.hutool.core.text.StrBuilder;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.noinch.mall.biz.pay.domain.base.AliPayRequest;
import com.noinch.mall.biz.pay.domain.base.PayRequest;
import com.noinch.mall.biz.pay.domain.base.PayResponse;
import com.noinch.mall.biz.pay.domain.common.PayChannelEnum;
import com.noinch.mall.biz.pay.domain.common.PayTradeTypeEnum;
import com.noinch.mall.biz.pay.infrastructure.config.properties.AliPayProperties;
import com.noinch.mall.biz.pay.infrastructure.handler.base.AbstractPayHandler;
import com.noinch.mall.springboot.starter.designpattern.strategy.AbstractExecuteStrategy;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;


/**
 * 阿里支付组件
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public final class AliPayQrCodeHandler extends AbstractPayHandler implements AbstractExecuteStrategy<PayRequest, PayResponse> {

    private final AliPayProperties aliPayProperties;
    private final AlipayClient alipayClient;

    @Override
    @SneakyThrows(value = AlipayApiException.class)
    public PayResponse pay(PayRequest payRequest) {

        AliPayRequest aliPayRequest = payRequest.getAliPayRequest();

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setOutTradeNo(aliPayRequest.getOrderRequestId());
        model.setTotalAmount(aliPayRequest.getTotalAmount());
        model.setSubject(aliPayRequest.getSubject());
        model.setProductCode("QR_CODE_OFFLINE");
        request.setNotifyUrl(aliPayProperties.getNotifyUrl());
        request.setBizModel(model);
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        log.info("发起支付宝支付，订单号：{}，子订单号：{}，订单请求号：{}，订单金额：{} \n调用支付返回：\n\n{}\n",
                aliPayRequest.getOrderSn(),
                aliPayRequest.getOutOrderSn(),
                aliPayRequest.getOrderRequestId(),
                aliPayRequest.getTotalAmount(),
                response.getBody());

        // 生成二维码图片并转为Base64直接返回给前端
        String qrCodeUrl = response.getQrCode();
        String qrCodeBase64 = generateQrCodeBase64(qrCodeUrl, 300, 300);
        return new PayResponse(qrCodeBase64);
    }

    private String generateQrCodeBase64(String content, int width, int height) {
        try {
            BufferedImage image = QrCodeUtil.generate(content, width, height);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", os);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("生成二维码失败", e);
        }
    }

    @Override
    public String mark() {
        return StrBuilder.create()
                .append(PayChannelEnum.ALI_PAY.name())
                .append("_")
                .append(PayTradeTypeEnum.NATIVE_QR_CODE.name())
                .toString();
    }

    @Override
    public PayResponse executeResp(PayRequest requestParam) {
        return pay(requestParam);
    }

}


package com.noinch.mall.biz.pay.domain.common;

/**
 * 交易环境枚举
 *
 */
public enum PayTradeTypeEnum {

    /**
     * 原生支付，一般是PC网站或线下场景生成二维码扫码支付
     */
    NATIVE,

    /**
     * JavaScript API 支付，一般是微信公众号、小程序
     */
    JSAPI,

     /**
     * 移动网页支付，一般是移动端浏览器打开支付链接支付
     */
    MWEB,

     /**
     * 去中心化应用支付，区块链/加密货币支付，智能合约支付
     */
    DAPP
}

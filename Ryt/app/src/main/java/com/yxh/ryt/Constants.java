package com.yxh.ryt;

public class Constants {

    //网络请求基地址
    //public static final String BASE_PATH="http://192.168.1.69:8001/app/";
    public static final String BASE_PATH="http://j.efeiyi.com:8080/app-wikiServer/app/";
    //融艺投APP_KEY
    public static final String APP_KEY="BL2QEuXUXNoGbNeHObD4EzlX+KuGc70U";
    //微信APP_ID
    public static final String APP_ID = "wxc7f08565e496134b";
    public static final String APP_SECRET = "9a4bf578e18230df0e6d4074d94fc40d";
    // 微信请求基本路径
    public static final String WX_BASE_PATH = "https://api.weixin.qq.com/sns/";
    // 微信登陆请求路径
    public static final String WX_LOGIN_PATH = "http://192.168.1.212";
    //商户号
    public static final String MCH_ID = "1243015702";
    // 微信登陆成功广播ACTION
    public static final String WX_LOGIN_ACTION = "wx_login_success";
    public static final String WX_PAY_ACTION_SUCCESS = "wx_pay_success";
    public static final String WX_PAY_ACTION_FAILURE = "wx_pay_failure";
    public static final String WX_INSTALL_ACTION = "wx_install_action";
    public static final String WX_NOTINSTALL_ACTION = "wx_notinstall_action";
    public static final String[] INDEX_TITLE = new String[] { "融资", "创作", "拍卖" };
    public static final String[] PAIHANG_TITLE = new String[] { "投资者排行", "艺术家排行" };
    public static final int pageSize=20;
}

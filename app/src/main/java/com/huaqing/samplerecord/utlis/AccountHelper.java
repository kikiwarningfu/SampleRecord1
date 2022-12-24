package com.huaqing.samplerecord.utlis;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;


public class AccountHelper {
    private static final String SP_IS_PERMISSION_SYTATUS = "is_permission_status";
    private static final String SP_IS_FIRST_OPEN = "is_first_opens";
    private static final String SP_CURRENT_TIME = "currentTime";
    private static final String SP_NAME = "AccountHelper";
    private static final String SP_TOKEN = "token";
    private static final String SP_NICKNAME = "nickname";
    private static final String SP_AVATAR = "avatar";
    private static final String SP_GENDER = "gender";
    private static final String SP_USERID = "userid";
    private static final String SP_MOBILE = "SP_MOBILE";
    private static final String SP_SUPPLIERID = "supplierid";//店铺id
    private static final String SP_CITY = "city";//店铺id
    private static final String SP_LOCAL_ADDRESS = "local_address";//店铺id
    //用户身份 0未登录 1普通用户 2医生 3咨询师 4医院
    private static final String SP_IDENTITY = "identity";
    //医院->座机电话
    private static final String SP_TELEPHONE = "telephone";
    //4中身份用户的 城市id 城市name 经纬度
    private static final String SP_CITY_ID = "city_id";
    //三方登录的信息
    private static final String SP_OPEN_ID = "open_id";
    private static final String SP_ICONURL = "iconurl";
    private static final String SP_UID = "uid";

    private static final String SP_CITY_NAME = "city_name";
    private static final String SP_LATITUDE = "latitude";
    private static final String SP_LONGITUDE = "longitude";

    private static final String SP_YUNXIN_ACCID = "yunxin_accid";
    private static final String SP_YUNXIN_TOKEN = "yunxin_token";
    public static final String IP_ADDRESS = "ip_address";
    private static final String SP_ONLINE_STATUS = "SP_ONLINE_STATUS";

    public static final String SP_MATCH_MONEY = "sp_match_money";
    public static final String SP_MATCH_PAY_TYPE = "sp_match_pay_type";
    private static final AccountHelper INSTANCE = new AccountHelper();
    public static final String IS_NEED_UPDATE = "is_need_update";
    private static final String SP_IS_FIRST_APP = "is_first_app";

    public static AccountHelper getInstance() {
        return INSTANCE;
    }

    private AccountHelper() {

    }

    public static void setIsNeedUpdate(String isNeedUpdate) {
        SpUtlis.getInstance(SP_NAME).put(IS_NEED_UPDATE, isNeedUpdate);
    }

    public static String getIsNeedUpdate() {
        return SpUtlis.getInstance(SP_NAME).getString(IS_NEED_UPDATE);
    }

    public static void setSpLocalAddress(String isloalAddress) {
        SpUtlis.getInstance(SP_NAME).put(SP_LOCAL_ADDRESS, isloalAddress);
    }

    public static String getSpLocalAddress() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_LOCAL_ADDRESS);
    }


    public static void setSpIsFirstOpen(String isFirstOpen) {
        SpUtlis.getInstance(SP_NAME).put(SP_IS_FIRST_OPEN, isFirstOpen);
    }

    public static void setInteger(String name, int integer) {
        SpUtlis.getInstance(SP_NAME).put(name, integer);
    }

    public static int getInteger(String name) {
        return SpUtlis.getInstance(SP_NAME).getInt(name, 0);
    }

    public static String getSpIsFirstOpen() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_IS_FIRST_OPEN);
    }

    public static void setisPermissionApp(boolean isPermissionStatus) {
        SpUtlis.getInstance(SP_NAME).put(SP_IS_PERMISSION_SYTATUS, isPermissionStatus);
    }

    public static String getSpIsFirstApp() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_IS_FIRST_APP);
    }

    public static void setSpIsFirstApp(String isFirstApp) {
        SpUtlis.getInstance(SP_NAME).put(SP_IS_FIRST_APP, isFirstApp);
    }


    public static boolean isLogin() {
        return !TextUtils.isEmpty(getToken());
    }

    public static void login(String token, long userId, String mobile, String gender, String avatar, String nickname) {
        SpUtlis.getInstance(SP_NAME).put(SP_TOKEN, token);
        SpUtlis.getInstance(SP_NAME).put(SP_USERID, userId);
        SpUtlis.getInstance(SP_NAME).put(SP_MOBILE, mobile);
        SpUtlis.getInstance(SP_NAME).put(SP_GENDER, gender);
        SpUtlis.getInstance(SP_NAME).put(SP_AVATAR, avatar);
        SpUtlis.getInstance(SP_NAME).put(SP_NICKNAME, nickname);
    }

    public static void saveOther(String open_id, String iconurl, String uid) {
        SpUtlis.getInstance(SP_NAME).put(SP_OPEN_ID, open_id);
        SpUtlis.getInstance(SP_NAME).put(SP_ICONURL, iconurl);
        SpUtlis.getInstance(SP_NAME).put(SP_UID, uid);
    }

    public static void setCity(String city_id, String city_name, String latitude, String longitude) {
        SpUtlis.getInstance(SP_NAME).put(SP_CITY_ID, city_id);
        SpUtlis.getInstance(SP_NAME).put(SP_CITY_NAME, city_name);
        SpUtlis.getInstance(SP_NAME).put(SP_LATITUDE, latitude);
        SpUtlis.getInstance(SP_NAME).put(SP_LONGITUDE, longitude);
    }

    public static void setCity(String latitude, String longitude) {
        SpUtlis.getInstance(SP_NAME).put(SP_LATITUDE, latitude);
        SpUtlis.getInstance(SP_NAME).put(SP_LONGITUDE, longitude);
    }

    public static String getIpAddress() {
        return SpUtlis.getInstance(SP_NAME).getString(IP_ADDRESS).replace(" ", "");
    }

    public static void setIpAddress(String IP_ADDRESSs) {
        SpUtlis.getInstance(SP_NAME).put(IP_ADDRESS, IP_ADDRESSs);
    }

    public static String getToken() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_TOKEN);
    }

    public static void setToken(String token) {
        SpUtlis.getInstance(SP_NAME).put(SP_TOKEN, token);
    }

    public static String getuserId() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_USERID);
    }

    public static void setUserId(String userId) {
        SpUtlis.getInstance(SP_NAME).put(SP_USERID, userId);
    }


    public static String getSpMatchMoney() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_MATCH_MONEY);
    }

    public static void setSpMatchMoney(String matchMoney) {
        SpUtlis.getInstance(SP_NAME).put(SP_MATCH_MONEY, matchMoney);
    }

    public static String getSpMatchPayType() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_MATCH_PAY_TYPE);
    }

    public static void setSpMatchPayType(String matchPayType) {
        SpUtlis.getInstance(SP_NAME).put(SP_MATCH_PAY_TYPE, matchPayType);
    }

    public static String getCity_Id() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_CITY_ID);
    }

    public static String getCity_Name() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_CITY_NAME);
    }


    public static String getGender() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_GENDER);
    }

    public static String getAvatar() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_AVATAR);
    }

    public static String getTelephone() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_TELEPHONE);
    }

    public static String getOpenId() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_OPEN_ID);
    }

    public static String getIconurl() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_ICONURL);
    }

    public static String getUid() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_UID);
    }

    public static void setTelephone(String telephone) {
        SpUtlis.getInstance(SP_NAME).put(SP_TELEPHONE, telephone);
    }

    public static void setAvatar(String avatar) {
        SpUtlis.getInstance(SP_NAME).put(SP_AVATAR, avatar);
    }

    public static String getNickname() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_NICKNAME);
    }

    public static void setNickname(String nickname) {
        SpUtlis.getInstance(SP_NAME).put(SP_NICKNAME, nickname);
    }

    public static void setGender(String nickname) {
        SpUtlis.getInstance(SP_NAME).put(SP_GENDER, nickname);
    }

    public static String getMobile() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_MOBILE);
    }

    public static void setMobile(String mobile) {
        SpUtlis.getInstance(SP_NAME).put(SP_MOBILE, mobile);
    }

    public static void setSupplierId(String supplierId) {
        SpUtlis.getInstance(SP_NAME).put(SP_SUPPLIERID, supplierId);
    }

    public static String getSupplierId() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_SUPPLIERID);
    }

    public static void setCityName(String cityName) {
        SpUtlis.getInstance(SP_NAME).put(SP_CITY, cityName);
    }

    public static String getCityName() {
        return SpUtlis.getInstance(SP_NAME).getString(SP_CITY);
    }

    public static void setOLStatus(int status) {
        SpUtlis.getInstance(SP_NAME).put(SP_ONLINE_STATUS, status);
    }

    public static int getOLStatus() {
        return SpUtlis.getInstance(SP_NAME).getInt(SP_ONLINE_STATUS, 0);
    }

    public static void logout() {
        login(null, 0, null, null, null, null);
        setSupplierId(null);
        setCityName(null);
        setCity(null, null, null, null);
        setIdentity(0);
        setOLStatus(0);
        setYunxinAccid("");
        setYunxinToken("");
        if (getInstance().onLogoutListener != null) {
            getInstance().onLogoutListener.onLogout();
        }
    }

    public static boolean shouldLogin(Context context) {
        if (!isLogin()) {
            context.startActivity(new Intent(context.getPackageName() + ".com.action.login"));
            return true;
        }
        return false;
    }

    public static long getCurrentTime() {
        return SpUtlis.getInstance(SP_NAME).getLong(SP_CURRENT_TIME, 1);
    }

    public static void setSpCurrentTime(long currentTime) {
        SpUtlis.getInstance(SP_NAME).put(SP_CURRENT_TIME, currentTime);
    }

    public static int getIdentity() {
        return SpUtlis.getInstance(SP_NAME).getInt(SP_IDENTITY, 1);
    }

    public static void setIdentity(int identity) {
        SpUtlis.getInstance(SP_NAME).put(SP_IDENTITY, identity);
    }

    public static void setYunxinAccid(String accid) {
        SpUtlis.getInstance(SP_NAME).put(SP_YUNXIN_ACCID, accid);
    }

    public static String getYunxinAccid() {
        return SpUtlis.getInstance(SP_NAME)
                .getString(SP_YUNXIN_ACCID);
    }

    public static void setYunxinToken(String yunxinToken) {
        SpUtlis.getInstance(SP_NAME).put(SP_YUNXIN_TOKEN, yunxinToken);
    }

    public static String getYunxinToken() {
        return SpUtlis.getInstance(SP_NAME)
                .getString(SP_YUNXIN_TOKEN);
    }

    private OnLogoutListener onLogoutListener;

    public void setOnLogoutListener(OnLogoutListener onLogoutListener) {
        this.onLogoutListener = onLogoutListener;
    }

    public interface OnLogoutListener {
        void onLogout();
    }
}
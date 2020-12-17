package net.renfei.discuz.ucenter.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.renfei.discuz.ucenter.client.Client;

/**
 * ================================================
 * Discuz! Ucenter API for JAVA
 * ================================================
 * 此类用来同步UC Server发出的操作指令
 * 可以根据业务需要添加相应的执行代码
 * <p>
 * 更多信息：http://code.google.com/p/discuz-ucenter-api-for-java
 * 原作者：梁平 (no_ten@163.com)
 * 创建时间：2009-2-20
 * 更多信息：https://github.com/renfei/discuz-ucenter-api-for-java
 * 修改者：任霏 (i@renfei.net)
 * 修改时间：2020-12-17
 */
public class UCClient {
    public static boolean IN_DISCUZ = true;
    public static String UC_CLIENT_VERSION = "1.6.0";            //note UCenter 版本标识
    public static String UC_CLIENT_RELEASE = "20170101";

    public static boolean API_DELETEUSER = true;                //note 用户删除 API 接口开关
    public static boolean API_RENAMEUSER = true;                //note 用户改名 API 接口开关
    public static boolean API_GETTAG = true;                    //note 获取标签 API 接口开关
    public static boolean API_SYNLOGIN = true;                //note 同步登录 API 接口开关
    public static boolean API_SYNLOGOUT = true;                //note 同步登出 API 接口开关
    public static boolean API_UPDATEPW = true;                //note 更改用户密码 开关
    public static boolean API_UPDATEBADWORDS = true;            //note 更新关键字列表 开关
    public static boolean API_UPDATEHOSTS = true;                //note 更新域名解析缓存 开关
    public static boolean API_UPDATEAPPS = true;                //note 更新应用列表 开关
    public static boolean API_UPDATECLIENT = true;            //note 更新客户端缓存 开关
    public static boolean API_UPDATECREDIT = true;            //note 更新用户积分 开关
    public static boolean API_GETCREDITSETTINGS = true;        //note 向 UCenter 提供积分设置 开关
    public static boolean API_GETCREDIT = true;                //note 获取用户的某项积分 开关
    public static boolean API_UPDATECREDITSETTINGS = true;    //note 更新应用积分设置 开关

    public static String API_RETURN_SUCCEED = "1";
    public static String API_RETURN_FAILED = "-1";
    public static String API_RETURN_FORBIDDEN = "-2";

    /**
     * 执行具体的Action
     * 所有服务器发出的参数均可通过get来获得。
     * 注意： request本身是不能得到参数值的。
     *
     * @param request
     * @param response
     * @return 操作状态或操作结果
     */
    public String doAnswer(Client client, HttpServletRequest request, HttpServletResponse response) {
        //处理
        String code = request.getParameter("code");
        if (code == null) {
            return API_RETURN_FAILED;
        }

        Map<String, String> getMap = new HashMap<String, String>();
        code = client.ucAuthcode(code, "DECODE");
        parseStr(code, getMap);

        if (getMap.isEmpty()) {
            return "Invalid Request";
        }
        if (time() - tolong(getMap.get("time")) > 3600) {
            return "Authracation has expiried";
        }

        String action = getMap.get("action");
        if (action == null) {
            return API_RETURN_FAILED;
        }

        if ("test".equals(action)) {

            return API_RETURN_SUCCEED;

        } else if ("deleteuser".equals(action)) {


            return API_RETURN_SUCCEED;

        } else if ("renameuser".equals(action)) {

            return API_RETURN_SUCCEED;

        } else if ("gettag".equals(action)) {

            if (!API_GETTAG) {
                return API_RETURN_FORBIDDEN;
            }

            //同步代码

            return API_RETURN_SUCCEED;


        } else if ("synlogin".equals(action)) {

            if (!API_SYNLOGIN) {
                return (API_RETURN_FORBIDDEN);
            }

            //note 同步登录 API 接口
            //obclean();
            response.addHeader("P3P", "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");

            int cookietime = 31536000;

            Cookie user = new Cookie("loginuser", getMap.get("username"));
            user.setMaxAge(cookietime);
            response.addCookie(user);

        } else if ("synlogout".equals(action)) {

            if (!API_SYNLOGOUT) {
                return (API_RETURN_FORBIDDEN);
            }

            //note 同步登出 API 接口
            //obclean();
            response.addHeader("P3P", " CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");

            //clearcookie();
            Cookie user = new Cookie("loginuser", "");
            user.setMaxAge(0);
            response.addCookie(user);

        } else if ("updateclient".equals(action)) {

            if (!API_UPDATECLIENT) {
                return API_RETURN_FORBIDDEN;
            }


            //同步代码

            return API_RETURN_SUCCEED;

        } else if ("updatepw".equals(action)) {

            if (!API_UPDATEPW) {
                return API_RETURN_FORBIDDEN;
            }

            //同步代码

            return API_RETURN_SUCCEED;

        } else if ("updatebadwords".equals(action)) {

            if (!API_UPDATEBADWORDS) {
                return API_RETURN_FORBIDDEN;
            }

            //同步代码

            return API_RETURN_SUCCEED;

        } else if ("updatehosts".equals(action)) {

            if (!API_UPDATEHOSTS) {
                return API_RETURN_FORBIDDEN;
            }


            return API_RETURN_SUCCEED;

        } else if ("updateapps".equals(action)) {

            if (!API_UPDATEAPPS) {
                return API_RETURN_FORBIDDEN;
            }


            return API_RETURN_SUCCEED;

        } else if ("updatecredit".equals(action)) {

            //if(!UPDATECREDIT ) return API_RETURN_FORBIDDEN;

            return API_RETURN_SUCCEED;

        } else if ("getcreditsettings".equals(action)) {

            //if(!GETCREDITSETTINGS ) return API_RETURN_FORBIDDEN;

            return "";//积分值

        } else if ("updatecreditsettings".equals(action)) {

            if (!API_UPDATECREDITSETTINGS) {
                return API_RETURN_FORBIDDEN;
            }


            //同步代码

            return API_RETURN_SUCCEED;

        } else {

            return (API_RETURN_FORBIDDEN);

        }
        return "";
    }

    private void parseStr(String str, Map<String, String> sets) {
        if (str == null || str.length() < 1) {
            return;
        }
        String[] ps = str.split("&");
        for (String p : ps) {
            String[] items = p.split("=");
            if (items.length == 2) {
                sets.put(items[0], items[1]);
            } else if (items.length == 1) {
                sets.put(items[0], "");
            }
        }
    }

    protected long time() {
        return System.currentTimeMillis() / 1000;
    }

    private static long tolong(Object s) {
        if (s != null) {
            String ss = s.toString().trim();
            if (ss.length() == 0) {
                return 0L;
            } else {
                return Long.parseLong(ss);
            }
        } else {
            return 0L;
        }
    }

}

package net.renfei.discuz.ucenter.client;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * ================================================
 * Discuz! Ucenter API for JAVA
 * ================================================
 * UC Client 通用调用该类来实现与UC Server之间的通信。
 * 该类实现与UC Server通信的所有接口函数
 * <p>
 * 更多信息：http://code.google.com/p/discuz-ucenter-api-for-java
 * 原作者：梁平 (no_ten@163.com)
 * 创建时间：2009-2-20
 * 更多信息：https://github.com/renfei/discuz-ucenter-api-for-java
 * 修改者：任霏 (i@renfei.net)
 * 修改时间：2021-03-04
 */
public class Client extends PHPFunctions {

    public static boolean IN_UC = true;
    public static String UC_IP;
    public static String UC_API;
    public static String UC_CONNECT;
    public static String UC_KEY;
    public static String UC_APPID;
    public static String UC_CLIENT_VERSION = "1.0";
    public static String UC_CLIENT_RELEASE = "20090212";
    public static String UC_ROOT = "";                          //note 用户中心客户端的根目录 UC_CLIENTROOT
    public static String UC_DATADIR = UC_ROOT + "./data/";      //note 用户中心的数据缓存目录
    public static String UC_DATAURL = "UC_API" + "/data";       //note 用户中心的数据 URL
    public static String UC_API_FUNC = "mysql".equals(UC_CONNECT) ? "uc_api_mysql" : "uc_api_post";
    public static String[] uc_controls = {};

    public Client(String ucApi, String ucIp, String ucKey, String ucAppId, String ucConnect) {
        UC_API = ucApi;
        UC_IP = ucIp;
        UC_KEY = ucKey;
        UC_APPID = ucAppId;
        UC_CONNECT = ucConnect;
    }

    protected String ucSerialize(String arr, int htmlon) {
        //return xml_serialize(arr, htmlon);
        return arr;
    }

    protected String ucUnserialize(String s) {
        //include_once UC_ROOT.'./lib/xml.class.php';
        //return xml_unserialize(s);
        return s;
    }

    protected String ucAddslashes(String string, int force, boolean strip) {
//		!defined('MAGIC_QUOTES_GPC') && define('MAGIC_QUOTES_GPC', get_magic_quotes_gpc());
//		if(!MAGIC_QUOTES_GPC || force) {
//			if(is_array(string)) {
//				foreach(string as key => val) {
//					string[key] = uc_addslashes(val, force, strip);
//				}
//			} else {
//				string = addslashes(strip ? stripslashes(string) : string);
//			}
//		}
        return string;
    }

    protected String daddslashes(String string, int force) {
        return ucAddslashes(string, force, false);
    }

    protected String ucStripslashes(String string) {
//		!defined('MAGIC_QUOTES_GPC') && define('MAGIC_QUOTES_GPC', get_magic_quotes_gpc());
//		if(MAGIC_QUOTES_GPC) {
//			return stripslashes(string);
//		} else {
        return string;
//		}
    }

    @Override
    public String ucApiPost(String module, String action, Map<String, Object> arg) {
        StringBuffer s = new StringBuffer();
        String sep = "";
        //foreach(arg as k => v) {
        for (String k : arg.keySet()) {
            //k = (k);
            Object v = arg.get(k);
            k = urlencode(k);

            if (v.getClass().isAssignableFrom(Map.class)) {
                String s2 = "";
                String sep2 = "";
                //foreach(v as k2 => v2) {
                for (String k2 : ((Map<String, Object>) v).keySet()) {
                    Object v2 = ((Map<String, Object>) v).get(k2);
                    k2 = urlencode(k2);
                    s2 += sep2 + "{" + k + "}[" + k2 + "]=" + urlencode(ucStripslashes(String.valueOf(v2)));
                    sep2 = "&";
                }
                s.append(sep).append(s2);
            } else {
                s.append(sep).append(k).append("=").append(urlencode(ucStripslashes(String.valueOf(v))));
            }
            sep = "&";
        }
        String postdata = ucApiRequestdata(module, action, s.toString(), "");
        return ucFopen2(UC_API + "/index.php", 500000, postdata, "", true, UC_IP, 20, true);
//        return ucFopen2(UC_API, 500000, postdata, "", true, UC_IP, 20, true);
    }


    /**
     * 构造发送给用户中心的请求数据
     *
     * @param module 请求的模块
     * @param action 请求的动作
     * @param arg    参数（会加密的方式传送）
     * @param extra  附加参数（传送时不加密）
     * @return string
     */
    protected String ucApiRequestdata(String module, String action, String arg, String extra) {
        String input = ucApiInput(arg);
        String post = "m=" + module + "&a=" + action + "&inajax=2&release=" + UC_CLIENT_RELEASE + "&input=" + input + "&appid=" + UC_APPID + extra;
        return post;
    }

    protected String ucApiUrl(String module, String action, String arg, String extra) {
        String url = UC_API + "/index.php?" + ucApiRequestdata(module, action, arg, extra);
        return url;
    }

    public String ucApiInput(String data) {
        //String s = data;
        //String s = urlencode(uc_authcode(data+"&agent="+md5(_SERVER["HTTP_USER_AGENT"])+"&time="+time(), "ENCODE", UC_KEY));
        //String s = urlencode(uc_authcode(data+"&agent="+md5("Java/1.5.0_01")+"&time="+time(), "ENCODE", UC_KEY));
        String s = urlencode(ucAuthcode(data + "&agent=" + md5("") + "&time=" + time(), "ENCODE", UC_KEY));
        return s;
    }

    /**
     * MYSQL 方式取指定的模块和动作的数据
     *
     * @param model  请求的模块
     * @param action 请求的动作
     * @param args   参数（会加密的方式传送）
     * @return mix
     */
    @Override
    public String ucApiMysql(String model, String action, Map args) {
//		global uc_controls;
//		if(empty(uc_controls[model])) {
//			include_once UC_ROOT.'./lib/db.class.php';
//			include_once UC_ROOT.'./model/base.php';
//			include_once UC_ROOT."./control/model.php";
//			eval("uc_controls['model'] = new {model}control();");
//		}
        if (action.charAt(0) != '_') {
//			args = uc_addslashes(args, 1, true);
//			action = "on"+action;
//			uc_controls[model]->input = args;
//			return uc_controls[model]->action(args);
            return null;
        } else {
            return "";
        }
    }

    /**
     * 字符串加密以及解密函数
     *
     * @param string    原文或者密文
     * @param operation 操作(ENCODE | DECODE), 默认为 DECODE
     * @return 处理后的 原文或者 经过 base64_encode 处理后的密文
     * @example a = authcode('abc', 'ENCODE', 'key');
     * b = authcode(a, 'DECODE', 'key');  // b(abc)
     * <p>
     * a = authcode('abc', 'ENCODE', 'key', 3600);
     * b = authcode('abc', 'DECODE', 'key'); // 在一个小时内，b(abc)，否则 b 为空
     */
    public String ucAuthcode(String string, String operation) {
        return ucAuthcode(string, operation, null);
    }

    /**
     * 字符串加密以及解密函数
     *
     * @param string    原文或者密文
     * @param operation 操作(ENCODE | DECODE), 默认为 DECODE
     * @param key       密钥
     * @return 处理后的 原文或者 经过 base64_encode 处理后的密文
     * @example a = authcode('abc', 'ENCODE', 'key');
     * b = authcode(a, 'DECODE', 'key');  // b(abc)
     * <p>
     * a = authcode('abc', 'ENCODE', 'key', 3600);
     * b = authcode('abc', 'DECODE', 'key'); // 在一个小时内，b(abc)，否则 b 为空
     */
    public String ucAuthcode(String string, String operation, String key) {
        return ucAuthcode(string, operation, key, 0);
    }

    /**
     * 字符串加密以及解密函数
     *
     * @param string    原文或者密文
     * @param operation 操作(ENCODE | DECODE), 默认为 DECODE
     * @param key       密钥
     * @param expiry    密文有效期, 加密时候有效， 单位 秒，0 为永久有效
     * @return 处理后的 原文或者 经过 base64_encode 处理后的密文
     * @example a = authcode('abc', 'ENCODE', 'key');
     * b = authcode(a, 'DECODE', 'key');  // b(abc)
     * <p>
     * a = authcode('abc', 'ENCODE', 'key', 3600);
     * b = authcode('abc', 'DECODE', 'key'); // 在一个小时内，b(abc)，否则 b 为空
     */
    public String ucAuthcode(String string, String operation, String key, int expiry) {

        int ckey_length = 4;    //note 随机密钥长度 取值 0-32;
        //note 加入随机密钥，可以令密文无任何规律，即便是原文和密钥完全相同，加密结果也会每次不同，增大破解难度。
        //note 取值越大，密文变动规律越大，密文变化 = 16 的 ckey_length 次方
        //note 当此值为 0 时，则不产生随机密钥

        key = md5(key != null ? key : UC_KEY);
        String keya = md5(substr(key, 0, 16));
        String keyb = md5(substr(key, 16, 16));
        String keyc = ckey_length > 0 ? (operation.equals("DECODE") ? substr(string, 0, ckey_length) : substr(md5(microTime()), -ckey_length)) : "";

        String cryptkey = keya + md5(keya + keyc);
        int key_length = cryptkey.length();

        string = operation.equals("DECODE") ? base64Decode(substr(string, ckey_length)) : sprintf("%010d", expiry > 0 ? expiry + time() : 0) + substr(md5(string + keyb), 0, 16) + string;
        int string_length = string.length();

        StringBuffer result1 = new StringBuffer();

        int[] box = new int[256];
        for (int i = 0; i < 256; i++) {
            box[i] = i;
        }

        int[] rndkey = new int[256];
        for (int i = 0; i <= 255; i++) {
            rndkey[i] = (int) cryptkey.charAt(i % key_length);
        }

        int j = 0;
        for (int i = 0; i < 256; i++) {
            j = (j + box[i] + rndkey[i]) % 256;
            int tmp = box[i];
            box[i] = box[j];
            box[j] = tmp;
        }

        j = 0;
        int a = 0;
        for (int i = 0; i < string_length; i++) {
            a = (a + 1) % 256;
            j = (j + box[a]) % 256;
            int tmp = box[a];
            box[a] = box[j];
            box[j] = tmp;

            result1.append((char) (((int) string.charAt(i)) ^ (box[(box[a] + box[j]) % 256])));

        }

        if (operation.equals("DECODE")) {
            String result = result1.toString();
            try {
                result = new String(result.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            } catch (Exception e) {
                result = result1.substring(0, result1.length());
            }
            if ((Integer.parseInt(substr(result, 0, 10)) == 0 || Long.parseLong(substr(result, 0, 10)) - time() > 0) && substr(result, 10, 16).equals(substr(md5(substr(result, 26) + keyb), 0, 16))) {
                return substr(result, 26);
            } else {
                return "";
            }
        } else {
            return keyc + base64Encode(result1.toString()).replaceAll("=", "");
        }
    }


    /**
     * 远程打开URL
     *
     * @param url      打开的url，　如 http://www.baidu.com/123.htm
     * @param limit    取返回的数据的长度
     * @param post     要发送的 POST 数据，如uid=1&password=1234
     * @param cookie   要模拟的 COOKIE 数据，如uid=123&auth=a2323sd2323
     * @param bysocket TRUE/FALSE 是否通过SOCKET打开
     * @param ip       IP地址
     * @param timeout  连接超时时间
     * @param block    是否为阻塞模式 defaul valuet:true
     * @return 取到的字符串
     */
    protected String ucFopen2(String url, int limit, String post, String cookie, boolean bysocket, String ip, int timeout, boolean block) {
//		long __times__ = isset(_GET["__times__"]) ? intval(_GET["__times__"]) + 1 : 1;
//		if(__times__ > 2) {
//			return "";
//		}
        url += url.indexOf("?") > 0 ? "&" : "?" + "__times__=1";
        return ucFopen(url, limit, post, cookie, bysocket, ip, timeout, block);
    }

    protected String ucFopen(String url, int limit, String post, String cookie, boolean bysocket, String ip, int timeout, boolean block) {
        StringBuilder returnString = new StringBuilder();

        URL matches = null;
        String host = "";
        String path = "";
        int port = 80;
        try {
            matches = new URL(url);
            host = matches.getHost();
            path = matches.getPath() != null ? matches.getPath() + (matches.getQuery() != null ? "?" + matches.getQuery() : "") : "/";
            if (matches.getPort() > 0) {
                port = matches.getPort();
            } else if ("https".equals(matches.getProtocol().toLowerCase())) {
                port = 443;
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }


        StringBuffer out = new StringBuffer();
        if (post != null && post.length() > 0) {
            out.append("POST ").append(path).append(" HTTP/1.0\r\n");
            out.append("Accept: */*\r\n");
            out.append("Accept-Language: zh-cn\r\n");
            out.append("Content-Type: application/x-www-form-urlencoded\r\n");
            out.append("User-Agent: \r\n");
            out.append("Host: ").append(host).append("\r\n");
            out.append("Content-Length: ").append(post.length()).append("\r\n");
            out.append("Connection: Close\r\n");
            out.append("Cache-Control: no-cache\r\n");
            out.append("Cookie: \r\n\r\n");
            out.append(post);
        } else {
            out.append("GET path HTTP/1.0\r\n");
            out.append("Accept: */*\r\n");
            //out .= "Referer: boardurl\r\n";
            out.append("Accept-Language: zh-cn\r\n");
            out.append("User-Agent: Java/1.5.0_01\r\n");
            out.append("Host: host\r\n");
            out.append("Connection: Close\r\n");
            out.append("Cookie: cookie\r\n\r\n");
        }

        try {
            Socket fp = null;
            String socketHost = ip != null && ip.length() > 10 ? ip : host;
            if ("https".equals(matches.getProtocol().toLowerCase())) {
                fp = SSLSocketFactory.getDefault().createSocket(socketHost, port);
            } else {
                fp = new Socket(socketHost, port);
            }
            if (!fp.isConnected()) {
                System.out.println("[ERROR]----------------------------------"
                        + "\nnet.renfei.discuz.ucenter.client.Client.ucFopen:"
                        + " Socket Not Connected!"
                        + "\nSocketHost:" + socketHost
                        + "\nSocketPort:" + port
                        + "\nProtocol:" + matches.getProtocol()
                        + "\n");
                return "";//note errstr : errno \r\n
            } else {

                OutputStream os = fp.getOutputStream();
                os.write(out.toString().getBytes());

                InputStream ins = fp.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
                StringBuilder header = new StringBuilder();
                while (true) {
                    String data = reader.readLine();
                    if (data == null || "".equals(data) || "\r\n".equals(data) || "\n".equals(data)) {
                        break;
                    } else {
                        header.append(data);
                    }
                }

                while (true) {
                    String data = reader.readLine();
                    if (data == null || data.equals("")) {
                        break;
                    } else {
                        returnString.append(data);
                    }
                }

                fp.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnString.toString();
    }

    public String ucAppLs() {
        String returnString = callUserFunc(UC_API_FUNC, "app", "ls", null);
        return UC_CONNECT.equals("mysql") ? returnString : ucUnserialize(returnString);
    }

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param email    Email
     * @return int
     * -1 : 用户名不合法
     * -2 : 包含不允许注册的词语
     * -3 : 用户名已经存在
     * -4 : email 格式有误
     * -5 : email 不允许注册
     * -6 : 该 email 已经被注册
     * >1 : 表示成功，数值为 UID
     */
    public String ucUserRegister(String username, String password, String email) {
        return ucUserRegister(username, password, email, "", "");
    }

    /**
     * 用户注册
     *
     * @param username   用户名
     * @param password   密码
     * @param email      Email
     * @param questionid 安全提问
     * @param answer     安全提问答案
     * @return int
     * -1 : 用户名不合法
     * -2 : 包含不允许注册的词语
     * -3 : 用户名已经存在
     * -4 : email 格式有误
     * -5 : email 不允许注册
     * -6 : 该 email 已经被注册
     * >1 : 表示成功，数值为 UID
     */
    public String ucUserRegister(String username, String password, String email, String questionid, String answer) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("username", username);
        args.put("password", password);
        args.put("email", email);
        args.put("questionid", questionid);
        args.put("answer", answer);
        return callUserFunc(UC_API_FUNC, "user", "register", args);
    }

    /**
     * 用户登陆检查
     *
     * @param username 用户名/uid
     * @param password 密码
     * @return array (uid/status, username, password, email)
     * 数组第一项
     * 1  : 成功
     * -1 : 用户不存在,或者被删除
     * -2 : 密码错
     */
    public String ucUserLogin(String username, String password) {
        return ucUserLogin(username, password, 0, 0);
    }

    /**
     * 用户登陆检查
     *
     * @param username  用户名/uid
     * @param password  密码
     * @param isuid     是否为uid
     * @param checkques 是否使用检查安全问答
     * @return array (uid/status, username, password, email)
     * 数组第一项
     * 1  : 成功
     * -1 : 用户不存在,或者被删除
     * -2 : 密码错
     */
    public String ucUserLogin(String username, String password, int isuid, int checkques) {
        return ucUserLogin(username, password, isuid, checkques, "", "");
    }

    /**
     * 用户登陆检查
     *
     * @param username   用户名/uid
     * @param password   密码
     * @param isuid      是否为uid
     * @param checkques  是否使用检查安全问答
     * @param questionid 安全提问
     * @param answer     安全提问答案
     * @return array (uid/status, username, password, email)
     * 数组第一项
     * 1  : 成功
     * -1 : 用户不存在,或者被删除
     * -2 : 密码错
     */
    public String ucUserLogin(String username, String password, int isuid, int checkques, String questionid, String answer) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("username", username);
        args.put("password", password);
        args.put("isuid", isuid);
        args.put("checkques", checkques);
        args.put("questionid", questionid);
        args.put("answer", answer);
        String returnString = callUserFunc(UC_API_FUNC, "user", "login", args);
        return UC_CONNECT.equals("mysql") ? returnString : ucUnserialize(returnString);
    }

    /**
     * 进入同步登录代码
     *
     * @param uid 用户ID
     * @return string        HTML代码
     */
    public String ucUserSynlogin(int uid) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("uid", uid);
        String returnString = ucApiPost("user", "synlogin", args);
        return returnString;
    }

    /**
     * 进入同步登出代码
     *
     * @return string        HTML代码
     */
    public String ucUserSynlogout() {
        String returnString = ucApiPost("user", "synlogout", new HashMap<String, Object>());
        return returnString;
    }

    /**
     * 取得用户数据
     *
     * @param username 用户名
     * @param isuid    是否为UID
     * @return array (uid, username, email)
     */
    public String ucGetUser(String username, int isuid) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("username", username);
        args.put("isuid", isuid);
        String returnString = callUserFunc(UC_API_FUNC, "user", "get_user", args);
        return UC_CONNECT.equals("mysql") ? returnString : ucUnserialize(returnString);
    }

    /**
     * 编辑用户
     *
     * @param username    用户名
     * @param oldpw       旧密码
     * @param newpw       新密码
     * @param email       Email
     * @param ignoreoldpw 是否忽略旧密码, 忽略旧密码, 则不进行旧密码校验.
     * @param questionid  安全提问
     * @param answer      安全提问答案
     * @return int
     * 1  : 修改成功
     * 0  : 没有任何修改
     * -1 : 旧密码不正确
     * -4 : email 格式有误
     * -5 : email 不允许注册
     * -6 : 该 email 已经被注册
     * -7 : 没有做任何修改
     * -8 : 受保护的用户，没有权限修改
     */
    public String ucUserEdit(String username, String oldpw, String newpw, String email, int ignoreoldpw, String questionid, String answer) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("username", username);
        args.put("oldpw", oldpw);
        args.put("newpw", newpw);
        args.put("email", email);
        args.put("ignoreoldpw", ignoreoldpw);
        args.put("questionid", questionid);
        args.put("answer", answer);
        return callUserFunc(UC_API_FUNC, "user", "edit", args);
    }

    /**
     * 删除用户
     *
     * @param uid 用户的 UID
     * @return int
     * >0 : 成功
     * 0 : 失败
     */
    public String ucUserDelete(String uid) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("uid", uid);
        return callUserFunc(UC_API_FUNC, "user", "delete", args);
    }

    /**
     * 删除用户头像
     *
     * @param uid 用户的 UID
     */
    public String ucUserDeleteavatar(String uid) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("uid", uid);
        return ucApiPost("user", "deleteavatar", args);
    }
}

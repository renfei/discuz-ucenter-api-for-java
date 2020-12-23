package net.renfei.discuz.ucenter.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

/**
 * ================================================
 * Discuz! Ucenter API for JAVA
 * ================================================
 * 构造本接口运行所需要PHP的内置函数
 * <p>
 * 更多信息：http://code.google.com/p/discuz-ucenter-api-for-java
 * 原作者：梁平 (no_ten@163.com)
 * 创建时间：2009-2-20
 * 更多信息：https://github.com/renfei/discuz-ucenter-api-for-java
 * 修改者：任霏 (i@renfei.net)
 * 修改时间：2020-12-17
 */
public abstract class PHPFunctions {
    //JAVA EXTRA METHOD

    protected String urlencode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    protected String md5(String input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return byte2hex(md.digest(input.getBytes()));
    }

    protected String md5(long input) {
        return md5(String.valueOf(input));
    }

    protected String base64Decode(String input) {
        try {
            return new String(Base64.getDecoder().decode(input), StandardCharsets.ISO_8859_1);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    protected String base64Encode(String input) {
        try {
            return new String(Base64.getEncoder().encode(input.getBytes(StandardCharsets.ISO_8859_1)));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    protected String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString();
    }

    protected String substr(String input, int begin, int length) {
        return input.substring(begin, begin + length);
    }

    protected String substr(String input, int begin) {
        if (begin > 0) {
            return input.substring(begin);
        } else {
            return input.substring(input.length() + begin);
        }
    }

    protected long microTime() {
        return System.currentTimeMillis();
    }

    protected long time() {
        return System.currentTimeMillis() / 1000;
    }

    protected String sprintf(String format, long input) {
        String temp = "0000000000" + input;
        return temp.substring(temp.length() - 10);
    }

    protected String callUserFunc(String function, String model, String action, Map<String, Object> args) {
        if ("uc_api_mysql".equals(function)) {
            return this.ucApiMysql(model, action, args);
        }
        if ("uc_api_post".equals(function)) {
            return this.ucApiPost(model, action, args);
        }
        return "";
    }

    public abstract String ucApiPost(String module, String action, Map<String, Object> arg);

    public abstract String ucApiMysql(String model, String action, Map args);

}

package com.glodon.bim.basic.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by chenwj-a on 2017/12/8.
 */

public class GlodonCookieJar implements CookieJar {

    private Map<String, List<Cookie>> m_map = new HashMap<>();
    private OnJSESSIONIDChangeListener onJSESSIONIDChangeListener;
    public static String JSESSIONID = null;


    @Override
    public List<Cookie> loadForRequest(HttpUrl arg0) {
        List<Cookie> result = new ArrayList<>();
        for (List<Cookie> cookies : m_map.values()) {
            if (cookies.size() > 0) {
                result.add(cookies.get(cookies.size() - 1));        // 注：相同 域名+路径 取最后一个
            }
        }
        return result;
    }

    @Override
    public void saveFromResponse(HttpUrl arg0, List<Cookie> cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                addCookie(cookie, m_map);
            }
        }
    }

    /**
     * JSESSIONID改变时监听
     *
     * @param onJSESSIONIDChangeListener
     */
    public void setOnJSESSIONIDChangeListener(OnJSESSIONIDChangeListener onJSESSIONIDChangeListener) {
        this.onJSESSIONIDChangeListener = onJSESSIONIDChangeListener;
    }


    private void addCookie(Cookie cookie, Map<String, List<Cookie>> m_map) {
        if (cookie == null) {
            return;
        }
        String domain = cookie.domain();
        if (domain == null) {
            domain = "";
        }
        String path = cookie.path();
        if (path == null) {
            path = "";
        }
        String key = domain + path;
        List<Cookie> cookies = m_map.get(key);
        if (cookies == null) {
            cookies = new ArrayList<>();
            m_map.put(key, cookies);
        }
        cookies.add(cookie);
        if (cookie.name().equals("JSESSIONID") && cookie.path().equals("/")) {
            JSESSIONID = cookie.value();
            if (onJSESSIONIDChangeListener != null) {
                onJSESSIONIDChangeListener.onChange(JSESSIONID);
            }
        }
    }

    /**
     * 清空cookie
     */
    public void clear() {
        m_map.clear();
        JSESSIONID = null;
    }

    public interface OnJSESSIONIDChangeListener {
        void onChange(String jsession);
    }

}

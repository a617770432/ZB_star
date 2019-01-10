package com.xingqiuzhibo.phonelive.http;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.im.ImPushUtil;

import org.apache.http.entity.StringEntity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@SuppressWarnings("deprecation")
public class NetWork {

    public static String result = "";

    /**
     * get方式访问后台
     *
     * @param url
     * @param json
     * @param request
     */
    public static void httpJsonGet(String url, String json, Context context, RequestCallBack<String> request) {
        RequestParams params = new RequestParams();
        if (!AppConfig.getInstance().checkToken(context)) {
            return;
        }
        if (null != json) {
            try {
                params.setBodyEntity(new StringEntity(json, "UTF-8"));
                params.setContentType("application/json");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        HttpUtils http = new HttpUtils();
        // 设置超时时间
        http.configTimeout(30 * 1000);
        http.configSoTimeout(30 * 1000);
        http.configCurrentHttpCacheExpiry(1000); // 设缓存时间 1s
        http.send(HttpRequest.HttpMethod.GET, url, params, request);

    }

    /**
     * get方式访问后台
     *
     * @param url
     * @param json
     * @param request
     */
    public static void httpJsonGet(String url, String json, int second, Context context, RequestCallBack<String> request) {
        RequestParams params = new RequestParams();
        if (!AppConfig.getInstance().checkToken(context)) {
            return;
        }
        if (null != json) {
            try {
                params.setBodyEntity(new StringEntity(json, "UTF-8"));
                params.setContentType("application/json");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        HttpUtils http = new HttpUtils();
        // 设置超时时间
        http.configTimeout(30 * 1000);
        http.configSoTimeout(30 * 1000);
        http.configCurrentHttpCacheExpiry(second * 1000); // 设缓存时间 s
        http.send(HttpRequest.HttpMethod.GET, url, params, request);

    }

    /**
     * get查询数据库数据
     *
     * @param url
     * @param map
     * @param request
     */
    public static void httpParamGet(String url, Map<String, Object> map, int second, Context context, RequestCallBack<String> request) {
        RequestParams params = new RequestParams();
        if (!AppConfig.getInstance().checkToken(context)) {
            return;
        }
        if (null != map) {
            if (map.size() != 0) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    params.addQueryStringParameter(entry.getKey(), (String) entry.getValue());
                    Log.d("TAG", entry.getKey() + ":" + (String) entry.getValue());
                }
            }
            params.setContentType("application/json");
        }

        HttpUtils http = new HttpUtils();
        // 设置超时时间
        http.configTimeout(30 * 1000);
        http.configSoTimeout(30 * 1000);
        http.configCurrentHttpCacheExpiry(second * 1000); // 设缓存时间 1s
        http.send(HttpRequest.HttpMethod.GET, url, params, request);

    }

    /**
     * get查询数据库数据
     *
     * @param url
     * @param map
     * @param request
     */
    public static void httpParamGet(String url, Map<String, Object> map, Context context, RequestCallBack<String> request) {
        RequestParams params = new RequestParams();
        if (context != null) {
            if (!AppConfig.getInstance().checkToken(context)) {
                return;
            }
        }

        if (null != map) {
            if (map.size() != 0) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
//                    params.addBodyParameter(String.valueOf(entry.getKey()), (String) entry.getValue());
                    params.addQueryStringParameter(entry.getKey(), (String) entry.getValue());
                    Log.e("TAG", "key:----->" + entry.getKey() + "_________value:----->" + entry.getValue());
                }
            }
            params.setContentType("application/json");
        }
        HttpUtils http = new HttpUtils();
        // 设置超时时间
        http.configTimeout(30 * 1000);
        http.configSoTimeout(30 * 1000);
        http.configCurrentHttpCacheExpiry(1000); // 设缓存时间 1s
        http.send(HttpRequest.HttpMethod.GET, url, params, request);

    }

    /**
     * 带上传文件的 Post请求 异步的
     *
     * @param url
     * @param jsonStr
     * @param request
     */
    public static void httpPost(String url, String jsonStr, Context context, RequestCallBack<String> request) {
        RequestParams params = new RequestParams();
        if (null != context)
            if (!AppConfig.getInstance().checkToken(context)) {
                return;
            }
        // jsonStr = "{\"mobile\": \"13800138000\",\"password\": \"1\"}";
        // 只包含字符串参数时默认使用BodyParamsEntity，
        // 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
        // params.addBodyParameter("name", "value");
        // 加入文件参数后默认使用MultipartEntity（"multipart/form-data"），
        // 如需"multipart/related"，xUtils中提供的MultipartEntity支持设置subType为"related"。
        // 使用params.setBodyEntity(httpEntity)可设置更多类型的HttpEntity（如：
        // MultipartEntity,BodyParamsEntity,FileUploadEntity,InputStreamUploadEntity,StringEntity）。
        // 例如发送json参数：params.setBodyEntity(new StringEntity(jsonStr,charset));
        // params.addBodyParameter("picture", new File(picString));
        try {
            params.setBodyEntity(new StringEntity(jsonStr, "UTF-8"));
            params.setContentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("TAG", "jsonStr" + jsonStr.toString());
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000); // 设缓存时间 1s
        http.send(HttpRequest.HttpMethod.POST, url, params, request);
    }

    /**
     * post查询数据库数据
     *
     * @param url
     * @param map
     * @param request
     */
    public static void httpPost(String url, Map<String, Object> map, Context context, RequestCallBack<String> request) {
        RequestParams params = new RequestParams();
        if (!AppConfig.getInstance().checkToken(context)) {
            return;
        }
        if (null != map) {
            if (map.size() != 0) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    params.addQueryStringParameter(entry.getKey(), (String) entry.getValue());
                    Log.d("TAG", entry.getKey() + ":" + (String) entry.getValue());
                }
            }
            params.setContentType("application/json");
        }
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10); // 设置超时时间 10s
        http.send(HttpRequest.HttpMethod.POST, url, params, request);

    }


    /**
     * 带上传文件的 Post请求 异步的
     *
     * @param url
     * @param file
     * @param width
     * @param height
     * @param request
     */
    public static void httpFilePost(String url, File file, int width, int height, Context context, RequestCallBack<String> request) {
        RequestParams params = new RequestParams();
        if (!AppConfig.getInstance().checkToken(context)) {
            return;
        }
        // jsonStr = "{\"mobile\": \"13800138000\",\"password\": \"1\"}";
        // 只包含字符串参数时默认使用BodyParamsEntity，
        // 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
        // params.addBodyParameter("name", "value");
        // 加入文件参数后默认使用MultipartEntity（"multipart/form-data"），
        // 如需"multipart/related"，xUtils中提供的MultipartEntity支持设置subType为"related"。
        // 使用params.setBodyEntity(httpEntity)可设置更多类型的HttpEntity（如：
        // MultipartEntity,BodyParamsEntity,FileUploadEntity,InputStreamUploadEntity,StringEntity）。
        // 例如发送json参数：params.setBodyEntity(new StringEntity(jsonStr,charset));
        // params.addBodyParameter("picture", new File(picString));
        params.addBodyParameter("file", file);
        params.addBodyParameter("width", width + "");
        params.addBodyParameter("height", height + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, params, request);
    }


}

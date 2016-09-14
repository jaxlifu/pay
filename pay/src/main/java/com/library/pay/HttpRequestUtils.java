package com.library.pay;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jax on 2016/9/13.
 * Description :
 * Version : V1.0.0
 */
public class HttpRequestUtils {

    protected static String TAG = "HttpRequestUtils";

    public static String httpRequest(String httpUrl) {
        HttpURLConnection urlConnection = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                urlConnection.connect();
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 * 8];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                is.close();

                result = String.valueOf(os);
                Log.d(TAG, "请求结果是 " + result);
            } else {
                Log.d(TAG, urlConnection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "连接服务器失败!");
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
        return result;
    }
}

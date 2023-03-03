package org.csu.tvds.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class HttpUtil {
    /**
     * 发送GET请求
     *
     * @param url 目标地址
     * @return 请求结果
     * @throws IOException IO异常将会抛出至上层
     * @author Kwanho
     */
    public static String get(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        String result;
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpResponse response = client.execute(get);
            result = EntityUtils.toString(response.getEntity());
        }
        return result;
    }
}

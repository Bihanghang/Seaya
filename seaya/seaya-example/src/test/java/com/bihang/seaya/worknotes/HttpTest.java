package com.bihang.seaya.worknotes;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created By bihang
 * 2019/1/17 11:56
 */
@Slf4j
public class HttpTest {

    private static CloseableHttpClient httpProxyClient;

    public static synchronized CloseableHttpClient getHttpClient(String proxyIp, int proxyPort, String protocol) {
        if (httpProxyClient == null) {
            HttpHost proxy = new HttpHost(proxyIp, proxyPort, protocol);
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setConnectTimeout(20000)
                    .setConnectionRequestTimeout(20000)
                    .setSocketTimeout(60000)
                    .setProxy(proxy)
                    .build();
            PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
            pool.setMaxTotal(200);
            pool.setDefaultMaxPerRoute(200);
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials("pengjun9", "ssap_0315!"));
            httpProxyClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).setProxy(proxy).build();
            return httpProxyClient;
        } else {
            return httpProxyClient;
        }
    }

    public static String post(String url, String jsonString) {
        CloseableHttpResponse response = null;
        BufferedReader in = null;
        String result = "";
        if(true) {
            httpProxyClient = getHttpClient("172.31.1.246", 8080, "http");
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
            httpPost.setConfig(requestConfig);
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setEntity(new StringEntity(jsonString, Charset.forName("UTF-8")));
            response = httpProxyClient.execute(httpPost);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                sb.append(line + NL);
            }
            in.close();
            result = sb.toString();
        } catch (IOException e) {
            log.error("信用飞行分请求异常："+e.getMessage()+"||"+e.getStackTrace());
            e.printStackTrace();
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String post = post("http://localhost:8019/api/car/basicdata/getmyairlines",
                "");
        System.out.println(post);
    }
}

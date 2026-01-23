package com.systemcontrol.corpsele.systemcontrol;

import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * OkHttp3 忽略 SSL 证书工具类（信任所有证书）
 *
 * 注意：仅用于开发/测试/内网环境，生产环境请勿使用！
 */
public final class UnsafeOkHttpClient {

    private UnsafeOkHttpClient() {
        // 工具类不允许实例化
    }

    /**
     * 创建一个信任所有证书的 OkHttpClient 实例
     *
     * @return OkHttpClient
     */
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // 1. 创建一个信任所有证书的 X509TrustManager
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                            // 不做任何校验，信任所有客户端证书
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                            // 不做任何校验，信任所有服务端证书
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            // 返回空数组
                            return new X509Certificate[]{};
                        }
                    }
            };

            // 2. 初始化 SSLContext，使用上面的 trustAllCerts
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            // 3. 获取 SSLSocketFactory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // 4. 构建 OkHttpClient，并注入自定义的 SSLSocketFactory + X509TrustManager
            //    同时设置 hostnameVerifier 为始终返回 true
            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create unsafe OkHttp client", e);
        }
    }
}

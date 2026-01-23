package com.systemcontrol.corpsele.systemcontrol;

import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public final class UnsafeOkHttpClientAllProtocols {

    private UnsafeOkHttpClientAllProtocols() {}

    public static OkHttpClient getUnsafeOkHttpWithAllProtocols() throws Exception {
        // 1. TrustManager：信任所有证书（仅测试用！）
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };

        // 2. SSLContext：使用 TLS
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        // 3. 查看本 JDK 支持的协议列表
        try (SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket()) {
            String[] supported = socket.getSupportedProtocols();
            String[] enabled = socket.getEnabledProtocols();
            System.out.println("Supported protocols: " + Arrays.toString(supported));
            System.out.println("Enabled protocols:   " + Arrays.toString(enabled));
            // 如要全部启用，可以在 SSLSocket/SSLEngine 层面 setEnabledProtocols(supported)
            // OkHttp 默认走 enabledProtocols 列表，一般就够用
        }

        // 4. OkHttpClient
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }
}

package com.systemcontrol.corpsele.systemcontrol;

import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public final class Android9OkHttpClient {

    private Android9OkHttpClient() {
    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // 1. 创建信任所有证书的 TrustManager
            final TrustManager[] trustAllCerts = new TrustManager[]{
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

            // 2. 初始化 SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            // 3. 关键点：获取默认 SSLSocketFactory，并包装它以支持旧版协议
            SSLSocketFactory sslSocketFactory = new TLSSocketFactory(sslContext.getSocketFactory());

            // 4. 构建 OkHttpClient
            return new OkHttpClient.Builder()
                    .addInterceptor(new TlsDebugInterceptor())
                    .connectTimeout(10800, TimeUnit.SECONDS)
                    .callTimeout(10800, TimeUnit.SECONDS)
                    .readTimeout(10800, TimeUnit.SECONDS)
                    // 必须传入自定义的 sslSocketFactory 和 trustManager
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    // 忽略 Hostname 验证
                    .hostnameVerifier((hostname, session) -> true)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 自定义 SSLSocketFactory，强制开启 TLSv1, TLSv1.1, TLSv1.2, TLSv1.3
     * 解决 Android 9 (P) 默认不支持 TLSv1, TLSv1.1 的问题
     */
    private static class TLSSocketFactory extends SSLSocketFactory {

        private final SSLSocketFactory delegate;

        public TLSSocketFactory(SSLSocketFactory base) {
            this.delegate = base;
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            return patch(delegate.createSocket(s, host, port, autoClose));
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException {
            return patch(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
            return patch(delegate.createSocket(host, port, localHost, localPort));
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            return patch(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            return patch(delegate.createSocket(address, port, localAddress, localPort));
        }

        private Socket patch(Socket socket) {
            if (socket instanceof SSLSocket) {
                // 核心代码：强制启用所有协议
                // Android 9 默认只开启 TLSv1.2+，这里强行开启 TLSv1 和 TLSv1.1
                ((SSLSocket) socket).setEnabledProtocols(new String[]{
                        "TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3"
                });
            }
            return socket;
        }
    }
}

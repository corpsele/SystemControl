package com.systemcontrol.corpsele.systemcontrol;

import org.conscrypt.Conscrypt;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class OkHttpSSLParamProtocols {

    private OkHttpSSLParamProtocols() {}

    public static OkHttpClient getUnsafeClientWithCustomProtocols(String[] protocols) throws Exception {
        // 1. 信任所有证书（仅测试用，生产环境千万不要这样做）
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

        // 2. 初始化 SSLContext（TLS）
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        // 3. 自定义 SSLSocketFactory，在 createSocket 里通过 SSLParameters.setProtocols 设置协议
        SSLSocketFactory rawFactory = sslContext.getSocketFactory();

        SSLSocketFactory customFactory = new SSLSocketFactory() {
            @Override
            public String[] getDefaultCipherSuites() {
                return rawFactory.getDefaultCipherSuites();
            }

            @Override
            public String[] getSupportedCipherSuites() {
                return rawFactory.getSupportedCipherSuites();
            }

            @Override
            public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
                SSLSocket socket = (SSLSocket) rawFactory.createSocket(s, host, port, autoClose);
                configureProtocols(socket);
                return socket;
            }

            @Override
            public Socket createSocket(String host, int port) throws IOException {
                SSLSocket socket = (SSLSocket) rawFactory.createSocket(host, port);
                configureProtocols(socket);
                return socket;
            }

            @Override
            public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
                SSLSocket socket = (SSLSocket) rawFactory.createSocket(host, port, localHost, localPort);
                configureProtocols(socket);
                return socket;
            }

            @Override
            public Socket createSocket(InetAddress host, int port) throws IOException {
                SSLSocket socket = (SSLSocket) rawFactory.createSocket(host, port);
                configureProtocols(socket);
                return socket;
            }

            @Override
            public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
                SSLSocket socket = (SSLSocket) rawFactory.createSocket(address, port, localAddress, localPort);
                configureProtocols(socket);
                return socket;
            }

            private void configureProtocols(SSLSocket socket) {
                SSLParameters params = new SSLParameters();

                // 这里就是你想要的“setProtocols”
                if (protocols != null && protocols.length > 0) {
                    params.setProtocols(protocols);
                } else {
                    // 如果不传，就使用 socket 支持的全部协议（所谓“全部协议”）
                    params.setProtocols(socket.getSupportedProtocols());
                }

                // 可选：设置密码套件
                params.setCipherSuites(socket.getSupportedCipherSuites());

                socket.setSSLParameters(params);
            }
        };

        Security.insertProviderAt(Conscrypt.newProvider(), 1);

        // 4. 构造 OkHttpClient
        return new OkHttpClient.Builder()
                .connectionSpecs(List.of(ConnectionSpec.MODERN_TLS))
                .addInterceptor(new TlsDebugInterceptor())
                .connectTimeout(3600, TimeUnit.SECONDS)
                .callTimeout(3600, TimeUnit.SECONDS)
                .readTimeout(3600, TimeUnit.SECONDS)
                .writeTimeout(3600, TimeUnit.SECONDS)
                .sslSocketFactory(customFactory, (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }

    // 示例：只启用 TLSv1.2 和 TLSv1.3
    public static OkHttpClient getUnsafeClientTls12AndTls13() throws Exception {
        String[] protocols = new String[]{"SSLv3", "TLSv1", "TLSv1.1" ,"TLSv1.2"};
        return getUnsafeClientWithCustomProtocols(protocols);
    }

    // 示例：尽可能启用全部 TLS 协议（包括不安全的）
    public static OkHttpClient getUnsafeClientAllProtocols() throws Exception {
        // 传 null，在内部会用 socket.getSupportedProtocols()
        return getUnsafeClientWithCustomProtocols(null);
    }
}

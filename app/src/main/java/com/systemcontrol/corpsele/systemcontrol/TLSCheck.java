package com.systemcontrol.corpsele.systemcontrol;

import javax.net.ssl.SSLContext;

public class TLSCheck {
    public static void checkTLS() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
            sslContext.init(null, null, null);
            System.out.println("✅ TLS 1.3 已启用");
        } catch (Exception e) {
            System.out.println("❌ TLS 1.3 不支持");
            e.printStackTrace();
        }
    }
}

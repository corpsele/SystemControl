package com.systemcontrol.corpsele.systemcontrol;

import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.gms.common.logging.Logger;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okio.Buffer;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import java.io.IOException;

public class TlsDebugInterceptor implements Interceptor {
//    @NonNull
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Response response = chain.proceed(chain.request());
//
//        TlsVersion tlsVersion = response.handshake() != null ?
//                response.handshake().tlsVersion() : null;
//
//        if (tlsVersion != null) {
//            System.out.println("TLS protocol: " + tlsVersion);
//        }
//
//        return response;
//    }

    @NonNull
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LogUtils.i("request:\n" + this.bodyToString(request));
        LogUtils.i("request header = \n " + request.headers());
        long startTime = System.currentTimeMillis();
        okhttp3.Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration=endTime-startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        LogUtils.i("----------Start----------------");
        LogUtils.i("| "+request.toString());
        LogUtils.i("| "+request.url());
        String method=request.method();
        if("POST".equals(method)){
            LogUtils.i("request:\n" + this.bodyToString(request));
        }
        LogUtils.i("| Response:" + content);
        LogUtils.i("----------End:"+duration+"毫秒----------");
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }

    private static String bodyToString(final Request request){

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "error";
        }
    }


}


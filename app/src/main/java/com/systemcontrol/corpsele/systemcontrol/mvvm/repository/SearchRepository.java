package com.systemcontrol.corpsele.systemcontrol.mvvm.repository;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ihongqiqu.util.LogUtils;
import com.systemcontrol.corpsele.systemcontrol.Android9OkHttpClient;
import com.systemcontrol.corpsele.systemcontrol.OkHttpSSLParamProtocols;
import com.systemcontrol.corpsele.systemcontrol.SSLSocketClient;
import com.systemcontrol.corpsele.systemcontrol.UnsafeOkHttpClient;
import com.systemcontrol.corpsele.systemcontrol.UnsafeOkHttpClientAllProtocols;
import com.systemcontrol.corpsele.systemcontrol.mvvm.callback.SearchCallBack;
import com.systemcontrol.corpsele.systemcontrol.mvvm.model.Search;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class SearchRepository {
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private static SearchRepository instance;

    public static SearchRepository getInstance() {
        if (instance == null) {
            instance = new SearchRepository();
        }
        return instance;
    }

    public void sendOllamaRequest(String apkKey, String keyWords, SearchCallBack searchCallBack) throws IOException {
        executor.execute(() -> {
            try {
                Thread.sleep(500);
                OkHttpClient client = Android9OkHttpClient.getUnsafeOkHttpClient();
//                OkHttpClient client = new OkHttpClient().newBuilder().build();
//        请用中文说出，iphone se2 开发调试没有connet via network
                MediaType mediaType = MediaType.parse("application/json");
//                RequestBody body = RequestBody.create(mediaType, "{\n    \"model\": \"qwen3-vl:235b-cloud\",\n    \"messages\": [\n        {\n            \"role\": \"user\",\n            \"content\": \"" + keyWords + "\"\n        }\n    ],\n    \"stream\": false\n}");
                RequestBody body = RequestBody.create(mediaType, "{\n    \"model\": \"GLM-4.7-Flash\",\n    \"messages\": [\n       {\n         \"role\": \"system\",       \"content\": \"你是一个得力的助手\"\n        },\n        {\n            \"role\": \"user\",\n            \"content\": \"" + keyWords + "\"\n        }\n    ],\n    \"stream\": false,\n     \"temperature\": 1.0,\n     \"max_tokens\": 1024\n}");

//                Request request = new Request.Builder()
//                        .url("https://ollama.com/api/chat")
//                        .method("POST", body)
//                        .addHeader("Authorization", "Bearer " + apkKey)
//                        .addHeader("Content-Type", "application/json")
//                        .build();

                Request request = new Request.Builder()
                        .url("https://open.bigmodel.cn/api/paas/v4/chat/completions")
                        .method("POST", body)
                        .addHeader("Authorization", "Bearer " + apkKey)
                        .addHeader("Content-Type", "application/json")
                        .build();

                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                String strJson = buffer.readUtf8();
                LogUtils.i(strJson);

                Response response = client.newCall(request).execute();
                String responseJson = response.body().string();
                Gson gson = new Gson();
//                Type type = new TypeToken<Map<String, Object>>(){}.getType();
//                Map<String, Object> bodyJson = gson.fromJson(responseJson, type);
                JsonObject bodyJson = gson.fromJson(responseJson, JsonObject.class);
                String strMessage;
                String strCode = "";
                if (response.code() != 200) {
                    JsonObject errorJson = bodyJson.get("error").getAsJsonObject();
                    strCode = errorJson.get("code").getAsString();
                    strMessage = errorJson.get("message").getAsString();
                } else {
                    JsonArray successArray = bodyJson.get("choices").getAsJsonArray();
                    JsonObject successJson = successArray.get(0).getAsJsonObject();
                    JsonObject messageJson = successJson.get("message").getAsJsonObject();
                    strMessage = messageJson.get("content").getAsString();
                }

                final Search result = new Search(keyWords, responseJson);
                // 切换回主线程回调 (LiveData通常在主线程观察，但这里手动回调最好切回主线程)
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (searchCallBack != null) {

                        searchCallBack.onSearchSuccess(result, strMessage);
                    }
                });

            } catch (Exception e) {
                searchCallBack.onError(e);
            } finally {

            }
        });

    }
}

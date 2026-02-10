package com.systemcontrol.corpsele.systemcontrol.mvvm.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.systemcontrol.corpsele.systemcontrol.mvvm.dao.ApiModelDao;
import com.systemcontrol.corpsele.systemcontrol.mvvm.database.AppDatabase;
import com.systemcontrol.corpsele.systemcontrol.mvvm.model.ApiModel;
import com.systemcontrol.corpsele.systemcontrol.mvvm.model.ApiModelFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ApiModelRepository {

    private ApiModelDao apiModelDao;
    private LiveData<List<ApiModel>> allApi;
    // Java 中没有协程，通常使用 ExecutorService 处理后台任务
    private ExecutorService executorService;
    public ApiModelRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        apiModelDao = database.apiModelDao();
        allApi = apiModelDao.getAllApi();
        executorService = Executors.newSingleThreadExecutor();
    }
    public LiveData<List<ApiModel>> getAllApi() {
        return allApi;
    }
    public void insert(String title, String description, String url, String apiKey) {
        // 核心点：在 Repository 中调用工厂创建对象
        ApiModel apiModel = ApiModelFactory.createApiModel(title, description, url, apiKey);
        executorService.execute(() -> apiModelDao.insert(apiModel));
    }
    public void delete(ApiModel apiModel) {
        executorService.execute(() -> apiModelDao.delete(apiModel));
    }
}
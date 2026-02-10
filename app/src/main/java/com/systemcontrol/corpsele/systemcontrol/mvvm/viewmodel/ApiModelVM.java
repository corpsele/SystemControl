package com.systemcontrol.corpsele.systemcontrol.mvvm.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.systemcontrol.corpsele.systemcontrol.mvvm.model.ApiModel;
import com.systemcontrol.corpsele.systemcontrol.mvvm.repository.ApiModelRepository;

import java.util.List;
public class ApiModelVM extends AndroidViewModel {

    private ApiModelRepository repository;
    private LiveData<List<ApiModel>> allApi;
    public ApiModelVM(@NonNull Application application) {
        super(application);
        repository = new ApiModelRepository(application);
        allApi = repository.getAllApi();
    }
    public LiveData<List<ApiModel>> getAllApi() {
        return allApi;
    }
    public void insert(String title, String description, String url, String apiKey) {
        repository.insert(title, description, url, apiKey);
    }
    public void delete(ApiModel apiModel) {
        repository.delete(apiModel);
    }
}

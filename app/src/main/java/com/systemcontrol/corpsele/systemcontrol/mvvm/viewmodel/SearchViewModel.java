package com.systemcontrol.corpsele.systemcontrol.mvvm.viewmodel;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ihongqiqu.util.LogUtils;

import com.systemcontrol.corpsele.systemcontrol.mvvm.callback.SearchCallBack;
import com.systemcontrol.corpsele.systemcontrol.mvvm.model.Search;
import com.systemcontrol.corpsele.systemcontrol.mvvm.repository.SearchRepository;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class SearchViewModel extends ViewModel {

    private SearchRepository searchRepository;
    private MutableLiveData<Search> searchResults = new MutableLiveData<>();
    // 输入框的内容 (双向绑定)
    public MutableLiveData<String> inputText = new MutableLiveData<>("");

    public MutableLiveData<String> apiKey = new MutableLiveData<>("");
    // 结果数据 (用于单向绑定给 UI)
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private MutableLiveData<ArrayList<OkHttpClient>> clients = new MutableLiveData<>();


    public MutableLiveData<Search> getSearchResults() { return searchResults; }

    public MutableLiveData<ArrayList<OkHttpClient>> getClients() { return clients; }

    public MutableLiveData<Boolean> getIsLoading() { return isLoading; }



    public void performSearch() {
        isLoading.setValue(true);
        try{
            String query = inputText.getValue();
            if (query == null) query = "";
            String key = apiKey.getValue();
            if (key == null) key = "";

            // 调用 Repository，并传入 Callback 实现回调逻辑
            SearchRepository.getInstance().sendOllamaRequest(key, query, new SearchCallBack() {
                @Override
                public void onSearchSuccess(Object search, Object response) {
                    LogUtils.d("-=-=--=-=--=-==- search = " + search.toString());
                    LogUtils.d(response.toString());
                    Search tmpSearch = new Search(((Search)search).getKeyWords(), (String)response);
                    searchResults.setValue(tmpSearch);
                    isLoading.postValue(false);
                }

                @Override
                public void onClientInit(Object object) {
                    if (object != null && object.getClass() == OkHttpClient.class) {
                        OkHttpClient client = (OkHttpClient) object;
                        if (clients.getValue() == null || clients.getValue().isEmpty()) {
                            ArrayList<OkHttpClient> tmpClient = new ArrayList<>();
                            tmpClient.add(client);
                            new Handler(Looper.getMainLooper()).post(() -> {
                                clients.setValue(tmpClient);
                            });

                        }else{
                            ArrayList<OkHttpClient> tmpClient = clients.getValue();
                            tmpClient.add(client);
                            new Handler(Looper.getMainLooper()).post(() -> {
                                clients.postValue(tmpClient);
                            });

                        }
                    }

                }

                @Override
                public void onError(Exception e) {
                    LogUtils.e(e.toString());
//                    searchResults.setValue(new Search("", ""));
                    searchResults.setValue(null);
                    isLoading.postValue(false);
                }
            });

//            searchRepository.sendOllamaRequest(search, new SearchCallBack() {
//                @Override
//                public void onSearchSuccess(Object search, Object response) {
//                    LogUtils.d("-=-=--=-=--=-==- search = " + search.toString());
//                    LogUtils.d(response.toString());
//                    Search tmpSearch = new Search(((Search)search).getKeyWords(), (String)response, ((Search)search).getApiKey());
//                    searchResults.postValue(tmpSearch);
//                    isLoading.postValue(false);
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    LogUtils.e(e.toString());
//                    isLoading.postValue(false);
//                }
//            });
        } catch (Exception e) {
            LogUtils.e("1", e.toString());
        }

    }
}

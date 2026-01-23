package com.systemcontrol.corpsele.systemcontrol.mvvm.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ihongqiqu.util.LogUtils;

import com.systemcontrol.corpsele.systemcontrol.mvvm.callback.SearchCallBack;
import com.systemcontrol.corpsele.systemcontrol.mvvm.model.Search;
import com.systemcontrol.corpsele.systemcontrol.mvvm.repository.SearchRepository;

public class SearchViewModel extends ViewModel {

    private SearchRepository searchRepository;
    private MutableLiveData<Search> searchResults = new MutableLiveData<>();
    // 输入框的内容 (双向绑定)
    public MutableLiveData<String> inputText = new MutableLiveData<>("");

    public MutableLiveData<String> apiKey = new MutableLiveData<>("");
    // 结果数据 (用于单向绑定给 UI)
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);


    public MutableLiveData<Search> getSearchResults() { return searchResults; }

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
                public void onError(Exception e) {
                    LogUtils.e(e.toString());
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

package com.systemcontrol.corpsele.systemcontrol.mvvm.callback;


public interface SearchCallBack extends CallBack {
    void onSearchSuccess(Object search, Object response);
    void onClientInit(Object object);


}

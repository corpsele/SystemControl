package com.systemcontrol.corpsele.systemcontrol;


import java.util.HashMap;

public class DataManager {

    private static volatile DataManager INSTANCE;

    private HashMap<String, Object> infoMap = new HashMap<String, Object>();

    private Boolean isPoped = false;

    private Boolean isOnAcc = false;

    public Boolean getOnAcc() {
        return isOnAcc;
    }

    public void setOnAcc(Boolean onAcc) {
        isOnAcc = onAcc;
    }

    public Boolean getPoped() {
        return isPoped;
    }

    public void setPoped(Boolean poped) {
        isPoped = poped;
    }

    private DataManager() {
    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            synchronized (DataManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataManager();
                }
            }
        }
        return INSTANCE;
    }

    public HashMap<String, Object> getInfoMap() {
        return infoMap;
    }

}
package com.systemcontrol.corpsele.systemcontrol.base;

import java.util.HashMap;

public class GlobalParam<T> {
    // 单例模式
    private static GlobalParam instance;

    private HashMap<String, Object> map;

    private GlobalParam() {
        map = new HashMap<>();
    }

    public static GlobalParam getInstance() {
        if (instance == null) {
            synchronized (GlobalParam.class) {
                if (instance == null) {
                    instance = new GlobalParam();
                }
            }
        }
        return instance;
    }

    // 存数据
    public void put(String key, Object value) {
        map.put(key, value);
    }

    // 取数据 (泛型)
    public T get(String key, Class clazz) {
        Object obj = map.get(key);
        if (obj != null && clazz.isInstance(obj)) {
            return (T) clazz.cast(obj);
        }
        return null;
    }

    // 移除数据 (防止内存泄漏)
    public void remove(String key) {
        map.remove(key);
    }

    // 清空所有
    public void clear() {
        map.clear();
    }
}


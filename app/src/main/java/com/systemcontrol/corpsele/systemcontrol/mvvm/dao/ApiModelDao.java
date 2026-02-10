package com.systemcontrol.corpsele.systemcontrol.mvvm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.systemcontrol.corpsele.systemcontrol.mvvm.model.ApiModel;

import java.util.List;
@Dao
public interface ApiModelDao {

    @Insert
    void insert(ApiModel apiModel); // 在 Java 中通常用 Executor 在后台线程调用
    @Delete
    void delete(ApiModel apiModel);
    @Query("DELETE FROM Api WHERE id = :id")
    void deleteById(int id);
    @Query("SELECT * FROM Api ORDER BY priority ASC, id DESC")
    LiveData<List<ApiModel>> getAllApi(); // LiveData 自动在主线程更新 UI
}

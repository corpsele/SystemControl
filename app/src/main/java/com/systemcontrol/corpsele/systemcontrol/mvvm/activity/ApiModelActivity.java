package com.systemcontrol.corpsele.systemcontrol.mvvm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.systemcontrol.corpsele.systemcontrol.R;
import com.systemcontrol.corpsele.systemcontrol.base.GlobalParam;
import com.systemcontrol.corpsele.systemcontrol.mvvm.adapter.ApiModelAdapter;
import com.systemcontrol.corpsele.systemcontrol.mvvm.model.ApiModel;
import com.systemcontrol.corpsele.systemcontrol.mvvm.viewmodel.ApiModelVM;

public class ApiModelActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextUrl;
    private EditText editTextApiKey;
    private Button buttonSave;
    private RecyclerView recyclerView;

    private ApiModelVM viewModel;
    private ApiModelAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_model);

        // 1. 接收 A 传过来的参数
        Intent intent = getIntent();
        if (intent != null) {
            String msg = intent.getStringExtra("any");

        }

        // 初始化 View
        editTextTitle = findViewById(R.id.et_apiModelTitle);
        editTextDescription = findViewById(R.id.et_apiModelDescription);
        editTextUrl = findViewById(R.id.et_apiModelUrl);
        editTextApiKey = findViewById(R.id.et_apiModelApiKey);
        buttonSave = findViewById(R.id.button_apiModelSave);
        recyclerView = findViewById(R.id.recycler_view_apiModel);
        // 设置 RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // 设置 Adapter
        adapter = new ApiModelAdapter(model -> {
            if(model instanceof ApiModel) {
                ApiModel apiModel = (ApiModel) model;
                GlobalParam.getInstance().put("apiModel", apiModel);

                // 2. 构造返回数据
                Intent result = new Intent();
                result.putExtra("key_result", "我是 B 的结果");
                result.putExtra("key_time", System.currentTimeMillis());
                result.putExtra("apiModel", apiModel);

                // 3. 设置结果码与结果 Intent
                setResult(RESULT_OK, result);

                // 4. 关闭当前页面，回到 A
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
        // 初始化 ViewModel
        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(ApiModelVM.class);
        // 观察数据变化
        viewModel.getAllApi().observe(this, apiModels -> {
            adapter.submitList(apiModels);
        });
        // 按钮点击事件
        buttonSave.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String url = editTextUrl.getText().toString().trim();
            String apikey = editTextApiKey.getText().toString().trim();
            if (title.isEmpty() || description.isEmpty() || url.isEmpty() || apikey.isEmpty()) {
                Toast.makeText(ApiModelActivity.this, "请输入完整信息", Toast.LENGTH_SHORT).show();
                return;
            }
            // 调用 ViewModel 插入数据
            viewModel.insert(title, description, url, apikey);

            Toast.makeText(ApiModelActivity.this, "Api saved", Toast.LENGTH_SHORT).show();
            editTextTitle.setText("");
            editTextDescription.setText("");
            editTextUrl.setText("");
            editTextApiKey.setText("");
        });

        // 实现点击删除功能
        new ItemTouchHelper(new androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(0,
                androidx.recyclerview.widget.ItemTouchHelper.LEFT | androidx.recyclerview.widget.ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                ApiModel apiModel = adapter.getApiModelAt(viewHolder.getAdapterPosition());
                viewModel.delete(apiModel);
                Toast.makeText(ApiModelActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }
}
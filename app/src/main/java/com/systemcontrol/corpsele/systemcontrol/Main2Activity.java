package com.systemcontrol.corpsele.systemcontrol;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Adapter;

import com.ihongqiqu.util.JSONUtils;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> list;
    private int listCount = 100;
    private RecycleAdapterDome adapterDome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String str = String.valueOf(bundle.getInt("type"));
        System.out.println(str);

        initData();

        initRecyclerView();

    }

    private void initData() {
        list = new ArrayList<String>();
        int i = 0;
        while (i < listCount) {
            i++;
            list.add(String.valueOf(i));
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);

        adapterDome = new RecycleAdapterDome(this, list);
        /*
        与ListView效果对应的可以通过LinearLayoutManager来设置
        与GridView效果对应的可以通过GridLayoutManager来设置
        与瀑布流对应的可以通过StaggeredGridLayoutManager来设置
        */
        //LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerView.LayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        //GridLayoutManager manager1 = new GridLayoutManager(context,2);
        //manager1.setOrientation(GridLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager manager2 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterDome);

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                adapterDome.setOnClick(new RecycleAdapterDome.ClickInterface() {
                    @Override
                    public void onItemClick(View view, int position) {
                        System.out.println(position);
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println(data.getIntExtra("type", 0));
        setResult(resultCode, data);
        finish();
    }
}

package com.systemcontrol.corpsele.systemcontrol;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxAdapter;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import kotlin.Unit;

public class Main3Activity extends AppCompatActivity {
    protected Button btn1;
    protected EditText editText1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        setupView();
        addEvent();
    }

    protected void setupView() {
        btn1 = findViewById(R.id.main3_btn1);
        editText1 = findViewById(R.id.editTextText);
    }

    protected void addEvent() {
        RxView.clicks(btn1).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Throwable {
                System.out.println("btn1 " + unit.toString());
            }
        });
        RxView.clicks(btn1).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Observer<Unit>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("btn1 ");
            }

            @Override
            public void onNext(@NonNull Unit unit) {
                System.out.println("btn1 " + unit.toString());
                System.out.println("editTextText " + editText1.getText());
                Handler handler = new Handler(); // 如果这个handler是在UI线程中创建的
                handler.postDelayed(new Runnable() {  // 开启的runnable也会在这个handler所依附线程中运行，即主线程
                    @Override
                    public void run() {
                        // 可更新UI或做其他事情
                        // 注意这里还在当前线程，没有开启新的线程
                        // new Runnable(){}，只是把Runnable对象以Message形式post到UI线程里的Looper中执行，并没有新开线程。
                        Intent intent1=new Intent(getBaseContext() ,MyService.class );
                        intent1.putExtra("identify","showNotification");
                        intent1.putExtra("notificationTitle","提醒推送");
                        String content = editText1.getText().toString();
                        intent1.putExtra("notificationContent", content);
                        startService(intent1);
                    }
                }, 3000); // 延时

            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("btn1 " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}

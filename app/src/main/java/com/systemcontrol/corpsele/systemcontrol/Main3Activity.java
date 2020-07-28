package com.systemcontrol.corpsele.systemcontrol;

import android.app.AppComponentFactory;
import android.os.Bundle;
import android.widget.Button;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        setupView();
        addEvent();
    }

    protected void setupView() {
        btn1 = findViewById(R.id.main3_btn1);
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

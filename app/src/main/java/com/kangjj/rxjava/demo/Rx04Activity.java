package com.kangjj.rxjava.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description: 过滤操作符
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo
 * @CreateDate: 2019/12/9 15:21
 */
public class Rx04Activity extends AppCompatActivity {
    private static final String TAG = Rx04Activity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx04);
    }

    /*
     *   filter 过滤
     * 需求：过滤掉 哪些不合格的奶粉，输出哪些合格的奶粉
     */
    public void click01(View view) {
        Observable.just("三鹿", "合生元", "飞鹤")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        // return true; // 不去过滤，默认全部都会打印
                        // return false; // 如果是false 就全部都不会打印
                        return !"三鹿".equals(s);
                    }
                })
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: " + s);
            }
        });
    }

    public void click02(View view) {
        //定时器 运行 只有再定时器运行基础上 加上take过滤操作符，才有take过滤操作符的价值
    //用这个可以替代线程池 轮询执行的功能。
        Observable.interval(100, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
                // 增加过滤操作符，停止定时器
                .take(8)// 执行次数达到8 停止下来
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
//                        Log.d(TAG, "下游 subscribe: " + Thread.currentThread().getName());
                        Log.d(TAG, "accept: " + aLong);
                    }
                });
    }
}

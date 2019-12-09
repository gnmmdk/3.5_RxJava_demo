package com.kangjj.rxjava.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * @Description: RxJava创建型操作符
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo
 * @CreateDate: 2019/12/9 15:21
 */
public class Rx02Activity extends AppCompatActivity {
    private static final String TAG = Rx02Activity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx02);
    }


    /**
     * fromArray 操作符 创建 Observable
     * @param view
     */
    public void click01(View view) {
        String[] strs = {"张三","李四","王五"};
       /* Observable.fromArray(strs).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {

            }
        });*/
       Observable.fromArray(strs)
               .subscribe(new Consumer<String>() {
                   @Override
                   public void accept(String s) throws Exception {
                       Log.d(TAG, "onNext: " + s);
                   }
               });
    }

    /**
     * 为什么只支持Object ？
     * 上游没有发射有值得事件，下游无法确定类型，默认Object，RxJava泛型 泛型默认类型==Object
     *
     * 做一个耗时操作，不需要任何数据来刷新UI， empty的使用场景之一
     *
     * @param view
     */
    public void click02(View view) {
        Observable.empty().subscribe(new Observer<Object>() { // 内部一定会只调用 发射 onComplete 完毕事件
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                // 没有事件可以接受
                Log.d(TAG, "onNext: " + o);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
                // 隐藏 加载框...
            }
        });
//        Observable.empty().subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(Object o) throws Exception {
//                // 接受不到
//                // 没有事件可以接受
//            }
//        });
    }

    public void click03(View view) {
        Observable.range(80,5)// 80开始  80 81 82 83 84  加    数量共5个
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer);
                    }
                });
    }
}

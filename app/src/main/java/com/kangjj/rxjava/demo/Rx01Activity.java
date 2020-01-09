package com.kangjj.rxjava.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kangjj.rxjava.demo.observer_pattern.ObservableImpl;
import com.kangjj.rxjava.demo.observer_pattern.ObserverImpl;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo
 * @CreateDate: 2019/12/9 15:21
 */
public class Rx01Activity extends AppCompatActivity {
    private final String TAG = Rx01Activity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx01);
    }

    public void click01(View view) {
        // 上游 Observable 被观察者
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 发射
                Log.d(TAG, "上游 subscribe: 开始发射..."); // todo 2
                emitter.onNext("RxJavaStudy");
                emitter.onComplete();
                // 上游的最后log才会打印
                Log.d(TAG, "上游 subscribe: 发射完成");
            }
        }).subscribe(
                new Observer<String>(){
            @Override
            public void onSubscribe(Disposable d) {
                // 弹出 加载框 ....
                Log.d(TAG, "上游和下游订阅成功 onSubscribe 1"); // todo 1
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "下游接收 onNext: " + s); // todo 3
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                // 隐藏加载框
                Log.d(TAG, "下游接收完成 onComplete"); // todo 5  只有接收完成之后，上游的最后log才会打印
            }
            }
        );
//        D/Rx01Activity: 上游和下游订阅成功 onSubscribe 1
//        D/Rx01Activity: 上游 subscribe: 开始发射...
//        D/Rx01Activity: 下游接收 onNext: RxJavaStudy
//        D/Rx01Activity: 下游接收完成 onComplete
//        D/Rx01Activity: 上游 subscribe: 发射完成
    }

    public void click02(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 发射
                Log.d(TAG, "上游 subscribe: 开始发射..."); // todo 2
                emitter.onNext("RxJavaStudy");
//                emitter.onComplete();
                // 上游的最后log才会打印
//                Log.d(TAG, "上游 subscribe: 发射完成");
                // TODO 结论：在 onComplete();/onError 发射完成 之后 再发射事件  下游不再接收上游的事件
                /*emitter.onNext("a");
                emitter.onNext("b");
                emitter.onNext("c");*/
                // 发一百个事件
                emitter.onError(new IllegalAccessException("error rxJava")); // 发射错误事件
                emitter.onComplete(); // 发射完成
                // TODO 结论：已经发射了onComplete();， 再发射onError RxJava会报错，不允许
                // TODO 结论：先发射onError，再onComplete();，不会报错， 有问题（onComplete不会接收到了）
            }
        }).subscribe(
                new Observer<String>(){
                    @Override
                    public void onSubscribe(Disposable d) {
                        // 弹出 加载框 ....
                        Log.d(TAG, "上游和下游订阅成功 onSubscribe 1"); // todo 1
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "下游接收 onNext: " + s); // todo 3
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "下游接收 onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 隐藏加载框
                        Log.d(TAG, "下游接收完成 onComplete"); // todo 5  只有接收完成之后，上游的最后log才会打印
                    }
                }
        );
    }

    Disposable disposable;
    /**
     * 切断下游，让下游不再接收上游的事件，也就是说不会去更新UI
     * @param view
     */
    public void click03(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
               disposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "下游接收 onNext: " + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable!=null){
            disposable.dispose();
        }
    }

    public void click04(View view) {//RX 与观察者模式的区别是， Rx是一个被观察者和一个观察者 观察者模式是一个被观察者和多个观察者
        com.kangjj.rxjava.demo.observer_pattern.Observer observer1= new ObserverImpl(); // 警察1  - 观察者
        com.kangjj.rxjava.demo.observer_pattern.Observer observer2= new ObserverImpl(); // 警察2  - 观察者
        com.kangjj.rxjava.demo.observer_pattern.Observer observer3= new ObserverImpl(); // 警察3  - 观察者
        com.kangjj.rxjava.demo.observer_pattern.Observer observer4= new ObserverImpl(); // 警察4  - 观察者
        com.kangjj.rxjava.demo.observer_pattern.Observer observer5= new ObserverImpl(); // 警察5  - 观察者
        // 一个小偷  被观察者
        com.kangjj.rxjava.demo.observer_pattern.Observable observable = new ObservableImpl();
        // 关联 注册
        observable.registerObserver(observer1);
        observable.registerObserver(observer2);
        observable.registerObserver(observer3);
        observable.registerObserver(observer4);
        observable.registerObserver(observer5);
        // 小偷发生改变(被观察者)
        observable.notifyObservers();
    }
}

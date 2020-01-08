package com.kangjj.custom.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class TestActivity extends AppCompatActivity {

    private final String TAG = TestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void click01(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> emitter) {
                Log.d(TAG, "subscribe: 上游开始发射...");
                // 发射事件  可写的
                // todo 使用者去调用发射 2
                emitter.onNext(1);//<? extends Integer>不可写 <? super Integer>可写 不完全可读
                emitter.onNext(2);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe() {
                // todo 1
                Log.d(TAG, "已经订阅成功，即将开始发射 onSubscribe: ");
            }

            // 接口的实现方法
            @Override
            public void onNext(Integer item) {
                // todo 3
                Log.d(TAG, "下游接收事件 onNext: " + item);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                // todo 4 最后一步
                Log.d(TAG, "onComplete: 下游接收事件完成√√√√√√√√√√√√√√");
            }
        });
    }

    public void click02(View view) {

        Observable.just("A", "B", "C", "D", "E", "F", "G")
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe() {
                        // todo 1
                        Log.d(TAG, "已经订阅成功，即将开始发射 onSubscribe: ");
                    }

                    @Override
                    public void onNext(String item) {
                        // todo 3
                        Log.d(TAG, "下游接收事件 onNext: " + item);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        // todo 4
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    public void click03(View view) {
        // 给同学们新增加的
        // todo Test fromArray
        String[] strings = {"三分归元气", "风神腿", "排云掌"};
        Observable.fromArray(strings)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe() {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(String item) {
                        Log.d(TAG, "onNext: " + item);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: e:" + e.getMessage() );
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });


        Log.d(TAG, "test2: -------------------------------------------------");


        String[] strings_1 = {"乔峰", "段誉", "虚竹"};
        String[] strings_2 = {"降龙十八掌", "六脉神剑", "逍遥派武功"};
        String[] strings_3 = {"男主角1", "男主角2", "男主角3"};

        Observable.fromArray(strings_1, strings_2, strings_3)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe() {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(String item) {
                        Log.d(TAG, "onNext: " + item);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: e:" + e.getMessage() );
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    public void click04(View view) {
        //todo create map 了几次就生成了对应的Observable，而真正生成Observable的地方new Observable<T>(source)，
        // 都要传入source，source就是ObservableOnSubscribe。
        // todo A 查看Map的流程要先从subscrbie方法看起来
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> emitter) {
                emitter.onNext(2);
                emitter.onComplete();
            }
        })
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        Log.d(TAG, "第1个变换 apply: " + integer);
                        return "【" + integer + "】";
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe() {
                        Log.d(TAG, "已经订阅成功，即将开始发射 onSubscribe: ");
                    }

                    @Override
                    public void onNext(String integer) {
                        Log.d(TAG, "下游接收事件 onNext: " + integer); // 【9】-----------------------
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: 下游接收事件完成√√√√√√√√√√√√√√");
                    }
                });
    }

    public void click05(View view) {
    }

    public void click06(View view) {
    }
}

package com.kangjj.rxjava.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * @Description: 合并型操作符
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo
 * @CreateDate: 2019/12/9 15:21
 */
public class Rx06Activity extends AppCompatActivity {
    private static final String TAG = Rx06Activity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx06);
    }

    /**
     * startWith 合并操作符, 被观察者1.startWith(被观察者2):
     * 先执行被观察者2 里面发射的事件，然后再执行 被观察者1 发射的事件
     * @param view
     */
    public void click01(View view) {
        Observable.just(1,2,3)
                .startWith(Observable.just(100,200,300,400))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: "+integer);
                    }
                });
    }

    /**
     * concatWith 和 startWith 的区别，是相反的
     * concatWith 合并操作符, 被观察者1.concatWith(被观察者2) 先执行 被观察者1 里面发射的事件，然后再执行 被观察者2 发射的事件
     * @param view
     */
    public void click02(View view) {
        Observable.just(1,2,3)
                .concatWith(Observable.just(100,200,300,400))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: "+integer);
                    }
                });
    }

    /**
     * concat 合并操作符 的特性：最多能够合并四个，按照我们存入的顺序执行
     * @param view
     */
    public void click03(View view) {
        Observable.concat(Observable.just("1"),Observable.just("2"),Observable.just("3"),Observable.just("4"))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: "+s);
                    }
                });
    }

    /**
     * merge 合并操作符 的特性：最多能够合并四个，并列执行 并发
     * @param view
     */
    public void click04(View view) {
        //start:开始累计，count累计多个数量，initDelay开始等待时间，period每隔多久执行，TimeUnit时间单位
        Observable observable1 = Observable.intervalRange(1,5,1,2, TimeUnit.SECONDS);// 1 2 3 4 5
        Observable observable2 = Observable.intervalRange(6, 5, 1, 2, TimeUnit.SECONDS);// 6 7 8 9 10
        Observable observable3 = Observable.intervalRange(11, 5, 1,2, TimeUnit.SECONDS); // 11 12 13 14 15
        Observable.merge(observable1,observable2,observable3)
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.d(TAG, "accept: "+o);
                    }
                });;
    }

    /**
     * zip 合并操作符：合并的被观察者发射的事件，需要对应
     * 需求：考试 课程 == 分数
     * @param view
     */
    public void click05(View view) {
        // 课程 被观察者
        Observable observable1 = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                e.onNext("英语"); // String
                e.onNext("数学");
                e.onNext("政治");
                e.onNext("物理");  // 被忽略
                e.onComplete();
            }
        });
        // 分数 被观察者
        Observable observable2 = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                e.onNext(85); // Integer
                e.onNext(90);
                e.onNext(96);
                e.onComplete();
            }
        });
        Observable.zip(observable1, observable2, new BiFunction<String,Integer,StringBuffer>() {
            @Override
            public StringBuffer apply(String str, Integer integer) throws Exception {
                return new StringBuffer().append("课程 " + str).append("==").append(integer+"");
            }
        }).subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: 准备进入考场，考试了....");
            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "onNext: 考试结果输出 " + o);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: 考试全部完毕");
            }
        });
    }
}

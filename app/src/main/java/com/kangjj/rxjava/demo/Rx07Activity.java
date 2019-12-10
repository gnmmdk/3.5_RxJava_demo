package com.kangjj.rxjava.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @Description: 异常处理操作符，在RxJava使用的时候 的异常处理，提供操作符
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo
 * @CreateDate: 2019/12/9 15:21
 */
public class Rx07Activity extends AppCompatActivity {

    private final String TAG = Rx07Activity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx07);
    }

    /**
     * onErrorReturn异常操作符：1.能够接收e.onError，  2.如果接收到异常，会中断上游后续发射的所有事件
     * @param view
     */
    public void click01(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {
                    if(i==5){
                        // RxJava中是不标准的
//                        throw new IllegalAccessError("我要报错了");
                        e.onError(new IllegalAccessError("我要报错了"));
                    }
                    e.onNext(i);
                }
                e.onComplete();
            }
        })
        // 在上游 和 下游之间 添加异常操作符
        .onErrorReturn(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable throwable) throws Exception {
                Log.d(TAG, "onErrorReturn: " + throwable.getMessage());
                return 400;// 400代表有错误，给下一层，目前 下游 观察者
            }
        }).subscribe(new Observer<Integer>(){
            @Override
            public void onSubscribe(Disposable d) {

            }
            // 如果使用了 异常操作符 onNext: 400
            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer); // 400
            }
            // 如果不使用 异常操作符 onError
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }

    /**
     * onErrorResumeNext 异常操作符： 1. 能够接收e.onError
     * onErrorReturn 可以返回标识 400 对比 onErrorResumeNext可以返回被观察者（被观察者可以再次发射多次事件给 下游）
     * @param view
     */
    public void click02(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {
                    if( i == 5){
                        e.onError(new Error("错误"));
                    }else {
                        e.onNext(i);
                    }
                }
                e.onComplete();
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> apply(Throwable throwable) throws Exception {
                return Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        e.onNext(400);
                        e.onNext(400);
                        e.onComplete();
                    }
                });
            }
        }).subscribe(new Observer<Integer>() { // 下游
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override               //结果是  0 1 2 3 4 400 400
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }

    /**
     * exception
     * onExceptionResumeNext 操作符，能在发生异常的时候，扭转乾坤，（这种错误一定是可以接受的，才这样使用）
     * 慎用：自己去考虑，是否该使用
     * @param view
     */
    public void click03(View view) {
        // 上游
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {
                    if (i == 3) {
                       /* throw new 其他Exception("错了");
                        throw new IllegalAccessException("错了");*/
//                        throw new Exception("错了");// 0 1 2 404 两种方式最后都不会走到观察者的onComplete，但是e.onComplet是会执行到。
                         e.onError(new IllegalAccessException("错了")); // 异常事件 0 1 2 404
                    } else {
                        e.onNext(i);
                    }
                }

                Log.d(TAG, "e.onComplete: ");
                e.onComplete(); // 一定要最后执行

                /**
                 * e.onComplete();
                 * e.onError
                 * 会报错
                 */
            }
        }).onExceptionResumeNext(new ObservableSource<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> observer) {
                observer.onNext(404); // 可以让程序 不崩溃的
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }

    /**
     * retry 重试操作符 异常处理操作符中 throw 和onError 都可以
     * @param view
     */
    public void click04(View view){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {
                    if (i == 5) {
                       /* throw new 其他Exception("错了");
                        throw new IllegalAccessException("错了");*/
//                         throw new Exception("错了");

                        // rxJava标准的
                        e.onError(new IllegalAccessException("错了")); // 异常事件
                    } else {
                        e.onNext(i);
                    }
                }
                e.onComplete(); // 一定要最后执行
            }
        })
                // todo 演示一
               /* .retry(new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) throws Exception {
                        Log.d(TAG, "retry: " + throwable.getMessage());
//                        return false;// 代表不去重试
                        return true; // 一直重试，不停的重试
                    }
                })*/
                // todo 演示二 重试次数
              /* .retry(3, new Predicate<Throwable>() {
                   @Override
                   public boolean test(Throwable throwable) throws Exception {
                       Log.d(TAG, "retry: " + throwable.getMessage());
                       return true;
                   }
               })*/
                // todo 演示三 打印重试了多少次，计数     Throwable  +  count
              .retry(new BiPredicate<Integer, Throwable>() {
                  @Override
                  public boolean test(Integer integer, Throwable throwable) throws Exception {
                      Thread.sleep(1000);
                      Log.d(TAG, "retry: 已经重试了:" + integer + "次  e：" + throwable.getMessage());
                      return true; // 重试
                  }
              })
                .subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }
}

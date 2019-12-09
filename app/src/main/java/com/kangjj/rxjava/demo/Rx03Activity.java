package com.kangjj.rxjava.demo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

/**
 * @Description: 变换 操作符 map flapMap concatMap groupBy buffer
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo
 * @CreateDate: 2019/12/9 15:21
 */
public class Rx03Activity extends AppCompatActivity {

    private static final String TAG = Rx04Activity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx03);
    }

    /**
     *  map 变换 操作符
     * @param view
     */
    public void click01(View view) {
        Observable.just(1)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        // 1
                        Log.d(TAG, "map1 apply: " + integer);

                        return "【" + integer + "】";
                    }
                })
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String strs) throws Exception {
                        // s == 【" + integer + "】
                        Log.d(TAG, "map2 apply: " + strs);
                        return Bitmap.createBitmap(1920, 1280, Bitmap.Config.ARGB_8888);
                    }
                }).subscribe(new Observer<Bitmap>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Bitmap bitmap) {
                Log.d(TAG, "下游 onNext: " + bitmap);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * flatMap 变换 操作符
     * @param view
     */
    @SuppressLint("CheckResult")
    public void click02(View view) {
        Observable.just(11)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final Integer integer) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> e) throws Exception {
                                e.onNext(integer + " flatMap变换操作符");
                                e.onNext(integer + " flatMap变换操作符");
                                e.onNext(integer + " flatMap变换操作符");
                            }
                        });
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String string) throws Exception {
                Log.d(TAG, "下游接收 变换操作符 发射的事件 accept: " + string);
            }
        });
    }

    /**
     * 体现 flatMap 变换 操作符 是不排序的
     * @param view
     */
    public void click03(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("步惊云"); // String
                e.onNext("雄霸");
                e.onNext("李四");
                e.onComplete();
            }
        }).flatMap(new Function<String, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(String s) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add(s + " 下标：" + (1 + i));
                }
                return Observable.fromIterable(list).delay(5, TimeUnit.SECONDS);// 创建型操作符，创建被观察者
            }
        }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "下游 onNext: " + o);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 体现 concatMap 变换操作符 排序的
     * @param view
     */
    public void click04(View view) {
        Observable.just("A","B","C")
                .concatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(String s) throws Exception {
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add(s + " 下标：" + (1 + i));
                        }
                        return Observable.fromIterable(list).delay(5,TimeUnit.SECONDS);
                    }
                }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Log.d(TAG, "accept: " + o);
            }
        });
    }

    /**
     * 分组变换 groupBy
     * @param view
     */
    public void click05(View view) {
        Observable.just(6000, 7000, 8000, 9000, 10000, 14000)
                .groupBy(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return integer>8000?"高端配置电脑":"中端配置电脑";
                    }
                }).subscribe(new Consumer<GroupedObservable<String, Integer>>() {
            @Override
            public void accept(final GroupedObservable<String, Integer> groupedObservable) throws Exception {
                Log.d(TAG, "accept: "+groupedObservable.getKey());//这里无法getValue
                // 以上还不能把信息给打印全面，只是拿到了，分组的key

                // 输出细节，还需要再包裹一层
                // 细节 GroupedObservable 被观察者
                groupedObservable.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: 类别：" + groupedObservable.getKey() + "  价格：" + integer);
                    }
                });
            }
        });
    }

    /**
     * 很多的数据，不想全部一起发射出去，分批次，先缓存到Buffer
     * @param view
     */
    public void click06(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {
                    e.onNext(i);
                }
                e.onComplete();
            }
        })
        .buffer(20)
        .subscribe(new Consumer<List<Integer>>() {      //注意 变成List<Integer>
            @Override
            public void accept(List<Integer> integer) throws Exception {
                Log.d(TAG, "accept: " + integer);
            }
        });
//        accept: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19]
//        accept: [20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39]
//        accept: [40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59]
//        accept: [60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79]
//        accept: [80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99]
    }
}

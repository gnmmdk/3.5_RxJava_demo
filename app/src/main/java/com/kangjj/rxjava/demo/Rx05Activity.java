package com.kangjj.rxjava.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * @Description: 条件 操作符
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo
 * @CreateDate: 2019/12/9 15:21
 */
public class Rx05Activity extends AppCompatActivity {
    private final String TAG = Rx05Activity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx05);
    }

    /**
     *  all，如同 if 那样的功能 ：全部为true，才是true，只要有一个为false，就是false
     * @param view
     */
    public void click01(View view) {
        String v1 = "1";
        String v2 = "2";
        String v3 = "3";
        String v4 = "cc";
        String v5 = "cc";
        // 需求：只要有一个为 cc的，就是true 不看if 感觉会乱，只要记住全部为true，才是true，只要有一个为false，就是false
        if (v1.equals("cc") && v2.equals("cc") && v3.equals("cc") && v4.equals("cc")&& v4.equals("cc")) {
            Log.d(TAG, "r01: " + true);
        }else{
            Log.d(TAG, "r01: " + false);
        }
        // RxJava的写法
        Observable.just(v1,v2,v3,v4,v5) // RxJava 2.X 之后 不能传递null，否则会报错
                .all(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        Log.d(TAG, "test: "+s);
                        return s.equals("cc");         //todo 只要return的值返回false，下方就不会再执行了
                        //todo s.equals("cc") 这里 打印test：1  因为第一个为"1".equals("cc")为false了，所以后面不再执行
                        //todo !s.equals("cc") 这里会打印 test：1 test：2 ... test:cc 。v5的cc不会打印，因为到v4的时候!"cc".equals("cc") 返回了false
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean s) throws Exception {
                Log.d(TAG, "accept: "+s);
            }
        });

    }

    /**
     * contains 是否包含
     * @param view
     */
    public void click02(View view) {
        Observable.just("JavaSE", "JavaEE", "JavaME", "Android", "iOS", "Rect.js", "NDK")
                .contains("Java")
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "accept: "+aBoolean);//accept: false  因为"Java".equals("item项目")，所以是false
                    }
                });
    }

    /**
     *  Any 和 All相反的，All全部为true，才是true，只要有一个为false，就是false
     *  any 全部为 false，才是false， 只要有一个为true，就是true
     * @param view
     */
    public void click03(View view) {
        Observable.just("JavaSE", "JavaEE", "JavaME", "Android", "iOS", "Rect.js", "NDK")
            .any(new Predicate<String>() {
                @Override
                public boolean test(String s) throws Exception {
                    Log.d(TAG, "test: "+s);
                    return s.equals("Android");
                }
            }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean s) throws Exception {
                Log.d(TAG, "accept: " + s);
            }
        });
    }
}

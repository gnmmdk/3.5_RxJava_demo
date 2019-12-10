package com.kangjj.rxjava.demo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kangjj.rxjava.demo.retrofit_rxjava.IRequestNetwork;
import com.kangjj.rxjava.demo.retrofit_rxjava.LoginRequest;
import com.kangjj.rxjava.demo.retrofit_rxjava.LoginResponse;
import com.kangjj.rxjava.demo.retrofit_rxjava.RegisterRequest;
import com.kangjj.rxjava.demo.retrofit_rxjava.RegisterResponse;
import com.kangjj.rxjava.demo.retrofit_rxjava.RetrofitManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description: todo 伪代码 无法运行
 *  * Retrofit + RxJava
 *  * 需求：
 *  * 1.请求服务器注册操作
 *  * 2.注册完成之后，更新注册UI
 *  * 3.马上去登录服务器操作
 *  * 4.登录完成之后，更新登录的UI
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo
 * @CreateDate: 2019/12/9 15:21
 */
public class Rx11Activity extends AppCompatActivity {
    private final String TAG = Rx11Activity.class.getSimpleName();

    private TextView tv_register_ui;
    private TextView tv_login_ui;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx11);
        tv_register_ui = findViewById(R.id.tv_login_ui);
        tv_login_ui = findViewById(R.id.tv_login_ui);
    }
    private ProgressDialog progressDialog;
    public void request(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在执行中...");
        /**
         * 一行代码 实现需求
         * 需求：
         *  * 1.请求服务器注册操作
         *  * 2.注册完成之后，更新注册UI
         *  * 3.马上去登录服务器操作
         *  * 4.登录完成之后，更新登录的UI
         */
        RetrofitManager.createRetrofit().create(IRequestNetwork.class)
                //  1.请求服务器注册操作  // todo 第二步 请求服务器 注册操作
                .registerAction(new RegisterRequest())// Observable<RegisterResponse> 上游 被观察者 耗时操作
                .subscribeOn(Schedulers.io()) // todo 给上游分配异步线程
                .observeOn(AndroidSchedulers.mainThread()) // todo 给下游切换 主线程
                // 2.注册完成之后，更新注册UI

                /**
                 *  这样不能订阅，如果订阅了，就无法执行
                 *      3 马上去登录服务器操作
                 *      4.登录完成之后，更新登录的UI
                 *
                 *  所以我们要去学习一个 .doOnNext()，可以在不订阅的情况下，更新UI
                 */
                .doOnNext(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse registerResponse) throws Exception {
                        // todo 第三步 更新注册相关的所有UI
                        // 更新注册相关的所有UI
                        tv_register_ui.setText("xxx");
                    }
                })
                // 3.马上去登录服务器操作 -- 耗时操作
                .subscribeOn(Schedulers.io()) // todo 给上游分配异步线程
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
                    @Override
                    public ObservableSource<LoginResponse> apply(RegisterResponse registerResponse) throws Exception {
                        Observable<LoginResponse> observable = RetrofitManager.createRetrofit().create(IRequestNetwork.class).loginAction(new LoginRequest());
                        return observable;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // todo 给下游切换 主线程
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // todo 第一步
                        progressDialog.show();
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        // 更新登录相关的所有UI
                        // todo 第五步 更新登录相关的所有UI
                        tv_login_ui.setText("xxxx");
                        // ...........
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        // todo 第六步
                        progressDialog.dismiss(); // 结束对话框 ，整个流程完成
                    }
                });
    }
}

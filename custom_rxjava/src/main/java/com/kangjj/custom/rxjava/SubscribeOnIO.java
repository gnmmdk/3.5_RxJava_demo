package com.kangjj.custom.rxjava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.custom.rxjava
 * @CreateDate: 2020/1/9 14:23
 */
public class SubscribeOnIO<T> implements ObservableOnSubscribe<T> {

    private final ExecutorService EXECUTOR_SERVER = Executors.newCachedThreadPool();

    //我的上一层
    private ObservableOnSubscribe<T> source;    //面向接口编程

    public SubscribeOnIO(ObservableOnSubscribe<T> source){
        this.source = source;
    }

    @Override
    public void subscribe(final Observer<? super T> observableEmitter) {
        // 开始异步切换了 一层层向上传递
        EXECUTOR_SERVER.submit(new Runnable(){
            @Override
            public void run() {
                source.subscribe(observableEmitter);
            }
        });
    }
}

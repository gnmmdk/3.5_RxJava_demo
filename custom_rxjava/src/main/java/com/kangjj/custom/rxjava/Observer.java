package com.kangjj.custom.rxjava;

/**
 * @Description: 观察者
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.custom.rxjava
 * @CreateDate: 2019/12/11 15:28
 */
public interface Observer<T> {

    void onSubscribe();

    void onNext(T t);

    void onError(Throwable e);

    void onComplete();
}

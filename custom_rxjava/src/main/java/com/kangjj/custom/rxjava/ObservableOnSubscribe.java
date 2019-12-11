package com.kangjj.custom.rxjava;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.custom.rxjava
 * @CreateDate: 2019/12/11 15:32
 */
public interface ObservableOnSubscribe<T> {
    //? super 代表可写 observableEmitter == 观察者
    void subscribe(Observer<? super T> emitter);
}

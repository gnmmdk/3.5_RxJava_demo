package com.kangjj.rxjava.demo.observer_pattern;

/**
 * @Description: 被观察者标准
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo.observer_pattern
 * @CreateDate: 2019/12/9 15:42
 */
public interface Observable {
    /**
     * 在观察者中  注册 观察者
     * @param observer
     */
    void registerObserver(Observer observer);

    /**
     * 在被观察者中 移除 观察者
     * @param observer
     */
    void removeObserver(Observer observer);

    /**
     * 在被观察者中 通知 所有注册的 观察者
     */
    void notifyObservers();
}

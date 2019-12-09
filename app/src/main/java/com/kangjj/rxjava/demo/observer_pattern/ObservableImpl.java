package com.kangjj.rxjava.demo.observer_pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Observeable 运行流程与观察者模式
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo.observer_pattern
 * @CreateDate: 2019/12/9 15:44
 */
public class ObservableImpl implements Observable {
    private List<Observer> observers = new ArrayList<>();
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            //在悲观者实现类中，通知所有注册号的观察者
            observer.changeAction("被观察者 发生了改变...");
        }
    }
}

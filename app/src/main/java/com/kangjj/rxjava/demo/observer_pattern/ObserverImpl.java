package com.kangjj.rxjava.demo.observer_pattern;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo.observer_pattern
 * @CreateDate: 2019/12/9 15:48
 */
public class ObserverImpl implements Observer{
    @Override
    public <T> void changeAction(T observableInfo) {
        System.out.println(observableInfo);
    }
}

package com.kangjj.rxjava.demo.observer_pattern;

/**
 * @Description: 观察者标准
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo.observer_pattern
 * @CreateDate: 2019/12/9 15:45
 */
public interface Observer {

    <T> void changeAction(T observableInfo);
}

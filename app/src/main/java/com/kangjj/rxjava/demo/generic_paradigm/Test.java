package com.kangjj.rxjava.demo.generic_paradigm;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo.generic_paradigm
 * @CreateDate: 2019/12/10 14:13
 */
public class Test<T> {
    private T t;

    public T getT() {
        return t;
    }

    public void add(T t) {
        this.t = t;
    }
}

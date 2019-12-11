package com.kangjj.custom.rxjava;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.custom.rxjava
 * @CreateDate: 2019/12/11 20:35
 */
public interface Function<T,R> {
    R apply(T t);
}

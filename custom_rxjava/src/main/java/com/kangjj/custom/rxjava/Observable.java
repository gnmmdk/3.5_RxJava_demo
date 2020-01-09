package com.kangjj.custom.rxjava;

/**
 * @Description: 被观察者
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.custom.rxjava
 * @CreateDate: 2019/12/11 15:30
 */
public class Observable<T> {           //类声明的泛型T Int
    ObservableOnSubscribe<T> source;

    private Observable(ObservableOnSubscribe source){
        this.source = source;
    }

    /**
     * @param source
     * @param <T>
     * @return
     *///参数中：ObservableOnSubscribe<? extends T>和可读可写没有任何关系，还是我们之前的那套思想（上限和下限）
    public static <T> Observable<T> create(ObservableOnSubscribe<? extends T> source){
        return new Observable<T>(source);
    }

    /**
     * just内部发射
     * @param ts 可变参数
     * @param <T>
     * @return
     */
    public static <T> Observable<T> just(final T... ts){
        // 想办法让 source 是不为null的，  而我们的create操作符是，使用者自己传递进来的
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> emitter) {
                for (T t : ts) {

                    // 发射用户传递的参数数据 去发射事件
                    emitter.onNext(t);
                }
                emitter.onComplete();
            }
        });
    }

    public static <T> Observable<T> just(final T t){
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> emitter) {
                emitter.onNext(t);
                emitter.onComplete();
            }
        });
    }

    public static <T> Observable<T> just(final T t1,final T t2){
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> emitter) {
                emitter.onNext(t1);
                emitter.onNext(t2);
                emitter.onComplete();
            }
        });
    }

    public static <T> Observable<T> just(final T t1,final T t2,final T t3){
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> emitter) {
                emitter.onNext(t1);
                emitter.onNext(t2);
                emitter.onNext(t3);
                emitter.onComplete();
            }
        });
    }

    public static <T> Observable<T> just(final T t1,final T t2,final T t3,final T t4){
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> emitter) {
                emitter.onNext(t1);
                emitter.onNext(t2);
                emitter.onNext(t3);
                emitter.onNext(t4);
                emitter.onComplete();
            }
        });
    }

    public static <T> Observable<T> just(final T t1,final T t2,final T t3,final T t4,final T t5){
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> emitter) {
                emitter.onNext(t1);
                emitter.onNext(t2);
                emitter.onNext(t3);
                emitter.onNext(t4);
                emitter.onNext(t5);
                emitter.onComplete();
            }
        });
    }

    /**
     * Function<? super T -》方法里面没有定义 说明这个T就是类里面的T
     * @param function
     * @param <R>
     * @return
     */
    public <R> Observable<R> map(Function<? super T,? extends R> function){
        //todo 这里的ObservableMap不是Obervable,而是ObservableOnSubscribe，传入的source也是ObservableOnSubscribe
        ObservableMap observableMap = new ObservableMap(source,function);
        return new Observable<R>(observableMap);
    }

//    public <R> Observable<R> map(Function<T,R> function){todo 泛型的 super extends
//        //todo 这里的ObservableMap不是Obervable,而是ObservableOnSubscribe，传入的source也是而是ObservableOnSubscribe
//        ObservableMap observableMap = new ObservableMap(source,function);
//        return new Observable<R>(observableMap);
//    }

    // 参数中：Observer<? extends T> 和可读可写模式没有任何关系，还是我们之前的那一套思想（上限和下限）
    public void subscribe(Observer<T> observer){

        observer.onSubscribe();

        source.subscribe(observer);
    }

}

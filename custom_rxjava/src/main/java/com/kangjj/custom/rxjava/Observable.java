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
        //todo A.2.2 这个source就是Observable.create的时候传递进来的
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
    // 给同学们新增加的
    // fromArray
    public static <T> Observable<T> fromArray(final T[] ts) {
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> observableEmitter) {
                // 根据使用者 传递的参数 分发事件下去
                for (T t : ts) {
                    observableEmitter.onNext(t);
                }
                // 分发完毕的事件
                observableEmitter.onComplete();
            }
        });
    }

    // 给同学们新增加的
    // fromArray
    public static <T> Observable<T> fromArray(final T[] ts, final T[] ts2) {
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> observableEmitter) {
                // 根据使用者 传递的参数 分发事件下去
                for (T t : ts) {
                    observableEmitter.onNext(t);
                }
                // 根据使用者 传递的参数 分发事件下去
                for (T t : ts2) {
                    observableEmitter.onNext(t);
                }
                // 分发完毕的事件
                observableEmitter.onComplete();
            }
        });
    }

    // 给同学们新增加的
    // fromArray
    public static <T> Observable<T> fromArray(final T[] ts, final T[] ts2, final T[] ts3) {
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> observableEmitter) {
                // 根据使用者 传递的参数 分发事件下去
                for (T t : ts) {
                    observableEmitter.onNext(t);
                }
                // 根据使用者 传递的参数 分发事件下去
                for (T t : ts2) {
                    observableEmitter.onNext(t);
                }
                // 根据使用者 传递的参数 分发事件下去
                for (T t : ts3) {
                    observableEmitter.onNext(t);
                }
                // 分发完毕的事件
                observableEmitter.onComplete();
            }
        });
    }

    // 给同学们新增加的
    // fromArray
    public static <T> Observable<T> fromArray(final T[] ts, final T[] ts2, final T[] ts3, final T[] ts4) {
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> observableEmitter) {
                // 根据使用者 传递的参数 分发事件下去
                for (T t : ts) {
                    observableEmitter.onNext(t);
                }
                // 根据使用者 传递的参数 分发事件下去
                for (T t : ts2) {
                    observableEmitter.onNext(t);
                }
                // 根据使用者 传递的参数 分发事件下去
                for (T t : ts3) {
                    observableEmitter.onNext(t);
                }
                // 根据使用者 传递的参数 分发事件下去
                for (T t : ts4) {
                    observableEmitter.onNext(t);
                }
                // 分发完毕的事件
                observableEmitter.onComplete();
            }
        });
    }

    // 给同学们新增加的
    // fromArray
    public static <T> Observable<T> fromArray(final T[]... ts) {
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> observableEmitter) {
                // 根据使用者 传递的参数 分发事件下去
                for (T[] t : ts) {
                    for (T t1 : t) {
                        observableEmitter.onNext(t1);
                    }
                }
                // 分发完毕的事件
                observableEmitter.onComplete();
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
        //todo 这里的ObservableMap不是Obervable,而是ObservableOnSubscribe，传入的source也是而是ObservableOnSubscribe
        //todo A.2.1 这里的source是谁传进来的 ，是上一层调用到map的Observable传进来的(有可能是Map，有可能FlapMap，也有可能是Create)，
        // 在Click4方法里面对应到了Create方法。去create方法看下
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
        // todo A.1 要明确这里的source是什么，是跟subscrbier最接近的操作符创建的ObservableOnSubscribe
        //  在这里（TestActivity的click04方法中）也就是ObservableMap，所以这里的subscribe会执行到
        //  ObservableMap中的subscribe方法（注意这里吧最后的observer传给进去了）
        source.subscribe(observer);
    }

    public Observable<T> subscribeIO_On(){
        return create(new SubscribeOnIO<>(source));
    }

    public Observable<T> observerAndroidMain_On() {
        ObserverOnAndroidMainThread<T> observableOnSubscribe = new ObserverOnAndroidMainThread<>(source);
        return create(observableOnSubscribe);
    }
}

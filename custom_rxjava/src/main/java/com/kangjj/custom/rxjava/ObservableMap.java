package com.kangjj.custom.rxjava;

/**
  * @Description:
  * @Author:        jj.kang
  * @Email:         345498912@qq.com
  * @ProjectName:   ${PROJECT_NAME}
  * @Package:       ${PACKAGE_NAME}
  * @CreateDate:    ${DATE} ${TIME}
 */
public class ObservableMap<T,R> implements ObservableOnSubscribe<R>{


    private final ObservableOnSubscribe<T> source;          //上一层的能力，用来调用source.subscribe
    private final Function<? super T, ? extends R> function;
//    private Observer<? super R> emitter;

    public ObservableMap(ObservableOnSubscribe<T> source, Function<? super T, ? extends R> function) {
        this.source = source;
        this.function = function;
    }

    @Override
    public void subscribe(Observer<? super R> observableEmitter) {        //emitter 最右边的观察者
//        this.emitter = emitter;

        MapObserver mapObserver = new MapObserver(observableEmitter,function);//真正的Obervable
        // todo A.2 这里的source在ObservableMap实例化时候传进来的，调用到哪里去了呢？看下Observable的Map方法。
        //  走完小步骤，知道这个source是上一层生成Observable的方法传进来的（可能是Map、Create等其他方法），
        //  这里对应到Create方法的source，所以就跑到了Click4方法的 Observer.create（ObservableOnSubscribe)里面的Subscribe
        //   该方法的subscribe执行了onNext发射数据。注意因为这里把上一行的MapObserver传递进去了，所以是它在onNext。
        source.subscribe(mapObserver);
    }

    class MapObserver<T> implements Observer<T>{
        private final Function<? super T, ? extends R> function; //可写模式<？super T> 可写，不完全可读  可读模式<? extends R> 可读，不可写

        private Observer<? super R> observableEmitter;            //右边的观察者 给下一层的类型，意思是 变换后的类型 也就是给下一层的类型R

        public MapObserver(Observer<? super R> observableEmitter, Function<? super T, ? extends R> function) {
            this.observableEmitter = observableEmitter;
            this.function = function;
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(T t) {
            //todo A.3 真正的做切换数据的操作
            /**
             * T Integer    变换     R String
             */
            R nextMapResultType = function.apply(t);            //apply 这里体现了可写 ；获取到R 体现了可读Test<? extends Person> test1 = null;  Person person = test1.get(); // 可读
            //todo A.4 这里的Observer<? super R> observableEmitter 是哪儿来的，看A.1
            // 是最后要执行的onNext方法了。
            observableEmitter.onNext(nextMapResultType);   // 调用下一层 onNext 方法
        }

        @Override
        public void onError(Throwable e) {
            observableEmitter.onError(e);
        }

        @Override
        public void onComplete() {
            observableEmitter.onComplete();
        }
    }
}

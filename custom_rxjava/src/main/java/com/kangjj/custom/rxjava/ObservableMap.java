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
            /**
             * T Integer    变换     R String
             */
            R nextMapResultType = function.apply(t);            //apply 这里体现了可写 ；获取到R 体现了可读Test<? extends Person> test1 = null;  Person person = test1.get(); // 可读

            // 调用下一层 onNext 方法
            observableEmitter.onNext(nextMapResultType);
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

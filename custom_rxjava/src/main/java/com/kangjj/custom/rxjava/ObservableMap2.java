package com.kangjj.custom.rxjava;

/**
  * @Description:
  * @Author:        jj.kang
  * @Email:         345498912@qq.com
  * @ProjectName:   ${PROJECT_NAME}
  * @Package:       ${PACKAGE_NAME}
  * @CreateDate:    ${DATE} ${TIME}
 */
public class ObservableMap2<T,R> implements ObservableOnSubscribe<R>{


    private final ObservableOnSubscribe<T> source;          //上一层的能力，用来调用source.subscribe
    private final Function<T, R> function;
//    private Observer<? super R> emitter;

    public ObservableMap2(ObservableOnSubscribe<T> source, Function<T,R> function) {
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
        private final Function<T, R> function;

        private Observer<? super R> observableEmitter;            //右边的观察者 给下一层的类型，意思是 变换后的类型 也就是给下一层的类型R

        public MapObserver(Observer<? super R> observableEmitter, Function<T, R> function) {
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
            R nextMapResultType = function.apply(t);

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

package com.kangjj.custom.rxjava;

import android.os.Handler;
import android.os.Looper;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.custom.rxjava
 * @CreateDate: 2020/1/9 14:57
 */
public class ObserverOnAndroidMainThread<T> implements ObservableOnSubscribe<T> {
    //上一层的能力
    private ObservableOnSubscribe<T> source;

    public ObserverOnAndroidMainThread(ObservableOnSubscribe source){
        this.source = source;
    }

    @Override
    public void subscribe(Observer<? super T> emitter) {
        PackageObserver packageObserver = new PackageObserver(emitter);
        source.subscribe(packageObserver);//todo 去上一层的subscribe
    }

    private final class PackageObserver<T> implements Observer<T>{

        private Observer<T> observer;   //包裹进行了保存

        public PackageObserver(Observer<T> observer){
            this.observer = observer;
        }
        // 不用做事情，不用管
        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(final T item) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {//todo 去下一层的自定义的Observer，没有自定义的话就找到下下层，一直找到最下层
                    observer.onNext(item);
                }
            });
        }

        @Override
        public void onError(final Throwable e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    observer.onError(e);
                }
            });
        }

        @Override
        public void onComplete() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    observer.onComplete();
                }
            });
        }
    }
}

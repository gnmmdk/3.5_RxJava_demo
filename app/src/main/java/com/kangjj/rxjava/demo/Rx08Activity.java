package com.kangjj.rxjava.demo;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description: RxJava线程切换的学习
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo
 * @CreateDate: 2019/12/9 15:21
 */
public class Rx08Activity extends AppCompatActivity {
    private final String TAG = Rx08Activity.class.getSimpleName();

    private ImageView imageView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx08);
        imageView =  findViewById(R.id.image);
    }

    /**
     * todo 异步线程区域
     * Schedulers.io():代表io流操作，网络操作，文件流，耗时操作
     * Schedulers.newThread() :比较常规
     * Schedulers.computation() 代表CPU大量计算 所需要的线程
     * todo main线程 主线程
     * AndroidSchedulers.mainThread() : 专门为Android main线程量身定做的
     * todo 给上游分配多次，只会在第一次切换，后面的不切换（忽略）
     * todo 给下游分配多次，每次都回去切换
     * @param view
     */
    public void click01(View view) {
        // RxJava如果不配置，默认就是主线程main todo 这句话不对，应该说RxJava如果不配置，默认就是当前线程
      /*  new Thread(new Runnable() {
            @Override
            public void run() {*/
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> e) throws Exception {
                    Log.d(TAG, "subscribe: "+Thread.currentThread().getName());
                    e.onNext("");
                }
            }).subscribeOn(Schedulers.io())             // todo 给上游配置异步线程    // 给上游分配多次，只会在第一次切换，后面的不切换了
            .subscribeOn(AndroidSchedulers.mainThread())// 被忽略
            .subscribeOn(AndroidSchedulers.mainThread())// 被忽略
            .subscribeOn(AndroidSchedulers.mainThread())// 被忽略
            .observeOn(AndroidSchedulers.mainThread())  // todo 给下游配置 安卓主线程    // 给下游分配多次，每次都会去切换
            .observeOn(AndroidSchedulers.mainThread())// 切换一次线程
            .observeOn(Schedulers.io())              // 切换一次线程
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            Log.d(TAG, "下游 subscribe: " + Thread.currentThread().getName());
                        }
                    });
          /*  }
        }).start();*/
    }

    /**
     * 同步和异步执行流程
     * @param view
     */
    public void click02(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "subscribe sync: 上游发送了一次 1 ");
                e.onNext(1);

                Log.d(TAG, "subscribe sync: 上游发送了一次 2 ");
                e.onNext(2);

                Log.d(TAG, "subscribe sync: 上游发送了一次 3 ");
                e.onNext(3);
            }
        }).subscribe(new Consumer<Integer>() { // 下游简化版
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "下游 accept sync: " + integer);
            }
        });
        Log.d(TAG, "---------------------------- 分割线 ------------------------------ ");

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "subscribe asyn: 上游发送了一次 1 ");
                e.onNext(1);

                Log.d(TAG, "subscribe asyn: 上游发送了一次 2 ");
                e.onNext(2);

                Log.d(TAG, "subscribe asyn: 上游发送了一次 3 ");
                e.onNext(3);
            }
        })
                .subscribeOn(Schedulers.io())   // 给上游分配 异步线程
                .observeOn(AndroidSchedulers.mainThread())  // 给下游分配 主线程
                .subscribe(new Consumer<Integer>() { // 下游简化版
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "下游 accept asyn: " + integer);
            }
        });
        /*12-10 10:44:46.514 6548-6548/com.kangjj.rxjava.demo D/Rx08Activity: subscribe sync: 上游发送了一次 1
        12-10 10:44:46.514 6548-6548/com.kangjj.rxjava.demo D/Rx08Activity: 下游 accept sync: 1
        12-10 10:44:46.514 6548-6548/com.kangjj.rxjava.demo D/Rx08Activity: subscribe sync: 上游发送了一次 2
        12-10 10:44:46.514 6548-6548/com.kangjj.rxjava.demo D/Rx08Activity: 下游 accept sync: 2
        12-10 10:44:46.514 6548-6548/com.kangjj.rxjava.demo D/Rx08Activity: subscribe sync: 上游发送了一次 3
        12-10 10:44:46.514 6548-6548/com.kangjj.rxjava.demo D/Rx08Activity: 下游 accept sync: 3
        12-10 10:44:46.514 6548-6548/com.kangjj.rxjava.demo D/Rx08Activity: ---------------------------- 分割线 ------------------------------
        12-10 10:44:46.524 6548-6590/com.kangjj.rxjava.demo D/Rx08Activity: subscribe asyn: 上游发送了一次 1
        12-10 10:44:46.524 6548-6590/com.kangjj.rxjava.demo D/Rx08Activity: subscribe asyn: 上游发送了一次 2
        12-10 10:44:46.524 6548-6590/com.kangjj.rxjava.demo D/Rx08Activity: subscribe asyn: 上游发送了一次 3
        12-10 10:44:46.528 6548-6548/com.kangjj.rxjava.demo D/Rx08Activity: 下游 accept asyn: 1
        12-10 10:44:46.528 6548-6548/com.kangjj.rxjava.demo D/Rx08Activity: 下游 accept asyn: 2
        12-10 10:44:46.528 6548-6548/com.kangjj.rxjava.demo D/Rx08Activity: 下游 accept asyn: 3*/
    }

    private final String PATH = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2458227883,4095122505&fm=26&gp=0.jpg";
    /**
     * 常规下载图片
     * @param view
     */
    public void click03(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在下载中...");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    URL url = new URL(PATH);
                    URLConnection urlConnection = url.openConnection();
                    HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                    httpURLConnection.setConnectTimeout(5000);
                    int responseCode = httpURLConnection.getResponseCode();
                    if(HttpURLConnection.HTTP_OK == responseCode){
                        Bitmap bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                        Message message = handler.obtainMessage();
                        message.obj = bitmap;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            imageView.setImageBitmap(bitmap);
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            return false;
        }
    });

    /**
     * todo 使用RxJava去下载图片
     * @param view
     */
    public void click04(View view) {
        Observable.just(PATH)

                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        URL url = new URL(PATH);
                        URLConnection urlConnection = url.openConnection();
                        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                        httpURLConnection.setConnectTimeout(5000);
                        int responseCode = httpURLConnection.getResponseCode();
                        if(HttpURLConnection.HTTP_OK == responseCode) {
                            Bitmap bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                            return bitmap;
                        }
                        return null;
                    }
                })
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(Bitmap bitmap) throws Exception {
                        // 给图片加水印
                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setTextSize(30);
                        Bitmap bitmapAddText = drawText2Bitmap(bitmap, "kangjj", paint, 60, 60);
                        return bitmapAddText;
                    }
                })
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(Bitmap bitmap) throws Exception {
                        Log.d(TAG, "apply: 下载的Bitmap 是这个样子的" + bitmap);
                        return bitmap;
                    }
                })
                .subscribeOn(Schedulers.io())// todo  给上游分配 异步线程
                .observeOn(AndroidSchedulers.mainThread())// todo  给下游分配 主线程
                .subscribe(new Observer<Bitmap>() {
            @Override
            public void onSubscribe(Disposable d) {
                progressDialog = new ProgressDialog(Rx08Activity.this);
                progressDialog.setMessage("RxJava下载图片中...");
                progressDialog.show();
            }

            @Override
            public void onNext(Bitmap bitmap) {
                if(imageView!=null){
                    imageView.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onError(Throwable e) {// 发生异常
                  if (imageView != null)
                  imageView.setImageResource(R.mipmap.ic_launcher); // 下载错误的图片
            }

            @Override
            public void onComplete() {
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }

    private Bitmap drawText2Bitmap(Bitmap bitmap,String text,Paint paint,int paddingLeft,int paddingTop){
        Bitmap.Config bitmapConfig = bitmap.getConfig();
        paint.setDither(true); // 获取更清晰的图像采样
        paint.setFilterBitmap(true);//过滤一些
        if(bitmapConfig == null){
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap =bitmap.copy(bitmapConfig,true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text,paddingLeft,paddingTop,paint);
        return bitmap;
    }
}

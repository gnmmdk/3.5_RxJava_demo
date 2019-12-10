package com.kangjj.rxjava.demo;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kangjj.rxjava.demo.generic_paradigm.Person;
import com.kangjj.rxjava.demo.generic_paradigm.Student;
import com.kangjj.rxjava.demo.generic_paradigm.Test;
import com.kangjj.rxjava.demo.generic_paradigm.Worker;
import com.kangjj.rxjava.demo.generic_paradigm.WorkerStub;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 泛型
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.5_RxJava_Demo
 * @Package: com.kangjj.rxjava.demo
 * @CreateDate: 2019/12/9 15:21
 */
public class Rx12Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx12);



        /*List list = new ArrayList();
        list.add("A");
        list.add(1);
        list.add(6.7);
        Object o = list.get(1); // 1  运行时，类型转换异常
        String s = (String) o; */

        // 泛型出现后
        List<String> list = new ArrayList();
        list.add("A");
        // list.add(6); // 编译期 就可以看到错误
        String s = list.get(0);

        Test<Worker> test = null;
        test.add(new Worker());         // 只能传递Worker 和WorkerStub
        test.add(new WorkerStub());

        //  下面时 上限 和 下限 的测试
        // todo 上限
//        show_extends(new Test<Object>());// Person的父类，会报错
        show_extends(new Test<Person>());
        show_extends(new Test<Student>());
        show_extends(new Test<Worker>());
        show_extends(new Test<WorkerStub>());
        // todo 下限
        show_super(new Test<Worker>());
        show_super(new Test<Person>());
        show_super(new Test<Object>());
//        show_super(new Test<Student>());      // 因为最低限制的子类 Worker，Student与Worker没关系
//        show_super(new Test<WorkerStub>());// 因为最低限制的子类 Worker，不能在低

        // todo 读写模式 读写模式就跟上方的 上限和下限完全没关系了

        // todo 可读模式
        Test<? extends Worker> test1 = null;
//        test1.add(new Person()); // 不可写
//        test1.add(new Student()); // 不可写
//        test1.add(new Object()); // 不可写
//        test1.add(new Worker()); // 不可写
        Worker worker = test1.getT();// 可读
        Person person = test1.getT();// 可读

        // todo 可写模式  不完全可读
        Test<? super Worker> test2 = null;
//        test2.add(new Person())   //不可写
        test2.add(new WorkerStub());// 可写
        test2.add(new Worker());// 可写
        Worker worker2 = (Worker) test2.getT();
        Person person2 = (Person) test2.getT();
        Object ob = test2.getT();
        WorkerStub stub = (WorkerStub) test2.getT();
    }

    /**
     * extends 上限 Person or Person的所有子类都可以，最高的类型只能是Person，把最高的类型限制住了
     * @param test
     */
    public void show_extends(Test<? extends Person> test){

    }

    /**
     * extends 下限 Worker or Worker的所有父类都可以 Worker,把最低的类型给限制住了
     * @param test
     */
    public void show_super(Test<? super Worker> test){

    }

}

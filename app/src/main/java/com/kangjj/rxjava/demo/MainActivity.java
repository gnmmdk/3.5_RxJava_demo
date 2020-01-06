package com.kangjj.rxjava.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click01(View view) {
        startActivity(new Intent(this,Rx01Activity.class));
    }

    public void click02(View view) {
        startActivity(new Intent(this,Rx02Activity.class));
    }

    public void click03(View view) {
        startActivity(new Intent(this,Rx03Activity.class));
    }

    public void click04(View view) {
        startActivity(new Intent(this,Rx04Activity.class));
    }

    public void click05(View view) {
        startActivity(new Intent(this,Rx05Activity.class));
    }

    public void click06(View view) {
        startActivity(new Intent(this,Rx06Activity.class));
    }

    public void click07(View view) {
        startActivity(new Intent(this,Rx07Activity.class));
    }

    public void click08(View view) {
        startActivity(new Intent(this,Rx08Activity.class));
    }

    public void click09(View view) {
        startActivity(new Intent(this,Rx09Activity.class));
    }

    public void click10(View view) {
        startActivity(new Intent(this,Rx10Activity.class));
    }

    public void click11(View view) {
        startActivity(new Intent(this,Rx11Activity.class));
    }

    public void click12(View view) {
        startActivity(new Intent(this,Rx12Activity.class));
    }

}

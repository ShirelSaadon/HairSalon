package com.example.hairsalon;

import android.app.Application;

import Classes.MyData;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MyData.init();
    }
}

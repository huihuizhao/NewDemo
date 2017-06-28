package com.example.newdemo;

import android.app.Application;

public class ApplicationParameters extends Application {
	private String videoPath;
    @Override
    public void onCreate() {
            super.onCreate();
            setvideoPath(PATH); //初始化全局变量
    }

	public String getvideoPath() {
		return videoPath;
	}

	public void setvideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
	private static final String PATH = "initPath";
}

package com.example.newdemo;

import android.app.Application;

public class ApplicationParameters extends Application {
	private String videoPath;

	public String getvideoPath() {
		return videoPath;
	}

	public void setvideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

}

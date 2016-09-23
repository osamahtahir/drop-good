package com.tba.dropgood;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tba.dropgood.DropGood;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//delete
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new DropGood(), config);
	}
}
//sjsjhs
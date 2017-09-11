package com.after.today;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xinaliu.testsokoban.R;

public class Act1 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.begin);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.begin:
			Intent intent = new Intent(Act1.this,PushBoxMain.class);
			startActivity(intent);
			break;			
		case R.id.exit:
			this.finish();
			break;
		}

	}

}

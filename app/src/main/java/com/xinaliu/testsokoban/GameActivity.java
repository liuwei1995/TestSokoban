package com.xinaliu.testsokoban;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.xinaliu.testsokoban.view.SokobanSurfaceView;

public class GameActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private SurfaceView mSvGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initView();
    }
    private SokobanSurfaceView mSokobanSurfaceView;

    private int p = 0;
    private void initView() {
        mSvGame = (SurfaceView) findViewById(R.id.sv_game);
        findViewById(R.id.textView).setOnClickListener(this);
        mSokobanSurfaceView = (SokobanSurfaceView) findViewById(R.id.sv_game2);
//        SurfaceHolder holder = mSvGame.getHolder();
//        holder.addCallback(this);
        mSokobanSurfaceView.setMap(SokobanSurfaceView.gameStateDataArray[p]);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        ++p;
        if (SokobanSurfaceView.gameStateDataArray.length == p){
            p = 0;
        }
        mSokobanSurfaceView.setMap(SokobanSurfaceView.gameStateDataArray[p]);
    }
}

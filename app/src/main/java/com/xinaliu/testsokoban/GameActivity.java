package com.xinaliu.testsokoban;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

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
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.up).setOnClickListener(this);
        findViewById(R.id.below).setOnClickListener(this);
        findViewById(R.id.left).setOnClickListener(this);
        findViewById(R.id.right).setOnClickListener(this);
        findViewById(R.id.next_level).setOnClickListener(this);
        findViewById(R.id.advance).setOnClickListener(this);
        mSokobanSurfaceView = (SokobanSurfaceView) findViewById(R.id.sv_game2);
//        SurfaceHolder holder = mSvGame.getHolder();
//        holder.addCallback(this);
        mSokobanSurfaceView.start(GameStateData.gameStateDataArray[p]);
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
        int id = v.getId();
        if (id == R.id.back){
            mSokobanSurfaceView.back();
        }else if (id == R.id.up){
            mSokobanSurfaceView.up();
        }
        else if (id == R.id.below){
            mSokobanSurfaceView.below();
        }
        else if (id == R.id.left){
            mSokobanSurfaceView.left();
        }
        else if (id == R.id.right){
            mSokobanSurfaceView.right();
        }
        else if (id == R.id.next_level){
            ++p;
            if (GameStateData.gameStateDataArray.length == p){
                p = 0;
            }
            mSokobanSurfaceView.start(GameStateData.gameStateDataArray[p]);
        }
        else if (id == R.id.advance){
            Toast.makeText(this,"努力建设中",Toast.LENGTH_LONG).show();
        }
    }
}

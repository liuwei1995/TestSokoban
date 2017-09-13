package com.xinaliu.testsokoban;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.xinaliu.testsokoban.view.SokobanSurfaceView;

import java.util.List;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

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
        findViewById(R.id.edit).setOnClickListener(this);
        mSokobanSurfaceView = (SokobanSurfaceView) findViewById(R.id.sv_game2);
        mSokobanSurfaceView.setPassCallback(new SokobanSurfaceView.PassCallback() {
            @Override
            public void onCallback(int[][] map, List<int[][]> stepList) {
                Toast.makeText(GameActivity.this,"恭喜你通过了",Toast.LENGTH_LONG).show();
                findViewById(R.id.next_level).performClick();
            }
        });
        mSokobanSurfaceView.start(GameStateDataProvider.getGameStateData(p));
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
            GameDataStruct gameDataStruct = new GameDataStruct(GameStateDataProvider.getGameStateData(p), true);
            int checkData = gameDataStruct.checkData();
            Toast.makeText(GameActivity.this,"checkData\t"+checkData,Toast.LENGTH_LONG).show();

            ++p;
            if (GameStateDataProvider.getGameStateToltalNumer() <= p){
                p = 0;
            }
            mSokobanSurfaceView.start(GameStateDataProvider.getGameStateData(p));
        }
        else if (id == R.id.advance){
            mSokobanSurfaceView.advance();
        }else {
            startActivity(new Intent(this,EditSokobanActivity.class));
        }
    }
}

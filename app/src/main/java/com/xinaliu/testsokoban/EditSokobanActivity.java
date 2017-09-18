package com.xinaliu.testsokoban;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinaliu.testsokoban.view.EditSokobanSurfaceView;

import java.util.ArrayList;
import java.util.List;

public class EditSokobanActivity extends AppCompatActivity implements View.OnClickListener {

    private EditSokobanSurfaceView mEditSokobanSurfaceView;
    private ImageView mPerson;
    private ImageView mWall;
    private ImageView mRoad;
    private ImageView mBox;
    private ImageView mGoal;
    private ImageView mFinish;
    private ImageView mClear;
    private ImageView mSelector;
    /**
     * 保存
     */
    private TextView mSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sokoban);
        initView();
        mEditSokobanSurfaceView = (EditSokobanSurfaceView) findViewById(R.id.essv);
    }

    private List<ImageView> list = new ArrayList<>();

    private void initView() {
        mPerson = (ImageView) findViewById(R.id.person);
        mPerson.setOnClickListener(this);
        list.add(mPerson);

        mWall = (ImageView) findViewById(R.id.wall);
        mWall.setOnClickListener(this);
        list.add(mWall);

        mRoad = (ImageView) findViewById(R.id.road);
        mRoad.setOnClickListener(this);
        list.add(mRoad);

        mBox = (ImageView) findViewById(R.id.box);
        mBox.setOnClickListener(this);
        list.add(mBox);

        mGoal = (ImageView) findViewById(R.id.goal);
        mGoal.setOnClickListener(this);
        list.add(mGoal);

        mFinish = (ImageView) findViewById(R.id.finish);
        mFinish.setOnClickListener(this);
        list.add(mFinish);


        mClear = (ImageView) findViewById(R.id.clear);
        mClear.setOnClickListener(this);
        list.add(mClear);

        mSelector = (ImageView) findViewById(R.id.selector);

        mSave = (TextView) findViewById(R.id.save);
        mSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int resId = R.drawable.wall_outside;
        switch (v.getId()) {
            case R.id.person:
                Toast.makeText(this, "点击了 人", Toast.LENGTH_LONG).show();
                mEditSokobanSurfaceView.setPaintType(mEditSokobanSurfaceView.WORKER);
                resId = R.drawable.man;
                break;
            case R.id.wall:
                Toast.makeText(this, "点击了 墙", Toast.LENGTH_LONG).show();
                mEditSokobanSurfaceView.setPaintType(mEditSokobanSurfaceView.WALL);
                resId = R.drawable.wall_outside;
                break;
            case R.id.road:
                Toast.makeText(this, "点击了 路", Toast.LENGTH_LONG).show();
                mEditSokobanSurfaceView.setPaintType(mEditSokobanSurfaceView.ROAD);
                resId = R.drawable.wall_inside;
                break;
            case R.id.box:
                mEditSokobanSurfaceView.setPaintType(mEditSokobanSurfaceView.BOX);
                Toast.makeText(this, "点击了 箱子", Toast.LENGTH_LONG).show();
                resId = R.drawable.box;
                break;
            case R.id.goal:
                mEditSokobanSurfaceView.setPaintType(mEditSokobanSurfaceView.GOAL);
                Toast.makeText(this, "点击了 目标", Toast.LENGTH_LONG).show();
                resId = R.drawable.goal;
                break;
            case R.id.finish:
                mEditSokobanSurfaceView.setPaintType(mEditSokobanSurfaceView.BOX_AT_GOAL);
                Toast.makeText(this, "点击了 完成", Toast.LENGTH_LONG).show();//步骤
                resId = R.drawable.box_at_goal;
                break;
            case R.id.clear:
                mEditSokobanSurfaceView.setPaintType(mEditSokobanSurfaceView.NULL);
                Toast.makeText(this, "点击了 清理", Toast.LENGTH_LONG).show();
                resId = R.drawable.ic_clear_01;
                break;
            case R.id.save:
                int[][] gameData = mEditSokobanSurfaceView.getGameData();
                GameDataStruct gameDataStruct = new GameDataStruct(gameData, false);
                int i = gameDataStruct.checkData();
//                GraphByMatrix graphByMatrix = new GraphByMatrix(false, true, gameData.length);
//                graphByMatrix.shortestPath_FLOYD(gameData);
                Toast.makeText(this, "点击了 保存=\t"+i, Toast.LENGTH_LONG).show();
                break;
        }
        mSelector.setImageResource(resId);
    }
}

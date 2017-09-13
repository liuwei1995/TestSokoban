package com.xinaliu.testsokoban;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
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

    }

    @Override
    public void onClick(View v) {
        for (ImageView imageView : list) {
            if (v == imageView){
                imageView.setSelected(true);
            }else {
                imageView.setSelected(false);
            }
        }
        switch (v.getId()) {
            case R.id.person:
                Toast.makeText(this,"点击了 人",Toast.LENGTH_LONG).show();
                break;
            case R.id.wall:
                Toast.makeText(this,"点击了 墙",Toast.LENGTH_LONG).show();
                break;
            case R.id.road:
                Toast.makeText(this,"点击了 墙",Toast.LENGTH_LONG).show();
                break;
            case R.id.box:
                Toast.makeText(this,"点击了 箱子",Toast.LENGTH_LONG).show();
                break;
            case R.id.goal:
                Toast.makeText(this,"点击了 目标",Toast.LENGTH_LONG).show();
                break;
            case R.id.finish:
                Toast.makeText(this,"点击了 完成",Toast.LENGTH_LONG).show();
                break;
        }
    }
}

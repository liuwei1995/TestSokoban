package com.xinaliu.testsokoban.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.xinaliu.testsokoban.GameStateData;
import com.xinaliu.testsokoban.PositionEntity;
import com.xinaliu.testsokoban.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuwei on 2017/9/13 15:15
 */

public class EditSokobanSurfaceView  extends SurfaceView implements
        SurfaceHolder.Callback ,Runnable
        ,View.OnTouchListener{

    private  int width = 0;
    public static int xoff = 25;
    public static int yoff = 50;

    private final SurfaceHolder mSurfaceHolder;
    private ExecutorService bee_cachedThreadPool = null;
    public final int WALL = GameStateData.DATA_FLAG.WALL;//声明墙的代号为1
    public final int GOAL = GameStateData.DATA_FLAG.DEST;//目标
    public final int ROAD = GameStateData.DATA_FLAG.PATH;//声明路的代号为3
    public final int BOX = GameStateData.DATA_FLAG.PATH | GameStateData.DATA_FLAG.BOX;//声明箱子的代号为4
    //    public final int BOX_AT_GOAL = GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX;  //位置已经放好了的
    public final int BOX_AT_GOAL = GameStateData.DATA_FLAG.MASK_MAP_AND_SPRITE;  //位置已经放好了的
    public final int WORKER = GameStateData.DATA_FLAG.PATH | GameStateData.DATA_FLAG.MP;//声明人的代号为6
    public final int NULL = GameStateData.DATA_FLAG.NULL;//声明空白区域的代号为

    public EditSokobanSurfaceView(Context context) {
        this(context,null);
    }

    public EditSokobanSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EditSokobanSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bee_cachedThreadPool = Executors.newFixedThreadPool(1);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        width = point.x > point.y ? point.y : point.x;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setOnTouchListener(this);
        getMapDetail();
        intPic();
    }

    @SuppressLint("UseSparseArrays")
    private void intPic() {
        // 初始化图片
        pic = new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadPic(WALL, this.getResources().getDrawable(R.drawable.wall_outside,null));
            loadPic(GOAL, this.getResources().getDrawable(R.drawable.goal,null));
            loadPic(ROAD, this.getResources().getDrawable(R.drawable.wall_inside,null));
            loadPic(BOX, this.getResources().getDrawable(R.drawable.box,null));
            loadPic(BOX_AT_GOAL,this.getResources().getDrawable(R.drawable.box_at_goal,null));
            loadPic(WORKER, this.getResources().getDrawable(R.drawable.man,null));
            loadPic(NULL, this.getResources().getDrawable(R.drawable.road,null));
        }else {
            loadPic(WALL, this.getResources().getDrawable(R.drawable.wall_outside));
            loadPic(GOAL, this.getResources().getDrawable(R.drawable.goal));
            loadPic(ROAD, this.getResources().getDrawable(R.drawable.wall_inside));
            loadPic(BOX, this.getResources().getDrawable(R.drawable.box));
            loadPic(BOX_AT_GOAL,this.getResources().getDrawable(R.drawable.box_at_goal));
            loadPic(WORKER, this.getResources().getDrawable(R.drawable.man));
            loadPic(NULL, this.getResources().getDrawable(R.drawable.road));
        }
    }
    /***
     * 加载图片
     * @param KEY k
     * @param dw d
     */
    private void loadPic(int KEY, Drawable dw) {
        try {
            // 构造Bitmap
            Bitmap bm = Bitmap.createBitmap(picSize, picSize,Bitmap.Config.ARGB_8888);
            dw.setBounds(0, 0, picSize, picSize);
            Canvas cs = new Canvas(bm);
            dw.draw(cs);
//            pic[KEY] = bm;
            pic.put(KEY,bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int row = 15;
    private int column = 13;

    private int picSize = 30;
    private void getMapDetail() {
        picSize = (int) Math.floor((width - 2 * xoff) / column);//得到图片大小
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int width = getWidth();
        if (width <= 0){
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            this.width = point.x > point.y ? point.y : point.x;
        }else {
            int height = getHeight();
            this.width = width >= height ? height : width;
        }
        getMapDetail();
        intPic();
        if (bee_cachedThreadPool == null || bee_cachedThreadPool.isShutdown())
            bee_cachedThreadPool = Executors.newFixedThreadPool(1);
        bee_cachedThreadPool.submit(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(bee_cachedThreadPool != null)
            bee_cachedThreadPool.shutdownNow();
    }
    private float y1;
    private float x1;
    private float y2;
    private float x2;

    private long time;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
            time = System.currentTimeMillis();
            for (PositionEntity positionEntity : list) {
                boolean contains = positionEntity.getRect().contains((int)x1, (int)y1);
                if (contains){
                    positionEntity.setType(paintType);
                    bee_cachedThreadPool.submit(this);
                    break;
                }
            }
        }else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            float x1 = event.getX();
            float y1 = event.getY();
            if (paintType == WORKER){
                PositionEntity entity = null;
                for (PositionEntity positionEntity : list) {
                    boolean contains = positionEntity.getRect().contains((int)x1, (int)y1);
                    if (entity == null){
                        if (contains){
                            entity = positionEntity;
                        }
                    }
                    int type = positionEntity.getType();
                    if (type == WORKER){
                        positionEntity.setType(NULL);
                    }
                }
                if (entity != null){
                    entity.setType(WORKER);
                }
                bee_cachedThreadPool.submit(this);
            }else
            for (PositionEntity positionEntity : list) {
                boolean contains = positionEntity.getRect().contains((int)x1, (int)y1);
                if (contains){
                    positionEntity.setType(paintType);
                    bee_cachedThreadPool.submit(this);
                    break;
                }
            }
        }else if(event.getAction() == MotionEvent.ACTION_UP) {
//            if (System.currentTimeMillis() - time > 500)return false;
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();

            boolean b = Math.abs(y1 - y2) >= Math.abs(x1 - x2);//
        }else if(event.getAction() == MotionEvent.ACTION_CANCEL) {
            Toast.makeText(getContext(),"ACTION_CANCEL",Toast.LENGTH_LONG).show();
        }
        return true;
    }
    private List<PositionEntity> list = new ArrayList<>();
    public Map<Integer,Bitmap> pic = null;//声明引入的图像集

    @Override
    public void run() {
        //1.这里就是核心了， 得到画布 ，然后在你的画布上画出要显示的内容
        Canvas canvas =  mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清空画布


        //3. 解锁画布   更新提交屏幕显示内容
        Paint paint = new Paint();
        paint.setTextSize(20f);
        paint.setColor(Color.WHITE);

        canvas.drawText("第"+String.valueOf(1)+"关", 2*width/5,yoff/2 , paint);


        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#B2cfEb"));
        paint.setStyle(Paint.Style.STROKE);
        if (list.isEmpty())
        for (int i = 0; i < row; i++) {//行
            for (int j = 0; j < column; j++) {//纵
                int left = xoff + j * picSize;
                int top = yoff+ i * picSize;
                PositionEntity po = new PositionEntity();
                Rect personRect = new Rect(left, top, left + picSize, top + picSize);
                po.setRect(personRect);
                po.setX(j);
                po.setY(i);
                list.add(po);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            PositionEntity positionEntity = list.get(i);
            int type = positionEntity.getType();
            if (type == 0){
                canvas.drawRect(positionEntity.getRect(),paint);
            }else if (type == WALL){
                canvas.drawBitmap(pic.get(type),null,positionEntity.getRect(), paint);
            }
            else if (type == WORKER){
                canvas.drawBitmap(pic.get(type),null,positionEntity.getRect(), paint);
            }
            else if (type == ROAD){
                canvas.drawBitmap(pic.get(type),null,positionEntity.getRect(), paint);
            }
            else if (type == BOX){
                canvas.drawBitmap(pic.get(type),null,positionEntity.getRect(), paint);
            }
            else if (type == GOAL){
                canvas.drawBitmap(pic.get(type),null,positionEntity.getRect(), paint);
            }else {
                try {
                    canvas.drawBitmap(pic.get(type),null,positionEntity.getRect(), paint);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    private int paintType = WALL;

    public void setPaintType(int paintType) {
        this.paintType = paintType;
    }
}

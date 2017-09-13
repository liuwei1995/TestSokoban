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
import android.support.annotation.CallSuper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.xinaliu.testsokoban.GameStateData;
import com.xinaliu.testsokoban.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * Created by liuwei on 2017/9/11 13:23
 */

public class SokobanSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback ,Runnable
        ,View.OnTouchListener
{

    public static int width = 0;
    public static int xoff = 25;
    public static int yoff = 50;

    public final int WALL = GameStateData.DATA_FLAG.WALL;//声明墙的代号为1
    public final int GOAL = GameStateData.DATA_FLAG.DEST;//目标
    public final int ROAD = GameStateData.DATA_FLAG.PATH;//声明路的代号为3
    public final int BOX = GameStateData.DATA_FLAG.PATH | GameStateData.DATA_FLAG.BOX;//声明箱子的代号为4
//    public final int BOX_AT_GOAL = GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX;  //位置已经放好了的
    public final int BOX_AT_GOAL = GameStateData.DATA_FLAG.MASK_MAP_AND_SPRITE;  //位置已经放好了的
    public final int WORKER = GameStateData.DATA_FLAG.PATH | GameStateData.DATA_FLAG.MP;//声明人的代号为6
    public final int NULL = GameStateData.DATA_FLAG.NULL;//声明空白区域的代号为


    public int picSize = 30;
    private SurfaceHolder mSurfaceHolder;

    public Map<Integer,Bitmap> pic = null;//声明引入的图像集

    public SokobanSurfaceView(Context context) {
        this(context, null);
    }
    public SokobanSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SokobanSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setFocusable(true);//设置当前view拥有触摸事件
        bee_cachedThreadPool = Executors.newFixedThreadPool(1);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        width = point.x > point.y ? point.y : point.x;
        getMapDetail();
        intPic();

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setOnTouchListener(this);
//        mGestureDetector = new GestureDetector(context,this);
    }

    private ExecutorService bee_cachedThreadPool = null;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (bee_cachedThreadPool == null || bee_cachedThreadPool.isShutdown())bee_cachedThreadPool = Executors.newFixedThreadPool(1);
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

    /**
     * 拷贝数组
     * @param srcMap 数组数据源
     * @return int[][]
     */
    private synchronized int[][] copy(int[][] srcMap){
        int [][] arr2 = new int[srcMap.length][srcMap[0].length];
        for (int i = 0; i < srcMap.length; i++) {
            System.arraycopy(srcMap[i], 0, arr2[i], 0, srcMap[i].length);
//            for (int i1 = 0; i1 < map[i].length; i1++)
//                arr2[i][i1] = map[i][i1];
        }
        return arr2;
    }

    private int[][] map = null;
    private int[][] tem = null;
    private boolean isPass = false;//是否过关
    public synchronized void start(int[][] map){
        if (map == null || map.length <= 0)return;
        tem = null;
        this.map = map;
        isPass = false;
        this.tem =  copy(map);
        if (boxRectList == null)
        boxRectList = new ArrayList<>();
        boxRectList.clear();

        if (backList == null)
        backList = new ArrayList<>();
        backList.clear();

        if (advanceList == null)
        advanceList = new ArrayList<>();
        advanceList.clear();

        getMapDetail();
        intPic();
        if (bee_cachedThreadPool == null || bee_cachedThreadPool.isShutdown())
            bee_cachedThreadPool = Executors.newFixedThreadPool(1);
        bee_cachedThreadPool.submit(this);
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
    public int row = 0;
    public int column = 0;

    private void getMapDetail() {
        if (tem == null)return;
        //得到网格大小
        row = tem.length;
        column = tem[0].length;//得到总地图的宽有几格
        picSize = (int) Math.floor((width - 2 * xoff) / column);//得到图片大小
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


    @CallSuper
    public void onDestroy() {
        getHolder().getSurface().release();
    }


    @Override
    public void run() {
        if (tem == null || boxRectList == null || isPass)return;
        boxRectList.clear();
        //1.这里就是核心了， 得到画布 ，然后在你的画布上画出要显示的内容
        Canvas canvas =  mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清空画布

        //3. 解锁画布   更新提交屏幕显示内容
        Paint paint = new Paint();
        paint.setTextSize(20f);
        paint.setColor(Color.WHITE);
//        try {
//            canvas.drawText("第"+String.valueOf(1)+"关", 2*width/5,yoff/2 , paint);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        int goalNumber = 0;
        //绘制地图
        for (int i = 0; i < row; i++){
            for (int j = 0; j < column; j++) {
                int i1 = tem[i][j];
                try {
                    if (i1 > 0){//临时
                        canvas.drawBitmap(pic.get(i1), xoff + j * picSize, yoff+ i * picSize, paint);
                        if (i1 == WORKER){//工人
                            personRow = i;
                            personColumn = j;
                            int left = xoff + j * picSize;
                            int top = yoff+ i * picSize;
                            personRect = new Rect(left, top, left + picSize, top + picSize);
                        }else if (i1 == BOX || i1 == BOX_AT_GOAL){
                            int left = xoff + j * picSize;
                            int top = yoff+ i * picSize;
                            boxRectList.add(new Rect(left, top, left + picSize, top + picSize));
                            if (i1 == BOX)  ++goalNumber;
                        }else if (i1 == GOAL){
                            ++goalNumber;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        mSurfaceHolder.unlockCanvasAndPost(canvas);

        if (goalNumber == 0){
            isPass = true;
            this.post(new Runnable() {
                @Override
                public void run() {
                    if (mPassCallback != null){
                        backList.add(tem);
                        mPassCallback.onCallback(map,backList);
                    }else {
                        Toast.makeText(getContext(),"恭喜你通过了",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    /**
     *
     * 绘制图片
     * @param       x 屏幕上的x坐标
     * @param       y 屏幕上的y坐标
     * @param       w 要绘制的图片的宽度
     * @param       h 要绘制的图片的高度
//     * @param       bx 图片上的x坐标
//     * @param       by 图片上的y坐标
     *
     * @return      null
     */
    public static Rect drawImage(Canvas canvas, Bitmap blt, int x, int y,int w, int h) {
//       Rect src = new Rect();// 图片 >>原矩形
        Rect dst = new Rect();// 屏幕 >>目标矩形
        dst.left = x;
        dst.top = y;
        dst.right = x + w;
        dst.bottom = y + h;
        // 画出指定的位图，位图将自动--》缩放/自动转换，以填补目标矩形
        // 这个方法的意思就像 将一个位图按照需求重画一遍，画后的位图就是我们需要的了
        canvas.drawBitmap(blt, null, dst, null);//这个方法  第一个参数是图片，第二个参数是 绘画该图片需显示多少。也就是说你想绘画该图片的某一些地方，而不是全部图片，第三个参数表示该图片绘画的位置
        return dst;
    }

    private int personRow = 0;//当前人的行
    private int personColumn = 0;//当前人的列
    private Rect personRect = null;//人的位置
    private List<Rect> boxRectList = null;//箱子的位置集合
    private List<int[][]> backList = null;//回退集合
    private List<int[][]> advanceList = null;//前进集合

    /**
     * 回退
     */
    public synchronized void back() {

        if(backList == null || backList.size() <= 0 || isPass)return;
        advanceList.add(copy(tem));//前进集合

        tem =  backList.remove(backList.size() - 1);

        if (bee_cachedThreadPool == null || bee_cachedThreadPool.isShutdown())
            bee_cachedThreadPool = Executors.newFixedThreadPool(1);
        bee_cachedThreadPool.submit(this);
    }

    /**
     * 前进
     */
    public synchronized void advance() {
        if (advanceList == null || advanceList.size() <= 0 || isPass)return;
        backList.add(copy(tem));//回退集合

        tem = advanceList.remove(advanceList.size() - 1);

        if (bee_cachedThreadPool == null || bee_cachedThreadPool.isShutdown())
            bee_cachedThreadPool = Executors.newFixedThreadPool(1);
        bee_cachedThreadPool.submit(this);
    }

    public synchronized void up(){
        if (tem == null || map == null || backList == null || isPass)return;
        if (personRow <= 0)return;
        if (advanceList != null)advanceList.clear();//把前进的集合清空

        backList.add(copy(tem));

        int i2 = tem[personRow - 1][personColumn];
        if (i2 == WALL || i2 == NULL){//墙 || 空白区域
            return;
        }
        if (i2 == ROAD || i2 == GOAL){//路 || 目标
            tem[personRow - 1][personColumn] = WORKER;
            int i = map[personRow][personColumn];
            if (i == GOAL || i == BOX_AT_GOAL){//目标
                tem[personRow][personColumn] = GOAL;
            }else if (i == ROAD || i == WORKER || i == BOX){//路
                tem[personRow][personColumn] = ROAD;
            }
        }else if (i2 == BOX || i2 == BOX_AT_GOAL){// 箱子 || 位置已经放好了的
            if (personRow <= 1)return;
            int i = tem[personRow - 2][personColumn];
            if (i == WALL || i == NULL || i == BOX || i == BOX_AT_GOAL){//墙 || 空白区域 || 箱子 ||  位置已经放好了的
                return;
            }
            if (i == ROAD) {//路
                tem[personRow - 2][personColumn] = BOX;//箱子
                tem[personRow - 1][personColumn] = WORKER;//人

                int ij = map[personRow][personColumn];
                if (ij == GOAL || ij == BOX_AT_GOAL){//目标
                    tem[personRow][personColumn] = GOAL;
                }else if (ij == ROAD || ij == WORKER || ij == BOX){//路
                    tem[personRow][personColumn] = ROAD;
                }
            }else if (i == GOAL){//  目标
                tem[personRow - 2][personColumn] = BOX_AT_GOAL;// 位置已经放好了的
                tem[personRow - 1][personColumn] = WORKER;//人

                int ij = map[personRow][personColumn];
                if (ij == GOAL || ij == BOX_AT_GOAL){//目标
                    tem[personRow][personColumn] = GOAL;
                }else if (ij == ROAD || ij == WORKER || ij == BOX){//路
                    tem[personRow][personColumn] = ROAD;
                }else {
                    Log.d("--------","---------------------");
                }
            } else {
                Log.d("--------","---------------------");
                return;
            }
        }

        if (bee_cachedThreadPool == null || bee_cachedThreadPool.isShutdown())
            bee_cachedThreadPool = Executors.newFixedThreadPool(1);

        bee_cachedThreadPool.submit(this);
    }
    public synchronized void below(){
        if (tem == null || map == null || backList == null || isPass)return;
        if (personRow >= row)return;
        if (advanceList != null)advanceList.clear();//把前进的集合清空

        backList.add(copy(tem));
        int i2 = tem[personRow + 1][personColumn];

        if (i2 == WALL || i2 == NULL){//墙 || 空白区域
            return;
        }

        if (i2 == ROAD || i2 == GOAL){//路 || 目标
            tem[personRow + 1][personColumn] = WORKER;
            int i = map[personRow][personColumn];

            if (i == GOAL || i == BOX_AT_GOAL){//目标
                tem[personRow][personColumn] = GOAL;
            }else if (i == ROAD || i == WORKER || i == BOX){//路
                tem[personRow][personColumn] = ROAD;
            }
            personRow =  personRow + 1;
        }else if (i2 == BOX || i2 == BOX_AT_GOAL){// 箱子 || 位置已经放好了的
            if (personRow >= row + 1)return;
            int i = tem[personRow + 2][personColumn];
            if (i == WALL || i == NULL || i == BOX || i == BOX_AT_GOAL){//墙 || 空白区域 || 箱子 ||  位置已经放好了的
                return;
            }
            if (i == ROAD) {//路
                tem[personRow + 2][personColumn] = BOX;//箱子
                tem[personRow + 1][personColumn] = WORKER;//人
                int ij = map[personRow][personColumn];

                if (ij == GOAL || ij == BOX_AT_GOAL){//目标
                    tem[personRow][personColumn] = GOAL;
                }else if (ij == ROAD || ij == WORKER || ij == BOX){//路 || 人 || 箱子
                    tem[personRow][personColumn] = ROAD;
                }
            }else if (i == GOAL){//  目标
                tem[personRow + 2][personColumn] = BOX_AT_GOAL;// 位置已经放好了的
                tem[personRow + 1][personColumn] = WORKER;//人
                int ij = map[personRow][personColumn];

                if (ij == GOAL || ij == BOX_AT_GOAL){//目标
                    tem[personRow][personColumn] = GOAL;
                }else if (ij == ROAD || ij == WORKER || ij == BOX){//路
                    tem[personRow][personColumn] = ROAD;
                }else {
                    Log.d("--------","---------------------");
                    return;
                }
            } else {
                Log.d("--------","---------------------");
                return;
            }
        }

        if (bee_cachedThreadPool == null || bee_cachedThreadPool.isShutdown())
            bee_cachedThreadPool = Executors.newFixedThreadPool(1);
        bee_cachedThreadPool.submit(this);
    }
    public synchronized void left(){
        if (tem == null || map == null || backList == null || isPass)return;
        if (personColumn == 0)return;
        if (advanceList != null)advanceList.clear();//把前进的集合清空

        backList.add(copy(tem));
        int i2 = tem[personRow][personColumn - 1];
        if (i2 == WALL || i2 == NULL){//墙 || 空白区域
            return;
        }
        if (i2 == ROAD || i2 == GOAL){//路 || 目标
            tem[personRow][personColumn - 1] = WORKER;
            int i = map[personRow][personColumn];
            if (i == GOAL || i == BOX_AT_GOAL){//目标
                tem[personRow][personColumn] = GOAL;
            }else if (i == ROAD || i == WORKER || i == BOX){//路
                tem[personRow][personColumn] = ROAD;
            }else {
                Toast.makeText(getContext(),"错误",Toast.LENGTH_LONG).show();
            }
        }else if (i2 == BOX || i2 == BOX_AT_GOAL){// 箱子 || 位置已经放好了的
            if (personColumn <= 1)return;
            int i = tem[personRow][personColumn - 2];
            if (i == WALL || i == NULL || i == BOX || i == BOX_AT_GOAL){//墙 || 空白区域 || 箱子 ||  位置已经放好了的
                return;
            }
            if (i == ROAD) {//路
                tem[personRow][personColumn - 2] = BOX;//箱子
                tem[personRow][personColumn - 1] = WORKER;//人

                int ij = map[personRow][personColumn];
                if (ij == GOAL || ij == BOX_AT_GOAL){//目标
                    tem[personRow][personColumn] = GOAL;
                }else if (ij == ROAD || ij == WORKER || ij == BOX){//路
                    tem[personRow][personColumn] = ROAD;
                }else  Toast.makeText(getContext(),"错误",Toast.LENGTH_LONG).show();
            }else if (i == GOAL){//  目标
                tem[personRow][personColumn - 2] = BOX_AT_GOAL;// 位置已经放好了的
                tem[personRow][personColumn - 1] = WORKER;//人

                int ij = map[personRow][personColumn];
                if (ij == GOAL || ij == BOX_AT_GOAL){//目标
                    tem[personRow][personColumn] = GOAL;
                }else if (ij == ROAD || ij == WORKER || ij == BOX){//路
                    tem[personRow][personColumn] = ROAD;
                }else {
                    Toast.makeText(getContext(),"错误",Toast.LENGTH_LONG).show();
                    Log.d("--------","---------------------");
                    return;
                }
            } else {
                Toast.makeText(getContext(),"错误",Toast.LENGTH_LONG).show();
                Log.d("--------","---------------------");
                return;
            }
        }

        if (bee_cachedThreadPool == null || bee_cachedThreadPool.isShutdown())
            bee_cachedThreadPool = Executors.newFixedThreadPool(1);
        bee_cachedThreadPool.submit(this);
    }
    public synchronized void right(){
        if (tem == null || map == null || backList == null || isPass)return;
        if (personColumn >= column)return;
        if (advanceList != null)advanceList.clear();//把前进的集合清空

        backList.add(copy(tem));
        int i2 = tem[personRow][personColumn + 1];

        if (i2 == WALL || i2 == NULL){//墙 || 空白区域
            return;
        }

        if (i2 == ROAD || i2 == GOAL){//路 || 目标
            tem[personRow][personColumn + 1] = WORKER;

            int i = map[personRow][personColumn];
            if (i == GOAL || i == BOX_AT_GOAL){//目标
                tem[personRow][personColumn] = GOAL;
            }else if (i == ROAD || i == WORKER || i == BOX){//路
                tem[personRow][personColumn] = ROAD;
            }
        }else if (i2 == BOX || i2 == BOX_AT_GOAL){// 箱子 || 位置已经放好了的
            if (personColumn >= column + 1)return;
            int i = tem[personRow][personColumn + 2];
            if (i == WALL || i == NULL || i == BOX || i == BOX_AT_GOAL){//墙 || 空白区域 || 箱子 ||  位置已经放好了的
                return;
            }
            if (i == ROAD) {//路
                tem[personRow][personColumn + 2] = BOX;//箱子
                tem[personRow][personColumn + 1] = WORKER;//人

                int ij = map[personRow][personColumn];
                if (ij == GOAL || ij == BOX_AT_GOAL){//目标
                    tem[personRow][personColumn] = GOAL;
                }else if (ij == ROAD || ij == WORKER || ij == BOX){//路
                    tem[personRow][personColumn] = ROAD;
                }
            }else if (i == GOAL){//  目标
                tem[personRow][personColumn + 2] = BOX_AT_GOAL;// 位置已经放好了的
                tem[personRow][personColumn + 1] = WORKER;//人

                int ij = map[personRow][personColumn];
                if (ij == GOAL || ij == BOX_AT_GOAL){//目标
                    tem[personRow][personColumn] = GOAL;
                }else if (ij == ROAD || ij == WORKER || ij == BOX){//路
                    tem[personRow][personColumn] = ROAD;
                }else {
                    Log.d("--------","---------------------");
                    return;
                }
            } else {
                Log.d("--------","---------------------");
                return;
            }
        }

        if(bee_cachedThreadPool == null)
            bee_cachedThreadPool = Executors.newFixedThreadPool(1);
        bee_cachedThreadPool.submit(this);
    }

    private Map<Integer,Integer> mapwei = new HashMap<>();


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
        }else if(event.getAction() == MotionEvent.ACTION_MOVE) {

        }else if(event.getAction() == MotionEvent.ACTION_UP) {
            if (System.currentTimeMillis() - time > 500)return false;
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            boolean b = Math.abs(y1 - y2) >= Math.abs(x1 - x2);//
            if (b && Math.abs(y1 - y2) > 50){
                if(y1 - y2 > 50) {
                    up();
                } else if(y2 - y1 > 50) {
                    below();
                }
            }else if (Math.abs(x1 - x2) > 50){
                if(x1 - x2 > 50) {
                    left();
                } else if(x2 - x1 > 50) {
                    right();
                }
            }else {
                if (personRect != null){
                    boolean contains = personRect.contains((int) x2, (int) y2);
                    if (contains)return false;
                    if (boxRectList != null){
                        for (Rect rect : boxRectList) {
                            if (rect.contains((int) x2, (int) y2)){
                                Toast.makeText(getContext(),"contains",Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                    }

                }
            }
        }else if(event.getAction() == MotionEvent.ACTION_CANCEL) {
            Toast.makeText(getContext(),"ACTION_CANCEL",Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private PassCallback mPassCallback;
    /**
     * 通关回调
     */
    public void setPassCallback(PassCallback mPassCallback) {
        this.mPassCallback = mPassCallback;
    }

    public interface PassCallback{

        /**
         * <p>通关回调</p>
         * @param map 地图
         * @param stepList 步骤
         */
        void onCallback(int[][] map, List<int[][]> stepList);

    }

}

package com.xinaliu.testsokoban.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.xinaliu.testsokoban.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuwei on 2017/9/11 13:23
 */

public class SokobanSurfaceView extends SurfaceView implements SurfaceHolder.Callback ,Runnable ,View.OnTouchListener{

    public static int width = 0;
    public static int xoff = 25;
    public static int yoff = 50;

    public final int WALL = DATA_FLAG.WALL;//声明墙的代号为1
    public final int GOAL = DATA_FLAG.DEST;//目标
    public final int ROAD = DATA_FLAG.PATH;//声明路的代号为3
    public final int BOX = DATA_FLAG.PATH | DATA_FLAG.BOX;//声明箱子的代号为4
    public final int BOX_AT_GOAL = DATA_FLAG.MASK_MAP_AND_SPRITE;
    public final int WORKER = DATA_FLAG.PATH | DATA_FLAG.MP;//声明人的代号为6
    public final int NULL = DATA_FLAG.NULL;//声明空白区域的代号为

//    public final int BACK=7;//声明返回按钮的代号为7
//    public final int UP=8;//声明向上按钮的代号为8
//    public final int DOWN=9;//声明向下按钮的代号为9
//    public final int LEFT=10;//声明向左按钮的代号为10
//    public final int RIGHT=11;//声明向右按钮的代号为11
//    public final int MUSIC=12;//声明音乐开关的代号为1

    public int picSize = 30;
    public Map<Integer,Bitmap> pic = null;//声明引入的图像集

    public SokobanSurfaceView(Context context) {
        this(context, null);
    }

    public SokobanSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    private SurfaceHolder mSurfaceHolder;

    public SokobanSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        width = getWidth();
        if (width <= 0){
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            width = point.x > point.y ? point.y : point.x;
        }

        intPic();
        getMapDetail();
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    private ExecutorService bee_cachedThreadPool = null;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(bee_cachedThreadPool != null)
            bee_cachedThreadPool.shutdownNow();
    }

    private int[][] map = null;
    private int[][] tem =null;


    public void setMap(int[][] map){
        if (map == null)return;
        this.map = map;
        getMapDetail();
        intPic();
        if(bee_cachedThreadPool != null)
            bee_cachedThreadPool.shutdownNow();
        bee_cachedThreadPool = Executors.newCachedThreadPool();
        bee_cachedThreadPool.submit(this);
    }


    private void intPic() {
        // 初始化图片
        pic = new HashMap<>();
        loadPic(WALL, this.getResources().getDrawable(R.drawable.wall_outside));
        loadPic(GOAL, this.getResources().getDrawable(R.drawable.goal));
        loadPic(ROAD, this.getResources().getDrawable(R.drawable.wall_inside));
        loadPic(BOX, this.getResources().getDrawable(R.drawable.box));
        loadPic(BOX_AT_GOAL,this.getResources().getDrawable(R.drawable.box_at_goal));
        loadPic(WORKER, this.getResources().getDrawable(R.drawable.man));
        loadPic(NULL, this.getResources().getDrawable(R.drawable.road));

//        loadPic(BACK, this.getResources().getDrawable(R.drawable.back));
//        loadPic(UP, this.getResources().getDrawable(R.drawable.up));
//        loadPic(DOWN, this.getResources().getDrawable(R.drawable.down));
//        loadPic(LEFT, this.getResources().getDrawable(R.drawable.left));
//        loadPic(RIGHT, this.getResources().getDrawable(R.drawable.right));
//        loadPic(MUSIC, this.getResources().getDrawable(R.drawable.music));

    }
    public int row = 0;
    public int column = 0;
    /*
         * public int[][] getMap(int grade) { return MapList.getMap(grade); }
         */

    private void getMapDetail() {
        if (map == null)return;
        //得到网格大小
        row = map.length;
        column = map[0].length;//得到总地图的宽有几格,可以看一下MapList类,仔细想想,你就明白了
        picSize = (int) Math.floor((width - 2 * xoff) / column);//得到图片大小
    }
    private void loadPic(int KEY, Drawable dw) {
        try {
            // 构造Bitmap
            Bitmap bm = Bitmap.createBitmap(picSize, picSize,
                    Bitmap.Config.ARGB_8888);
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

    private int gate = 0;

    @Override
    public void run() {
// 画笔
        //1.这里就是核心了， 得到画布 ，然后在你的画布上画出要显示的内容
        Canvas canvas =  mSurfaceHolder.lockCanvas();
        //3. 解锁画布   更新提交屏幕显示内容
        Paint paint = new Paint();
        paint.setTextSize(20f);
        paint.setColor(Color.WHITE);
        canvas.drawText("第"+String.valueOf(gate+1)+"关", 2*width/5,yoff/2 , paint);

        //绘制地图
        try {
            for (int i = 0; i < row; i++){
                for (int j = 0; j < column; j++) {
                    int i1 = map[i][j];
                    try {
                        if (i1 > 0){
                            int left = xoff + j * picSize;
                            int top = yoff+ i * picSize;
                            canvas.drawBitmap(pic.get(i1), xoff + j * picSize, yoff+ i * picSize, paint);
                            if (i1 == WORKER){//工人
                                Rect rect = new Rect(left, top, left + picSize, top + picSize);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


    public static final class DATA_FLAG
    {
        public static final int NULL = 0x0000;
        public static final int MASK_ALL = 0xffff;
        public static final int MASK_MAP = 0x00ff;
        public static final int MASK_SPRITE = 0x7f00;
        public static final int MASK_MAP_AND_SPRITE = 0x7fff;
        public static final int WALL = 0x0001;
        public static final int PATH = 0x0002;
        public static final int DEST = 0x0004;
        public static final int BOX = 0x0100;
        public static final int MP = 0x0200;
        public static final int CURSOR = 0x8000;
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
    public static Rect drawImage(Canvas canvas, Bitmap blt, int x, int y,
                                 int w, int h) {
//       Rect src = new Rect();// 图片 >>原矩形
        Rect dst = new Rect();// 屏幕 >>目标矩形
        dst.left = x;
        dst.top = y;
        dst.right = x + w;
        dst.bottom = y + h;
        // 画出指定的位图，位图将自动--》缩放/自动转换，以填补目标矩形
        // 这个方法的意思就像 将一个位图按照需求重画一遍，画后的位图就是我们需要的了
        canvas.drawBitmap(blt, null, dst, null);
        return dst;
    }

   public static final int[][][] gameStateDataArray =
            {
                    //-----------------------------------------------------------
                    //state 1
                    {
		   /* 1 */
                            {
		       /* 01 */DATA_FLAG.NULL,
		       /* 02 */DATA_FLAG.NULL,
		       /* 03 */DATA_FLAG.WALL,
		       /* 04 */DATA_FLAG.WALL,
		       /* 05 */DATA_FLAG.WALL,
		       /* 06 */DATA_FLAG.NULL,
		       /* 07 */DATA_FLAG.NULL,
		       /* 08 */DATA_FLAG.NULL
                            },
		   /* 2 */
                            {
		       /* 01 */DATA_FLAG.NULL,
		       /* 02 */DATA_FLAG.NULL,
		       /* 03 */DATA_FLAG.WALL,
		       /* 04 */DATA_FLAG.DEST,
		       /* 05 */DATA_FLAG.WALL,
		       /* 06 */DATA_FLAG.NULL,
		       /* 07 */DATA_FLAG.NULL,
		       /* 08 */DATA_FLAG.NULL
                            },
		   /* 3 */
                            {
		       /* 01 */DATA_FLAG.NULL,
		       /* 02 */DATA_FLAG.NULL,
		       /* 03 */DATA_FLAG.WALL,
		       /* 04 */DATA_FLAG.PATH,
		       /* 05 */DATA_FLAG.WALL,
		       /* 06 */DATA_FLAG.WALL,
		       /* 07 */DATA_FLAG.WALL,
		       /* 08 */DATA_FLAG.WALL
                            },
		   /* 4 */
                            {
		       /* 01 */DATA_FLAG.WALL,
		       /* 02 */DATA_FLAG.WALL,
		       /* 03 */DATA_FLAG.WALL,
		       /* 04 */DATA_FLAG.PATH | DATA_FLAG.BOX,
		       /* 05 */DATA_FLAG.PATH,
		       /* 06 */DATA_FLAG.PATH | DATA_FLAG.BOX,
		       /* 07 */DATA_FLAG.DEST,
		       /* 08 */DATA_FLAG.WALL
                            },
		   /* 5 */
                            {
		       /* 01 */DATA_FLAG.WALL,
		       /* 02 */DATA_FLAG.DEST,
		       /* 03 */DATA_FLAG.PATH,
		       /* 04 */DATA_FLAG.PATH | DATA_FLAG.BOX,
		       /* 05 */DATA_FLAG.PATH | DATA_FLAG.MP,
		       /* 06 */DATA_FLAG.WALL,
		       /* 07 */DATA_FLAG.WALL,
		       /* 08 */DATA_FLAG.WALL
                            },
		   /* 6 */
                            {
		       /* 01 */DATA_FLAG.WALL,
		       /* 02 */DATA_FLAG.WALL,
		       /* 03 */DATA_FLAG.WALL,
		       /* 04 */DATA_FLAG.WALL,
		       /* 05 */DATA_FLAG.PATH | DATA_FLAG.BOX,
		       /* 06 */DATA_FLAG.WALL,
		       /* 07 */DATA_FLAG.NULL,
		       /* 08 */DATA_FLAG.NULL
                            },
		   /* 7 */
                            {
		       /* 01 */DATA_FLAG.NULL,
		       /* 02 */DATA_FLAG.NULL,
		       /* 03 */DATA_FLAG.NULL,
		       /* 04 */DATA_FLAG.WALL,
		       /* 05 */DATA_FLAG.DEST,
		       /* 06 */DATA_FLAG.WALL,
		       /* 07 */DATA_FLAG.NULL,
		       /* 08 */DATA_FLAG.NULL
                            },
		   /* 8 */
                            {
		       /* 01 */DATA_FLAG.NULL,
		       /* 02 */DATA_FLAG.NULL,
		       /* 03 */DATA_FLAG.NULL,
		       /* 04 */DATA_FLAG.WALL,
		       /* 05 */DATA_FLAG.WALL,
		       /* 06 */DATA_FLAG.WALL,
		       /* 07 */DATA_FLAG.NULL,
		       /* 08 */DATA_FLAG.NULL
                            }
                    }

                    		,
		//-----------------------------------------------------------
		//state 2
		{
		    /* 1 */
		    {
		        /* 01 */DATA_FLAG.WALL,
		        /* 02 */DATA_FLAG.WALL,
		        /* 03 */DATA_FLAG.WALL,
		        /* 04 */DATA_FLAG.WALL,
		        /* 05 */DATA_FLAG.WALL,
		        /* 06 */DATA_FLAG.NULL,
		        /* 07 */DATA_FLAG.NULL,
		        /* 08 */DATA_FLAG.NULL,
		        /* 09 */DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */DATA_FLAG.WALL,
		        /* 02 */DATA_FLAG.PATH | DATA_FLAG.MP,
		        /* 03 */DATA_FLAG.PATH,
		        /* 04 */DATA_FLAG.PATH,
		        /* 05 */DATA_FLAG.WALL,
		        /* 06 */DATA_FLAG.NULL,
		        /* 07 */DATA_FLAG.NULL,
		        /* 08 */DATA_FLAG.NULL,
		        /* 09 */DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */DATA_FLAG.WALL,
		        /* 02 */DATA_FLAG.PATH,
		        /* 03 */DATA_FLAG.PATH | DATA_FLAG.BOX,
		        /* 04 */DATA_FLAG.PATH | DATA_FLAG.BOX,
		        /* 05 */DATA_FLAG.WALL,
		        /* 06 */DATA_FLAG.NULL,
		        /* 07 */DATA_FLAG.WALL,
		        /* 08 */DATA_FLAG.WALL,
		        /* 09 */DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */DATA_FLAG.WALL,
		        /* 02 */DATA_FLAG.PATH,
		        /* 03 */DATA_FLAG.PATH | DATA_FLAG.BOX,
		        /* 04 */DATA_FLAG.PATH,
		        /* 05 */DATA_FLAG.WALL,
		        /* 06 */DATA_FLAG.NULL,
		        /* 07 */DATA_FLAG.WALL,
		        /* 08 */DATA_FLAG.DEST,
		        /* 09 */DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */DATA_FLAG.WALL,
		        /* 02 */DATA_FLAG.WALL,
		        /* 03 */DATA_FLAG.WALL,
		        /* 04 */DATA_FLAG.PATH,
		        /* 05 */DATA_FLAG.WALL,
		        /* 06 */DATA_FLAG.WALL,
		        /* 07 */DATA_FLAG.WALL,
		        /* 08 */DATA_FLAG.DEST,
		        /* 09 */DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */DATA_FLAG.NULL,
		        /* 02 */DATA_FLAG.WALL,
		        /* 03 */DATA_FLAG.WALL,
		        /* 04 */DATA_FLAG.PATH,
		        /* 05 */DATA_FLAG.PATH,
		        /* 06 */DATA_FLAG.PATH,
		        /* 07 */DATA_FLAG.PATH,
		        /* 08 */DATA_FLAG.DEST,
		        /* 09 */DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */DATA_FLAG.NULL,
		        /* 02 */DATA_FLAG.WALL,
		        /* 03 */DATA_FLAG.PATH,
		        /* 04 */DATA_FLAG.PATH,
		        /* 05 */DATA_FLAG.PATH,
		        /* 06 */DATA_FLAG.WALL,
		        /* 07 */DATA_FLAG.PATH,
		        /* 08 */DATA_FLAG.PATH,
		        /* 09 */DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */DATA_FLAG.NULL,
		        /* 02 */DATA_FLAG.WALL,
		        /* 03 */DATA_FLAG.PATH,
		        /* 04 */DATA_FLAG.PATH,
		        /* 05 */DATA_FLAG.PATH,
		        /* 06 */DATA_FLAG.WALL,
		        /* 07 */DATA_FLAG.WALL,
		        /* 08 */DATA_FLAG.WALL,
		        /* 09 */DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */DATA_FLAG.NULL,
		        /* 02 */DATA_FLAG.WALL,
		        /* 03 */DATA_FLAG.WALL,
		        /* 04 */DATA_FLAG.WALL,
		        /* 05 */DATA_FLAG.WALL,
		        /* 06 */DATA_FLAG.WALL,
		        /* 07 */DATA_FLAG.NULL,
		        /* 08 */DATA_FLAG.NULL,
		        /* 09 */DATA_FLAG.NULL
		    }
		}
            };
}

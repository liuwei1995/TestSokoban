package com.after.today;
//答案:F

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.xinaliu.testsokoban.R;

import java.util.ArrayList;

public class GameView extends View /*最好用SurfaceView,由于鄙人较懒☺,就直接View了*/{

	public PushBoxMain gameMain = null;
	public static int width = 0;
	public static int xoff = 25;
	public static int yoff = 50;

	public final int WALL = 1;//声明墙的代号为1
	public final int GOAL = 2;
	public final int ROAD = 3;//声明路的代号为3
	public final int BOX = 4;//声明箱子的代号为4
	public final int BOX_AT_GOAL = 5;
	public final int WORKER = 6;//声明人的代号为6
	public final int BACK=7;//声明返回按钮的代号为7
	public final int UP=8;//声明向上按钮的代号为8
	public final int DOWN=9;//声明向下按钮的代号为9
	public final int LEFT=10;//声明向左按钮的代号为10
	public final int RIGHT=11;//声明向右按钮的代号为11
	public final int MUSIC=12;//声明音乐开关的代号为1

	public Bitmap pic[] = null;//声明引入的图像集
	private int[][] map = null;
	private int[][] tem =null;
	private int gate =0;//关卡标志
	private int manRow = 0;
	private int manColumn = 0;
	public int picSize = 30;
	public int row = 0;
	public int column = 0;
	
	MediaPlayer m,m1;
	
	
	class CurrentMap{
		int [][] curMap;
		public CurrentMap(int[][] myMap){
			int r=myMap.length;
			int c=myMap[0].length;
			int[][] temp=new int [r][c];
			for(int i=0;i<r;i++)
				for(int j=0;j<r;j++)
					temp[i][j]=myMap[i][j];
			this.curMap=temp;
		}
		public int[][] getMap(){
			return curMap;
		}
	}
  private ArrayList <CurrentMap> list=new ArrayList<CurrentMap>();
	
  
  	public GameView(Context context) {
		super(context);
		gameMain = (PushBoxMain) context;
		WindowManager win = gameMain.getWindowManager();
		width = win.getDefaultDisplay().getWidth();
		Log.d("width", String.valueOf(width));
		Log.d("count", String.valueOf(MapList.getCount()));

		intMap();//初始化地图
		intPic();//初始化图片
		initSounds();//初始化声音

	}
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		gameMain = (PushBoxMain) context;
		WindowManager win = gameMain.getWindowManager();
		width = win.getDefaultDisplay().getWidth();
		Log.d("width", String.valueOf(width));
		Log.d("count", String.valueOf(MapList.getCount()));
		
		intMap();//初始化地图
		intPic();//初始化图片
		initSounds();//初始化声音
		
	}
	public void initSounds(){
		/*通过MediaPlayer开启BGM(background music即背景音乐)*/
		
		m = MediaPlayer.create(this.getContext(), R.raw.jimi);
		m.start();
		m1 = MediaPlayer.create(this.getContext(), R.raw.dingdong );
	} 

	private void intMap() {
		// 初始化地图
		map = MapList.getMap(gate);
		// tem=MapList.getMap(gate);
		getMapDetail();
		getManPosition();
	}

	private void getManPosition() {
		//得到人的位置
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] == WORKER) {
					manRow = i;
					manColumn = j;
					break;
				}
			}

	}

	/*
	 * public int[][] getMap(int grade) { return MapList.getMap(grade); }
	 */

	private void getMapDetail() {
		//得到网格大小
		row = map.length;
		column = map[0].length;//得到总地图的宽有几格,可以看一下MapList类,仔细想想,你就明白了
		picSize = (int) Math.floor((width - 2 * xoff) / column);//得到图片大小

		tem = MapList.getMap(gate);
	}

	private void intPic() {
		// 初始化图片
		pic = new Bitmap[13];
		loadPic(WALL, this.getResources().getDrawable(R.drawable.wall));
		loadPic(GOAL, this.getResources().getDrawable(R.drawable.goal));
		loadPic(ROAD, this.getResources().getDrawable(R.drawable.road));
		loadPic(BOX, this.getResources().getDrawable(R.drawable.box));
		loadPic(BOX_AT_GOAL,this.getResources().getDrawable(R.drawable.box_at_goal));
		loadPic(WORKER, this.getResources().getDrawable(R.drawable.man));
   
		loadPic(BACK, this.getResources().getDrawable(R.drawable.back));
		loadPic(UP, this.getResources().getDrawable(R.drawable.up));
		loadPic(DOWN, this.getResources().getDrawable(R.drawable.down));
		loadPic(LEFT, this.getResources().getDrawable(R.drawable.left));
		loadPic(RIGHT, this.getResources().getDrawable(R.drawable.right));
		loadPic(MUSIC, this.getResources().getDrawable(R.drawable.music));
		
	}

	private void loadPic(int KEY, Drawable dw) {
		// 构造Bitmap
		Bitmap bm = Bitmap.createBitmap(picSize, picSize,
				Bitmap.Config.ARGB_8888);
		dw.setBounds(0, 0, picSize, picSize);
		Canvas cs = new Canvas(bm);
		dw.draw(cs);
		pic[KEY] = bm;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 画笔
		Paint paint = new Paint();
		paint.setTextSize(20f);
	    paint.setColor(Color.WHITE);
		canvas.drawText("第"+String.valueOf(gate+1)+"关", 2*width/5,yoff/2 , paint);
		
		//绘制地图
		for (int i = 0; i < row; i++)
			for (int j = 0; j < column; j++) {
				if (map[i][j] > 0)
					canvas.drawBitmap(pic[map[i][j]], xoff + j * picSize, yoff+ i * picSize, paint);
			}
		//绘制按钮
		canvas.drawBitmap(pic[BACK], xoff +picSize, 2*yoff+ row* picSize, paint);
		canvas.drawBitmap(pic[UP], xoff +2*picSize, 2*yoff+ row* picSize, paint);
		canvas.drawBitmap(pic[DOWN], xoff +3*picSize, 2*yoff+ row* picSize, paint);
		canvas.drawBitmap(pic[LEFT], xoff +4*picSize, 2*yoff+ row* picSize, paint);
		canvas.drawBitmap(pic[RIGHT], xoff +5*picSize, 2*yoff+ row* picSize, paint);
		canvas.drawBitmap(pic[MUSIC], xoff +6*picSize, 2*yoff+ row* picSize, paint);
		

		super.onDraw(canvas);
	}

	private int roadOrGoal() {
		//判断是路还是目标
		int result = ROAD;
		if (tem[manRow][manColumn] == GOAL)
			result = GOAL;
		return result;
	}

	private float x=0;
	private float y=0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		//得到点击的x,y坐标
		x=event.getX();
		y=event.getY();
	
			
		
		//按钮监听,代码多了,不解释,就这样吧,这里我忍不住想吐槽几句,Android的View也好,SurfaceView也好,触屏监听都要这么麻烦,真受不了
		
		if(y>2*yoff+ row* picSize && y<2*yoff+ row* picSize+picSize){
			if(x> xoff+picSize&& x<xoff+2*picSize){
				backMap();
			}
			else if(x> xoff+2*picSize&& x<xoff+3*picSize){
				moveUp();
			}
			else if(x> xoff+3*picSize&& x<xoff+4*picSize){
				moveDown();
			}
			else if(x> xoff+4*picSize&& x<xoff+5*picSize){
				moveLeft();
			}
			else if(x> xoff+5*picSize&& x<xoff+6*picSize){
				moveRight();
			}
			else if(x> xoff+6*picSize&& x<xoff+7*picSize){
			
				if(!m.isPlaying()){
					m.start();//播放
			   }else
				   m.pause();//暂停
		  }
		}
		
		//触屏监听
		if(x>xoff+manColumn*picSize && x<xoff+manColumn*picSize+picSize){
			if(y<yoff+manRow*picSize)
				moveUp();
			
			else if(y>yoff+manRow*picSize+picSize)
			    moveDown();
		   
		}else if(y>yoff+manRow*picSize && y<yoff+manRow*picSize+picSize){
			if(x<xoff+manColumn*picSize)
				moveLeft();
			
			if(x>xoff+manColumn*picSize+picSize)
				moveRight();
		}
	
		if (gameIsFinished()) {
			nextGate();//下一关

		}
		invalidate();//记住刷新draw();
		return super.onTouchEvent(event);
	}
	
	
	 

	
	private void backMap(){
		if(list.size()>0){
			CurrentMap priMap=list.get(list.size()-1);
			map=priMap .getMap();
			getManPosition();
			list.remove(list.size()-1);
			
		}
		else
			Toast.makeText(this.getContext(), "You can't back the game!", Toast.LENGTH_LONG).show();
	}
	
	private void storeMap(int [][] maps){
		CurrentMap curMap=new CurrentMap(maps);
		list.add(curMap);
		if(list.size()>10)
			list.remove(0);
	}
	
	private boolean gameIsFinished() {
		// TODO Auto-generated method stub
		boolean gameFinsh = true;
		for (int i = 0; i < row; i++)
			for (int j = 0; j < column; j++)
				if (map[i][j] == BOX)
					gameFinsh = false;

		return gameFinsh;
	}

	
	private void nextGate() {
		
		if(MapList.getCount()==gate+1){//判断是否是最后一关
			AlertDialog.Builder builder=new AlertDialog.Builder(gameMain);
			builder.setTitle("恭喜你完成全部游戏"); 
			builder.setMessage("重点是学会了什么东西?"); 
			builder.setPositiveButton("再来", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					gate=0;
					intMap();
					invalidate();
				}
				
			});
			builder.setNegativeButton("算了吧", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					gameMain.finish();//结束游戏
					m.stop(); //结束BGM
				}
				
			});
			builder.create();
			builder.show();
		}
		
		else{
			gate++;//下一关
			m1.start();//BGM播放
			intMap();//加载下一个关卡地图
		}
		
		
	}

	private void moveDown() {
		if (map[manRow + 1][manColumn] == BOX|| map[manRow + 1][manColumn] == BOX_AT_GOAL) {
			if (map[manRow + 2][manColumn] == GOAL|| map[manRow + 2][manColumn] == ROAD) {
				storeMap(map);
				map[manRow + 2][manColumn] = map[manRow + 2][manColumn] == GOAL ? BOX_AT_GOAL
						: BOX;
				map[manRow + 1][manColumn] = WORKER;
				map[manRow][manColumn] = roadOrGoal();
				manRow++;
				return;
			}
		} else {
			if (map[manRow + 1][manColumn] == ROAD|| map[manRow + 1][manColumn] == GOAL) {
				storeMap(map);
				map[manRow + 1][manColumn] = WORKER;
				map[manRow][manColumn] = roadOrGoal();
				manRow++;
				return;
			}
		}
	}

	private void moveUp() {
		// TODO Auto-generated method stub
		if (map[manRow - 1][manColumn] == BOX|| map[manRow - 1][manColumn] == BOX_AT_GOAL) {
			if (map[manRow - 2][manColumn] == GOAL|| map[manRow - 2][manColumn] == ROAD) {
				storeMap(map);
				map[manRow - 2][manColumn] = map[manRow - 2][manColumn] == GOAL ? BOX_AT_GOAL: BOX;
				map[manRow - 1][manColumn] = WORKER;
				map[manRow][manColumn] = roadOrGoal();
				manRow--;
				return;
			}
		} else {
			if (map[manRow - 1][manColumn] == ROAD|| map[manRow - 1][manColumn] == GOAL) {
				storeMap(map);
				map[manRow - 1][manColumn] = WORKER;
				map[manRow][manColumn] = roadOrGoal();
				manRow--;
				return;
			}
		}
	}

	private void moveLeft() {
		// TODO Auto-generated method stub
		if (map[manRow][manColumn - 1] == BOX|| map[manRow][manColumn - 1] == BOX_AT_GOAL) {
			if (map[manRow][manColumn - 2] == GOAL|| map[manRow][manColumn - 2] == ROAD) {
				storeMap(map);
				map[manRow][manColumn - 2] = map[manRow][manColumn - 2] == GOAL ? BOX_AT_GOAL: BOX;
				map[manRow][manColumn - 1] = WORKER;
				map[manRow][manColumn] = roadOrGoal();
				manColumn--;
			}
		} else {
			if (map[manRow][manColumn - 1] == ROAD|| map[manRow][manColumn - 1] == GOAL) {
				storeMap(map);
				map[manRow][manColumn - 1] = WORKER;
				map[manRow][manColumn] = roadOrGoal();
				manColumn--;
			}
		}
	}

	private void moveRight() {
		// TODO Auto-generated method stub
		if (map[manRow][manColumn + 1] == BOX|| map[manRow][manColumn + 1] == BOX_AT_GOAL) {
			if (map[manRow][manColumn + 2] == GOAL|| map[manRow][manColumn + 2] == ROAD) {
				storeMap(map);
				map[manRow][manColumn + 2] = map[manRow][manColumn + 2] == GOAL ? BOX_AT_GOAL: BOX;
				map[manRow][manColumn + 1] = WORKER;
				map[manRow][manColumn] = roadOrGoal();
				manColumn++;
			}
		} else {
			if (map[manRow][manColumn + 1] == ROAD|| map[manRow][manColumn + 1] == GOAL) {
				storeMap(map);
				map[manRow][manColumn + 1] = WORKER;
				map[manRow][manColumn] = roadOrGoal();
				manColumn++;
			}
		}
	}

}

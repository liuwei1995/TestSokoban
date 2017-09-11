package com.after.today;

import android.app.Activity;
import android.os.Bundle;

import com.xinaliu.testsokoban.R;

public class PushBoxMain extends Activity {
	
	//先说一下重点,也是难点,这个是本次绘制地图的关键
	//1,int[]表示一个int类型的数组
	//  如int[] i=new int[]{1,2,3}; i[0]表示1,这里相信大家都会,我就不赘述了
	//2,int[][]可以理解为int[]的int数组
	//  可以这样想:int[] i1=new int[]{1,2,3};
	//           int[] i2=new int[]{4,5,6};
	//           int[][] i={ {1,2,3}/*表示i1*/, {4,5,6}/*表示i2*/ }
	//           i[0]就指i1,i[0][0]就指的是i1[0]即1
	//           同理,可得i[1]即指i2,i[1][0]就是i2[0]即4
	//           可以这样"拆分"数组,化难为易
	//           先别急着看第三点,做一个练习看看掌握了没有
	//           int[][] i={ {123,456,789},{521,1314} }; i[1][2]表示什么? A.123 B.456 C.789 D.521 E.1314 F.不存在 答案:GameView类开头
	//3,可以想,有几个[]就有几个大括号
	//           int[][][] i={ { {},{} } , { {},{} } }
	//           尝试拆分一下吧, 哈哈😄
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//也可以用setContentView(new GameView(this));
	    setContentView(R.layout.activity_main);
	
	}

}

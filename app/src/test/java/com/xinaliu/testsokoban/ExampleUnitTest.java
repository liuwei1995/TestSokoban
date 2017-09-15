package com.xinaliu.testsokoban;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public final int WALL = GameStateData.DATA_FLAG.WALL;//声明墙的代号为1
    public final int GOAL = GameStateData.DATA_FLAG.DEST;//目标
    public final int ROAD = GameStateData.DATA_FLAG.PATH;//声明路的代号为3
    public final int BOX = GameStateData.DATA_FLAG.PATH | GameStateData.DATA_FLAG.BOX;//声明箱子的代号为4
    public final int BOX_AT_GOAL = GameStateData.DATA_FLAG.MASK_MAP_AND_SPRITE;  //位置已经放好了的
    public final int WORKER = GameStateData.DATA_FLAG.PATH | GameStateData.DATA_FLAG.MP;//声明人的代号为6
    public final int NULL = GameStateData.DATA_FLAG.NULL;//声明空白区域的代号为
    int i_x = 0;
    int i_y = 0;
    @Test
    public void addition_isCorrect() throws Exception {
        int p = 0;
        int[][] gameStateData = GameStateDataProvider.getGameStateData(p);
        StringBuilder sb = new StringBuilder();
        sb.append(p+"=="+gameStateData.length+"\t"+gameStateData[0].length+"\n");

        for (int i = 0; i < gameStateData.length; i++) {
            for (int i1 = 0; i1 < gameStateData[i].length; i1++) {
                int i2 = gameStateData[i][i1];
                if (i2 == WORKER){
                    i_x = i;
                    i_y = i1;
                }
                sb.append(""+i2+"\t");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
        set(gameStateData,4,2);

    }
    public void set(int gameStateData[][],int x, int y){
        int i = gameStateData[x][y];


    }


}
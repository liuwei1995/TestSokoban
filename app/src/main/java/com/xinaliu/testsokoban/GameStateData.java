package com.xinaliu.testsokoban;

/**
 * Created by liuwei on 2017/9/12 10:39
 */

public class GameStateData {


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

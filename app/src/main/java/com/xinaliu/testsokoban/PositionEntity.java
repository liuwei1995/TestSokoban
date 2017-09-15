package com.xinaliu.testsokoban;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 位置
 * Created by liuwei on 2017/9/13 16:03
 */

public class PositionEntity implements Serializable, Parcelable {

    private Rect rect;

    private int type = 0;

    private int x;

    private int y;

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    /**
     * 列
     * @return column
     */
    public int getX() {
        return x;
    }

    /**
     * 列
     * @param x column
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * 行
     * @return row
     */
    public int getY() {
        return y;
    }

    /**
     * 行
     * @param y row
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.rect, flags);
        dest.writeInt(this.type);
        dest.writeInt(this.x);
        dest.writeInt(this.y);
    }

    public PositionEntity(Rect rect, int type, int x, int y) {
        this.rect = rect;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public PositionEntity() {
    }

    protected PositionEntity(Parcel in) {
        this.rect = in.readParcelable(Rect.class.getClassLoader());
        this.type = in.readInt();
        this.x = in.readInt();
        this.y = in.readInt();
    }

    public static final Parcelable.Creator<PositionEntity> CREATOR = new Parcelable.Creator<PositionEntity>() {
        @Override
        public PositionEntity createFromParcel(Parcel source) {
            return new PositionEntity(source);
        }

        @Override
        public PositionEntity[] newArray(int size) {
            return new PositionEntity[size];
        }
    };
}

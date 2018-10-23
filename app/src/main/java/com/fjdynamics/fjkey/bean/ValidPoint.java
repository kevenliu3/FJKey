package com.fjdynamics.fjkey.bean;

/**
 * @author Administrator QQ:824679118
 * @class name：com.keven.hibox.Bean
 * @time 2018/9/9 10:29
 * @class describe
 */
public class ValidPoint {

    private int row;
    private int col;
    private long startTime;
    private long endTime;

    public ValidPoint(int row, int col, long startTime, long endTime) {
        this.row = row;
        this.col = col;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}

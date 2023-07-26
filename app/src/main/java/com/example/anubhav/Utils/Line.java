package com.example.anubhav.Utils;
import android.graphics.Color;
import android.graphics.Paint;

public class Line {

    private float startX, startY, stopX, stopY;
    private int orientation;
    Paint paint;

    public int getOrientation() {
        return orientation;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public void setStopX(float stopX) {
        this.stopX = stopX;
    }

    public void setStopY(float stopY) {
        this.stopY = stopY;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getStopX() {
        return stopX;
    }

    public float getStopY() {
        return stopY;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setColor(String color) {
        paint.setColor(Color.parseColor(color));
    }

    public Line(Paint paint) {
        this.paint = new Paint(paint);
    }

    public Line(float startX, float startY, float stopX, float stopY) {
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
    }

    public Paint getPaint() {
        return paint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        Line line = (Line) o;
        return (Float.compare(line.startX, startX) == 0 &&
                Float.compare(line.startY, startY) == 0 &&
                Float.compare(line.stopX, stopX) == 0 &&
                Float.compare(line.stopY, stopY) == 0)
                ||
                (Float.compare(line.startX, stopX) == 0 &&
                        Float.compare(line.startY, stopY) == 0 &&
                        Float.compare(line.stopX, startX) == 0 &&
                        Float.compare(line.stopY, startY) == 0);
    }
}

package com.yy.music.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class TimeView extends View {

    private int measureWidth;
    private int measureHeight;
    private Paint paint;

    private String text = "5";
    private Paint paint2;


    public TimeView(Context context) {
        super(context);
        init();
    }

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setData(String text){
        this.text = text;
        invalidate();
    };

    public void init(){
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);

        paint2 = new Paint();
        paint2.setColor(Color.RED);
        paint2.setTextSize(100);
        paint2.setStrokeWidth(12);
        paint2.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int num = Math.min(measureWidth,measureHeight);
        canvas.drawCircle(num/2,num/2,num/2-6,paint);

        Rect rect = new Rect();
        paint2.getTextBounds(text,0,text.length(),rect);
        Paint.FontMetricsInt fontMetrics = paint2.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;

        canvas.drawText(text, num/2,baseLine, paint2);
        super.onDraw(canvas);
    }
}

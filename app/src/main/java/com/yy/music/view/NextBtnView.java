package com.yy.music.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class NextBtnView extends View {

    private int measureHeight;
    private Paint paint;
    private Paint paint2;
    private Path path;

    public NextBtnView(Context context) {
        super(context);
    }

    public NextBtnView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("ResourceAsColor")
    public void init(){
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);


        paint2 = new Paint();
        paint2.setColor(Color.GREEN);
        paint2.setStyle(Paint.Style.FILL);

        path = new Path();
        path.moveTo(measureHeight/3,measureHeight/3);
        path.lineTo(measureHeight/3,measureHeight*2/3);
        path.lineTo(measureHeight*2/3,measureHeight/2);
        path.close();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        init();
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        setMeasuredDimension(measureHeight, measureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path,paint2);
        @SuppressLint("DrawAllocation")
        RectF rectF = new RectF(measureHeight*2/3,measureHeight/3,measureHeight*2/3+measureHeight/16,measureHeight*2/3);
        canvas.drawRect(rectF,paint2);
    }
}

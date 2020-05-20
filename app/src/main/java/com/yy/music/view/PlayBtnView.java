package com.yy.music.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


public class PlayBtnView extends View {

    private Paint paint;
    private int measureHeight;

    private boolean isPlaying;
    private int maxProgress=100;
    private int progress=0;
    private Paint paint2;
    @SuppressLint("DrawAllocation")
    private Path path;
    @SuppressLint("DrawAllocation")
    private RectF rectF1;
    @SuppressLint("DrawAllocation")
    private RectF rectF2;

    public PlayBtnView(Context context) {
        super(context);
        //init();
    }
    public PlayBtnView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //init();
    }

    public void setMax(int maxProgress){
        this.maxProgress = maxProgress;
    }
    public void setData(boolean isPlaying,int progress){
        this.isPlaying = isPlaying;
        this.progress = progress;
        invalidate();
    }
    public void setData(boolean isPlaying,int maxProgress,int progress){
        this.isPlaying = isPlaying;
        this.maxProgress = maxProgress;
        this.progress = progress;
        invalidate();
    }
    public void setData(boolean isPlaying){
        this.isPlaying = isPlaying;
        invalidate();
    }

    @SuppressLint("ResourceAsColor")
    public void init(){
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(measureHeight/16);


        paint2 = new Paint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            paint2.setColor(Color.GREEN);
        }
        paint2.setStyle(Paint.Style.FILL);

        path = new Path();
        path.moveTo(measureHeight/3+measureHeight/16,measureHeight/3);
        path.lineTo(measureHeight/3+measureHeight/16,measureHeight*2/3);
        path.lineTo(measureHeight*2/3+measureHeight/16,measureHeight/2);
        path.close();

        rectF1 = new RectF(measureHeight/3,measureHeight/3,measureHeight*5/12,measureHeight*2/3);
        rectF2 = new RectF(measureHeight*7/12,measureHeight/3,measureHeight*2/3,measureHeight*2/3);
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
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(maxProgress==0)
            maxProgress=1;
        int nowDegree = 360*progress/maxProgress;
        canvas.drawCircle(measureHeight/2,measureHeight/2,measureHeight*15/32,paint);
        @SuppressLint("DrawAllocation")
        RectF rectF = new RectF(measureHeight*3/32,measureHeight*3/32,measureHeight*29/32,measureHeight*29/32);
        canvas.drawArc(rectF,-90,nowDegree,false,paint);

        if(isPlaying){
            canvas.drawPath(path,paint2);
        }else{
            canvas.drawRect(rectF1,paint2);
            canvas.drawRect(rectF2,paint2);
        }

    }




}

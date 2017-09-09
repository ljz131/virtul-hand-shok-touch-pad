package com.example.ljz131.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
*   author:ljz131 on 2017/5/7 15:29
*   email:raj_prunus@foxmail.com
*/
public class TouchPad extends SurfaceView {


    private Paint paint;
    private Path path;
    private Path path2;
    private Context context;

    public TouchPad(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TouchPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public TouchPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        path = new Path();
        path2 = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawPath(path, paint);
        canvas.drawPath(path2, paint);
    }

    private float cur_x, cur_y;     //单指当前座标
    private float cur_x2, cur_y2;     //第二指当前座标
    private float downX, downY;     //手指按下的座标
    private float endX, endY;       //手指抬起座标
    private float detaX, detaY,detaX2,detaY2;     //第一指和第二指的座标增量
    int num,index,index2;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        index = event.getActionIndex();
        index2 = event.getPointerId(event.getActionIndex());
        num = event.getPointerCount();
        //if(num == 1){

            float x = event.getX(0);
            float y = event.getY(0);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = x;
                    downY = y;
                    cur_x = x;
                    cur_y = y;
                    path.moveTo(cur_x, cur_y);
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if(index2 == 1){
                        path2.quadTo(cur_x2, cur_y2, x, y);
                        detaX = x - cur_x;
                        detaY = y - cur_y;
                        cur_x2 = x;
                        cur_y2 = y;
                    }else {
                        detaX = x - cur_x;
                        detaY = y - cur_y;
                        cur_x = x;
                        cur_y = y;
                        path.quadTo(cur_x, cur_y, x, y);
                        if(num == 2) {
                            float x2 = event.getX(1);
                            float y2 = event.getY(1);
                            detaX2 = x2 - cur_x2;
                            detaY2 = y2 - cur_y2;
                            path2.quadTo(cur_x2, cur_y2, x2, y2);
                            cur_x2 = x2;
                            cur_y2 = y2;
                        }else {
                            cur_x2 = 0;
                            cur_y2 = 0;
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    if(path.isEmpty()){
                        path2.reset();
                    }else {
                        endX = x;
                        endY = y;
                        path.reset();
                    }
                    break;
                }
                case 261: { //256+5，第二指的按下
                    float x2 = event.getX(1);
                    float y2 = event.getY(1);
                    detaX2 = 0;
                    detaY2 = 0;//这里还是上一次的
                    cur_x2 = x2;
                    cur_y2 = y2;
                    path2.moveTo(cur_x2, cur_y2);
                    break;
                }
                case 262: { //256+6，第二指的抬起
                    cur_x2 = 0;
                    cur_y2 = 0;
                    path2.reset();
                    break;
                }
                case MotionEvent.ACTION_POINTER_DOWN :{//5
                    path.moveTo(x,y);
                    break;
                }

                case MotionEvent.ACTION_POINTER_UP: {//6
                    cur_x = 0;
                    cur_y = 0;
                    path.reset();
                    break;
                }
            }

        mtouchPadChangeListner.report(cur_x,cur_y,cur_x2,cur_y2,detaX,detaY,detaX2,detaY2);
        invalidate();
        return true;
    }

   /* 用来测试轨迹残留效果*/
/*    public void reset() {

        postDelayed(new Runnable() {
            @Override
            public void run() {
                path.reset();
                invalidate();
            }
        }, 1000);

    }
    public void reset2() {

        postDelayed(new Runnable() {
            @Override
            public void run() {
                path2.reset();
                invalidate();
            }
        }, 1000);

    }*/

    TouchPadChangeListner mtouchPadChangeListner = null;
    public void setTouchPadListner(TouchPadChangeListner touchPadChangeListner){
        mtouchPadChangeListner = touchPadChangeListner;
    }
    interface TouchPadChangeListner{
        void report(float x1,float y1,float x2,float y2,float detax1,float detay1,float detax2,float detay2);
    }
}

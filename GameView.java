package com.example.hackgt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;


public class GameView extends View {
    Handler handler;
    Runnable runnable;
    final int UPDATE_MILLIS = 30;
    Bitmap back;
    Bitmap toptube, bottomtube;
    Display display;
    Point point;
    int dWidth, dHeight;
    Rect rect;

    Bitmap[] birds;
    int birdFrame = 0;
    int velocity = 0, gravity=3;
    int birdX, birdY;
    boolean gameState = false;
    int gap = 400; //Gap bw top tube and bottom one
    int minTubeOffset, maxTubeOffset;
    int numberOfTubes = 4;
    int distanceBetweenTubes;
    int[] tubeX = new int[numberOfTubes];
    int[] topTubeY = new int[numberOfTubes];
    Random random;
    int tubeVelocity = 8;

    public GameView(Context context){
        super(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        back = BitmapFactory.decodeResource(getResources(),R.drawable.back);
        toptube = BitmapFactory.decodeResource(getResources(), R.drawable.toptube);
        bottomtube = BitmapFactory.decodeResource(getResources(), R.drawable.bottomtube);
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        rect = new Rect(0,0,dWidth,dHeight);
        birds = new Bitmap[2];
        birds[0] = BitmapFactory.decodeResource(getResources(),R.drawable.b1);
        birds[1] = BitmapFactory.decodeResource(getResources(),R.drawable.b2);
        birdX = dWidth/2 - birds[0].getWidth()/2; //Bird will be initially in the center
        birdY = dHeight/2 - birds[0].getHeight()/2;
        distanceBetweenTubes = dWidth*3/4;
        minTubeOffset = gap/2;
        minTubeOffset = dHeight - minTubeOffset - gap;
        random = new Random();
        for(int i=0; i<numberOfTubes;i++){
            tubeX[i] = dWidth + i*distanceBetweenTubes;
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //canvas.drawBitmap(back,0,0,null);
        canvas.drawBitmap(back,null,rect,null); //fixed
        if(birdFrame == 0) {
            birdFrame = 1;
        }else{
            birdFrame = 0;
        }
        if(gameState) {

            if (birdY < dHeight - birds[0].getHeight() || velocity < 0)//the subject(bird) stays on the screen

                velocity += gravity; //as bird falls it is getting faster.
            birdY += velocity;
        }
        for(int i=0;i<numberOfTubes;i++) {
            tubeX[i] -= tubeVelocity;
            if(tubeX[i] < -toptube.getWidth())
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset +1);
            canvas.drawBitmap(toptube, tubeX[i], topTubeY[i] - toptube.getHeight(), null);
            canvas.drawBitmap(bottomtube, tubeX[i], topTubeY[i] + gap, null);

        }


        canvas.drawBitmap(birds[birdFrame],birdX,birdY,null);
        handler.postDelayed(runnable,UPDATE_MILLIS);
    }

    public boolean onTouchEvent(MotionEvent event){

            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN){
                velocity = -30;
                gameState = true;

            }

        return true; //touch event ends
    }
}

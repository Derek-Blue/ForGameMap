package com.example.forgamemap;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ImageView cat;

    int screenWidth, screenHeight, catWidth, catheight;

    float catX, catY, touchX, touchY, catSpeed;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    boolean walking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cat = findViewById(R.id.cat);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();//取得螢幕大小
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        catSpeed = Math.round(screenWidth / 60);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            touchX = event.getX();
            touchY = event.getY();

            if (walking == false) {  //只有點擊第一下開始執行 Timer 執行緒

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                position();
                            }
                        });
                    }
                }, 0, 20);// 0秒開始, 每20毫秒執行(run)一次

                walking = true; // 否則會一直重覆執行(人物越走越快)
            }
        }


        return false;
    }

    private void position() {

        //先取得人物圖片的座標
        catX = cat.getX();
        catY = cat.getY();

        //TouchPosition扣掉寬或高除已2  否則人物圖片會停在點擊的右下角
        catWidth = cat.getWidth(); //人物圖片的寬
        catheight = cat.getHeight(); //人物圖片的高

        if (touchX > catX){

            catX += catSpeed;
            if (catX > touchX - (catWidth/2)){
                catX = touchX - (catWidth/2);
            }

        }else if (touchX < catX){

            catX -= catSpeed;
            if (catX < touchX - (catWidth/2)){
                catX = touchX - (catWidth/2);
            }
        }

        float speedY = catSpeed * (Math.abs(catY-touchY)/Math.abs(catX-touchX));
        Log.v("Y=?",""+speedY );

        if (touchY > catY){

            catY += speedY;
            if (catY > touchY - (catheight/2)){
                catY = touchY - (catheight/2);
            }

        }else if (touchY < catY){

            catY -= speedY;
            if (catY < touchY - (catheight/2)){
                catY = touchY - (catheight/2);
            }

        }

        cat.setX(catX);
        cat.setY(catY);

    }
}

package com.example.wangyueke.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class SplashActivity extends Activity {

    Button button1 = null;
    boolean flag = false;
    // private final int SPLASH_DISPLAY_LENGHT = 2000; // 两秒后进入系统
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        //getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_splash);
        button1 = (Button)findViewById(R.id.jump );
        button1.setOnClickListener(new View.OnClickListener( ) {         //注册按钮
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                flag = true;
            }
        });
        Thread myThread=new Thread(){//创建子线程
            @Override
            public void run() {
                try{
                    sleep(3000);//使程序休眠三秒
                    if(!flag) {
                        Intent it=new Intent(getApplicationContext(),MainActivity.class);//启动MainActivity
                        startActivity(it);
                        finish();//关闭当前活动
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }
}
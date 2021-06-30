package com.uniquik.ecollege;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    ImageView splashImg;
    private  static int SPLASH_SCREEN = 4000;
    TextView splashTitle;
    Animation animMoveRight, animMoveLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashImg = findViewById(R.id.splash_img);
        splashTitle = findViewById(R.id.splash_title);
        animMoveRight = AnimationUtils.loadAnimation(this,R.anim.move_right);
        animMoveLeft = AnimationUtils.loadAnimation(this,R.anim.move_left);
        splashImg.setAnimation(animMoveRight);
        splashTitle.setAnimation(animMoveLeft);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,UserActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}

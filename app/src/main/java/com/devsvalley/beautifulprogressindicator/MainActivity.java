package com.devsvalley.beautifulprogressindicator;

import android.animation.ValueAnimator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;

import com.deepakbaliga.beautifulprogressbar.BeautifulCircularProgressBar;

public class MainActivity extends AppCompatActivity {

    private BeautifulCircularProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        getSupportActionBar().hide();


        progressBar = (BeautifulCircularProgressBar) findViewById(R.id.progress);


        ValueAnimator anim = ValueAnimator.ofFloat(progressBar.progress, 0.8f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(12000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progressBar.setProgress((float) valueAnimator.getAnimatedValue());
            }
        });

        anim.start();


    }
}

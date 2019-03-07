package kr.dangguel.domestictravel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {
    ImageView iv;
    Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        iv=findViewById(R.id.iv);
        Animation ani = AnimationUtils.loadAnimation(this,R.anim.logo);
        iv.startAnimation(ani);

        timer.schedule(task,4000);

    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Intent intent = new Intent(IntroActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    };


}

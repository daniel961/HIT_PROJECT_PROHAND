package com.hit.daniel.hit_project_prohand;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class SplashScreen extends AppCompatActivity {


    LinearLayout l1,l2;
    MediaPlayer splashSound;
    Animation uptodown,downtoup,rotate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* ADD SOUND
        splashSound = MediaPlayer.create(SplashScreen.this,R.raw.splashsound);
        splashSound.start();
        */

        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);





        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        rotate = AnimationUtils.loadAnimation(this,R.anim.rotate);

        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);
        //l3.setAnimation(rotate);




        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                //Create an Intent that will start the Menu-Activity.

                Intent mainIntent = new Intent(SplashScreen.this, LoginScreen.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();

            }
        }, 4000);








    }
}

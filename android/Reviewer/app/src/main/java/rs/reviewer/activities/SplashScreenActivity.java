package rs.reviewer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import rs.reviewer.MainActivity;
import rs.reviewer.R;
import rs.reviewer.utils.UserUtil;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        int SPLASH_TIME_OUT = 2000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                String userString = UserUtil.getLogInUser(getApplicationContext());

                if(userString == null || userString.equals("")){
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish(); // da nebi mogao da ode back na splash
                }
                else{
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}

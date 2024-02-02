package com.moutamid.moneytransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.fxn.stash.Stash;
import com.moutamid.moneytransfer.activities.LanguageSelectionActivity;
import com.moutamid.moneytransfer.activities.WelcomeScreenActivity;
import com.moutamid.moneytransfer.utilis.Constants;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(() -> {
            if (!Stash.getString(Constants.LANGUAGE).isEmpty()){
                if (Constants.auth().getCurrentUser() != null){
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, WelcomeScreenActivity.class));
                    finish();
                }
            } else {
                startActivity(new Intent(SplashScreenActivity.this, LanguageSelectionActivity.class));
                finish();
            }
        }, 2000);
    }
}
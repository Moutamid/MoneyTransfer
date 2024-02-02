package com.moutamid.moneytransfer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.moutamid.moneytransfer.utilis.Constants;
import com.moutamid.moneytransfer.R;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        Constants.checkApp(this);
        Constants.setLocale(getBaseContext(), Stash.getString(Constants.LANGUAGE, "en"));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getColor(R.color.violet));

        // ((ImageView) findViewById(R.id.vector)).setImageResource(R.drawable.transaction);

        ((MaterialButton) findViewById(R.id.signup)).setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
        ((MaterialButton) findViewById(R.id.login)).setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
    }
}
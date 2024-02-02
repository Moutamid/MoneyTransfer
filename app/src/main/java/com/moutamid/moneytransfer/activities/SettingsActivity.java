package com.moutamid.moneytransfer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.fxn.stash.Stash;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.SplashScreenActivity;
import com.moutamid.moneytransfer.databinding.ActivitySettingsBinding;
import com.moutamid.moneytransfer.utilis.Constants;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.setLocale(getBaseContext(), Stash.getString(Constants.LANGUAGE, "en"));
        binding.toolbar.title.setText(getString(R.string.setting));
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());


        binding.logout.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this).setTitle(getString(R.string.logout)).setMessage(getString(R.string.do_you_really_want_to_logout))
                    .setPositiveButton(getString(R.string.yes), ((dialog, which) -> {
                        Constants.auth().signOut();
                        startActivity(new Intent(this, SplashScreenActivity.class));
                        finish();
                    })).setNegativeButton(getString(R.string.no), ((dialog, which) -> dialog.dismiss()))
                    .show();
        });

        binding.update.setOnClickListener(v -> {
            startActivity(new Intent(this, EditProfileActivity.class));
        });

        binding.language.setOnClickListener(v -> {
            startActivity(new Intent(this, LanguageSelectionActivity.class));
        });

        binding.policy.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com")));
        });

        binding.help.setOnClickListener(v -> {
            startActivity(Intent.createChooser(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:help@example.com")), ""));
        });

    }
}
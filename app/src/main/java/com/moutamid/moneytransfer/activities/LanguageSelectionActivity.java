package com.moutamid.moneytransfer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.moutamid.moneytransfer.MainActivity;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.databinding.ActivityLanguageSelectionBinding;
import com.moutamid.moneytransfer.utilis.Constants;

public class LanguageSelectionActivity extends AppCompatActivity {
    ActivityLanguageSelectionBinding binding;
    String selected = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText(getString(R.string.language));
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.backIcon.setImageResource(R.drawable.round_clear_24);

        if (!Stash.getString(Constants.LANGUAGE).isEmpty()) {
            String lang = Stash.getString(Constants.LANGUAGE);
            if (lang.equals("en")) {
                binding.english.setChecked(true);
            } else {
                binding.arabic.setChecked(true);
            }
            Constants.setLocale(getBaseContext(), lang);
        }

        binding.choose.setOnClickListener(v -> {
            selected = binding.english.isChecked() ? "en" : "ar";
            Stash.put(Constants.LANGUAGE, selected);
            if (Constants.auth().getCurrentUser() != null) {
                startActivity(new Intent(LanguageSelectionActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(LanguageSelectionActivity.this, WelcomeScreenActivity.class));
                finish();
            }
        });

    }
}
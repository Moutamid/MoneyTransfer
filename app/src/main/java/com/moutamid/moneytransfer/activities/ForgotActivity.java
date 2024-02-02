package com.moutamid.moneytransfer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.utilis.Constants;
import com.moutamid.moneytransfer.databinding.ActivityForgotBinding;

public class ForgotActivity extends AppCompatActivity {
    ActivityForgotBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.setLocale(getBaseContext(), Stash.getString(Constants.LANGUAGE, "en"));
        binding.toolbar.title.setText(getString(R.string.forgot_password));
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.reset.setOnClickListener(v -> {
            if (valid()){
                Constants.showDialog();
                Constants.auth().sendPasswordResetEmail(
                        binding.email.getEditText().getText().toString()
                ).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    Snackbar.make(this, binding.getRoot(), getString(R.string.a_password_reset_link_is_sent_to_your_email), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.dismiss), v1 -> {})
                            .show();

                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
    }

    private boolean valid() {
        if (binding.email.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.email_is_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()){
            Toast.makeText(this, getString(R.string.email_is_not_valid), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
package com.moutamid.moneytransfer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.moutamid.moneytransfer.utilis.Constants;
import com.moutamid.moneytransfer.MainActivity;
import com.moutamid.moneytransfer.databinding.ActivitySignupBinding;
import com.moutamid.moneytransfer.models.Rating;
import com.moutamid.moneytransfer.models.UserModel;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        binding.toolbar.title.setText("Create Account");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.country.setCustomMasterCountries(Constants.CountriesCodes);
        binding.countryCodePick.setCustomMasterCountries(Constants.CountriesCodes);

        binding.login.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        binding.create.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                Constants.auth().createUserWithEmailAndPassword(
                        binding.email.getEditText().getText().toString(),
                        binding.password.getEditText().getText().toString()
                ).addOnSuccessListener(authResult -> {
                    UserModel userModel = new UserModel(
                            Constants.auth().getCurrentUser().getUid(),
                            binding.name.getEditText().getText().toString(),
                            "",
                            (binding.countryCodePick.getSelectedCountryCodeWithPlus() + "-" + binding.number.getText().toString()),
                            binding.email.getEditText().getText().toString(),
                            binding.password.getEditText().getText().toString(),
                            getCountry(),
                            binding.country.getSelectedCountryNameCode(),
                            new Rating(0,0,0,0,0)
                    );
                    Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                            .setValue(userModel)
                            .addOnSuccessListener(unused -> {
                                Constants.dismissDialog();
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            }).addOnFailureListener(e -> {
                                Constants.dismissDialog();
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private String getCountry() {
        return binding.country.getSelectedCountryEnglishName().equals("United Arab Emirates (UAE)") ? "United Arab Emirates" : binding.country.getSelectedCountryEnglishName();
    }

    private boolean valid() {
        if (binding.name.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.number.getText().toString().isEmpty()) {
            Toast.makeText(this, "Phone Number is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.email.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()){
            Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.password.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
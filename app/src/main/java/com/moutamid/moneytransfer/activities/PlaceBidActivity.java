package com.moutamid.moneytransfer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.moneytransfer.databinding.ActivityPlaceBidBinding;
import com.moutamid.moneytransfer.utilis.Constants;

public class PlaceBidActivity extends AppCompatActivity {
    ActivityPlaceBidBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceBidBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Place Your Bid");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        Constants.initDialog(this);

    }
}
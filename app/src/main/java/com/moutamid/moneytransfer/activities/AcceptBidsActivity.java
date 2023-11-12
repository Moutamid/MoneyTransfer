package com.moutamid.moneytransfer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.databinding.ActivityAcceptBidsBinding;

public class AcceptBidsActivity extends AppCompatActivity {
    ActivityAcceptBidsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAcceptBidsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Bid");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

    }
}
package com.moutamid.moneytransfer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.moutamid.moneytransfer.activities.EditProfileActivity;
import com.moutamid.moneytransfer.activities.SettingsActivity;
import com.moutamid.moneytransfer.databinding.ActivityMainBinding;
import com.moutamid.moneytransfer.fragments.ChatFragment;
import com.moutamid.moneytransfer.fragments.HomeFragment;
import com.moutamid.moneytransfer.fragments.MyBidsFragment;
import com.moutamid.moneytransfer.models.CountriesRates;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);
        Constants.initDialog(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

    private void updateNavHead() {

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    Stash.put(Constants.STASH_USER, userModel);
                    String country = userModel.getCountry().replace(" ", "_");
                    Log.d("updateNavHead", "updateNavHead: " + country);
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateNavHead();
    }
}
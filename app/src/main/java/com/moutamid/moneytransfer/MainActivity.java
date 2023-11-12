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
import com.moutamid.moneytransfer.models.CountriesRates;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;

    TextView headerName, headerEmail;
    CircleImageView headerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);
        Constants.initDialog(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                binding.drawLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.drawLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        binding.navView.setCheckedItem(R.id.nav_home);

        binding.navView.getHeaderView(0).setOnClickListener(view -> {
//            startActivity(new Intent(this, ProfileActivity.class));
        });

    }

    private void updateNavHead(NavigationView navView) {
        Constants.showDialog();
        View Header = navView.getHeaderView(0);
        headerName = Header.findViewById(R.id.tv_nav_Name);
        headerEmail = Header.findViewById(R.id.tv_nav_yourEmail);
        headerImage = Header.findViewById(R.id.img_nav_logo);

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    Constants.dismissDialog();
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    Stash.put(Constants.STASH_USER, userModel);
                    headerName.setText(userModel.getName());
                    headerEmail.setText(userModel.getEmail());
                    Glide.with(MainActivity.this).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(headerImage);
                    String country = userModel.getCountry().replace(" ", "_");
                    Log.d("updateNavHead", "updateNavHead: " + country);

                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    Constants.dismissDialog();
                });

        ArrayList<CountriesRates> countriesRatesList = new ArrayList<>();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
        if (item.getItemId() == R.id.nav_chat) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();
        }
        if (item.getItemId() == R.id.nav_Support) {
            startActivity(Intent.createChooser(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:help@example.com")), ""));
        }
        if (item.getItemId() == R.id.nav_privacy) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com")));
        }
        if (item.getItemId() == R.id.nav_settings) {
            startActivity(new Intent(this, EditProfileActivity.class));
        }
        if (item.getItemId() == R.id.nav_logout) {
            new MaterialAlertDialogBuilder(this).setTitle("Logout").setMessage("Do you really want to logout?")
                    .setPositiveButton("Yes", ((dialog, which) -> {
                        Constants.auth().signOut();
                        startActivity(new Intent(this, SplashScreenActivity.class));
                        finish();
                    })).setNegativeButton("No", ((dialog, which) -> dialog.dismiss()))
                    .show();
        }
        binding.drawLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavHead(binding.navView);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
package com.moutamid.moneytransfer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moutamid.moneytransfer.MainActivity;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.databinding.ActivityEditProfileBinding;
import com.moutamid.moneytransfer.models.Rating;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    UserModel userModel;
    Uri imageURi;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.title.setText("Edit Profile");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        Constants.initDialog(this);

        binding.country.setCustomMasterCountries(Constants.CountriesCodes);
        binding.countryCodePick.setCustomMasterCountries(Constants.CountriesCodes);

        Constants.showDialog();
        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()){
                        userModel = dataSnapshot.getValue(UserModel.class);
                        binding.name.getEditText().setText(userModel.getName());
                        binding.email.getEditText().setText(userModel.getEmail());
                        Glide.with(this).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.imgNavLogo);
                        String num = userModel.getPhone().split("-")[1];
                        String code = userModel.getPhone().split("-")[0];
                        binding.number.setText(num);
                        binding.countryCodePick.setCountryForPhoneCode(Integer.parseInt(code.replace("+", "")));
                        binding.country.setCountryForNameCode(userModel.getCountryCode());
                    }
                    Constants.dismissDialog();
                }).addOnFailureListener(e->{
                    e.printStackTrace();
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        binding.imgNavLogo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE_REQUEST);
        });

        binding.update.setOnClickListener(v -> {
            if (imageURi != null){
                uploadImage();
            } else {
                updateData(userModel.getImage());
            }
        });

    }

    private void uploadImage() {
        Constants.showDialog();
        Constants.storageReference(Constants.auth().getCurrentUser().getUid())
                .child("logos").child(UUID.randomUUID().toString()).putFile(imageURi)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        updateData(uri.toString());
                    }).addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateData(String image) {
        UserModel model = new UserModel(
                Constants.auth().getCurrentUser().getUid(),
                binding.name.getEditText().getText().toString(),
                image,
                (binding.countryCodePick.getSelectedCountryCodeWithPlus() + "-" + binding.number.getText().toString()),
                userModel.getEmail(),
                userModel.getPassword(),
                getCountry(),
                binding.country.getSelectedCountryNameCode(),
                userModel.getRating()
        );
        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .setValue(model)
                .addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String getCountry() {
        return binding.country.getSelectedCountryEnglishName().equals("United Arab Emirates (UAE)") ? "United Arab Emirates" : binding.country.getSelectedCountryEnglishName();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageURi = data.getData();
            Glide.with(EditProfileActivity.this).load(imageURi).placeholder(R.drawable.profile_icon).into(binding.imgNavLogo);
        } else {
            Toast.makeText(this, "Failed to get the image", Toast.LENGTH_SHORT).show();
        }
    }

}
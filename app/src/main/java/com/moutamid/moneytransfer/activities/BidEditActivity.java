package com.moutamid.moneytransfer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.SplashScreenActivity;
import com.moutamid.moneytransfer.databinding.ActivityBidEditBinding;
import com.moutamid.moneytransfer.models.BidModel;
import com.moutamid.moneytransfer.models.CountriesRates;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.Date;
import java.util.IllegalFormatPrecisionException;
import java.util.UUID;

public class BidEditActivity extends AppCompatActivity {
    ActivityBidEditBinding binding;
    String ID;
    BidModel bidModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBidEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ID = getIntent().getStringExtra("ID");
        Constants.initDialog(this);

        binding.toolbar.title.setText("Edit Your Bid");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.popup.setVisibility(View.VISIBLE);
        binding.toolbar.secondIcon.setImageResource(R.drawable.round_delete_24);

        binding.toolbar.popup.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this).setTitle("Delete Bid").setMessage("Do you really want to delete this bid?")
                    .setPositiveButton("Yes", ((dialog, which) -> {
                        dialog.dismiss();
                        deleteBid();
                    })).setNegativeButton("No", ((dialog, which) -> dialog.dismiss()))
                    .show();
        });

        binding.country.setCustomMasterCountries(Constants.CountriesCodes);
        binding.countryTo.setCustomMasterCountries(Constants.CountriesCodes);

        getData();

        binding.bid.setOnClickListener(v -> {
            if (binding.price.getEditText().getText().toString().isEmpty()) {
                Toast.makeText(this, "Enter price in your currency", Toast.LENGTH_SHORT).show();
            } else {
                Constants.showDialog();
                Constants.databaseReference().child(Constants.Values).child(getCountry()).get().addOnSuccessListener(dataSnapshot1 -> {
                    CountriesRates countriesRates = dataSnapshot1.getValue(CountriesRates.class);
                    Stash.put(Constants.Values, countriesRates);
                    placeBid();
                }).addOnFailureListener(e -> e.printStackTrace());
            }
        });

        binding.country.setOnCountryChangeListener(() -> {
            Constants.showDialog();
            String name = getCountry().replace(" ", "_");
            Constants.databaseReference().child(Constants.Values).child(name).get().addOnSuccessListener(dataSnapshot1 -> {
                CountriesRates countriesRates = dataSnapshot1.getValue(CountriesRates.class);
                Stash.put("PLACEEEE", countriesRates);
                Constants.dismissDialog();
                double pr = Double.parseDouble(binding.price.getEditText().getText().toString()) * getCurrency();
                binding.bidAmount.getEditText().setText(String.format("%.2f", pr));
            }).addOnFailureListener(e -> {
                Constants.dismissDialog();
                Toast.makeText(BidEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        binding.countryTo.setOnCountryChangeListener(() -> {
            double pr = Double.parseDouble(binding.price.getEditText().getText().toString()) * getCurrency();
            binding.bidAmount.getEditText().setText(String.format("%.2f", pr));
        });

        binding.price.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!s.toString().isEmpty()) {
                        double pr = Double.parseDouble(s.toString()) * getCurrency();
                        binding.bidAmount.getEditText().setText(String.format("%.2f", pr));
                    }
                } catch (IllegalFormatPrecisionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void getData() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.BIDS).child(ID).get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                bidModel = dataSnapshot.getValue(BidModel.class);
                if (bidModel.isFaceToFace()) {
                    binding.faceToFace.setChecked(true);
                } else {
                    binding.transfer.setChecked(true);
                }
                binding.country.setCountryForNameCode(Constants.getNameCode(bidModel.getMyCountry()));
                binding.countryTo.setCountryForNameCode(Constants.getNameCode(bidModel.getBidCountry()));
                binding.bidAmount.getEditText().setText(bidModel.getPrice_ioc() + "");
                binding.price.getEditText().setText(bidModel.getPrice() + "");

            }
            Constants.dismissDialog();
        }).addOnFailureListener(e -> {
            Constants.dismissDialog();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void deleteBid() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.BIDS).child(bidModel.getID()).removeValue()
                .addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, "Bid Deleted", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                });
    }

    private void placeBid() {
        bidModel.setMyCountry(getCountry());
        bidModel.setBidCountry(getCountryTO());
        bidModel.setFaceToFace(binding.faceToFace.isChecked());
        bidModel.setTimestamp(new Date().getTime());
        bidModel.setPrice_ioc(Double.parseDouble(binding.bidAmount.getEditText().getText().toString()));
        bidModel.setPrice(Double.parseDouble(binding.price.getEditText().getText().toString()));

        Constants.databaseReference().child(Constants.BIDS).child(bidModel.getID())
                .setValue(bidModel).addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, "Your Bid is Updated", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private double getCurrency() {
        CountriesRates countriesRates = (CountriesRates) Stash.getObject("PLACEEEE", CountriesRates.class);
        if (countriesRates == null) {
            countriesRates = (CountriesRates) Stash.getObject(Constants.Values, CountriesRates.class);
        }
        if (getCountryTO().equals(Constants.EGYPT)) {
            return countriesRates.getRates().getEgypt();
        } else if (getCountryTO().equals(Constants.ITALY)) {
            return countriesRates.getRates().getItaly();
        } else if (getCountryTO().equals(Constants.United_Arab_Emirates)) {
            return countriesRates.getRates().getUAE();
        } else if (getCountryTO().equals(Constants.SAUDI_ARABIA)) {
            return countriesRates.getRates().getSaudi_Arabia();
        } else if (getCountryTO().equals(Constants.QATAR)) {
            return countriesRates.getRates().getQatar();
        } else if (getCountryTO().equals(Constants.MOROCCO)) {
            return countriesRates.getRates().getMorocco();
        } else if (getCountryTO().equals(Constants.SUDAN)) {
            return countriesRates.getRates().getSudan();
        } else if (getCountryTO().equals(Constants.OMAN)) {
            return countriesRates.getRates().getOman();
        } else if (getCountryTO().equals(Constants.RUSSIA)) {
            return countriesRates.getRates().getRussia();
        } else if (getCountryTO().equals(Constants.SYRIA)) {
            return countriesRates.getRates().getSyria();
        } else if (getCountryTO().equals(Constants.PALESTINE)) {
            return countriesRates.getRates().getPalestine();
        }
        return 0;
    }

    private String getCountryTO() {
        return binding.countryTo.getSelectedCountryEnglishName().equals("United Arab Emirates (UAE)") ? "United Arab Emirates" : binding.countryTo.getSelectedCountryEnglishName();
    }

    private String getCountry() {
        return binding.country.getSelectedCountryEnglishName().equals("United Arab Emirates (UAE)") ? "United Arab Emirates" : binding.country.getSelectedCountryEnglishName();
    }


}
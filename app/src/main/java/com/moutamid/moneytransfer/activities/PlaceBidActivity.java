package com.moutamid.moneytransfer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.hbb20.CountryCodePicker;
import com.moutamid.moneytransfer.databinding.ActivityPlaceBidBinding;
import com.moutamid.moneytransfer.models.BidModel;
import com.moutamid.moneytransfer.models.CountriesRates;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.text.Bidi;
import java.util.UUID;

public class PlaceBidActivity extends AppCompatActivity {
    ActivityPlaceBidBinding binding;
    UserModel stashUSer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceBidBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Place Your Bid");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        Constants.initDialog(this);
        stashUSer = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        binding.country.setCustomMasterCountries(Constants.CountriesCodes);
        binding.countryTo.setCustomMasterCountries(Constants.CountriesCodes);
        binding.country.setCountryForNameCode(stashUSer.getCountryCode());
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

        binding.country.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Constants.showDialog();
                String name = getCountry().replace(" ", "_");
                Constants.databaseReference().child(Constants.Values).child(name).get().addOnSuccessListener(dataSnapshot1 -> {
                    CountriesRates countriesRates = dataSnapshot1.getValue(CountriesRates.class);
                    Stash.put("PLACEEEE", countriesRates);
                    Constants.dismissDialog();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(PlaceBidActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
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
                if (!s.toString().isEmpty()) {
                    double pr = Double.parseDouble(s.toString()) * getCurrency();
                    binding.bidAmount.getEditText().setText(String.format("%.2f", pr));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void placeBid() {
        BidModel bidModel = new BidModel(UUID.randomUUID().toString(),
                stashUSer.getID(), stashUSer.getName(), stashUSer.getImage(), stashUSer.getRating(),
                Double.parseDouble(binding.price.getEditText().getText().toString()),
                Double.parseDouble(binding.bidAmount.getEditText().getText().toString()),
                getCountry(),
                getCountryTO(), binding.faceToFace.isChecked());
        Constants.databaseReference().child(Constants.BIDS).child(bidModel.getID())
                .setValue(bidModel).addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, "Your Bid is Placed", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Stash.clear("PLACEEEE");
    }

    private double getCurrency() {
        CountriesRates countriesRates = (CountriesRates) Stash.getObject("PLACEEEE", CountriesRates.class);
        if (countriesRates != null) {
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
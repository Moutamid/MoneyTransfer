package com.moutamid.moneytransfer.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.moneytransfer.MainActivity;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.activities.OthersBidActivity;
import com.moutamid.moneytransfer.activities.PlaceBidActivity;
import com.moutamid.moneytransfer.adapters.CurrencyAdapter;
import com.moutamid.moneytransfer.adapters.TransactionAdapter;
import com.moutamid.moneytransfer.databinding.FragmentHomeBinding;
import com.moutamid.moneytransfer.models.CountriesRates;
import com.moutamid.moneytransfer.models.Rating;
import com.moutamid.moneytransfer.models.TransactionModel;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.ArrayList;
import java.util.Comparator;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    UserModel userModel;
    ArrayList<TransactionModel> list;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        binding.placeBid.setOnClickListener(v -> startActivity(new Intent(requireContext(), PlaceBidActivity.class)));
        binding.othersBid.setOnClickListener(v -> startActivity(new Intent(requireContext(), OthersBidActivity.class)));

        list = new ArrayList<>();

        binding.transactionRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.transactionRC.setHasFixedSize(false);

        Constants.databaseReference().child(Constants.TRANSACTIONS).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                TransactionModel model = dataSnapshot.getValue(TransactionModel.class);
                                list.add(model);
                            }
                            list.sort(Comparator.comparing(TransactionModel::getTimestamp));
                            if (list.size() > 0) {
                                binding.transactionRC.setVisibility(View.VISIBLE);
                                binding.noLayout.setVisibility(View.GONE);
                            } else {
                                binding.transactionRC.setVisibility(View.GONE);
                                binding.noLayout.setVisibility(View.VISIBLE);
                            }
                            TransactionAdapter adapter = new TransactionAdapter(requireContext(), list);
                            binding.transactionRC.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.initDialog(requireContext());
        Constants.showDialog();
        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    userModel = dataSnapshot.getValue(UserModel.class);
                    updateUI();
                    Constants.dismissDialog();
                    String name = userModel.getCountry().replace(" ", "_");
                    Constants.databaseReference().child(Constants.Values).child(name).get().addOnSuccessListener(dataSnapshot1 -> {
                        CountriesRates countriesRates = dataSnapshot1.getValue(CountriesRates.class);
                        Stash.put(Constants.Values, countriesRates);
                        updateRecyler();
                    }).addOnFailureListener(e -> e.printStackTrace());

                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    Constants.dismissDialog();
                });
    }

    private void updateRecyler() {
        CountriesRates countriesRates = (CountriesRates) Stash.getObject(Constants.Values, CountriesRates.class);
        String name =  countriesRates.getName().equals("United_Arab_Emirates") ? "uae" : countriesRates.getName();
        ArrayList<String> rateList = getRateList(name, countriesRates.getRates());
        CurrencyAdapter adapter = new CurrencyAdapter(requireContext(), rateList);
        binding.currencyRC.setHasFixedSize(false);
        binding.currencyRC.setAdapter(adapter);
    }

    private ArrayList<String> getRateList(String name, CountriesRates.Rates rates) {
        ArrayList<String> ratesList = new ArrayList<>();
        if (rates != null) {
            java.lang.reflect.Field[] fields = CountriesRates.Rates.class.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                String fieldName = field.getName();
                String currency = "";
                Log.d("getRateList", "getRateList: " + fieldName);
                if (!fieldName.equalsIgnoreCase(name)) {
                    if (fieldName.equalsIgnoreCase("egypt")){
                        currency = Constants.getCurrencyCode("Egypt");
                    } else if (fieldName.equalsIgnoreCase("italy")){
                        currency = Constants.getCurrencyCode("Italy");
                    } else if (fieldName.equalsIgnoreCase("morocco")){
                        currency = Constants.getCurrencyCode("Morocco");
                    } else if (fieldName.equalsIgnoreCase("oman")){
                        currency = Constants.getCurrencyCode("Oman");
                    } else if (fieldName.equalsIgnoreCase("palestine")){
                        currency = Constants.getCurrencyCode("Palestine");
                    } else if (fieldName.equalsIgnoreCase("qatar")){
                        currency = Constants.getCurrencyCode("Qatar");
                    } else if (fieldName.equalsIgnoreCase("russia")){
                        currency = Constants.getCurrencyCode("Russia");
                    } else if (fieldName.equalsIgnoreCase("saudi_Arabia")){
                        currency = Constants.getCurrencyCode("Saudi Arabia");
                    } else if (fieldName.equalsIgnoreCase("sudan")){
                        currency = Constants.getCurrencyCode("Sudan");
                    } else if (fieldName.equalsIgnoreCase("syria")){
                        currency = Constants.getCurrencyCode("Syria");
                    } else if (fieldName.equalsIgnoreCase("uae")){
                        currency = Constants.getCurrencyCode("United Arab Emirates");
                    }
                    try {
                        field.setAccessible(true);
                        ratesList.add(currency + " " + field.get(rates));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {
                        field.setAccessible(false);
                    }
                }
            }
        }

        return ratesList;
    }

    private void updateUI() {
        binding.tvNavName.setText(userModel.getName());
        binding.currencyRate.setText("Currency Rate for " + userModel.getCountry());
        Glide.with(HomeFragment.this).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.imgNavLogo);
        Rating rating = userModel.getRating();
        int rr = rating.getStar1() + rating.getStar2() + rating.getStar3() + rating.getStar4() + rating.getStar5();
        double rate = rr / 5;
        if (rate > 0.0){
            binding.star1.setImageResource(R.drawable.round_star_24);
        } else if (rate >= 1.0){
            binding.star1.setImageResource(R.drawable.round_star_24);
            binding.star2.setImageResource(R.drawable.round_star_24);
        } else if (rate >= 2.0){
            binding.star1.setImageResource(R.drawable.round_star_24);
            binding.star2.setImageResource(R.drawable.round_star_24);
            binding.star3.setImageResource(R.drawable.round_star_24);
        } else if (rate >= 3.0){
            binding.star1.setImageResource(R.drawable.round_star_24);
            binding.star2.setImageResource(R.drawable.round_star_24);
            binding.star3.setImageResource(R.drawable.round_star_24);
            binding.star4.setImageResource(R.drawable.round_star_24);
        } else if (rate >= 4.0){
            binding.star1.setImageResource(R.drawable.round_star_24);
            binding.star2.setImageResource(R.drawable.round_star_24);
            binding.star3.setImageResource(R.drawable.round_star_24);
            binding.star4.setImageResource(R.drawable.round_star_24);
            binding.star5.setImageResource(R.drawable.round_star_24);
        }
    }
}
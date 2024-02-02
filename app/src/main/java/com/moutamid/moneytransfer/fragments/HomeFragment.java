package com.moutamid.moneytransfer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.activities.OthersBidActivity;
import com.moutamid.moneytransfer.activities.PlaceBidActivity;
import com.moutamid.moneytransfer.activities.SettingsActivity;
import com.moutamid.moneytransfer.adapters.CurrencyAdapter;
import com.moutamid.moneytransfer.adapters.TransactionAdapter;
import com.moutamid.moneytransfer.databinding.FragmentHomeBinding;
import com.moutamid.moneytransfer.models.CountriesRates;
import com.moutamid.moneytransfer.models.CurrenciesModel;
import com.moutamid.moneytransfer.models.TransactionModel;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.ArrayList;
import java.util.Calendar;
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
        binding.chats.setOnClickListener(v -> getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).addToBackStack("Chat").commit());
        binding.myBid.setOnClickListener(v -> getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyBidsFragment()).addToBackStack("BID").commit());
        binding.setting.setOnClickListener(v -> startActivity(new Intent(requireContext(), SettingsActivity.class)));

        list = new ArrayList<>();

        binding.transactionRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.transactionRC.setHasFixedSize(false);

//        PagerSnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(binding.currencyRC);

/*        binding.currencyRC.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.scrollBy((int) (dx * 1f), 0);
            }
        });*/

        Constants.databaseReference().child(Constants.TRANSACTIONS).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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
        Constants.setLocale(requireContext(), Stash.getString(Constants.LANGUAGE, "en"));
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
                    }).addOnFailureListener(Throwable::printStackTrace);
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    Constants.dismissDialog();
                });
    }

    private void updateRecyler() {
        CountriesRates countriesRates = (CountriesRates) Stash.getObject(Constants.Values, CountriesRates.class);
        String name = countriesRates.getName().equals("United_Arab_Emirates") ? "uae" : countriesRates.getName();
        ArrayList<CurrenciesModel> rateList = getRateList(name, countriesRates.getRates());
        CurrencyAdapter adapter = new CurrencyAdapter(requireContext(), rateList);
        binding.currencyRC.setHasFixedSize(false);
        binding.currencyRC.setAdapter(adapter);
    }

    private ArrayList<CurrenciesModel> getRateList(String name, CountriesRates.Rates rates) {
        ArrayList<CurrenciesModel> ratesList = new ArrayList<>();
        if (rates != null) {
            java.lang.reflect.Field[] fields = CountriesRates.Rates.class.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                String fieldName = field.getName();
                String currency = "";
                String country = "";
                String sign = "";
                int icon = 0;
                Log.d("getRateList", "getRateList: " + fieldName);
                if (!fieldName.equalsIgnoreCase(name)) {
                    if (fieldName.equalsIgnoreCase("egypt")) {
                        icon = R.drawable.eg;
                        country = "Egyptian Pound";
                        sign = "E£";
                        currency = Constants.getCurrencyCode("Egypt");
                    } else if (fieldName.equalsIgnoreCase("italy")) {
                        icon = R.drawable.it;
                        country = "Euro";
                        sign = "€";
                        currency = Constants.getCurrencyCode("Italy");
                    } else if (fieldName.equalsIgnoreCase("morocco")) {
                        icon = R.drawable.ma;
                        country = "Moroccan Dirham";
                        sign = "MAD";
                        currency = Constants.getCurrencyCode("Morocco");
                    } else if (fieldName.equalsIgnoreCase("oman")) {
                        icon = R.drawable.om;
                        country = "Omani Rial";
                        sign = "OMR";
                        currency = Constants.getCurrencyCode("Oman");
                    } else if (fieldName.equalsIgnoreCase("palestine")) {
                        icon = R.drawable.ps;
                        country = "Israeli New Shekel";
                        sign = "₪";
                        currency = Constants.getCurrencyCode("Palestine");
                    } else if (fieldName.equalsIgnoreCase("qatar")) {
                        icon = R.drawable.qa;
                        country = "Qatari Riyal";
                        sign = "QAR";
                        currency = Constants.getCurrencyCode("Qatar");
                    } else if (fieldName.equalsIgnoreCase("russia")) {
                        icon = R.drawable.ru;
                        country = "Russian Ruble";
                        sign = "RUB";
                        currency = Constants.getCurrencyCode("Russia");
                    } else if (fieldName.equalsIgnoreCase("saudi_Arabia")) {
                        icon = R.drawable.sa;
                        country = "Saudi Riyal";
                        sign = "SAR";
                        currency = Constants.getCurrencyCode("Saudi Arabia");
                    } else if (fieldName.equalsIgnoreCase("sudan")) {
                        icon = R.drawable.sd;
                        country = "Sudanese Pound";
                        sign = "SDG";
                        currency = Constants.getCurrencyCode("Sudan");
                    } else if (fieldName.equalsIgnoreCase("syria")) {
                        icon = R.drawable.sy;
                        country = "Syrian Pound";
                        sign = "SYP";
                        currency = Constants.getCurrencyCode("Syria");
                    } else if (fieldName.equalsIgnoreCase("uae")) {
                        icon = R.drawable.ae;
                        country = "Arab Emirates Dirham";
                        sign = "AED";
                        currency = Constants.getCurrencyCode("United Arab Emirates");
                    }
                    try {
                        field.setAccessible(true);
                        ratesList.add(new CurrenciesModel(currency, sign, "" + field.get(rates), country, icon));
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
        binding.wish.setText(getWish());
        binding.tvNavName.setText(userModel.getName());
        binding.currencyRate.setText(getString(R.string.currency_rates_for) + " " + userModel.getCountry());
        Glide.with(HomeFragment.this).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.imgNavLogo);
    }

    private String getWish() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay < 12) {
            return getString(R.string.good_morning);
        } else if (timeOfDay < 16) {
            return getString(R.string.good_afternoon);
        } else if (timeOfDay < 21) {
            return getString(R.string.good_evening);
        } else {
            return getString(R.string.good_night);
        }
    }
}
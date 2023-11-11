package com.moutamid.moneytransfer.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.moutamid.moneytransfer.MainActivity;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.activities.OthersBidActivity;
import com.moutamid.moneytransfer.activities.PlaceBidActivity;
import com.moutamid.moneytransfer.databinding.FragmentHomeBinding;
import com.moutamid.moneytransfer.models.Rating;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    UserModel userModel;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    userModel = dataSnapshot.getValue(UserModel.class);
                    updateUI();
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                });

        binding.placeBid.setOnClickListener(v -> startActivity(new Intent(requireContext(), PlaceBidActivity.class)));
        binding.othersBid.setOnClickListener(v -> startActivity(new Intent(requireContext(), OthersBidActivity.class)));

        return binding.getRoot();
    }

    private void updateUI() {
        binding.tvNavName.setText(userModel.getName());
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
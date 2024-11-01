package com.group1.scansaver.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.group1.scansaver.LoginActivity;
import com.group1.scansaver.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ///////////
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            final TextView textView = binding.textProfile;
            profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

            Button testButton = binding.testButton;
            testButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent testIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(testIntent);
                }
            });
        } else {
            // User is signed in, proceed with loading fragment data or functionality
            // You can set up your UI or load data for the signed-in user here
            binding.mainLayout.removeView(binding.testButton);
            final TextView profileTextView = binding.textProfile;
            profileTextView.setText("Welcome to your profile!");

        }
        ////////////

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
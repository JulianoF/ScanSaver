package com.group1.scansaver.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group1.scansaver.MapActivity;
import com.group1.scansaver.R;
import com.group1.scansaver.databinding.FragmentDashboardBinding;
import com.group1.scansaver.dataobjects.Item;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        populateItemCards(inflater);
        ///////

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void populateItemCards(@NonNull LayoutInflater inflater) {
        LinearLayout itemLayout = binding.scrollerInner;
        itemLayout.removeAllViews();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("items")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Item item = document.toObject(Item.class);

                            if (item != null) {
                                View itemCard = inflater.inflate(R.layout.item_card, itemLayout, false);

                                TextView itemName = itemCard.findViewById(R.id.itemName);
                                TextView itemLowestPrice = itemCard.findViewById(R.id.itemPrice);
                                TextView itemUPC = itemCard.findViewById(R.id.upcCode);
                                ImageView itemIcon = itemCard.findViewById(R.id.imageView);
                                Button itemMapButton = itemCard.findViewById(R.id.viewOnMap);
                                Button deleteItemButton = itemCard.findViewById(R.id.deleteButton);

                                itemName.setText(item.getNAME());
                                itemLowestPrice.setText("$" + item.getPRICE());
                                itemUPC.setText(item.getUPC());

                                itemMapButton.setOnClickListener(v -> {
                                    Intent intent = new Intent(getActivity(), MapActivity.class);// START MAP ACTIVITY SEND GEO DATA TO IT
                                    startActivity(intent);
                                });

                                itemLayout.addView(itemCard);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error getting items", e);
                    });
        } else {
            Log.w("Auth", "No user is currently signed in.");
        }
    }

}
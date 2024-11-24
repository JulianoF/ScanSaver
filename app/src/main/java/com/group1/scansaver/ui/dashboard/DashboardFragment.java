package com.group1.scansaver.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
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

import com.group1.scansaver.MapActivity;
import com.group1.scansaver.R;
import com.group1.scansaver.databinding.FragmentDashboardBinding;
import com.group1.scansaver.AddItemActivity;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private Button addItemButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        addItemButton = root.findViewById(R.id.addNewItemButton);
        addItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddItemActivity.class);
            startActivity(intent);
        });

        populateItemCards(inflater);
        ///////

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void populateItemCards(@NonNull LayoutInflater inflater){

        LinearLayout itemLayout = binding.scrollerInner;
        itemLayout.removeAllViews();

        for (int i = 0; i <= 5; i++){

            View itemCard = inflater.inflate(R.layout.item_card, itemLayout, false);

            TextView itemName = itemCard.findViewById(R.id.itemName);
            TextView itemLowestPrice = itemCard.findViewById(R.id.itemPrice);
            TextView itemUPC = itemCard.findViewById(R.id.upcCode);
            ImageView itemIcon = itemCard.findViewById(R.id.imageView);
            Button itemMapButton = itemCard.findViewById(R.id.viewOnMap);
            Button deleteItemButton = itemCard.findViewById(R.id.deleteButton);

            //TEST CODE WILL BE ADDED PROGRAMATICALLY
            itemName.setText("Apples");
            itemLowestPrice.setText("$1.99");
            itemUPC.setText("123456789123");


            itemMapButton.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            });

            deleteItemButton.setOnClickListener(v -> {
                itemLayout.removeView(itemCard);
            });

            itemLayout.addView(itemCard);
        }
    }
}
package com.group1.scansaver.ui.home;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.group1.scansaver.LoginActivity;
import com.group1.scansaver.databinding.FragmentHomeBinding;
import com.group1.scansaver.MapActivity;
import com.group1.scansaver.R;
import com.group1.scansaver.databasehelpers.ItemsDBHandlerLocal;

import com.group1.scansaver.dataobjects.Item;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView homeText = binding.textHome;

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            homeText.setText("Signed Out Home"); //WHAT YOU WANT USER TO SEE WHEN SIGNED OUT GOES HERE
        }else{
            homeText.setText("Favourite Items"); //WHAT YOU WANT USER TO SEE WHEN SIGNED IN GOES HERE
            populateItemCards(inflater);
        }


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

        ItemsDBHandlerLocal database = new ItemsDBHandlerLocal(getContext());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            List<Item> itemList = database.getAllFavorites();

            for (Item item : itemList) {
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
                    Intent intent = new Intent(getActivity(), MapActivity.class); // START MAP ACTIVITY SEND GEO DATA TO IT
                    startActivity(intent);
                });

                deleteItemButton.setOnClickListener(v -> {
                    database.removeFavorite(item.getUPC());
                    itemLayout.removeView(itemCard);
                });


                itemLayout.addView(itemCard);
            }
        }
    }
}

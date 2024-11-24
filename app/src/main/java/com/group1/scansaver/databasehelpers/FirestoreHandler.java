package com.group1.scansaver.databasehelpers;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group1.scansaver.SignUpActivity;
import com.group1.scansaver.dataobjects.Item;

public class FirestoreHandler {

    private FirebaseFirestore firestore;

    public FirestoreHandler(){
        firestore = FirebaseFirestore.getInstance();
    }

    public void insertItemIntoFirestore(Item item) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            firestore.collection("items")
                    .document(String.valueOf(item.getUPC()))
                    .set(item)
                    .addOnSuccessListener(aVoid -> {
                        Log.e("FIRE", "Storing Item Success");
                        firestore.collection("items")
                                .document(String.valueOf(item.getUPC()))
                                .update("userId", userId)
                                .addOnSuccessListener(aVoid1 -> {
                                    Log.e("FIRE", "UserId added to the item.");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FIRE", "Failed to add userId to the item", e);
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FIRE", "Storing Item FAILURE", e);
                    });
        } else {
            Log.w("FIRE", "No user is currently signed in.");
        }
    }

}

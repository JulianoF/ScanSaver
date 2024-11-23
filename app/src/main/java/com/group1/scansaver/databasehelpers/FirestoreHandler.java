package com.group1.scansaver.databasehelpers;

import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.group1.scansaver.SignUpActivity;
import com.group1.scansaver.dataobjects.Item;

public class FirestoreHandler {

    private FirebaseFirestore firestore;

    public FirestoreHandler(){
        firestore = FirebaseFirestore.getInstance();
    }

    public void insertItemIntoFirestore(int userID, Item item){
        firestore.collection("items").document(String.valueOf(userID))
                .set(item)
                .addOnSuccessListener(aVoid -> {
                    // ON SUCCESS
                })
                .addOnFailureListener(e -> {
                    // ON FAILURE
                });
    }
}

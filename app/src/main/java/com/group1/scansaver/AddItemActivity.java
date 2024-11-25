package com.group1.scansaver;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.group1.scansaver.dataobjects.Item;
import com.group1.scansaver.databasehelpers.FirestoreHandler;

public class AddItemActivity extends AppCompatActivity {

    private EditText inputName, inputUPC, inputPrice;
    private Button buttonBack, buttonSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_item_input);

        inputName = findViewById(R.id.inputName);
        inputUPC = findViewById(R.id.inputUPC);
        inputPrice = findViewById(R.id.inputPrice);
        buttonBack = findViewById(R.id.buttonBack);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonBack.setOnClickListener(v -> finish()); // Finishes the current activity and goes back

        buttonSubmit.setOnClickListener(v -> {
            String itemName = inputName.getText().toString().trim();
            String itemUPC = inputUPC.getText().toString().trim();
            String itemPriceStr = inputPrice.getText().toString().trim();

            if (itemName.isEmpty() || itemUPC.isEmpty() || itemPriceStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    saveItemToDatabase();
                    Toast.makeText(this, "Item Added: " + itemName, Toast.LENGTH_SHORT).show();
                    finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid price format.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveItemToDatabase() {
        Item savedItem = new Item(inputName.getText().toString(), inputUPC.getText().toString(), Double.parseDouble(inputPrice.getText().toString()));

        FirestoreHandler database = new FirestoreHandler();
        //UNCOMMENT THIS FOR ITEMS TO BE SAVED TO DB
        database.insertItemIntoFirestore(savedItem);
    }
}

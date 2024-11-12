package com.example.blogappll;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.blogappll.Database.ReclamationDatabase;
import com.example.blogappll.Entity.Reclamation;

public class Publish extends AppCompatActivity {

    private ReclamationDatabase db;
    private int reclamationId = -1;  // Used to identify if we're in add or update mode

    private static final int REQUEST_CODE_PERMISSIONS = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        db = Room.databaseBuilder(getApplicationContext(), ReclamationDatabase.class, "reclamation_database")
                .allowMainThreadQueries()  // For testing purposes only
                .build();

        EditText typeField = findViewById(R.id.b_type);
        EditText statusField = findViewById(R.id.b_status);
        EditText descField = findViewById(R.id.b_desc);

        // Check if a reclamation_id is passed for update
        Intent intent = getIntent();
        if (intent.hasExtra("reclamation_id")) {
            reclamationId = intent.getIntExtra("reclamation_id", -1);
            if (reclamationId != -1) {
                Log.d("PublishActivity", "reclamationId received: " + reclamationId);
                loadReclamationData(reclamationId, typeField, statusField, descField);
            } else {
                Log.e("PublishActivity", "Invalid reclamationId received");
            }
        } else {
            Log.e("PublishActivity", "No reclamation_id found in intent");
        }

        // Handle the publish button action
        Button btnPublish = findViewById(R.id.btn_pub);
        btnPublish.setText(reclamationId != -1 ? "UPDATE" : "PUBLISH");  // Change button text based on mode
        btnPublish.setOnClickListener(v -> {
            if (validateFields()) {  // Check form validity before publishing
                if (reclamationId != -1) {
                    updateReclamation(typeField.getText().toString(), statusField.getText().toString(), descField.getText().toString());
                } else {
                    insertReclamation(typeField.getText().toString(), statusField.getText().toString(), descField.getText().toString());
                }
                returnToHome();  // Return to home screen
            }
        });
    }

    private void loadReclamationData(int id, EditText typeField, EditText statusField, EditText descField) {
        Reclamation reclamation = db.reclamationDao().getReclamationById(id);
        if (reclamation != null) {
            typeField.setText(reclamation.type);
            statusField.setText(reclamation.status);
            descField.setText(reclamation.description);
        } else {
            Log.e("PublishActivity", "Failed to load Reclamation with id: " + id);
            Toast.makeText(this, "Failed to load reclamation data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertReclamation(String type, String status, String description) {
        Reclamation reclamation = new Reclamation();
        reclamation.type = type;
        reclamation.status = status;
        reclamation.description = description;
        db.reclamationDao().insert(reclamation);
        Toast.makeText(this, "Reclamation published successfully", Toast.LENGTH_SHORT).show();
    }

    private void updateReclamation(String type, String status, String description) {
        Reclamation reclamation = db.reclamationDao().getReclamationById(reclamationId);
        if (reclamation != null) {
            reclamation.type = type;
            reclamation.status = status;
            reclamation.description = description;
            db.reclamationDao().update(reclamation);
            Toast.makeText(this, "Reclamation updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateFields() {
        EditText typeField = findViewById(R.id.b_type);
        EditText statusField = findViewById(R.id.b_status);
        EditText descField = findViewById(R.id.b_desc);

        if (typeField.getText().toString().isEmpty()) {
            typeField.setError("Type is required");
            return false;
        }
        if (statusField.getText().toString().isEmpty()) {
            statusField.setError("Status is required");
            return false;
        }
        if (descField.getText().toString().isEmpty()) {
            descField.setError("Description is required");
            return false;
        }
        return true;
    }

    private void returnToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}

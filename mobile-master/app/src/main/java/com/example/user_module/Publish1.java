package com.example.user_module;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.user_module.Database.RateComDatabase;
import com.example.user_module.entity.RateCom;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Publish1 extends AppCompatActivity {

    private RateComDatabase db;
    private Uri imageUri;
    private ImageView imageView;
    private int id = -1;  // Utilisé pour identifier si on est en mode ajout ou mise à jour

    // Declare constants
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final int REQUEST_CODE_SELECT_IMAGE = 102;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PublishActivity", "onCreate called");  // Log to check if onCreate is called
        setContentView(R.layout.activity_publish1);

        db = Room.databaseBuilder(getApplicationContext(), RateComDatabase.class, "ratecom_database")
                .allowMainThreadQueries()  // Pour les tests uniquement
                .build();

        imageView = findViewById(R.id.image_tmb);
        EditText titleField = findViewById(R.id.b_title);
        EditText descField = findViewById(R.id.btn_desc);
        EditText authorField = findViewById(R.id.btn_auth);

        Intent intent = getIntent();
        Log.d("PublishActivity", "Received Intent: " + intent.toString());  // Log entire intent for debugging

        if (intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);
            Log.d("PublishActivity", "Received ratecomId: " + id);  // Log the id received
            loadRateComData(id);  // Charger les données du blog existant
        } else {
            Log.d("PublishActivity", "No id passed in the Intent");
        }

        // Gérer l'action du bouton publier
        Button btnPublish = findViewById(R.id.btn_pub);
        btnPublish.setText(id != -1 ? "UPDATE" : "PUBLISH");  // Changer le texte du bouton en fonction du mode
        Log.d("PublishActivity", "Button text set to: " + (id != -1 ? "UPDATE" : "PUBLISH") + ", ID: " + id);

        btnPublish.setOnClickListener(v -> {
            if (validateFields()) {  // Vérifier la validité du formulaire avant de publier
                if (id != -1) {
                    updateRateCom(titleField.getText().toString(), descField.getText().toString(), authorField.getText().toString());
                } else {
                    insertRateCom(titleField.getText().toString(), descField.getText().toString(), authorField.getText().toString());
                }
                returnToHome();  // Retourner à l'écran d'accueil
            }
        });
    }

    private void checkPermissionsAndSelectImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PERMISSIONS);
            } else {
                selectImage();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
            } else {
                selectImage();
            }
        } else {
            selectImage(); // Pas de permissions nécessaires
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            Toast.makeText(this, "Permission denied. Cannot select images.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri != null) {
                getContentResolver().takePersistableUriPermission(
                        imageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );
                try {
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void loadRateComData(int id) {
        RateCom rateCom = db.rateComDao().getRateComById(id);
        if (rateCom != null) {
            ((EditText) findViewById(R.id.b_title)).setText(rateCom.rate);
            ((EditText) findViewById(R.id.btn_desc)).setText(rateCom.commentaire);
            ((EditText) findViewById(R.id.btn_auth)).setText(rateCom.author);
            if (rateCom.imageUri != null) {
                imageUri = Uri.parse(rateCom.imageUri);
                imageView.setImageURI(imageUri);
                imageView.setVisibility(View.VISIBLE);
            }
        } else {
            Log.d("PublishActivity", "RateCom with ID: " + id + " not found.");
        }
    }

    private void insertRateCom(String rate, String commentaire, String author) {
        RateCom rateCom = new RateCom();
        rateCom.rate = rate;
        rateCom.commentaire = commentaire;
        rateCom.author = author;
        rateCom.imageUri = imageUri != null ? imageUri.toString() : null;
        db.rateComDao().insert(rateCom);
        Toast.makeText(this, "Rate published successfully", Toast.LENGTH_SHORT).show();
    }

    private void updateRateCom(String rate, String commentaire, String author) {
        RateCom rateCom = db.rateComDao().getRateComById(id);
        if (rateCom != null) {
            Log.d("PublishActivity", "Updating RateCom with ID: " + id);
            rateCom.rate = rate;
            rateCom.commentaire = commentaire;
            rateCom.author = author;
            if (imageUri != null) {
                rateCom.imageUri = imageUri.toString();
            }
            db.rateComDao().update(rateCom);
            Toast.makeText(this, "RateCom updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("PublishActivity", "RateCom with ID " + id + " not found.");
        }
    }

    private boolean validateFields() {
        EditText titleField = findViewById(R.id.b_title);
        EditText descField = findViewById(R.id.btn_desc);
        EditText authorField = findViewById(R.id.btn_auth);

        if (titleField.getText().toString().isEmpty()) {
            titleField.setError("Rate is required");
            return false;
        }
        if (descField.getText().toString().isEmpty()) {
            descField.setError("Commentaire is required");
            return false;
        }
        if (authorField.getText().toString().isEmpty()) {
            authorField.setError("Author is required");
            return false;
        }
        return true;
    }

    private void returnToHome() {
        Intent intent = new Intent(this, MainActivity1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
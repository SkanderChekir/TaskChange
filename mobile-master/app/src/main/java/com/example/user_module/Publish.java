package com.example.user_module;
import android.Manifest;
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

import com.example.user_module.Database.EvenementDatabase;
import com.example.user_module.entity.Evenement;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Publish extends AppCompatActivity {

    private EvenementDatabase db;
    private Uri imageUri;
    private ImageView imageView;
    private int EvenementId = -1;  // Utilisé pour identifier si on est en mode ajout ou mise à jour

    // Declare constants
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final int REQUEST_CODE_SELECT_IMAGE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        db = Room.databaseBuilder(getApplicationContext(), EvenementDatabase.class, "Evenement_database")
                .allowMainThreadQueries()  // Pour les tests uniquement
                .build();

        imageView = findViewById(R.id.image_tmb);
        EditText titleField = findViewById(R.id.b_title);
        EditText descField = findViewById(R.id.btn_desc);
        EditText lieuField = findViewById(R.id.btn_auth);

        // Vérifier si un Evenement_id est passé pour la mise à jour
        Intent intent = getIntent();
        if (intent.hasExtra("Evenement_id")) {
            EvenementId = intent.getIntExtra("Evenement_id", -1);
            if (EvenementId != -1) {
                Log.d("PublishActivity", "EvenementId received: " + EvenementId);
                loadEvenementData(EvenementId, titleField, descField, lieuField);
            } else {
                Log.e("PublishActivity", "Invalid EvenementId received");
            }
        } else {
            Log.e("PublishActivity", "No Evenement_id found in intent");
        }

        Button btnSelectImage = findViewById(R.id.b_selectimage);
        btnSelectImage.setOnClickListener(v -> checkPermissionsAndSelectImage());

        // Gérer l'action du bouton publier
        Button btnPublish = findViewById(R.id.btn_pub);
        btnPublish.setText(EvenementId != -1 ? "UPDATE" : "PUBLISH");  // Changer le texte du bouton en fonction du mode
        btnPublish.setOnClickListener(v -> {
            if (validateFields()) {  // Vérifier la validité du formulaire avant de publier
                if (EvenementId != -1) {
                    updateEvenement(titleField.getText().toString(), descField.getText().toString(), lieuField.getText().toString());
                } else {
                    insertEvenement(titleField.getText().toString(), descField.getText().toString(), lieuField.getText().toString());
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

    private void loadEvenementData(int id, EditText titleField, EditText descField, EditText lieuField) {
        Evenement evenement = db.EvenementDao().getEvenementById(id);
        if (evenement != null) {
            titleField.setText(evenement.title);
            descField.setText(evenement.description);
            lieuField.setText(evenement.lieu);
            if (evenement.imageUri != null) {
                imageUri = Uri.parse(evenement.imageUri);
                imageView.setImageURI(imageUri);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        } else {
            Log.e("PublishActivity", "Failed to load Evenement with id: " + id);
            Toast.makeText(this, "Failed to load event data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertEvenement(String title, String description, String lieu) {
        Evenement Evenement = new Evenement();
        Evenement.title = title;
        Evenement.description = description;
        Evenement.lieu = lieu;
        Evenement.imageUri = imageUri != null ? imageUri.toString() : null;
        db.EvenementDao().insert(Evenement);
        Toast.makeText(this, "Evenement published successfully", Toast.LENGTH_SHORT).show();
    }

    private void updateEvenement(String title, String description, String lieu) {
        Evenement Evenement = db.EvenementDao().getEvenementById(EvenementId);
        if (Evenement != null) {
            Evenement.title = title;
            Evenement.description = description;
            Evenement.lieu = lieu;
            if (imageUri != null) {
                Evenement.imageUri = imageUri.toString();
            }
            db.EvenementDao().update(Evenement);
            Toast.makeText(this, "Evenement updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateFields() {
        EditText titleField = findViewById(R.id.b_title);
        EditText descField = findViewById(R.id.btn_desc);
        EditText lieuField = findViewById(R.id.btn_auth);

        if (titleField.getText().toString().isEmpty()) {
            titleField.setError("Title is required");
            return false;
        }
        if (descField.getText().toString().isEmpty()) {
            descField.setError("Description is required");
            return false;
        }
        if (lieuField.getText().toString().isEmpty()) {
            lieuField.setError("lieu is required");
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

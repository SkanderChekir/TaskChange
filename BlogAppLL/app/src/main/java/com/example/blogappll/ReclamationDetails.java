package com.example.blogappll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ReclamationDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);

        // Références aux composants de l'interface utilisateur
        TextView description = findViewById(R.id.blog_details_description);
        TextView status = findViewById(R.id.blog_details_status);
        TextView type = findViewById(R.id.blog_details_type);

        // Référence à la flèche de retour
        ImageView backArrow = findViewById(R.id.back_arrow);

        // Gestion du clic pour revenir en arrière
        backArrow.setOnClickListener(v -> onBackPressed());

        // Récupération des données depuis l'intent
        Intent intent = getIntent();
        String reclamationDescription = intent.getStringExtra("description");
        String reclamationStatus = intent.getStringExtra("status");
        String reclamationType = intent.getStringExtra("type");

        // Assignation des données aux composants de l'interface
        description.setText(reclamationDescription != null ? reclamationDescription : "Aucune description");
        status.setText(reclamationStatus != null ? reclamationStatus : "Aucun statut");
        type.setText(reclamationType != null ? reclamationType : "Aucun type");
    }
}

package com.example.user_module;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EvenementDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);

        // Get references to the UI components
        TextView title = findViewById(R.id.blog_details_title);
        TextView description = findViewById(R.id.blog_details_description);
        TextView lieu = findViewById(R.id.blog_details_author);
        ImageView image = findViewById(R.id.blog_details_image);

        // Get reference to the back arrow ImageView
        ImageView backArrow = findViewById(R.id.back_arrow);  // Assuming you added the ImageView for the back arrow in XML

        // Set click listener for the back arrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();  // This will take the user back to the previous activity or fragment
            }
        });

        // Receive data from the intent
        Intent intent = getIntent();
        String EvenementTitle = intent.getStringExtra("title");
        String EvenementDescription = intent.getStringExtra("description");
        String Evenementlieu = intent.getStringExtra("lieu");
        String imageUri = intent.getStringExtra("imageUri");

        // Set the data in UI components
        title.setText(EvenementTitle != null ? EvenementTitle : "No Title");
        description.setText(EvenementDescription != null ? EvenementDescription : "No Description");
        lieu.setText(Evenementlieu != null ? Evenementlieu : "No lieu");

        // Display the image, if available
        if (imageUri != null && !imageUri.isEmpty()) {
            Uri uri = Uri.parse(imageUri);
            image.setImageURI(uri);  // Set the image URI to the ImageView
        } else {
            image.setImageResource(R.drawable.ic_launcher_background);  // Default image if no URI
        }
    }
}

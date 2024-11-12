package com.example.user_module;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RateComDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratecom_details);

        // Get references to the UI components
        TextView title = findViewById(R.id.blog_details_title);
        TextView description = findViewById(R.id.blog_details_description);
        TextView author = findViewById(R.id.blog_details_author);
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
        String blogTitle = intent.getStringExtra("rate");
        String blogDescription = intent.getStringExtra("commentaire");
        String blogAuthor = intent.getStringExtra("author");
        String imageUri = intent.getStringExtra("imageUri");

        // Set the data in UI components
        title.setText(blogTitle != null ? blogTitle : "No Rate");
        description.setText(blogDescription != null ? blogDescription : "No Commentaire");
        author.setText(blogAuthor != null ? blogAuthor : "No Author");

        // Display the image, if available
        if (imageUri != null && !imageUri.isEmpty()) {
            Uri uri = Uri.parse(imageUri);
            image.setImageURI(uri);  // Set the image URI to the ImageView
        } else {
            image.setImageResource(R.drawable.ic_launcher_background);  // Default image if no URI
        }
    }
}

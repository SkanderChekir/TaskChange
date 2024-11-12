package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.user_module.Home;
import com.example.user_module.EvenementDetails;
import com.example.user_module.MainActivity;
import com.example.user_module.MainActivity1;
import com.example.user_module.MainActivity2;
import com.example.user_module.MainActivity4;
import com.example.user_module.Publish;
import com.example.user_module.activity.ProfileActivity;
import com.example.user_module.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard); // The layout with the included navbar

        // Access the navbar elements
        ImageView backArrowButton = findViewById(R.id.backArrowButton);
        TextView navbarTitle = findViewById(R.id.navbarTitle);
        ImageView menuButton = findViewById(R.id.menuButton);

        // Set up the back button action
        backArrowButton.setOnClickListener(v -> finish()); // Navigates back to the previous screen

        // Customize the navbar title
        navbarTitle.setText("Dashboard"); // Customize the title for the specific page

        // Set up the menu button action
        menuButton.setOnClickListener(v -> showMenu()); // Opens the menu when clicked

    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.menuButton));
        popupMenu.getMenuInflater().inflate(R.menu.dashboard_menu, popupMenu.getMenu());

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_profile) {
                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                return true;
            } else if (item.getItemId() == R.id.menu_service) {
                Log.d("PopupMenu", "Service menu clicked");
                Toast.makeText(this, "Service clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                return true;
            }else if (item.getItemId() == R.id.menu_rateCom) {
                Log.d("PopupMenu", "RATE menu clicked");
                Toast.makeText(this, "Service clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DashboardActivity.this, MainActivity1.class));
                return true;
            }else if (item.getItemId() == R.id.menu_reclamation) {
                Log.d("PopupMenu", "reclamation menu clicked");
                Toast.makeText(this, "Service clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DashboardActivity.this, MainActivity2.class));
                return true;
            }else if (item.getItemId() == R.id.menu_card) {
                Log.d("PopupMenu", "card menu clicked");
                Toast.makeText(this, "Card clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DashboardActivity.this, MainActivity4.class));
                return true;
            }
            return false;
        });


        popupMenu.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_service) {
            // Navigate to Home activity
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

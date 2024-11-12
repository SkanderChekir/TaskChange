package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.user_module.AppDatabase;
import com.example.user_module.Dao.UserDao;
import com.example.user_module.R;
import com.example.user_module.entity.User;

import org.mindrot.jbcrypt.BCrypt;

import java.util.concurrent.Executors;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText newPasswordEditText;
    private Button resetPasswordButton;
    private String resetToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPasswordEditText = findViewById(R.id.newPasswordEditText);

        resetPasswordButton = findViewById(R.id.confirmResetButton);

        // Retrieve reset token from intent
        resetToken = getIntent().getStringExtra("resetToken");

        resetPasswordButton.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String newPassword = newPasswordEditText.getText().toString().trim();

        if (newPassword.isEmpty() ) {
            Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
            return;
        }



        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user_database").build();

        Executors.newSingleThreadExecutor().execute(() -> {
            UserDao userDao = db.userDao();
            User user = userDao.getUserByResetToken(resetToken);

            if (user != null) {
                String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                user.setPassword(hashedPassword);
                user.setResetToken(null);  // Clear reset token
                userDao.update(user);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Password reset successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to LoginActivity
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Invalid reset token", Toast.LENGTH_SHORT).show());
            }
        });
    }
}

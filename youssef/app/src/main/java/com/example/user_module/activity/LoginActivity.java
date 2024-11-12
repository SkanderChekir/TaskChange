package com.example.user_module.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.user_module.AppDatabase;
import com.example.user_module.Dao.UserDao;
import com.example.user_module.R;
import com.example.user_module.entity.User;
import com.example.user_module.util.SessionManager;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpTextView, forgotPasswordTextView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpTextView = findViewById(R.id.signUpTextView);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);

        // Initialize SessionManager
        sessionManager = new SessionManager(getApplicationContext());

        signUpTextView.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> loginUser());
        forgotPasswordTextView.setOnClickListener(view -> initiatePasswordReset());
    }

    private void loginUser() {
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user_database").build();

        new Thread(() -> {
            UserDao userDao = db.userDao();
            User user = userDao.getUserByEmail(email);

            if (user != null && BCrypt.checkpw(password, user.password)) {
                // Create a login session
                sessionManager.createLoginSession(String.valueOf(user.id));

                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void initiatePasswordReset() {
        final String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required for password reset");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            emailEditText.requestFocus();
            return;
        }

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user_database").build();

        new Thread(() -> {
            UserDao userDao = db.userDao();
            User user = userDao.getUserByEmail(email);

            if (user != null) {
                String resetToken = UUID.randomUUID().toString();
                user.setResetToken(resetToken);
                userDao.update(user);

                sendResetEmail(email, resetToken);

                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Reset email sent. Check your email for instructions.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("resetToken", resetToken);
                    startActivity(intent);
                });
            } else {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "No account associated with this email", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void sendResetEmail(String recipient, String resetToken) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("salimaloui406@gmail.com", "zuxz eqce ipkj goed");
                }
            });

            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("salimaloui406@gmail.com"));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                message.setSubject("Password Reset Request");
                message.setText("Your password reset token is:\n\n" + resetToken + "\n\n" +
                        "Please use this token in the app to reset your password.");

                Transport.send(message);

                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(LoginActivity.this, "Email sent successfully to: " + recipient, Toast.LENGTH_SHORT).show()
                );

            } catch (MessagingException e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(LoginActivity.this, "Failed to send email. Check logs for details.", Toast.LENGTH_SHORT).show()
                );
            } finally {
                executor.shutdown();
            }
        });
    }
}

package com.fsc.cicerone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.mailer.Mailer;
import com.fsc.cicerone.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        TextInputEditText emailText = findViewById(R.id.emailTextForgotPassword);
        TextInputLayout emailContainer = findViewById(R.id.emailForgotPassword);
        Button send = findViewById(R.id.resetForgotPassword);

        send.setOnClickListener(view -> {
            String email = emailText.getText() != null ? emailText.getText().toString().trim() : "";
            if(User.validateEmail(email)){
                emailContainer.setError(null);
                Log.e("PRE EMAIL", email);
                Mailer.sendPasswordResetLink(this, email, v -> Toast.makeText(this, getString(R.string.email_sent), Toast.LENGTH_SHORT).show());
                startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                finish();
            }else{
                emailContainer.setError(getString(R.string.email_not_valid));
            }
        });
    }
}

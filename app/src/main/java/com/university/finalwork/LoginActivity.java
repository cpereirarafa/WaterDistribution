package com.university.finalwork;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setFirebase();
        setUi();
    }

    private void setFirebase() {

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        userRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    private void setUi() {

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        loadingProgressBar = findViewById(R.id.loading);

        usernameEditText.setOnFocusChangeListener(onFocusUser());
        passwordEditText.setOnFocusChangeListener(onFocusPassword());
        loginButton.setOnClickListener(onClickLogin());
        registerButton.setOnClickListener(onClickRegister());
    }

    private View.OnFocusChangeListener onFocusPassword() {
        return (v, hasFocus) -> {

            if(!hasFocus && passwordEditText.getText().toString().length() < 6)
                passwordEditText.setError(getString(R.string.invalid_password));
            else if(passwordEditText.getText().toString().length() >= 6 && checkMail())
                loginButton.setEnabled(true);
            else
                loginButton.setEnabled(false);
        };
    }

    private View.OnFocusChangeListener onFocusUser() {
        return (v, hasFocus) -> {
            if(!hasFocus && !checkMail())
                 usernameEditText.setError(getString(R.string.invalid_username));
            else if(passwordEditText.getText().toString().length() >= 6 && checkMail())
                loginButton.setEnabled(true);
            else
                loginButton.setEnabled(false);
        };
    }

    private boolean checkMail() {

        return usernameEditText.getText().toString().matches("^(.+)@(.+)\\.(.+)$");
    }

    private View.OnClickListener onClickLogin() {
        return v -> {

            loadingProgressBar.setVisibility(View.VISIBLE);
            String mail = usernameEditText.getText().toString();
            String passwd = passwordEditText.getText().toString();

            mAuth.signInWithEmailAndPassword(mail, passwd)
                    .addOnCompleteListener(this, task -> {
                        loadingProgressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startMatchingHome(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            showLoginFailed( getString(R.string.login_failed) + " " + task.getException().getMessage());
                        }
                    });
        };
    }

    private View.OnClickListener onClickRegister() {
        return v -> {

            Intent intent;
            intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        };
    }

    private void startMatchingHome(FirebaseUser user) {

        userRef.child(user.getUid()).child("admin").addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Intent intent;
                boolean isAdmin;
                isAdmin = (boolean) snapshot.getValue();

                if(isAdmin)
                    intent = new Intent(LoginActivity.this, HomeActivityManager.class);
                else
                    intent = new Intent(LoginActivity.this, HomeActivityUser.class);

                intent.putExtra("User", user);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser !=  null)
            startMatchingHome(currentUser);
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
    }
}
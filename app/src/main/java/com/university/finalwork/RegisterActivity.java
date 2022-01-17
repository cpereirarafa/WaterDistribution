package com.university.finalwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.university.finalwork.database.Users;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText addressEditText;
    private EditText cityEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private Button registerButton;
    private ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setFirebase();
        setUi();
    }

    private void setFirebase() {

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.keepSynced(false);
    }

    private void setUi() {

        nameEditText = findViewById(R.id.name);
        addressEditText = findViewById(R.id.address);
        cityEditText = findViewById(R.id.city);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        passwordConfirmEditText = findViewById(R.id.passwordConfirm);
        registerButton = findViewById(R.id.register);
        loadingProgressBar = findViewById(R.id.loading);

        nameEditText.setOnFocusChangeListener(onFocusName());
        addressEditText.setOnFocusChangeListener(onFocusAddress());
        cityEditText.setOnFocusChangeListener(onFocusCity());
        usernameEditText.setOnFocusChangeListener(onFocusUser());
        passwordEditText.setOnFocusChangeListener(onFocusPassword());
        passwordConfirmEditText.setOnFocusChangeListener(onFocusPasswordConfirm());
        registerButton.setOnClickListener(onClickRegister());
    }

    private View.OnFocusChangeListener onFocusName() {
        return (v, hasFocus) -> {
            if(!hasFocus && !checkName())
                nameEditText.setError(getString(R.string.invalid_name));
            else
                verifyFields();
        };
    }

    private View.OnFocusChangeListener onFocusAddress() {
        return (v, hasFocus) -> {
            if(!hasFocus && addressEditText.getText().toString().trim().isEmpty())
                addressEditText.setError(getString(R.string.invalid_address));
            else
                verifyFields();
        };
    }

    private View.OnFocusChangeListener onFocusCity() {
        return (v, hasFocus) -> {
            if(!hasFocus && cityEditText.getText().toString().trim().isEmpty())
                cityEditText.setError(getString(R.string.invalid_city));
            else
                verifyFields();
        };
    }

    private View.OnFocusChangeListener onFocusUser() {
        return (v, hasFocus) -> {
            if(!hasFocus && !checkMail())
                usernameEditText.setError(getString(R.string.invalid_username));
            else
                verifyFields();
        };
    }

    private View.OnFocusChangeListener onFocusPassword() {
        return (v, hasFocus) -> {

            if(!hasFocus && passwordEditText.getText().toString().trim().length() < 6)
                passwordEditText.setError(getString(R.string.invalid_password));
            else
                verifyFields();
        };
    }

    private View.OnFocusChangeListener onFocusPasswordConfirm() {
        return (v, hasFocus) -> {

            if(!hasFocus && (passwordConfirmEditText.getText().toString().trim().length() < 6 || !passwordConfirmEditText.getText().toString().equals(passwordEditText.getText().toString())))
                passwordConfirmEditText.setError(getString(R.string.invalid_password));
            else
                verifyFields();
        };
    }

    private boolean checkName() {

        return nameEditText.getText().toString().trim().matches("^(.+) (.+)$");
    }

    private boolean checkMail() {

        return usernameEditText.getText().toString().trim().matches("^(.+)@(.+)\\.(.+)$");
    }

    private void verifyFields(){

        if(checkName()
                && !addressEditText.getText().toString().trim().isEmpty()
                && !cityEditText.getText().toString().trim().isEmpty()
                && checkMail()
                && passwordEditText.getText().toString().length() >= 6
                && passwordConfirmEditText.getText().toString().equals(passwordEditText.getText().toString()))
            registerButton.setEnabled(true);
        else
            registerButton.setEnabled(false);
    }

    private View.OnClickListener onClickRegister() {
        return v -> {

            loadingProgressBar.setVisibility(View.VISIBLE);
            String mail = usernameEditText.getText().toString();
            String passwd = passwordEditText.getText().toString();

            mAuth.createUserWithEmailAndPassword(mail, passwd)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            saveUser();
                        } else {
                            loadingProgressBar.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            showRegistrationFailed( getString(R.string.registration_failed) + " " + task.getException().getMessage());
                        }
                    });
        };
    }

    private void saveUser() {

        // Sign in success, update UI with the signed-in user's information
        //Log.d(TAG, "createUserWithEmail:success");
        FirebaseUser currUser = mAuth.getCurrentUser();

        String usrId = currUser.getUid();
        Users user = new Users(
                usrId,
                false,
                nameEditText.getText().toString().trim(),
                addressEditText.getText().toString().trim(),
                cityEditText.getText().toString().trim(),
                usernameEditText.getText().toString().trim());
        userRef.child(usrId).setValue(user).addOnSuccessListener(aVoid -> {

            loadingProgressBar.setVisibility(View.GONE);

            Intent intent;
            intent = new Intent(RegisterActivity.this, HomeActivityUser.class);
            intent.putExtra("User", currUser);
            startActivity(intent);
        })
                .addOnFailureListener(e -> {

                    loadingProgressBar.setVisibility(View.GONE);

                    showRegistrationFailed( getString(R.string.registration_failed) + " " + e.getMessage());
                });
    }

    private void showRegistrationFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
    }
}
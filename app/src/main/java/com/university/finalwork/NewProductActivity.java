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
import com.university.finalwork.database.Products;
import com.university.finalwork.database.Users;

public class NewProductActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText sizeEditText;
    private EditText priceEditText;
    private EditText stockEditText;
    private Button createButton;
    private ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        setFirebase();
        setUi();
    }

    private void setFirebase() {

        mAuth = FirebaseAuth.getInstance();
        productRef = FirebaseDatabase.getInstance().getReference("Products");
        productRef.keepSynced(true);
    }

    private void setUi() {

        nameEditText = findViewById(R.id.name);
        descriptionEditText = findViewById(R.id.description);
        sizeEditText = findViewById(R.id.size);
        priceEditText = findViewById(R.id.price);
        stockEditText = findViewById(R.id.stock);
        createButton = findViewById(R.id.create);
        loadingProgressBar = findViewById(R.id.loading);

        nameEditText.setOnFocusChangeListener(onFocusName());
        descriptionEditText.setOnFocusChangeListener(onFocusDescription());
        sizeEditText.setOnFocusChangeListener(onFocusSize());
        priceEditText.setOnFocusChangeListener(onFocusPrice());
        stockEditText.setOnFocusChangeListener(onFocusStock());
        createButton.setOnClickListener(onClickCreate());
    }

    private View.OnFocusChangeListener onFocusName() {
        return (v, hasFocus) -> {
            if(!hasFocus && (nameEditText.getText().toString().isEmpty() || nameEditText.getText().toString().trim().isEmpty()))
                nameEditText.setError(getString(R.string.invalid_prod_name));
            else
                verifyFields();
        };
    }

    private View.OnFocusChangeListener onFocusDescription() {
        return (v, hasFocus) -> {
            if(!hasFocus && (descriptionEditText.getText().toString().isEmpty() || descriptionEditText.getText().toString().trim().isEmpty()))
                descriptionEditText.setError(getString(R.string.invalid_prod_description));
            else
                verifyFields();
        };
    }

    private View.OnFocusChangeListener onFocusSize() {
        return (v, hasFocus) -> {
            if(!hasFocus && (sizeEditText.getText().toString().isEmpty() || sizeEditText.getText().toString().trim().isEmpty()))
                sizeEditText.setError(getString(R.string.invalid_prod_size));
            else
                verifyFields();
        };
    }

    private View.OnFocusChangeListener onFocusPrice() {
        return (v, hasFocus) -> {
            if(!hasFocus && (priceEditText.getText().toString().isEmpty() || Double.parseDouble(priceEditText.getText().toString().trim()) <= 0D))
                priceEditText.setError(getString(R.string.invalid_prod_price));
            else
                verifyFields();
        };
    }

    private View.OnFocusChangeListener onFocusStock() {
        return (v, hasFocus) -> {

            if(!hasFocus && (stockEditText.getText().toString().isEmpty() || Long.parseLong(stockEditText.getText().toString().trim()) <= 0L))
                stockEditText.setError(getString(R.string.invalid_prod_stock));
            else
                verifyFields();
        };
    }

    private void verifyFields(){

        if( !nameEditText.getText().toString().isEmpty() && !nameEditText.getText().toString().trim().isEmpty()
                && !descriptionEditText.getText().toString().isEmpty() && !descriptionEditText.getText().toString().trim().isEmpty()
                && !sizeEditText.getText().toString().isEmpty() && !sizeEditText.getText().toString().trim().isEmpty()
                && !priceEditText.getText().toString().isEmpty() && Double.parseDouble(priceEditText.getText().toString().trim()) > 0D
                && !stockEditText.getText().toString().isEmpty() && Long.parseLong(stockEditText.getText().toString().trim()) > 0L)
            createButton.setEnabled(true);
        else
            createButton.setEnabled(false);
    }

    private View.OnClickListener onClickCreate() {
        return v -> {

            loadingProgressBar.setVisibility(View.VISIBLE);
            saveProduct();
        };
    }

    private void saveProduct() {

        String productId = productRef.push().getKey();
        Products product = new Products(
                productId,
                nameEditText.getText().toString().trim(),
                descriptionEditText.getText().toString().trim(),
                sizeEditText.getText().toString().trim(),
                Double.parseDouble(priceEditText.getText().toString().trim()),
                Long.parseLong(stockEditText.getText().toString().trim()));
        productRef.child(productId).setValue(product).addOnSuccessListener(aVoid -> {

            loadingProgressBar.setVisibility(View.GONE);
            showSuccess(getString(R.string.new_product_success));
            finish();
        })
                .addOnFailureListener(e -> {

                    loadingProgressBar.setVisibility(View.GONE);
                    showError(getString(R.string.new_product_failed));
                });
    }

    private void showSuccess(String sucessString) {
        Toast.makeText(getApplicationContext(), sucessString, Toast.LENGTH_SHORT).show();
    }

    private void showError(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
    }
}
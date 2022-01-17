package com.university.finalwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.university.finalwork.database.Orders;
import com.university.finalwork.database.Products;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivityUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseListAdapter listAdapter;
    private DatabaseReference productRef;
    private DatabaseReference orderRef;
    private ListView prodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        Toolbar toolbar = findViewById(R.id.toolbar);

        user = (FirebaseUser) getIntent().getParcelableExtra("User");

        toolbar.setSubtitle(user.getEmail());
        setSupportActionBar(toolbar);

        setFirebase();
        setUi();
    }

    @Override
    protected void onStart() {
        super.onStart();

        listAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        listAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();

        if (i == R.id.menu_login_logout) {

            mAuth.signOut();
            HomeActivityUser.this.finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void setFirebase() {

        mAuth = FirebaseAuth.getInstance();
        productRef = FirebaseDatabase.getInstance().getReference("Products");
        orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        FirebaseListOptions<Products> products = new FirebaseListOptions.Builder<Products>()
                .setLayout(R.layout.product_item)
                .setLifecycleOwner(HomeActivityUser.this)
                .setQuery(productRef, Products.class)
                .build();

        listAdapter = new FirebaseListAdapter(products) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {

                TextView productName = (TextView) v.findViewById(R.id.product_name);
                TextView productDescription = (TextView) v.findViewById(R.id.product_description);
                TextView productSize = (TextView) v.findViewById(R.id.product_size);
                TextView productPrice = (TextView) v.findViewById(R.id.product_price);
                TextView productStock = (TextView) v.findViewById(R.id.product_stock);
                EditText productQuantity = (EditText) v.findViewById(R.id.product_order_qtty);
                Button productOrder = (Button) v.findViewById(R.id.product_order);

                Products product = (Products) model;
                productName.setText(product.getProductName());
                productDescription.setText(product.getProductDescription());
                productSize.setText(product.getProductSize());
                productPrice.setText(String.valueOf(product.getProductPrice()));
                productStock.setText(String.valueOf(product.getProductStock()));
                productOrder.setOnClickListener(v1 -> {

                    if(Long.valueOf(productQuantity.getText().toString()) <= product.getProductStock() && Long.valueOf(productQuantity.getText().toString()) > 0)
                        orderProduct(product.getProductId(), product.getProductStock(), Long.valueOf(productQuantity.getText().toString()));
                    else
                        showError(getString(R.string.new_order_invalid_quantity));
                });

            }
        };
    }

    private void orderProduct(String productId, long olderStock, long quantity) {

        String orderId = orderRef.push().getKey();
        Orders order = new Orders(
                orderId,
                mAuth.getUid(),
                productId,
                quantity,
                false);
        orderRef.child(orderId).setValue(order).addOnSuccessListener(aVoid -> {

            productRef.child(productId).child("productStock").setValue(olderStock - quantity);
            showSuccess(getString(R.string.new_order_success));
        })
                .addOnFailureListener(e -> {

                    showError(getString(R.string.new_order_failed));
                });
    }

    private void setUi() {

        prodList = (ListView) findViewById(R.id.productList);
        prodList.setAdapter(listAdapter);
    }


    private void showSuccess(String sucessString) {
        Toast.makeText(getApplicationContext(), sucessString, Toast.LENGTH_SHORT).show();
    }

    private void showError(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
    }
}
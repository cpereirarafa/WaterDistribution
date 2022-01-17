package com.university.finalwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivityManager extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button newProductButton;
    private Button ordersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);

        user = (FirebaseUser) getIntent().getParcelableExtra("User");
        mAuth = FirebaseAuth.getInstance();

        toolbar.setSubtitle(user.getEmail());
        setSupportActionBar(toolbar);
        setUi();
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
            HomeActivityManager.this.finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void setUi() {

        newProductButton = findViewById(R.id.newProductBtn);
        ordersButton = findViewById(R.id.orderListBtn);

        newProductButton.setOnClickListener(onClickNewProduct());
        ordersButton.setOnClickListener(onClickOrders());
    }

    private View.OnClickListener onClickNewProduct() {
        return v -> {

            Intent intent;
            intent = new Intent(HomeActivityManager.this, NewProductActivity.class);
            startActivity(intent);
        };
    }

    private View.OnClickListener onClickOrders() {
        return v -> {

            Fragment fragment = new OrdersFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.home_manager, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
        };
    }
}
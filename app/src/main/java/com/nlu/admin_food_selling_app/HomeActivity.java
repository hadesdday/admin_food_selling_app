package com.nlu.admin_food_selling_app;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.nlu.admin_food_selling_app.ui.view.account.fragment.MyAccountFragment;
import com.nlu.admin_food_selling_app.ui.view.customer.fragment.CustomerFragment;
import com.nlu.admin_food_selling_app.ui.view.food.fragment.FoodFragment;
import com.nlu.admin_food_selling_app.ui.view.order.fragment.OrderFragment;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_ORDER = 1;
    private static final int FRAGMENT_CUSTOMER = 2;
    private static final int FRAGMENT_FOOD = 3;
    private static final int FRAGMENT_SALE = 4;
    private static final int FRAGMENT_VOUCHER = 5;
    private static final int FRAGMENT_PROFILE = 6;

    private int mCurrentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view_area);
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new StatisticFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (mCurrentFragment != FRAGMENT_HOME) {
                replaceFragment(new StatisticFragment());
                mCurrentFragment = FRAGMENT_HOME;
            }
        } else if (id == R.id.nav_order) {
            if (mCurrentFragment != FRAGMENT_ORDER) {
                replaceFragment(new OrderFragment());
                mCurrentFragment = FRAGMENT_ORDER;
            }
        } else if (id == R.id.nav_customer) {
            if (mCurrentFragment != FRAGMENT_CUSTOMER) {
                replaceFragment(new CustomerFragment());
                mCurrentFragment = FRAGMENT_CUSTOMER;
            }
        } else if (id == R.id.nav_food) {
            if (mCurrentFragment != FRAGMENT_FOOD) {
                replaceFragment(new FoodFragment());
                mCurrentFragment = FRAGMENT_FOOD;
            }
        } else if (id == R.id.nav_sale) {
            if (mCurrentFragment != FRAGMENT_SALE) {
                replaceFragment(new SaleFragment());
                mCurrentFragment = FRAGMENT_SALE;
            }
        } else if (id == R.id.nav_voucher) {
            if (mCurrentFragment != FRAGMENT_VOUCHER) {
                replaceFragment(new VoucherFragment());
                mCurrentFragment = FRAGMENT_VOUCHER;
            }
        } else if (id == R.id.nav_profile) {
            if (mCurrentFragment != FRAGMENT_PROFILE) {
                replaceFragment(new MyAccountFragment());
                mCurrentFragment = FRAGMENT_PROFILE;
            }
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content, fragment);
        transaction.commit();
    }
}
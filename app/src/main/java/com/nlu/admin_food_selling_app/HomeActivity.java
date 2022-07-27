package com.nlu.admin_food_selling_app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setFragment(StatisticFragment.class);

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_nav:
                    setFragment(StatisticFragment.class);
                    break;
                case R.id.order_management:
                    setFragment(OrderFragment.class);
                    break;
                case R.id.customer_management:
                    setFragment(CustomerFragment.class);
                    break;
                case R.id.food_management:
                    setFragment(FoodFragment.class);
                    break;
                case R.id.my_profile:
                    setFragment(MyAccountFragment.class);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    public boolean destroyFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public void setFragment(Class fragment) {
        try {
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            if (fragmentList.size() > 0) {
                destroyFragment(fragmentList.get(0));
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                .add(R.id.fragment_container_view, fragment, null)
                .commit();
    }
}
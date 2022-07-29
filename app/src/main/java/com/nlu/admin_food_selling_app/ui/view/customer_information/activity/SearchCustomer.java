package com.nlu.admin_food_selling_app.ui.view.customer_information.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.R;

public class SearchCustomer extends AppCompatActivity {
    ImageButton finishSearchCustomer;
    TextView applySearchCustomer;
    EditText sCustomerSearchKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);

        finishSearchCustomer = findViewById(R.id.finishSearchCustomer);
        applySearchCustomer = findViewById(R.id.applySearchCustomer);
        sCustomerSearchKeyword = findViewById(R.id.sCustomerSearchKeyword);

        finishSearchCustomer.setOnClickListener(view -> {
            finish();
        });

        applySearchCustomer.setOnClickListener(view -> {
            Intent intent = new Intent(SearchCustomer.this, SearchCustomerResult.class);
            String key = String.valueOf(sCustomerSearchKeyword.getText());

            if (key.isEmpty()) {
                sCustomerSearchKeyword.setError("Vui lòng nhập mã khách hàng");
                sCustomerSearchKeyword.requestFocus();
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("keyword", key);
                intent.putExtra("searchCustomerBundles", bundle);
                startActivity(intent);
                this.finish();
            }
        });
    }
}
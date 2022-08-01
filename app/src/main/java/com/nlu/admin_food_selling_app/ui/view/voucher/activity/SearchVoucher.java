package com.nlu.admin_food_selling_app.ui.view.voucher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.R;


public class SearchVoucher extends AppCompatActivity {
    ImageButton finishSearchVoucher;
    TextView applySearchVoucher;
    EditText vSearchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_voucher);

        finishSearchVoucher = findViewById(R.id.finishSearchVoucher);
        applySearchVoucher = findViewById(R.id.applySearchVoucher);
        vSearchKey = findViewById(R.id.vSearchKey);

        finishSearchVoucher.setOnClickListener(view -> {
            finish();
        });

        applySearchVoucher.setOnClickListener(view -> {
            Intent intent = new Intent(SearchVoucher.this, SearchVoucherResult.class);

            String id = String.valueOf(vSearchKey.getText());
            if (id.isEmpty()) {
                vSearchKey.setError("Vui lòng nhập mã voucher");
                vSearchKey.requestFocus();
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtra("searchBundles", bundle);
                startActivity(intent);
                SearchVoucher.this.finish();
            }
        });
    }
}
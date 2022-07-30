package com.nlu.admin_food_selling_app.ui.view.sale.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Sale;
import com.nlu.admin_food_selling_app.data.repository.SaleRepository;
import com.nlu.admin_food_selling_app.ui.view.customer_information.activity.AddNewCustomerActivity;
import com.nlu.admin_food_selling_app.ui.view.customer_information.activity.CustomerInformationActivity;

public class SaleInformation extends AppCompatActivity {

    TextView saleDetailsId, sdId, sdType, sdRate, sdEndTime, sdStatus, sdDescription;
    Button cancelSale;
    ImageButton finishView, editSaleAction;
    String[] foodTypes;
    String[] status;
    SaleRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_information);

        repository = new SaleRepository(this);

        foodTypes = getResources().getStringArray(R.array.food_type_entries);
        status = getResources().getStringArray(R.array.sale_status_entries);

        saleDetailsId = findViewById(R.id.saleDetailsId);
        sdId = findViewById(R.id.sdId);
        sdType = findViewById(R.id.sdType);
        sdRate = findViewById(R.id.sdRate);
        sdEndTime = findViewById(R.id.sdEndTime);
        sdStatus = findViewById(R.id.sdStatus);
        sdDescription = findViewById(R.id.sdDescription);
        editSaleAction = findViewById(R.id.editSaleAction);

        cancelSale = findViewById(R.id.cancelSale);

        finishView = findViewById(R.id.finishView);

        finishView.setOnClickListener(view -> {
            finish();
        });

        Intent callerIntent = getIntent();
        Bundle saleBundles = callerIntent.getBundleExtra("saleBundles");
        Sale sale = saleBundles.getParcelable("sale");

        saleDetailsId.setText(String.valueOf(sale.getId()));
        sdId.setText(String.valueOf(sale.getId()));

        sdType.setText(foodTypes[sale.getFoodType() - 1]);
        sdRate.setText(String.valueOf(sale.getRate()));
        sdEndTime.setText(String.valueOf(sale.getEndTime()));
        sdStatus.setText(status[sale.getActive() - 1]);
        sdDescription.setText(sale.getDescription());

        cancelSale.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Dừng đợt giảm giá?")
                    .setMessage("Đợt giảm giá này sẽ dừng hoạt động")
                    .setPositiveButton("ĐỒNG Ý", (dialogInterface, i) -> {
                        repository.deactivateSaleTask(sale.getId());
                    })
                    .setNegativeButton("BỎ QUA", null)
                    .setIcon(R.drawable.ic_danger)
                    .show();
        });

        editSaleAction.setOnClickListener(view -> {
            Intent intent = new Intent(SaleInformation.this, AddNewSale.class);
            String title = "Sửa thông tin đợt giảm giá";
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putParcelable("sale", sale);
            intent.putExtra("saleInformationBundles", bundle);
            startActivity(intent);
            finish();
        });

    }
}
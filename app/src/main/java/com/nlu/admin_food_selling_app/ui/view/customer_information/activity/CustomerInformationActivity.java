package com.nlu.admin_food_selling_app.ui.view.customer_information.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Customer;
import com.nlu.admin_food_selling_app.data.repository.CustomerRepository;

public class CustomerInformationActivity extends AppCompatActivity {

    ImageButton finishCustomerInformation, editCustomerAction;
    Button deleteCustomer;
    TextView ciPhone, ciName, ciId, ciName2, ciAddress, ciUsername, ciFirstChar;
    CustomerRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_information);

        repository = new CustomerRepository(this);

        finishCustomerInformation = findViewById(R.id.finishCustomerInformation);
        editCustomerAction = findViewById(R.id.editCustomerAction);
        deleteCustomer = findViewById(R.id.deleteCustomer);
        ciPhone = findViewById(R.id.ciPhone);
        ciName = findViewById(R.id.ciName);
        ciId = findViewById(R.id.ciId);
        ciName2 = findViewById(R.id.ciName2);
        ciAddress = findViewById(R.id.ciAddress);
        ciUsername = findViewById(R.id.ciUsername);
        ciFirstChar = findViewById(R.id.ciFirstChar);

        Intent callerIntent = getIntent();
        Bundle customerBundles = callerIntent.getBundleExtra("customerBundles");
        Customer customer = customerBundles.getParcelable("customer");

        ciId.setText(String.valueOf(customer.getId()));
        ciName2.setText(customer.getName());
        ciName.setText(customer.getName());
        ciPhone.setText(customer.getPhoneNumber());
        ciAddress.setText(customer.getAddress());
        ciUsername.setText(customer.getUsername());
        ciFirstChar.setText(String.valueOf(customer.getName().toCharArray()[0]));

        finishCustomerInformation.setOnClickListener(view -> {
            this.finish();
        });

        deleteCustomer.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa khách hàng?")
                    .setMessage("Tất cả dữ liệu liên quan đến khách hàng sẽ bị xóa?")
                    .setPositiveButton("ĐỒNG Ý", (dialogInterface, i) -> {
                        repository.deleteCustomerTask(customer.getId());
                    })
                    .setNegativeButton("BỎ QUA", null)
                    .setIcon(R.drawable.ic_danger)
                    .show();
        });

        ciPhone.setOnClickListener(view -> {
            String phoneNum = customer.getPhoneNumber();
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNum));
            startActivity(callIntent);
        });

        editCustomerAction.setOnClickListener(view -> {
            Intent intent = new Intent(CustomerInformationActivity.this, AddNewCustomerActivity.class);
            String title = "Sửa thông tin khách hàng";
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putParcelable("customer", customer);
            intent.putExtra("customerInformationBundles", bundle);
            startActivity(intent);
            finish();
        });
    }
}
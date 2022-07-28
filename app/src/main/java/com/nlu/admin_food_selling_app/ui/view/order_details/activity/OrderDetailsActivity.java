package com.nlu.admin_food_selling_app.ui.view.order_details.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nlu.admin_food_selling_app.ui.view.order_details.adapter.OrderDetailsAdapter;
import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Customer;
import com.nlu.admin_food_selling_app.data.model.Order;
import com.nlu.admin_food_selling_app.data.model.OrderDetails;
import com.nlu.admin_food_selling_app.data.repository.OrderDetailsRepository;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {
    ImageButton finishOrderDetails;
    ArrayList<OrderDetails> orderDetailsList;
    TextView orderDetailsId, odCustomerName, odDate, odTotalPrice, odStatus;
    Button odCancelOrder;
    Customer orderCustomer;
    OrderDetailsRepository repository;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        repository = new OrderDetailsRepository(getResources().getString(R.string.API_URL), OrderDetailsActivity.this);

        orderDetailsList = new ArrayList<>();
        orderCustomer = new Customer();

        orderDetailsId = findViewById(R.id.orderDetailsId);
        odCustomerName = findViewById(R.id.odCustomerName);
        odDate = findViewById(R.id.odDate);
        odTotalPrice = findViewById(R.id.odTotalPrice);
        odStatus = findViewById(R.id.odStatus);
        odCancelOrder = findViewById(R.id.odCancelOrder);

        Intent callerIntent = getIntent();
        Bundle orderBundles = callerIntent.getBundleExtra("orderBundles");
        Order order = orderBundles.getParcelable("order");
        String date = orderBundles.getString("date");

        repository.getOrderDetailsTask(order.getId(), orderDetailsList);
        repository.getCustomerTask(order.getCustomerId(), orderCustomer);

        orderDetailsId.setText(Integer.toString(order.getId()));
        odCustomerName.setText(orderCustomer.getName());
        odDate.setText(date);
        odTotalPrice.setText(Double.toString(order.getTotalPrice()));
        odStatus.setText(getStatus(order.getStatus()));

        RecyclerView orderDetailsView = findViewById(R.id.orderDetailsView);
        orderDetailsView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        orderDetailsView.setAdapter(new OrderDetailsAdapter(orderDetailsList));

        finishOrderDetails = findViewById(R.id.finishOrderDetails);
        finishOrderDetails.setOnClickListener(view -> {
            this.finish();
        });

        odCancelOrder.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Hủy hóa đơn?")
                    .setMessage("Bạn có chắc chắn muốn hủy hóa đơn " + order.getId() + " không?")
                    .setPositiveButton("HỦY ĐƠN", (dialogInterface, i) -> {
                        repository.cancelOrderTask(order.getId());
                    })
                    .setNegativeButton("BỎ QUA", null)
                    .show();
        });
    }

    private String getStatus(int status) {
        String[] items = getResources().getStringArray(R.array.order_status_entries);
        return items[status - 1];
    }
}
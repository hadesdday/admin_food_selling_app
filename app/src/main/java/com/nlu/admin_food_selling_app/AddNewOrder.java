package com.nlu.admin_food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class AddNewOrder extends AppCompatActivity {
    Spinner aoPaymentMethod, aoOrderStatus;
    ImageView cancelAction;
    TextView submitNewOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_order);

        aoPaymentMethod = findViewById(R.id.aoPaymentMethod);
        aoOrderStatus = findViewById(R.id.aoOrderStatus);
        cancelAction = findViewById(R.id.cancelAddOrder);
        submitNewOrder = findViewById(R.id.submitNewOrder);

        String[] paymentMethodItems = getResources().getStringArray(R.array.payment_method_entries);
        String[] statusItems = getResources().getStringArray(R.array.order_status_entries);

        ArrayAdapter<String> paymentMethodAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paymentMethodItems);
        aoPaymentMethod.setAdapter(paymentMethodAdapter);

        ArrayAdapter<String> orderStatusAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, statusItems);
        aoOrderStatus.setAdapter(orderStatusAdapter);

        cancelAction.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Thoát")
                    .setMessage("Thay đổi trên hóa đơn sẽ không được lưu lại. Bạn có chắc chắn muốn thoát?")
                    .setPositiveButton("ĐỒNG Ý", (dialogInterface, i) -> this.finish())
                    .setNegativeButton("BỎ QUA", null)
                    .setIcon(R.drawable.ic_danger)
                    .show();
        });

        submitNewOrder.setOnClickListener(view -> {
            this.finish();
        });
    }
}
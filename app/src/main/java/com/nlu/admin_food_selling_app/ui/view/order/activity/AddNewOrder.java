package com.nlu.admin_food_selling_app.ui.view.order.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Order;
import com.nlu.admin_food_selling_app.data.repository.OrderRepository;

public class AddNewOrder extends AppCompatActivity {
    Spinner aoPaymentMethod, aoOrderStatus;
    ImageView cancelAction;
    TextView submitNewOrder;
    EditText aoCustomerId, aoTotalPrice, aoVoucher;
    OrderRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_order);

        repository = new OrderRepository(this);

        aoPaymentMethod = findViewById(R.id.aoPaymentMethod);
        aoOrderStatus = findViewById(R.id.aoOrderStatus);
        cancelAction = findViewById(R.id.cancelAddOrder);
        submitNewOrder = findViewById(R.id.submitNewOrder);
        aoCustomerId = findViewById(R.id.aoCustomerId);
        aoTotalPrice = findViewById(R.id.aoTotalPrice);
        aoVoucher = findViewById(R.id.aoVoucher);

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

            EditText[] inp = new EditText[]{aoCustomerId, aoTotalPrice};

            boolean valid = true;
            for (EditText e : inp) {
                if (e.getText().toString().isEmpty()) {
                    e.setError("Vui lòng nhập dữ liệu đúng định dạng");
                    valid = false;
                }
            }
            if (valid) {
                int customerId = Integer.parseInt(String.valueOf(aoCustomerId.getText()));
                double totalPrice = Double.parseDouble(String.valueOf(aoTotalPrice.getText()));
                String paymentMethod = (String) aoPaymentMethod.getSelectedItem();
                int status = aoOrderStatus.getSelectedItemPosition() + 1;
                String voucher = aoVoucher.getText().toString();
                String voucherId = voucher.isEmpty() ? "" : voucher;
                Order order = new Order(customerId, totalPrice, paymentMethod, status, voucherId);
               repository.insertBillTask(order);
            }
        });
    }
}
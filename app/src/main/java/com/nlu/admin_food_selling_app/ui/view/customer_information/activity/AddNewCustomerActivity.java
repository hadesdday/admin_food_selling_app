package com.nlu.admin_food_selling_app.ui.view.customer_information.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Customer;
import com.nlu.admin_food_selling_app.data.repository.CustomerRepository;

public class AddNewCustomerActivity extends AppCompatActivity {

    CustomerRepository repository;
    EditText txtName, txtAddress, txtPhone, txtUsername;
    TextView txtSave, ecId, customerActionTitle;
    ImageButton cancelAdd;
    String titleName = "";
    Customer inCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);

        repository = new CustomerRepository(this);

        Intent callerIntent = getIntent();
        try {
            Bundle customerBundles = callerIntent.getBundleExtra("customerInformationBundles");
            titleName = customerBundles.getString("title");
            inCustomer = customerBundles.getParcelable("customer");
        } catch (Exception e) {
        }

        txtName = findViewById(R.id.acCustomerName);
        txtAddress = findViewById(R.id.acAddress);
        txtPhone = findViewById(R.id.acPhoneNum);
        txtUsername = findViewById(R.id.acUsername);
        ecId = findViewById(R.id.ecId);
        customerActionTitle = findViewById(R.id.customerActionTitle);

        txtSave = findViewById(R.id.submitNewCustomer);

        cancelAdd = findViewById(R.id.cancelAddCustomer);

        if (inCustomer != null && titleName.equals("Sửa thông tin khách hàng")) {
            customerActionTitle.setText(titleName);
            ecId.setText(String.valueOf(inCustomer.getId()));
            txtName.setText(inCustomer.getName());
            txtAddress.setText(inCustomer.getAddress());
            txtPhone.setText(inCustomer.getPhoneNumber());
            txtUsername.setText(inCustomer.getUsername());
        }

        cancelAdd.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Thoát")
                    .setMessage("Thay đổi trên khách hàng sẽ không được lưu lại. Bạn có chắc chắn muốn thoát?")
                    .setPositiveButton("ĐỒNG Ý", (dialogInterface, i) -> this.finish())
                    .setNegativeButton("BỎ QUA", null)
                    .setIcon(R.drawable.ic_danger)
                    .show();
        });

        txtSave.setOnClickListener(view -> {
            String name = String.valueOf(txtName.getText());
            String address = String.valueOf(txtAddress.getText());
            String phone = String.valueOf(txtPhone.getText());
            String username = String.valueOf(txtUsername.getText());

            boolean valid = true;

            EditText[] inputs = new EditText[]{txtName, txtPhone};
            for (EditText e : inputs) {
                if (checkError(String.valueOf(e.getText()))) {
                    valid = false;
                    e.setError("Vui lòng nhập dữ liệu đúng định dạng");
                    e.requestFocus();
                }
            }
            if (valid && titleName.isEmpty()) {
                Customer c = new Customer(name, address, phone, username);
                repository.insertCustomerTask(c);
            } else if (valid && !titleName.isEmpty()) {
                int id = Integer.parseInt(String.valueOf(ecId.getText()));
                Customer newCustomer = new Customer(name, address, phone, username);
                newCustomer.setId(id);
                repository.updateCustomerInformationTask(newCustomer);
            }
        });
    }

    private boolean checkError(String in) {
        return in == null || in.isEmpty();
    }
}
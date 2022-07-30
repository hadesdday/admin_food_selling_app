package com.nlu.admin_food_selling_app.ui.view.voucher.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Voucher;
import com.nlu.admin_food_selling_app.data.repository.VoucherRepository;

public class AddNewVoucher extends AppCompatActivity {

    ImageButton cancelAddVoucher;
    TextView submitNewVoucher;
    EditText avId, avRate;
    Spinner avStatus;
    VoucherRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_voucher);

        repository = new VoucherRepository(this);

        cancelAddVoucher = findViewById(R.id.cancelAddVoucher);
        submitNewVoucher = findViewById(R.id.submitNewVoucher);
        avId = findViewById(R.id.avId);
        avRate = findViewById(R.id.avRate);
        avStatus = findViewById(R.id.avStatus);

        String[] status = getResources().getStringArray(R.array.voucher_status_entries);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, status);
        avStatus.setAdapter(statusAdapter);

        cancelAddVoucher.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Thoát")
                    .setMessage("Thay đổi trên voucher sẽ không được lưu lại. Bạn có chắc chắn muốn thoát?")
                    .setPositiveButton("ĐỒNG Ý", (dialogInterface, i) -> this.finish())
                    .setNegativeButton("BỎ QUA", null)
                    .setIcon(R.drawable.ic_danger)
                    .show();
        });

        submitNewVoucher.setOnClickListener(view -> {
            boolean valid = true;

            EditText[] inputs = new EditText[]{avId, avRate};
            for (EditText e : inputs) {
                if (checkError(String.valueOf(e.getText()))) {
                    valid = false;
                    e.setError("Vui lòng nhập dữ liệu đúng định dạng");
                    e.requestFocus();
                }
            }

            if (valid) {
                String id = String.valueOf(avId.getText());
                double rate = Double.parseDouble(String.valueOf(avRate.getText()));
                int active = avStatus.getSelectedItemPosition() + 1;
                Voucher v = new Voucher(id, rate, active);
                repository.addVoucherTask(v);
            }
        });
    }

    private boolean checkError(String in) {
        return in == null || in.isEmpty();
    }
}
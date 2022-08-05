package com.nlu.admin_food_selling_app.ui.view.voucher.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Voucher;
import com.nlu.admin_food_selling_app.data.repository.VoucherRepository;

import es.dmoral.toasty.Toasty;

public class VoucherInformation extends AppCompatActivity {

    ImageButton finishView;
    TextView voucherDetailsTitle, voucherDetailsId, vdRate, vdStatus;
    Button deleteVoucher;
    VoucherRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_information);

        repository = new VoucherRepository(VoucherInformation.this);

        voucherDetailsTitle = findViewById(R.id.voucherDetailsTitle);
        voucherDetailsId = findViewById(R.id.voucherDetailsId);
        vdRate = findViewById(R.id.vdRate);
        vdStatus = findViewById(R.id.vdStatus);
        finishView = findViewById(R.id.finishView);
        deleteVoucher = findViewById(R.id.deleteVoucher);

        finishView.setOnClickListener(view -> {
            finish();
        });

        Intent callerIntent = getIntent();
        Bundle voucherBundles = callerIntent.getBundleExtra("voucherBundles");
        Voucher voucher = voucherBundles.getParcelable("voucher");

        voucherDetailsTitle.setText(voucher.getId());
        voucherDetailsId.setText(voucher.getId());
        vdRate.setText(String.valueOf(voucher.getRate()));
        vdStatus.setText(getResources().getStringArray(R.array.voucher_status_entries)[voucher.getActive() - 1]);

        deleteVoucher.setOnClickListener(view -> {
            Toasty.info(this, voucher.getId(), Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(this)
                    .setTitle("Xóa voucher?")
                    .setMessage("Mã voucher " + voucher.getId() + " sẽ bị xóa?")
                    .setPositiveButton("ĐỒNG Ý", (dialogInterface, i) -> {
                        repository.deleteVoucherTask(voucher.getId());
                    })
                    .setNegativeButton("BỎ QUA", null)
                    .setIcon(R.drawable.ic_danger)
                    .show();
        });
    }
}
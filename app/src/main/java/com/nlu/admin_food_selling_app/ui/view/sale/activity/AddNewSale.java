package com.nlu.admin_food_selling_app.ui.view.sale.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Sale;
import com.nlu.admin_food_selling_app.data.repository.SaleRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNewSale extends AppCompatActivity {
    EditText asEndTime, asRate, asDescription;
    Spinner asFoodType, asStatus;
    TextView submitNewSale, esId, saleActionTitle;
    SaleRepository repository;
    String titleName = "";
    Sale inSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_sale);

        repository = new SaleRepository(this);

        Intent callerIntent = getIntent();
        try {
            Bundle saleBundles = callerIntent.getBundleExtra("saleInformationBundles");
            titleName = saleBundles.getString("title");
            inSale = saleBundles.getParcelable("sale");
        } catch (Exception e) {
            System.out.println("add action");
        }

        asEndTime = findViewById(R.id.asEndTime);
        asFoodType = findViewById(R.id.asFoodType);
        asStatus = findViewById(R.id.asStatus);
        asRate = findViewById(R.id.asRate);
        asDescription = findViewById(R.id.asDescription);
        submitNewSale = findViewById(R.id.submitNewSale);
        esId = findViewById(R.id.esId);
        saleActionTitle = findViewById(R.id.saleActionTitle);

        String[] foodTypes = getResources().getStringArray(R.array.food_type_entries);
        String[] status = getResources().getStringArray(R.array.sale_status_entries);

        ArrayAdapter<String> foodTypeAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, foodTypes);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, status);

        asFoodType.setAdapter(foodTypeAdapter);
        asStatus.setAdapter(statusAdapter);

        asEndTime.setInputType(InputType.TYPE_NULL);

        asEndTime.setOnClickListener(view -> {
            showDateTimeDialog(asEndTime);
        });

        if (inSale != null && titleName.equals("Sửa thông tin đợt giảm giá")) {
            saleActionTitle.setText(titleName);
            esId.setText(String.valueOf(inSale.getId()));
            asFoodType.setSelection(inSale.getFoodType() - 1);
            asStatus.setSelection(inSale.getActive() - 1);
            asRate.setText(String.valueOf(inSale.getRate()));
            asDescription.setText(inSale.getDescription());
            asEndTime.setText(inSale.getEndTime());
        }

        submitNewSale.setOnClickListener(view -> {
            int foodType = asFoodType.getSelectedItemPosition() + 1;
            double rate = Double.parseDouble(String.valueOf(asRate.getText()));
            int statusSale = asStatus.getSelectedItemPosition() + 1;
            String description = asDescription.getText().toString();
            String endTime = asEndTime.getText().toString();

            boolean valid = true;

            EditText[] inputs = new EditText[]{asEndTime, asRate, asDescription};
            for (EditText e : inputs) {
                if (checkError(String.valueOf(e.getText()))) {
                    valid = false;
                    e.setError("Vui lòng nhập dữ liệu đúng định dạng");
                    e.requestFocus();
                }
            }

            Sale sale = new Sale(foodType, rate, endTime, description, statusSale);

            if (valid && titleName.isEmpty())
                repository.addSaleTask(sale);
            else if (valid && titleName.equals("Sửa thông tin đợt giảm giá")) {
                sale.setId(Integer.parseInt((String) esId.getText()));
                repository.editSaleTask(sale);
            }
        });
    }

    private void showDateTimeDialog(final EditText asEndTime) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        asEndTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(AddNewSale.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(AddNewSale.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean checkError(String in) {
        return in == null || in.isEmpty();
    }
}
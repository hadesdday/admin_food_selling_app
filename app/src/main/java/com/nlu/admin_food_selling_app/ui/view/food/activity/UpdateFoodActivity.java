package com.nlu.admin_food_selling_app.ui.view.food.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Food;
import com.nlu.admin_food_selling_app.data.model.FoodType;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class UpdateFoodActivity extends AppCompatActivity {
    EditText foodName, foodDescription, foodImage, foodPrice;
    Spinner foodTypeList;
    Button update;
    Food food;
    int foodtypeid;
    ArrayList<FoodType> foodTypeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food);

        new doFoodTypeList().execute();

        food = (Food) getIntent().getSerializableExtra("food");
        foodName = findViewById(R.id.foodNameTxt);
        foodDescription = findViewById(R.id.foodDescriptionTxt);
        foodImage = findViewById(R.id.foodImageTxt);
        foodPrice = findViewById(R.id.foodPriceTxt);
        foodTypeList = findViewById(R.id.foodTypeList);
        update = findViewById(R.id.update);

        foodName.setText(food.getFoodName());
        foodDescription.setText(food.getFoodDescription());
        foodImage.setText(food.getFoodImage());
        foodPrice.setText(String.valueOf(food.getFoodPrice()));
        update.setOnClickListener(view -> {
            if (foodName.getText().toString().length() == 0 ||
            foodImage.getText().toString().length() == 0 ||
            foodDescription.getText().toString().length() == 0 ||
            foodPrice.getText().toString().length() == 0)
                Toast.makeText(this, "Oops!", Toast.LENGTH_SHORT).show();
            else
                new update().execute();
        });
    }

    class doFoodTypeList extends AsyncTask<Void, Void, Void> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(UpdateFoodActivity.this);
        private final String NAMESPACE = getResources().getString(R.string.NAMESPACE);
        private final String URL = getResources().getString(R.string.URL);
        private final String METHOD = "getFoodType";
        private final String SOAP_ACTION = NAMESPACE + METHOD;

        @Override
        protected Void doInBackground(Void... voids) {
            SoapObject soapObjectRequest = new SoapObject(NAMESPACE, METHOD);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObjectRequest);
            MarshalFloat marshalFloat = new MarshalFloat();
            marshalFloat.register(envelope);

            HttpTransportSE transportSE = new HttpTransportSE(URL);

            try {
                transportSE.call(SOAP_ACTION, envelope);
                SoapObject soapObjectResponse = (SoapObject) envelope.getResponse();
                int foodTypeSize = soapObjectResponse.getPropertyCount();
                foodTypeArrayList = new ArrayList<>();

                for (int i = 0; i < foodTypeSize; i++) {
                    SoapObject item = (SoapObject) soapObjectResponse.getProperty(i);
                    int foodTypeId = Integer.parseInt(item.getProperty("FoodTypeId").toString());
                    String foodTypeName = item.getProperty("FoodTypeName").toString();
                    foodTypeArrayList.add(new FoodType(foodTypeId, foodTypeName));
                }
            } catch (Exception e) {
                exc = true;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Đang tải dữ liệu...");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();

            if (exc)
                Toast.makeText(UpdateFoodActivity.this, "Tải dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                makeSpinner();
                exc = false;
            }
        }
    }

    class update extends AsyncTask<Void, Void, String> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(UpdateFoodActivity.this);
        private final String NAMESPACE = getResources().getString(R.string.NAMESPACE);
        private final String URL = getResources().getString(R.string.URL);
        private final String METHOD = "updateFood";
        private final String SOAP_ACTION = NAMESPACE + METHOD;

        @Override
        protected String doInBackground(Void... voids) {
            SoapObject soapObjectRequest = new SoapObject(NAMESPACE, METHOD);
            soapObjectRequest.addProperty("foodId", food.getFoodId());
            soapObjectRequest.addProperty("foodName", foodName.getText().toString());
            soapObjectRequest.addProperty("foodImage", foodImage.getText().toString());
            soapObjectRequest.addProperty("foodDescription", foodDescription.getText().toString());
            soapObjectRequest.addProperty("foodPrice", Integer.parseInt(foodPrice.getText().toString()));
            soapObjectRequest.addProperty("foodTypeId", foodtypeid);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObjectRequest);
            MarshalFloat marshalFloat = new MarshalFloat();
            marshalFloat.register(envelope);

            HttpTransportSE transportSE = new HttpTransportSE(URL);
            try {
                transportSE.call(SOAP_ACTION, envelope);
            } catch (Exception e) {
                exc = true;
                e.printStackTrace();
            }

            SoapObject soapObjectResponse = (SoapObject) envelope.bodyIn;
            SoapPrimitive primitive = (SoapPrimitive) soapObjectResponse.getProperty("updateFoodResult");
            return primitive.toString();
        }

        @Override
        protected void onPostExecute(String unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();

            if (exc)
                Toast.makeText(UpdateFoodActivity.this,
                        "Cập nhật dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(UpdateFoodActivity.this,
                        "Cập nhật dữ liệu thành công. :)", Toast.LENGTH_LONG).show();
                exc = false;
            }
        }
    }

    private void makeSpinner() {
        foodTypeList = findViewById(R.id.foodTypeList);
        ArrayAdapter<FoodType> foodTypeAdapter = new ArrayAdapter<>
                (UpdateFoodActivity.this, android.R.layout.simple_list_item_1, foodTypeArrayList);
        foodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodTypeList.setAdapter(foodTypeAdapter);

        foodTypeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                foodtypeid = foodTypeAdapter.getItem(i).getFoodTypeId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
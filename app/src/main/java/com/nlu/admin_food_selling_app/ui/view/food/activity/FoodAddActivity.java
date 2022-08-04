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
import com.nlu.admin_food_selling_app.data.model.FoodType;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class FoodAddActivity extends AppCompatActivity {
    EditText foodNameTxt, foodImageTxt, foodDescriptionTxt, foodPriceTxt;
    Spinner foodTypeList;
    Button addButton;
    int foodtypeid;
    ArrayList<FoodType> foodTypeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_add);

        foodNameTxt = findViewById(R.id.foodNameTxt);
        foodImageTxt = findViewById(R.id.foodImageTxt);
        foodDescriptionTxt = findViewById(R.id.foodDescriptionTxt);
        foodPriceTxt = findViewById(R.id.foodPriceTxt);
        addButton = findViewById(R.id.addButton);

        new doFoodTypeList().execute();

        addButton.setOnClickListener(view -> {
            new add().execute();
            finish();
        });

    }

    class doFoodTypeList extends AsyncTask<Void, Void, Void> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(FoodAddActivity.this);
        private final String NAMESPACE = getResources().getString(R.string.API_NAMESPACE);
        private final String URL = getResources().getString(R.string.API_URL);
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
                    int foodTypeId = Integer.parseInt(item.getProperty("id").toString());
                    String foodTypeName = item.getProperty("name").toString();
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
                Toast.makeText(FoodAddActivity.this, "Tải dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                makeSpinner();
                exc = false;
            }
        }
    }

    private void makeSpinner() {
        foodTypeList = findViewById(R.id.foodTypeList);
        ArrayAdapter<FoodType> foodTypeAdapter = new ArrayAdapter<>
                (FoodAddActivity.this, android.R.layout.simple_list_item_1, foodTypeArrayList);
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

    class add extends AsyncTask<Void, Void, String> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(FoodAddActivity.this);
        private final String NAMESPACE = getResources().getString(R.string.API_NAMESPACE);
        private final String URL = getResources().getString(R.string.API_URL);
        private final String METHOD = "addFood";
        private final String SOAP_ACTION = NAMESPACE + METHOD;

        @Override
        protected String doInBackground(Void... voids) {
            SoapObject soapObjectRequest = new SoapObject(NAMESPACE, METHOD);
            soapObjectRequest.addProperty("foodName", foodNameTxt.getText().toString());
            soapObjectRequest.addProperty("foodImage", foodImageTxt.getText().toString());
            soapObjectRequest.addProperty("foodDescription", foodDescriptionTxt.getText().toString());
            soapObjectRequest.addProperty("foodPrice", Integer.parseInt(foodPriceTxt.getText().toString()));
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
            SoapPrimitive primitive = (SoapPrimitive) soapObjectResponse.getProperty("addFoodResult");
            return primitive.toString();
        }

        @Override
        protected void onPostExecute(String unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();

            if (exc)
                Toast.makeText(FoodAddActivity.this,
                        "Cập nhật dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(FoodAddActivity.this,
                        "Cập nhật dữ liệu thành công. :)", Toast.LENGTH_LONG).show();
                exc = false;
            }
        }
    }
}
package com.nlu.admin_food_selling_app.ui.view.food.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class FoodTypeAdd extends AppCompatActivity {
    EditText foodNameTxt;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_type_add);

        foodNameTxt = findViewById(R.id.foodNameTxt);
        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(view -> {
            if (foodNameTxt.getText().toString().length() == 0)
                Toast.makeText(this, "Oops!", Toast.LENGTH_SHORT).show();
            else {
                new add().execute();
                finish();
            }
        });
    }

    class add extends AsyncTask<Void, Void, String> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(FoodTypeAdd.this);
        private final String NAMESPACE = getResources().getString(R.string.API_NAMESPACE);
        private final String URL = getResources().getString(R.string.API_URL);
        private final String METHOD = "addFoodType";
        private final String SOAP_ACTION = NAMESPACE + METHOD;

        @Override
        protected String doInBackground(Void... voids) {
            SoapObject soapObjectRequest = new SoapObject(NAMESPACE, METHOD);
            soapObjectRequest.addProperty("foodTypeName", foodNameTxt.getText().toString());
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
            SoapPrimitive primitive = (SoapPrimitive) soapObjectResponse.getProperty("addFoodTypeResult");
            return primitive.toString();
        }

        @Override
        protected void onPostExecute(String unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();

            if (exc)
                Toast.makeText(FoodTypeAdd.this,
                        "Cập nhật dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(FoodTypeAdd.this,
                        "Cập nhật dữ liệu thành công. :)", Toast.LENGTH_LONG).show();
                exc = false;
            }
        }
    }
}
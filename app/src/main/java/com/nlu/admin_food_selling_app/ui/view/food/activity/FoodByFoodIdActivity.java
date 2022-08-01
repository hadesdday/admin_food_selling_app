package com.nlu.admin_food_selling_app.ui.view.food.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Food;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.text.MessageFormat;

public class FoodByFoodIdActivity extends AppCompatActivity {
    TextView foodId, foodName, foodPrice, foodDescription;
    ImageView foodImage;
    Button update, delete, rate;
    Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_food_id);

        food = (Food) getIntent().getSerializableExtra("food");
        doFoodByFoodId();
    }

    private void doFoodByFoodId() {
        food = (Food) getIntent().getSerializableExtra("food");

        foodImage = findViewById(R.id.foodImage);
        foodId = findViewById(R.id.foodId);
        foodName = findViewById(R.id.foodName);
        foodPrice = findViewById(R.id.foodPrice);
        foodDescription = findViewById(R.id.foodDescription);
        update = findViewById(R.id.foodUpdate);
        rate = findViewById(R.id.foodRating);
        delete = findViewById(R.id.foodDelete);

        foodId.setText(MessageFormat.format("ID: {0}", food.getFoodId()));
        foodName.setText(food.getFoodName());
        foodDescription.setText(food.getFoodDescription());
        foodPrice.setText(new DecimalFormat("###,###,###").format(food.getFoodPrice()).concat("đ"));
        Picasso.with(getApplicationContext()).load(food.getFoodImage())
                .error(R.drawable.ic_baseline_error_24)
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(foodImage);

        update.setOnClickListener(view -> {
            Intent intent = new Intent(FoodByFoodIdActivity.this, UpdateFoodActivity.class);
            intent.putExtra("food", food);
            startActivity(intent);
        });

        rate.setOnClickListener(view -> {
            Intent intent = new Intent(this, FoodRateActivity.class);
            intent.putExtra("foodid", food.getFoodId());
            startActivity(intent);
        });

        delete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Xóa " + food.getFoodName());
            builder.setMessage("Bạn có muốn xóa " + food.getFoodName() + "?");
            builder.setCancelable(true);
            builder.setPositiveButton("Có", (dialogInterface, i) -> {
                new doDeleteFood().execute();
                finish();
            });
            builder.setNegativeButton("Không", (dialogInterface, i) -> {
                dialogInterface.cancel();
            });

            builder.show().create();
        });
    }

    class doDeleteFood extends AsyncTask<Void, Void, String> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(FoodByFoodIdActivity.this);
        private final String NAMESPACE = getResources().getString(R.string.NAMESPACE);
        private final String URL = getResources().getString(R.string.URL);
        private final String METHOD = "delFood";
        private final String SOAP_ACTION = NAMESPACE + METHOD;

        @Override
        protected String doInBackground(Void... voids) {
            SoapObject soapObjectRequest = new SoapObject(NAMESPACE, METHOD);
            soapObjectRequest.addProperty("foodId", food.getFoodId());
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
            SoapPrimitive primitive = (SoapPrimitive) soapObjectResponse.getProperty("delFoodResult");
            return primitive.toString();
        }

        @Override
        protected void onPostExecute(String unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();

            if (exc)
                Toast.makeText(FoodByFoodIdActivity.this,
                        "Xóa dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(FoodByFoodIdActivity.this,
                        "Xóa dữ liệu thành công. :)", Toast.LENGTH_LONG).show();
                exc = false;
            }
        }
    }
}
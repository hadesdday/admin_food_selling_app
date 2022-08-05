package com.nlu.admin_food_selling_app.ui.view.food.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.FoodRating;
import com.nlu.admin_food_selling_app.ui.view.food.adapter.FoodRateAdapter;
import com.nlu.admin_food_selling_app.utils.RecyclerTouchListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class FoodRateActivity extends AppCompatActivity {
    ArrayList<FoodRating> foodRatingArrayList;
    RecyclerView rateList;
    FoodRateAdapter adapter;
    int foodId, rateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_rate);
        foodId = getIntent().getIntExtra("foodid", 0);
        new doFoodRate().execute();
    }

    class doFoodRate extends AsyncTask<Void, Void, Void> {
        boolean exc = false;
        private final String NAMESPACE = getResources().getString(R.string.API_NAMESPACE);
        private final String URL = getResources().getString(R.string.API_URL);
        private final ProgressDialog dialog = new ProgressDialog(FoodRateActivity.this);
        private final String METHOD = "getFoodRateByFoodId";
        private final String SOAP_ACTION = NAMESPACE + METHOD;

        @Override
        protected Void doInBackground(Void... voids) {
            SoapObject soapObjectRequest = new SoapObject(NAMESPACE, METHOD);
            soapObjectRequest.addProperty("foodId", foodId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObjectRequest);
            MarshalFloat marshalFloat = new MarshalFloat();
            marshalFloat.register(envelope);

            HttpTransportSE transportSE = new HttpTransportSE(URL);
            try {
                transportSE.call(SOAP_ACTION, envelope);
                SoapObject soapObjectResponse = (SoapObject) envelope.getResponse();
                int foodRateSize = soapObjectResponse.getPropertyCount();
                foodRatingArrayList = new ArrayList<>();

                for (int i = 0; i < foodRateSize; i++) {
                    SoapObject item = (SoapObject) soapObjectResponse.getProperty(i);
                    int rateId = Integer.parseInt(item.getProperty("id").toString());
                    int foodId = Integer.parseInt(item.getProperty("food_id").toString());
                    float foodRate = Float.parseFloat(item.getProperty("rate").toString());
                    String foodComment = item.getProperty("comment").toString();
                    foodRatingArrayList.add(new FoodRating(rateId, foodId, foodRate, foodComment));
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
                Toasty.error(FoodRateActivity.this, "Tải dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                makeRecyclerView();
                exc = false;
            }
        }
    }

    private void makeRecyclerView() {
        rateList = findViewById(R.id.ratelist);
        adapter = new FoodRateAdapter(this, foodRatingArrayList);
        rateList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rateList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rateList.setAdapter(adapter);

        adapter.setOnClickListener(position -> {
            rateId = foodRatingArrayList.get(position).getRateId();
            AlertDialog.Builder builder = new AlertDialog.Builder(FoodRateActivity.this);

            builder.setTitle("Xóa bình luận?");
            builder.setMessage("Bạn có muốn xóa bình luận đó không?");
            builder.setCancelable(true);
            builder.setPositiveButton("Có", (dialogInterface, i) -> {
                new doDeleteRate().execute();
            });
            builder.setNegativeButton("Không", (dialogInterface, i) -> {
                dialogInterface.cancel();
            });
            builder.show().create();
        });
    }

    class doDeleteRate extends AsyncTask<Void, Void, String> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(FoodRateActivity.this);
        private final String NAMESPACE = getResources().getString(R.string.API_NAMESPACE);
        private final String URL = getResources().getString(R.string.API_URL);
        private final String METHOD = "delFoodRating";
        private final String SOAP_ACTION = NAMESPACE + METHOD;

        @Override
        protected String doInBackground(Void... voids) {
            SoapObject soapObjectRequest = new SoapObject(NAMESPACE, METHOD);
            soapObjectRequest.addProperty("rateId", rateId);
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
            SoapPrimitive primitive = (SoapPrimitive) soapObjectResponse.getProperty("delFoodRatingResult");
            return primitive.toString();
        }

        @Override
        protected void onPostExecute(String unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();

            if (exc)
                Toasty.error(FoodRateActivity.this,
                        "Xóa dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                Toasty.success(FoodRateActivity.this,
                        "Xóa dữ liệu thành công. :)", Toast.LENGTH_LONG).show();
                exc = false;
            }

            new doFoodRate().execute();
        }
    }
}
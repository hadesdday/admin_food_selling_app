package com.nlu.admin_food_selling_app.ui.view.sale.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Sale;
import com.nlu.admin_food_selling_app.helper.GetVariable;
import com.nlu.admin_food_selling_app.ui.view.sale.adapter.SaleAdapter;
import com.nlu.admin_food_selling_app.utils.MarshalDouble;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class SearchSaleResult extends AppCompatActivity {

    ImageButton finishSearchSaleResult;
    RecyclerView recyclerView;
    SaleAdapter adapter;
    View noSearchDataView;
    TextView searchSaleKeywordTitle;
    ArrayList<Sale> saleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sale_result);

        noSearchDataView = findViewById(R.id.noSearchDataView);
        recyclerView = findViewById(R.id.searchSaleResultList);
        finishSearchSaleResult = findViewById(R.id.finishSearchSaleResult);

        Intent callerIntent = getIntent();
        Bundle searchBundles = callerIntent.getBundleExtra("searchBundles");
        int id = searchBundles.getInt("id");

        searchSaleKeywordTitle = findViewById(R.id.searchSaleKeywordTitle);
        searchSaleKeywordTitle.setText(getResources().getStringArray(R.array.food_type_entries)[id]);

        saleList = new ArrayList<Sale>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new SaleAdapter(saleList);
        recyclerView.setAdapter(adapter);
        searchSaleTask(id + 1);

        finishSearchSaleResult = findViewById(R.id.finishSearchSaleResult);
        finishSearchSaleResult.setOnClickListener(view -> {
            this.finish();
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void searchSaleTask(int id) {
        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            private final LottieDialog dialog = new LottieDialog(SearchSaleResult.this);

            @Override
            protected void onPreExecute() {
                dialog.setAnimation(R.raw.loading);
                dialog.setAnimationRepeatCount(LottieDialog.INFINITE);
                dialog.setMessage("Vui lòng chờ");
                dialog.setAutoPlayAnimation(true);
                dialog.show();
            }

            @Override
            protected Void doInBackground(Integer... ints) {
                searchSale(id);
                return null;
            }

            @Override
            protected void onPostExecute(Void voids) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
    }

    public void searchSale(int id) {
        try {
            String METHOD_NAME = "GetSaleListByFoodType";
            String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
            SoapObject request = new SoapObject("http://tempuri.org/", METHOD_NAME);
            request.addProperty("foodType", id);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.encodingStyle = SoapSerializationEnvelope.XSD;
            envelope.setOutputSoapObject(request);
            MarshalDouble marshal = new MarshalDouble();
            marshal.register(envelope);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(getResources().getString(R.string.API_URL));
            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapObject responseList = (SoapObject) envelope.getResponse();
            int count = responseList.getPropertyCount();
            saleList.clear();
            for (int i = 0; i < count; i++) {
                SoapObject sale = (SoapObject) responseList.getProperty(i);
                int sid = GetVariable.getIntFormat(String.valueOf(sale.getProperty("Id")));
                String endTime, description = "";
                double rate = 0;
                int active, foodType = 0;

                try {
                    foodType = Integer.parseInt(String.valueOf(sale.getProperty("FoodType")));
                } catch (Exception e) {
                    foodType = 0;
                }
                try {
                    endTime = String.valueOf(sale.getProperty("EndTime"));
                } catch (Exception e) {
                    endTime = "";
                }
                try {
                    description = String.valueOf(sale.getProperty("Description"));
                } catch (Exception e) {
                    description = "";
                }
                try {
                    rate = Double.parseDouble(String.valueOf(sale.getProperty("Rate")));
                } catch (Exception e) {
                    rate = 0;
                }
                try {
                    active = Integer.parseInt(String.valueOf(sale.getProperty("Active")));
                } catch (Exception e) {
                    active = 0;
                }

                Sale re = new Sale(foodType, rate, endTime, description, active);
                re.setId(sid);
                saleList.add(re);
            }
        } catch (Exception e) {
            System.out.println("search customer error while proccessing " + e);
        }
    }
}
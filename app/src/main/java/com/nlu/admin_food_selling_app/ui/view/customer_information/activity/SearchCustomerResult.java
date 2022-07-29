package com.nlu.admin_food_selling_app.ui.view.customer_information.activity;

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
import com.nlu.admin_food_selling_app.data.model.Customer;
import com.nlu.admin_food_selling_app.helper.GetVariable;
import com.nlu.admin_food_selling_app.ui.view.customer.adapter.CustomerAdapter;
import com.nlu.admin_food_selling_app.utils.MarshalDouble;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class SearchCustomerResult extends AppCompatActivity {

    ImageButton finishSearchCustomerResult;
    RecyclerView recyclerView;
    CustomerAdapter adapter;
    View noSearchDataView;
    TextView searchCustomerKeywordTitle;
    ArrayList<Customer> customerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer_result);

        noSearchDataView = findViewById(R.id.noSearchDataView);
        recyclerView = findViewById(R.id.searchCustomerResultList);

        Intent callerIntent = getIntent();
        Bundle searchBundles = callerIntent.getBundleExtra("searchCustomerBundles");
        String keyword = searchBundles.getString("keyword");

        searchCustomerKeywordTitle = findViewById(R.id.searchCustomerKeywordTitle);
        searchCustomerKeywordTitle.setText(String.valueOf(keyword));

        customerList = new ArrayList<Customer>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new CustomerAdapter(customerList);
        recyclerView.setAdapter(adapter);
        searchCustomerTask(keyword);

//        if (customerList.isEmpty()) {
//            recyclerView.setVisibility(View.GONE);
//            noSearchDataView.setVisibility(View.VISIBLE);
//        } else {
//            recyclerView.setVisibility(View.VISIBLE);
//            noSearchDataView.setVisibility(View.GONE);
//        }

        finishSearchCustomerResult = findViewById(R.id.finishSearchCustomerResult);
        finishSearchCustomerResult.setOnClickListener(view -> {
            this.finish();
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void searchCustomerTask(String query) {
        AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {
            private final LottieDialog dialog = new LottieDialog(SearchCustomerResult.this);

            @Override
            protected void onPreExecute() {
                dialog.setAnimation(R.raw.loading);
                dialog.setAnimationRepeatCount(LottieDialog.INFINITE);
                dialog.setMessage("Vui lòng chờ");
                dialog.setAutoPlayAnimation(true);
                dialog.show();
            }

            @Override
            protected Void doInBackground(String... strings) {
                searchCustomer(query);
                return null;
            }

            @Override
            protected void onPostExecute(Void voids) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, query);
    }

    public void searchCustomer(String query) {
        try {
            String METHOD_NAME = "GetCustomerListByQueries";
            String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
            SoapObject request = new SoapObject("http://tempuri.org/", METHOD_NAME);
            request.addProperty("key", query);

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
            customerList.clear();
            for (int i = 0; i < count; i++) {
                SoapObject customer = (SoapObject) responseList.getProperty(i);
                int id = GetVariable.getIntFormat(String.valueOf(customer.getProperty("Id")));
                String name, address, phone, username = "";
                try {
                    name = customer.getProperty("Name").toString();
                } catch (Exception e) {
                    name = "";
                }

                try {
                    address = customer.getProperty("Address").toString();
                } catch (Exception e) {
                    address = "";
                }

                try {
                    phone = customer.getProperty("Phone").toString();
                } catch (Exception e) {
                    phone = "";
                }

                try {
                    username = customer.getProperty("Username").toString();
                } catch (Exception e) {
                    username = "";
                }

                Customer c = new Customer(name, address, phone, username);
                c.setId(id);
                customerList.add(c);
            }
        } catch (Exception e) {
            System.out.println("search customer error while proccessing " + e);
        }
    }
}
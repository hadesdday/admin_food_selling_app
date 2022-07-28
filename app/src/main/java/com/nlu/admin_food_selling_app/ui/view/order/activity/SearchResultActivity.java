package com.nlu.admin_food_selling_app.ui.view.order.activity;

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
import com.nlu.admin_food_selling_app.data.model.Order;
import com.nlu.admin_food_selling_app.helper.GetVariable;
import com.nlu.admin_food_selling_app.ui.view.order.adapter.OrderAdapter;
import com.nlu.admin_food_selling_app.utils.MarshalDouble;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    ImageButton finishSearch;
    TextView keywordTitle;
    ArrayList<Order> orderList;
    View noSearchDataView;
    RecyclerView recyclerView;
    OrderAdapter adapter;

    private static String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "GetBillListByCustomerId";
    private static final String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        URL = getResources().getString(R.string.API_URL);
        noSearchDataView = findViewById(R.id.noSearchDataView);
        recyclerView = findViewById(R.id.searchResultList);

        Intent callerIntent = getIntent();
        Bundle searchBundles = callerIntent.getBundleExtra("searchBundles");
        int keyword = searchBundles.getInt("keyword");

        keywordTitle = findViewById(R.id.searchKeywordTitle);
        keywordTitle.setText("Mã khách hàng : " + String.valueOf(keyword));

        orderList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(adapter);
        getOrderListTask(keyword);

        if (orderList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noSearchDataView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noSearchDataView.setVisibility(View.GONE);
        }

        finishSearch = findViewById(R.id.finishSearchOrderResult);
        finishSearch.setOnClickListener(view -> {
            this.finish();
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void getOrderListTask(int keyword) {
        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            private final LottieDialog dialog = new LottieDialog(SearchResultActivity.this);

            @Override
            protected void onPreExecute() {
                dialog.setAnimation(R.raw.loading);
                dialog.setAnimationRepeatCount(LottieDialog.INFINITE);
                dialog.setMessage("Vui lòng chờ");
                dialog.setAutoPlayAnimation(true);
                dialog.show();
            }

            @Override
            protected Void doInBackground(Integer... integers) {
                getOrderList(keyword);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (orderList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    noSearchDataView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noSearchDataView.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, keyword);
    }


    public void getOrderList(int keyword) {
        try {
            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
            request.addProperty("id", keyword);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.encodingStyle = SoapSerializationEnvelope.XSD;
            envelope.setOutputSoapObject(request);
            MarshalDouble marshal = new MarshalDouble();
            marshal.register(envelope);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapObject responseList = (SoapObject) envelope.getResponse();
            int count = responseList.getPropertyCount();
            orderList.clear();
            for (int i = 0; i < count; i++) {
                SoapObject bill = (SoapObject) responseList.getProperty(i);
                int id = GetVariable.getIntFormat(String.valueOf(bill.getProperty("Id")));
                String voucher = "";
                try {
                    voucher = String.valueOf(bill.getProperty("Voucher"));
                } catch (Exception e) {
                    voucher = "";
                }
                int customerId = GetVariable.getIntFormat(String.valueOf(bill.getProperty("CustomerId")));
                double price = GetVariable.getDoubleFormat(String.valueOf(bill.getProperty("Price")));
                String paymentMethod = String.valueOf(bill.getProperty("PaymentMethod"));
                int status = GetVariable.getIntFormat(String.valueOf(bill.getProperty("Status")));
                String date = String.valueOf(bill.getProperty("CreatedAt"));

                Order order = new Order(customerId, price, paymentMethod, status, voucher);
                order.setId(id);
                order.setDate(date);
                orderList.add(order);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
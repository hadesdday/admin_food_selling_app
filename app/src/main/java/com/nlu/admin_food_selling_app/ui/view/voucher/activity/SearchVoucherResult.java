package com.nlu.admin_food_selling_app.ui.view.voucher.activity;

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
import com.nlu.admin_food_selling_app.data.model.Voucher;
import com.nlu.admin_food_selling_app.ui.view.voucher.adapter.VoucherAdapter;
import com.nlu.admin_food_selling_app.utils.MarshalDouble;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class SearchVoucherResult extends AppCompatActivity {
    ImageButton finishSearchVoucherResult;
    TextView searchVoucherKeywordTitle;
    RecyclerView searchVoucherResultList;
    VoucherAdapter adapter;
    View noSearchDataView;
    ArrayList<Voucher> voucherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_voucher_result);

        finishSearchVoucherResult = findViewById(R.id.finishSearchVoucherResult);
        searchVoucherKeywordTitle = findViewById(R.id.searchVoucherKeywordTitle);
        searchVoucherResultList = findViewById(R.id.searchVoucherResultList);
        noSearchDataView = findViewById(R.id.noSearchDataView);

        Intent callerIntent = getIntent();
        Bundle searchBundles = callerIntent.getBundleExtra("searchBundles");
        String id = searchBundles.getString("id");

        searchVoucherKeywordTitle.setText(id);

        voucherList = new ArrayList<Voucher>();

        searchVoucherResultList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new VoucherAdapter(voucherList);
        searchVoucherResultList.setAdapter(adapter);
        searchVoucherTask(id);
    }

    @SuppressLint("StaticFieldLeak")
    public void searchVoucherTask(String id) {
        AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {
            private final LottieDialog dialog = new LottieDialog(SearchVoucherResult.this);

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
                searchVoucher(id);
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

    public void searchVoucher(String id) {
        try {
            String METHOD_NAME = "GetVoucherById";
            String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
            SoapObject request = new SoapObject("http://tempuri.org/", METHOD_NAME);
            request.addProperty("id", id);

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
            voucherList.clear();
            for (int i = 0; i < count; i++) {
                SoapObject voucher = (SoapObject) responseList.getProperty(i);
                String vid = String.valueOf(voucher.getProperty("Id"));
                double rate = 0;
                int active = Integer.parseInt(String.valueOf(voucher.getProperty("Active")));

                try {
                    rate = Double.parseDouble(String.valueOf(voucher.getProperty("Rate")));
                } catch (Exception e) {
                    rate = 0;
                }

                Voucher v = new Voucher(vid, rate, active);
                voucherList.add(v);
            }
        } catch (Exception e) {
            System.out.println("error while searching voucher " + e);
        }
    }
}
package com.nlu.admin_food_selling_app.ui.view.sale.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Sale;
import com.nlu.admin_food_selling_app.helper.GetVariable;
import com.nlu.admin_food_selling_app.ui.view.sale.activity.AddNewSale;
import com.nlu.admin_food_selling_app.ui.view.sale.activity.SearchSale;
import com.nlu.admin_food_selling_app.ui.view.sale.adapter.SaleAdapter;
import com.nlu.admin_food_selling_app.utils.MarshalDouble;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class SaleFragment extends Fragment {
    ArrayList<Sale> saleList;
    ImageView plusAction, searchAction;
    View noDataView;
    RecyclerView recyclerView;
    SaleAdapter adapter;

    private static String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "GetSaleList";
    private static final String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;

    public SaleFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        saleList = new ArrayList<>();
        adapter = new SaleAdapter(saleList);
        recyclerView.setAdapter(adapter);
        getSaleList(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sale, container, false);
        URL = getResources().getString(R.string.API_URL);

        noDataView = v.findViewById(R.id.sNoDataView);

        searchAction = v.findViewById(R.id.sSearchAct);

        saleList = new ArrayList<>();
        recyclerView = v.findViewById(R.id.saleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new SaleAdapter(saleList);
        recyclerView.setAdapter(adapter);
        getSaleList(0);

//        SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.refreshSaleView);
//        pullToRefresh.setOnRefreshListener(() -> {
//            getSaleList();
//            adapter.notifyDataSetChanged();
//            pullToRefresh.setRefreshing(true);
//        });

        plusAction = v.findViewById(R.id.sPlusAction);
        plusAction.setOnClickListener(view -> {
            openAddSaleView();
        });

        searchAction.setOnClickListener(view -> {
            openSearchSaleView();
        });
        return v;
    }

    @SuppressLint("StaticFieldLeak")
    public void getSaleList(int id) {
        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            private final LottieDialog dialog = new LottieDialog(SaleFragment.this.getContext());

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
                getSaleList();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (saleList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    noDataView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noDataView.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
    }


    public void getSaleList() {
        try {
            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
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
            saleList.clear();
            for (int i = 0; i < count; i++) {
                SoapObject sale = (SoapObject) responseList.getProperty(i);
                int id = GetVariable.getIntFormat(String.valueOf(sale.getProperty("Id")));
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
                re.setId(id);
                saleList.add(re);
            }
        } catch (Exception e) {
            System.out.println("Get sale list error " + e);
        }
    }

    public void openAddSaleView() {
        Intent intent = new Intent(getActivity(), AddNewSale.class);
        startActivity(intent);
    }

    public void openSearchSaleView() {
        Intent intent = new Intent(getActivity(), SearchSale.class);
        startActivity(intent);
    }
}
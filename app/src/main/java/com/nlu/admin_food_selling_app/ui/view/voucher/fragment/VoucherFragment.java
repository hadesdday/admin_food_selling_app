package com.nlu.admin_food_selling_app.ui.view.voucher.fragment;

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
import com.nlu.admin_food_selling_app.data.model.Voucher;
import com.nlu.admin_food_selling_app.ui.view.voucher.activity.AddNewVoucher;
import com.nlu.admin_food_selling_app.ui.view.voucher.activity.SearchVoucher;
import com.nlu.admin_food_selling_app.ui.view.voucher.adapter.VoucherAdapter;
import com.nlu.admin_food_selling_app.utils.MarshalDouble;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class VoucherFragment extends Fragment {

    ArrayList<Voucher> voucherList;
    ImageView plusAction, searchAction;
    View noDataView;
    RecyclerView recyclerView;
    VoucherAdapter adapter;

    private static String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "GetVoucherList";
    private static final String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;

    public VoucherFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        voucherList = new ArrayList<>();
        adapter = new VoucherAdapter(voucherList);
        recyclerView.setAdapter(adapter);
        getVoucherListTask(1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_voucher, container, false);
        URL = getResources().getString(R.string.API_URL);

        noDataView = v.findViewById(R.id.sNoDataView);

        searchAction = v.findViewById(R.id.sSearchAct);

        voucherList = new ArrayList<>();
        recyclerView = v.findViewById(R.id.voucherRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new VoucherAdapter(voucherList);
        recyclerView.setAdapter(adapter);
        getVoucherListTask(1);

//        SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.refreshSaleView);
//        pullToRefresh.setOnRefreshListener(() -> {
//            getVoucherList();
//            adapter.notifyDataSetChanged();
//            pullToRefresh.setRefreshing(true);
//        });

        plusAction = v.findViewById(R.id.sPlusAction);
        plusAction.setOnClickListener(view -> {
            openAddVoucherView();
        });

        searchAction.setOnClickListener(view -> {
            openSearchVoucherView();
        });
        return v;
    }

    @SuppressLint("StaticFieldLeak")
    public void getVoucherListTask(int id) {
        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            private final LottieDialog dialog = new LottieDialog(VoucherFragment.this.getContext());

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
                getVoucherList();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (voucherList.isEmpty()) {
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


    public void getVoucherList() {
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
            voucherList.clear();
            for (int i = 0; i < count; i++) {
                SoapObject voucher = (SoapObject) responseList.getProperty(i);
                String id = String.valueOf(voucher.getProperty("Id"));
                double rate = 0;
                int active = Integer.parseInt(String.valueOf(voucher.getProperty("Active")));

                try {
                    rate = Double.parseDouble(String.valueOf(voucher.getProperty("Rate")));
                } catch (Exception e) {
                    rate = 0;
                }

                Voucher v = new Voucher(id, rate, active);
                if (!id.equals(" "))
                    voucherList.add(v);
            }
        } catch (Exception e) {
            System.out.println("Get voucher list error " + e);
        }
    }

    public void openAddVoucherView() {
        Intent intent = new Intent(getActivity(), AddNewVoucher.class);
        startActivity(intent);
    }

    public void openSearchVoucherView() {
        Intent intent = new Intent(getActivity(), SearchVoucher.class);
        startActivity(intent);
    }
}
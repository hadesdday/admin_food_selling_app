package com.nlu.admin_food_selling_app;

import android.app.ProgressDialog;
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

import com.nlu.admin_food_selling_app.data.model.Order;
import com.nlu.admin_food_selling_app.helper.GetVariable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    ArrayList<Order> orderList;
    ImageView plusAction, searchAction;
    View noDataView;
    RecyclerView recyclerView;
    OrderAdapter adapter;

    private static String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "GetBillList";
    private static final String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;

    public OrderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        URL = getResources().getString(R.string.API_URL);

        noDataView = v.findViewById(R.id.noDataView);

        orderList = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.orderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(adapter);

        getOrderList(0);

//        SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.refreshOrderView);
//        pullToRefresh.setOnRefreshListener(() -> {
//            getOrderList();
//            adapter.notifyDataSetChanged();
//            pullToRefresh.setRefreshing(true);
//        });

        plusAction = v.findViewById(R.id.plusAction);
        plusAction.setOnClickListener(view -> {
            openAddOrderView();
        });

        searchAction = v.findViewById(R.id.searchAct);
        searchAction.setOnClickListener(view -> {
            openSearchOrderView();
        });
        return v;
    }

    public void getOrderList(int id) {
        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            private ProgressDialog dialog = new ProgressDialog(OrderFragment.this.getContext());

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Vui lòng chờ");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Integer... integers) {
                getOrderList();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (orderList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    noDataView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noDataView.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        };
        task.execute(id);
    }


    public void getOrderList() {
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
            System.out.println("count " + count);
            for (int i = 0; i <= count; i++) {
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
                System.out.println("index : " + i + " " + order.toString());
            }
        } catch (Exception e) {
            System.out.println("loi cmnr " + e.getMessage());
        }
    }

    public void openAddOrderView() {
        Intent intent = new Intent(getActivity(), AddNewOrder.class);
        startActivity(intent);
    }

    public void openSearchOrderView() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }
}
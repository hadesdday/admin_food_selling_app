package com.nlu.admin_food_selling_app.ui.view.customer.fragment;

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
import com.nlu.admin_food_selling_app.data.model.Customer;
import com.nlu.admin_food_selling_app.helper.GetVariable;
import com.nlu.admin_food_selling_app.ui.view.customer.adapter.CustomerAdapter;
import com.nlu.admin_food_selling_app.ui.view.customer_information.activity.AddNewCustomerActivity;
import com.nlu.admin_food_selling_app.ui.view.customer_information.activity.SearchCustomer;
import com.nlu.admin_food_selling_app.utils.MarshalDouble;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class CustomerFragment extends Fragment {

    ArrayList<Customer> customerList;
    ImageView plusAction, searchAction;
    View noDataView;
    RecyclerView recyclerView;
    CustomerAdapter adapter;

    private static String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "GetCustomerList";
    private static final String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;

    public CustomerFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        customerList = new ArrayList<>();
        adapter = new CustomerAdapter(customerList);
        recyclerView.setAdapter(adapter);
        getCustomerList(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer, container, false);
        URL = getResources().getString(R.string.API_URL);

        noDataView = v.findViewById(R.id.cNoDataView);

        customerList = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.customerRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new CustomerAdapter(customerList);
        recyclerView.setAdapter(adapter);
        getCustomerList(0);

//        SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.refreshCustomerView);
//        pullToRefresh.setOnRefreshListener(() -> {
//            getCustomerList();
//            adapter.notifyDataSetChanged();
//            pullToRefresh.setRefreshing(true);
//        });

        plusAction = v.findViewById(R.id.cPlusAction);
        plusAction.setOnClickListener(view -> {
            openAddCustomerView();
        });

        searchAction = v.findViewById(R.id.searchAct);
        searchAction.setOnClickListener(view -> {
            openSearchCustomerView();
        });
        return v;
    }

    @SuppressLint("StaticFieldLeak")
    public void getCustomerList(int id) {
        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            private final LottieDialog dialog = new LottieDialog(CustomerFragment.this.getContext());

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
                getCustomerList();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (customerList.isEmpty()) {
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


    public void getCustomerList() {
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
            customerList.clear();
            for (int i = 0; i < count; i++) {
                SoapObject customer = (SoapObject) responseList.getProperty(i);
                int id = GetVariable.getIntFormat(String.valueOf(customer.getProperty("Id")));
                String name, address, phone, username = "";

                try {
                    name = String.valueOf(customer.getProperty("Name"));
                } catch (Exception e) {
                    name = "";
                }
                try {
                    address = String.valueOf(customer.getProperty("Address"));
                } catch (Exception e) {
                    address = "";
                }
                try {
                    phone = String.valueOf(customer.getProperty("Phone"));
                } catch (Exception e) {
                    phone = "";
                }
                try {
                    username = String.valueOf(customer.getProperty("Username"));
                } catch (Exception e) {
                    username = "";
                }

                Customer re = new Customer(id, name, address, phone, username);
                customerList.add(re);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void openAddCustomerView() {
        Intent intent = new Intent(getActivity(), AddNewCustomerActivity.class);
        startActivity(intent);
    }

    public void openSearchCustomerView() {
        Intent intent = new Intent(getActivity(), SearchCustomer.class);
        startActivity(intent);
    }
}
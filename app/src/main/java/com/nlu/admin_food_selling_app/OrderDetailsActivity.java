package com.nlu.admin_food_selling_app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nlu.admin_food_selling_app.adapters.OrderDetailsAdapter;
import com.nlu.admin_food_selling_app.data.model.Customer;
import com.nlu.admin_food_selling_app.data.model.Order;
import com.nlu.admin_food_selling_app.data.model.OrderDetails;
import com.nlu.admin_food_selling_app.helper.GetVariable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {
    ImageButton finishOrderDetails;
    ArrayList<OrderDetails> orderDetailsList;
    TextView orderDetailsId, odCustomerName, odDate, odTotalPrice, odStatus;
    Button odCancelOrder;
    Customer orderCustomer;

    private static String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static String METHOD_NAME = "GetOrderDetails";
    private static String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        URL = getResources().getString(R.string.API_URL);

        orderDetailsList = new ArrayList<>();
        orderCustomer = new Customer();

        orderDetailsId = findViewById(R.id.orderDetailsId);
        odCustomerName = findViewById(R.id.odCustomerName);
        odDate = findViewById(R.id.odDate);
        odTotalPrice = findViewById(R.id.odTotalPrice);
        odStatus = findViewById(R.id.odStatus);
        odCancelOrder = findViewById(R.id.odCancelOrder);

        Intent callerIntent = getIntent();
        Bundle orderBundles = callerIntent.getBundleExtra("orderBundles");
        Order order = orderBundles.getParcelable("order");
        String date = orderBundles.getString("date");
        getOrderDetailsList(order.getId());
        getCustomerTask(order.getCustomerId());
        orderDetailsId.setText(Integer.toString(order.getId()));
        odCustomerName.setText(orderCustomer.getName());
        odDate.setText(date);
        odTotalPrice.setText(Double.toString(order.getTotalPrice()));
        odStatus.setText(getStatus(order.getStatus()));

        RecyclerView orderDetailsView = findViewById(R.id.orderDetailsView);
        orderDetailsView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        orderDetailsView.setAdapter(new OrderDetailsAdapter(orderDetailsList));

        finishOrderDetails = findViewById(R.id.finishOrderDetails);
        finishOrderDetails.setOnClickListener(view -> {
            this.finish();
        });

        odCancelOrder.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Hủy hóa đơn?")
                    .setMessage("Bạn có chắc chắn muốn hủy hóa đơn " + order.getId() + " không?")
                    //update order status here
                    .setPositiveButton("HỦY ĐƠN", (dialogInterface, i) -> this.finish())
                    .setNegativeButton("BỎ QUA", null)
                    .show();
        });
    }

    private String getStatus(int status) {
        String[] items = getResources().getStringArray(R.array.order_status_entries);
        return items[status - 1];
    }

    public void getCustomerTask(int cid) {
        AsyncTask<Integer, Void, Void> getCustomerTask = new AsyncTask<Integer, Void, Void>() {
            private ProgressDialog dialog = new ProgressDialog(OrderDetailsActivity.this);

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Vui lòng chờ");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Integer... integers) {
                getCustomer(cid);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };
        getCustomerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cid);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getOrderDetailsList(int id) {
        AsyncTask<Integer, Void, Void> getOrderDetailsTask = new AsyncTask<Integer, Void, Void>() {
            private ProgressDialog dialog = new ProgressDialog(OrderDetailsActivity.this);

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Vui lòng chờ");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Integer... integers) {
                getOrderDetails(id);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };
        getOrderDetailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
    }

    public void getOrderDetails(int id) {
        try {
            METHOD_NAME = "GetOrderDetails";
            SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);

            request.addProperty("BillId", id);

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
            for (int i = 0; i < count; i++) {
                SoapObject details = (SoapObject) responseList.getProperty(i);
                int foodId = GetVariable.getIntFormat(String.valueOf(details.getProperty("FoodId")));
                int billId = GetVariable.getIntFormat(String.valueOf(details.getProperty("BillId")));
                int amount = GetVariable.getIntFormat(String.valueOf(details.getProperty("Amount")));

                OrderDetails detail = new OrderDetails(foodId, billId, amount);
                orderDetailsList.add(detail);
            }
        } catch (Exception e) {
            System.out.println("get order details error : " + e);
        }
    }

    public void getCustomer(int id) {
        try {
            METHOD_NAME = "GetCustomer";
            SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);

            request.addProperty("id", id);

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
            for (int i = 0; i < count; i++) {
                int cid = GetVariable.getIntFormat(String.valueOf(responseList.getProperty("Id")));
                String name = String.valueOf(responseList.getProperty("Name"));
                String address = "";
                try {
                    address = String.valueOf(responseList.getProperty("Address"));
                } catch (Exception e) {
                    address = "";
                }

                String phone = "";
                try {
                    phone = String.valueOf(responseList.getProperty("Phone"));
                } catch (Exception e) {
                    phone = "";
                }

                String username = "";

                try {
                    username = String.valueOf(responseList.getProperty("Username"));
                } catch (Exception e) {
                    username = "";
                }
                orderCustomer.setId(cid);
                orderCustomer.setName(name);
                orderCustomer.setAddress(address);
                orderCustomer.setUsername(username);
                orderCustomer.setPhoneNumber(phone);
//                orderCustomer = new Customer(cid, name, address, phone, username);
//                re = new Customer(cid, name, address, phone, username);
            }
        } catch (Exception e) {
            System.out.println("get customer error : " + e);
        }
    }
}
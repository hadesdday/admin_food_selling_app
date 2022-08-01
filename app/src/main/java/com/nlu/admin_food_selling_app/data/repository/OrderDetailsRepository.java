package com.nlu.admin_food_selling_app.data.repository;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.nlu.admin_food_selling_app.utils.MarshalDouble;
import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Customer;
import com.nlu.admin_food_selling_app.data.model.OrderDetails;
import com.nlu.admin_food_selling_app.helper.GetVariable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class OrderDetailsRepository {
    Context context;
    private static String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static String METHOD_NAME = "";
    private static String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;

    public OrderDetailsRepository(Context ctx) {
        context = ctx;
        URL = ctx.getResources().getString(R.string.API_URL);
    }

    @SuppressLint("StaticFieldLeak")
    public void cancelOrderTask(int id) {
        AsyncTask<Integer, Boolean, Boolean> cancelOrderTask = new AsyncTask<Integer, Boolean, Boolean>() {
            private final LottieDialog dialog = new LottieDialog(context);

            @Override
            protected void onPreExecute() {
                dialog.setAnimation(R.raw.loading);
                dialog.setAnimationRepeatCount(LottieDialog.INFINITE);
                dialog.setMessage("Vui lòng chờ");
                dialog.setAutoPlayAnimation(true);
                dialog.show();
            }

            @Override
            protected Boolean doInBackground(Integer... integers) {
                return cancelOrder(id);
            }

            @Override
            protected void onPostExecute(Boolean response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (response) {
                    Toasty.success(context, "Hủy hóa đơn thành công", Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.error(context, "Hủy hóa đơn thất bại", Toast.LENGTH_SHORT, true).show();
                }
                ((Activity) context).finish();
            }
        };
        cancelOrderTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
    }

    public boolean cancelOrder(int id) {
        try {
            METHOD_NAME = "CancelOrder";
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

            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            return Boolean.parseBoolean(soapPrimitive.toString());
        } catch (Exception e) {
            System.out.println("cancel order error : " + e);
            return false;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void getCustomerTask(int cid, Customer output) {
        AsyncTask<Integer, Void, Void> getCustomerTask = new AsyncTask<Integer, Void, Void>() {
            private final LottieDialog dialog = new LottieDialog(context);

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
                getCustomer(cid, output);
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

    @SuppressLint("StaticFieldLeak")
    public void getOrderDetailsTask(int id, ArrayList<OrderDetails> output) {
        AsyncTask<Integer, Void, Void> getOrderDetailsTask = new AsyncTask<Integer, Void, Void>() {
            private final LottieDialog dialog = new LottieDialog(context);

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
                getOrderDetails(id, output);
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

    public void getOrderDetails(int id, ArrayList<OrderDetails> output) {
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
                output.add(detail);
            }
        } catch (Exception e) {
            System.out.println("get order details error : " + e);
        }
    }

    public void getCustomer(int id, Customer output) {
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
                output.setId(cid);
                output.setName(name);
                output.setAddress(address);
                output.setUsername(username);
                output.setPhoneNumber(phone);
            }
        } catch (Exception e) {
            System.out.println("get customer error : " + e);
        }
    }
}

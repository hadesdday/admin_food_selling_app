package com.nlu.admin_food_selling_app.data.repository;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Customer;
import com.nlu.admin_food_selling_app.utils.MarshalDouble;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import es.dmoral.toasty.Toasty;

public class CustomerRepository {
    private final Context context;
    private static String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static String METHOD_NAME = "";
    private static String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;

    public CustomerRepository(Context ctx) {
        this.context = ctx;
        URL = ctx.getResources().getString(R.string.API_URL);
    }

    @SuppressLint("StaticFieldLeak")
    public void insertCustomerTask(Customer customer) {
        AsyncTask<Customer, Boolean, Boolean> task = new AsyncTask<Customer, Boolean, Boolean>() {
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
            protected Boolean doInBackground(Customer... customers) {
                return insertCustomer(customer);
            }

            @Override
            protected void onPostExecute(Boolean response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (response) {
                    Toasty.success(context, "Thêm mới khách hàng thành công", Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.error(context, "Thêm mới khách hàng thất bại", Toast.LENGTH_SHORT, true).show();
                }
                ((Activity) context).finish();
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, customer);
    }

    @SuppressLint("StaticFieldLeak")
    public void updateCustomerInformationTask(Customer customer) {
        AsyncTask<Customer, Boolean, Boolean> task = new AsyncTask<Customer, Boolean, Boolean>() {
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
            protected Boolean doInBackground(Customer... customers) {
                return updateCustomerInformation(customer);
            }

            @Override
            protected void onPostExecute(Boolean response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (response) {
                    Toasty.success(context, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.error(context, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT, true).show();
                }
                ((Activity) context).finish();
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, customer);
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteCustomerTask(int id) {
        AsyncTask<Integer, Boolean, Boolean> task = new AsyncTask<Integer, Boolean, Boolean>() {
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
                return deleteCustomer(id);
            }

            @Override
            protected void onPostExecute(Boolean response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (response) {
                    Toasty.success(context, "Xóa khách hàng thành công", Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.error(context, "Xóa khách hàng thất bại", Toast.LENGTH_SHORT, true).show();
                }
                ((Activity) context).finish();
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
    }


    public boolean insertCustomer(Customer c) {
        try {
            METHOD_NAME = "AddCustomer";
            SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
            SoapObject customerObject = new SoapObject(NAME_SPACE, "customer");

            customerObject.addProperty("Name", c.getName());
            customerObject.addProperty("Address", c.getAddress());
            customerObject.addProperty("Phone", c.getPhoneNumber());
            customerObject.addProperty("Username", c.getUsername());

            request.addProperty("customer", customerObject);

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

            boolean responseStatus = Boolean.parseBoolean(soapPrimitive.toString());
            return responseStatus;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteCustomer(int id) {
        try {
            METHOD_NAME = "DeleteCustomer";
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

            boolean responseStatus = Boolean.parseBoolean(soapPrimitive.toString());
            return responseStatus;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateCustomerInformation(Customer c) {
        try {
            METHOD_NAME = "UpdateCustomerInformation";
            SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
            SoapObject customerObject = new SoapObject(NAME_SPACE, "customer");

            customerObject.addProperty("Id", c.getId());
            customerObject.addProperty("Name", c.getName());
            customerObject.addProperty("Address", c.getAddress());
            customerObject.addProperty("Phone", c.getPhoneNumber());
            customerObject.addProperty("Username", c.getUsername());

            request.addProperty("customer", customerObject);

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

            boolean responseStatus = Boolean.parseBoolean(soapPrimitive.toString());
            return responseStatus;
        } catch (Exception e) {
            return false;
        }
    }

}

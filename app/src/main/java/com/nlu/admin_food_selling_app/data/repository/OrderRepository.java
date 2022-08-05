package com.nlu.admin_food_selling_app.data.repository;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Order;
import com.nlu.admin_food_selling_app.utils.MarshalDouble;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import es.dmoral.toasty.Toasty;

public class OrderRepository {
    private final Context context;
    private static String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static String METHOD_NAME = "";
    private static String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;

    public OrderRepository(Context ctx) {
        this.context = ctx;
        URL = ctx.getResources().getString(R.string.API_URL);
    }

    @SuppressLint("StaticFieldLeak")
    public void insertBillTask(Order order) {
        AsyncTask<Order, Boolean, Boolean> task = new AsyncTask<Order, Boolean, Boolean>() {
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
            protected Boolean doInBackground(Order... orders) {
                try {
                    return insertBill(orders[0]);
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response) {
                    Toasty.success(context, "Thêm hóa đơn mới thành công", Toast.LENGTH_SHORT).show();
                    ((Activity) context).finish();
                } else {
                    Toasty.error(context, "Lỗi dữ liệu vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                }
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, order);
    }

    public boolean insertBill(Order order) {
        try {
            METHOD_NAME = "InsertBill";
            SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
            SoapObject orderObject = new SoapObject(NAME_SPACE, "bill");

            orderObject.addProperty("Voucher", order.getVoucher());
            orderObject.addProperty("CustomerId", order.getCustomerId());
            orderObject.addProperty("Price", order.getTotalPrice());
            orderObject.addProperty("PaymentMethod", order.getPaymentMethod());
            orderObject.addProperty("Status", order.getStatus());

            request.addProperty("bill", orderObject);

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

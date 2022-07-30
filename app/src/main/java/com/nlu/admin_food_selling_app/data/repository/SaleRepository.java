package com.nlu.admin_food_selling_app.data.repository;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Sale;
import com.nlu.admin_food_selling_app.utils.MarshalDouble;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import es.dmoral.toasty.Toasty;

public class SaleRepository {
    private Context context;
    private static String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static String METHOD_NAME = "";
    private static String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;

    public SaleRepository(Context ctx) {
        this.context = ctx;
        URL = ctx.getResources().getString(R.string.API_URL);
    }

    @SuppressLint("StaticFieldLeak")
    public void addSaleTask(Sale sale) {
        AsyncTask<Sale, Boolean, Boolean> task = new AsyncTask<Sale, Boolean, Boolean>() {
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
            protected Boolean doInBackground(Sale... sales) {
                return addSale(sale);
            }

            @Override
            protected void onPostExecute(Boolean response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (response) {
                    Toasty.success(context, "Thêm mới sale thành công", Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.error(context, "Thêm mới sale thất bại", Toast.LENGTH_SHORT, true).show();
                }
                ((Activity) context).finish();
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sale);
    }

    @SuppressLint("StaticFieldLeak")
    public void editSaleTask(Sale sale) {
        AsyncTask<Sale, Boolean, Boolean> task = new AsyncTask<Sale, Boolean, Boolean>() {
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
            protected Boolean doInBackground(Sale... sales) {
                return editSaleInformation(sale);
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
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sale);
    }

    @SuppressLint("StaticFieldLeak")
    public void deactivateSaleTask(int id) {
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
                return deactivateSale(id);
            }

            @Override
            protected void onPostExecute(Boolean response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (response) {
                    Toasty.success(context, "Dừng hoạt động thành công", Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.error(context, "Dừng hoạt động thất bại", Toast.LENGTH_SHORT, true).show();
                }
                ((Activity) context).finish();
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
    }

    public boolean addSale(Sale sale) {
        try {
            METHOD_NAME = "AddSale";
            SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
            SoapObject saleObject = new SoapObject(NAME_SPACE, "sale");

            saleObject.addProperty("FoodType", sale.getFoodType());
            saleObject.addProperty("Rate", sale.getRate());
            saleObject.addProperty("EndTime", sale.getEndTime());
            saleObject.addProperty("Description", sale.getDescription());
            saleObject.addProperty("Active", sale.getActive());

            request.addProperty("sale", saleObject);

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

    public boolean deactivateSale(int id) {
        try {
            METHOD_NAME = "DeactivateSale";
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

    public boolean editSaleInformation(Sale sale) {
        try {
            METHOD_NAME = "EditSale";
            SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
            SoapObject saleObject = new SoapObject(NAME_SPACE, "sale");

            saleObject.addProperty("Id", sale.getId());
            saleObject.addProperty("FoodType", sale.getFoodType());
            saleObject.addProperty("Rate", sale.getRate());
            saleObject.addProperty("EndTime", sale.getEndTime());
            saleObject.addProperty("Description", sale.getDescription());
            saleObject.addProperty("Active", sale.getActive());

            request.addProperty("sale", saleObject);

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

package com.nlu.admin_food_selling_app.data.repository;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.nlu.admin_food_selling_app.HomeActivity;
import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.User;
import com.nlu.admin_food_selling_app.utils.MarshalDouble;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import es.dmoral.toasty.Toasty;

public class AuthenticationRepository {
    private final Context context;
    private static String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static String METHOD_NAME = "";
    private static String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;

    public AuthenticationRepository(Context ctx) {
        this.context = ctx;
        URL = ctx.getResources().getString(R.string.API_URL);
    }

    @SuppressLint("StaticFieldLeak")
    public void loginTask(String username, String password) {
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
                return login(username, password);
            }

            @Override
            protected void onPostExecute(Boolean res) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (res) {
                    Toasty.success(context, "Đăng nhập thành công", Toast.LENGTH_SHORT, true).show();
                    Intent intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else {
                    Toasty.error(context, "Đăng nhập thất bại", Toast.LENGTH_SHORT, true).show();
                }
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
    }

    @SuppressLint("StaticFieldLeak")
    public void forgotPasswordTask(String username) {
        AsyncTask<String, Boolean, Boolean> task = new AsyncTask<String, Boolean, Boolean>() {
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
            protected Boolean doInBackground(String... sales) {
                return forgotPassword(username);
            }

            @Override
            protected void onPostExecute(Boolean response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (response) {
                    Toasty.success(context, "Mật khẩu đã được gửi đến email của tài khoản", Toast.LENGTH_SHORT, true).show();
                    ((Activity) context).finish();
                } else {
                    Toasty.error(context, "Không tìm thấy tài khoản", Toast.LENGTH_SHORT, true).show();
                }
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, username);
    }

    public boolean login(String username, String password) {
        User user = new User();
        try {
            METHOD_NAME = "Login";
            SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);

            request.addProperty("username", username);
            request.addProperty("password", password);

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
            String uname = responseList.getProperty("Username").toString();
            String pass = responseList.getProperty("Password").toString();
            String email = responseList.getProperty("Email").toString();
            String role = responseList.getProperty("Role").toString();
            user = new User(uname, pass, email, role);
            if (user != null) {
                SharedPreferences session = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("username", user.getUsername());
                editor.putString("email", user.getEmail());
                editor.commit();
            } else {
                Toasty.error(context, "Đăng nhập thất bại", Toast.LENGTH_SHORT, true).show();
            }
        } catch (Exception e) {
            return false;
        }
        return user != null;
    }

    public boolean forgotPassword(String username) {
        try {
            METHOD_NAME = "ForgotPassword";
            SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);

            request.addProperty("username", username);

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

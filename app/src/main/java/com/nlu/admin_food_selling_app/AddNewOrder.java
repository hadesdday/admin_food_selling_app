package com.nlu.admin_food_selling_app;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.nlu.admin_food_selling_app.data.model.Order;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class AddNewOrder extends AppCompatActivity {
    private static String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "InsertBill";
    private static final String SOAP_ACTION = "http://tempuri.org/InsertBill";

    Spinner aoPaymentMethod, aoOrderStatus;
    ImageView cancelAction;
    TextView submitNewOrder, aoCustomerId, aoTotalPrice, aoVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_order);

        URL = getResources().getString(R.string.API_URL);

        aoPaymentMethod = findViewById(R.id.aoPaymentMethod);
        aoOrderStatus = findViewById(R.id.aoOrderStatus);
        cancelAction = findViewById(R.id.cancelAddOrder);
        submitNewOrder = findViewById(R.id.submitNewOrder);
        aoCustomerId = findViewById(R.id.aoCustomerId);
        aoTotalPrice = findViewById(R.id.aoTotalPrice);
        aoVoucher = findViewById(R.id.aoVoucher);

        String[] paymentMethodItems = getResources().getStringArray(R.array.payment_method_entries);
        String[] statusItems = getResources().getStringArray(R.array.order_status_entries);

        ArrayAdapter<String> paymentMethodAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paymentMethodItems);
        aoPaymentMethod.setAdapter(paymentMethodAdapter);

        ArrayAdapter<String> orderStatusAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, statusItems);
        aoOrderStatus.setAdapter(orderStatusAdapter);

        cancelAction.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Thoát")
                    .setMessage("Thay đổi trên hóa đơn sẽ không được lưu lại. Bạn có chắc chắn muốn thoát?")
                    .setPositiveButton("ĐỒNG Ý", (dialogInterface, i) -> this.finish())
                    .setNegativeButton("BỎ QUA", null)
                    .setIcon(R.drawable.ic_danger)
                    .show();
        });

        submitNewOrder.setOnClickListener(view -> {
            int customerId = Integer.parseInt(String.valueOf(aoCustomerId.getText()));
            double totalPrice = Double.parseDouble(String.valueOf(aoTotalPrice.getText()));
            String paymentMethod = (String) aoPaymentMethod.getSelectedItem();
            int status = aoOrderStatus.getSelectedItemPosition() + 1;
            String voucher = aoVoucher.getText().toString();
            String voucherId = voucher.isEmpty() ? "" : voucher;

            Order order = new Order(customerId, totalPrice, paymentMethod, status, voucherId);

            new AddNewOrderService().execute(order);
        });
    }

    private class AddNewOrderService extends AsyncTask<Order, Boolean, Boolean> {
        private final LottieDialog dialog = new LottieDialog(AddNewOrder.this);

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
                Toast.makeText(AddNewOrder.this, "Thêm hóa đơn mới thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddNewOrder.this, "Lỗi dữ liệu vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean insertBill(Order order) {
        try {
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
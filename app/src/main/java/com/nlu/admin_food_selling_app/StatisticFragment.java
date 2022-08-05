package com.nlu.admin_food_selling_app;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.nlu.admin_food_selling_app.utils.MarshalDouble;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class StatisticFragment extends Fragment {

    private static String URL = "";
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "GetStats";
    private static final String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
    List<Entry> entryList;

    public StatisticFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistic, container, false);
        URL = getResources().getString(R.string.API_URL);
        entryList = new ArrayList<Entry>();
        getStatsTask(v);
        return v;
    }

    @SuppressLint("StaticFieldLeak")
    public void getStatsTask(View v) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            private final LottieDialog dialog = new LottieDialog(StatisticFragment.this.getContext());

            @Override
            protected void onPreExecute() {
                dialog.setAnimation(R.raw.loading);
                dialog.setAnimationRepeatCount(LottieDialog.INFINITE);
                dialog.setMessage("Vui lòng chờ");
                dialog.setAutoPlayAnimation(true);
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                getStats(v);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getStats(View v) {
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

            entryList.clear();
            for (int i = 0; i < count; i++) {
                SoapPrimitive sum = (SoapPrimitive) responseList.getProperty(i);
                double s = Double.parseDouble(sum.toString());
                String e = String.valueOf(s);
                entryList.add(new Entry(i + 1, Float.parseFloat(e)));
            }

            LineChart chart = (LineChart) v.findViewById(R.id.chart);
            LineDataSet dataSet = new LineDataSet(entryList, "Doanh thu");
            LineData lineData = new LineData(dataSet);
            chart.setData(lineData);
            chart.getDescription().setText("Biểu đồ doanh thu mỗi tháng trong năm");
            chart.invalidate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
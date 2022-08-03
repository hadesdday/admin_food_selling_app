package com.nlu.admin_food_selling_app.ui.view.food.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Food;
import com.nlu.admin_food_selling_app.data.model.FoodType;
import com.nlu.admin_food_selling_app.ui.view.food.activity.FilteredFood;
import com.nlu.admin_food_selling_app.ui.view.food.activity.FoodAddActivity;
import com.nlu.admin_food_selling_app.ui.view.food.activity.FoodByFoodIdActivity;
import com.nlu.admin_food_selling_app.ui.view.food.activity.FoodSearchActivity;
import com.nlu.admin_food_selling_app.ui.view.food.activity.FoodTypeAdd;
import com.nlu.admin_food_selling_app.ui.view.food.adapter.FoodAdapter;
import com.nlu.admin_food_selling_app.utils.RecyclerTouchListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FoodFragment extends Fragment {
    SearchView foodSearch;
    ImageButton foodAdd;
    Spinner foodTypeList;
    RecyclerView foodList;
    ArrayList<FoodType> foodTypeArrayList;
    ArrayList<Food> foodArrayList;
    FoodAdapter foodAdapter;

    public FoodFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        new doFoodTypeList().execute();
        new doFoodList().execute();

        foodAdd = view.findViewById(R.id.addButton);
        foodAdd.setOnClickListener(view1 -> click());

        foodSearch = view.findViewById(R.id.foodSearch);
        foodSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(getActivity(), FoodSearchActivity.class);
                intent.putExtra("query", s);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        foodTypeList = view.findViewById(R.id.foodTypeList);
        foodList = view.findViewById(R.id.foodList);

        return view;
    }

    private void click() {
        PopupMenu menu = new PopupMenu(getActivity(), foodAdd);
        menu.inflate(R.menu.add_popup_menu);
        menu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.addFoodType:
                    startActivity(new Intent(getActivity(), FoodTypeAdd.class));
                    break;
                case R.id.addFood:
                    startActivity(new Intent(getActivity(), FoodAddActivity.class));
                    break;
            }

            return true;
        });
        menu.show();
    }

    class doFoodTypeList extends AsyncTask<Void, Void, Void> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        private final String NAMESPACE = getResources().getString(R.string.API_NAMESPACE);
        private final String URL = getResources().getString(R.string.API_URL);
        private final String METHOD = "getFoodType";
        private final String SOAP_ACTION = NAMESPACE + METHOD;

        @Override
        protected Void doInBackground(Void... voids) {
            SoapObject soapObjectRequest = new SoapObject(NAMESPACE, METHOD);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObjectRequest);
            MarshalFloat marshalFloat = new MarshalFloat();
            marshalFloat.register(envelope);

            HttpTransportSE transportSE = new HttpTransportSE(URL);

            try {
                transportSE.call(SOAP_ACTION, envelope);
                SoapObject soapObjectResponse = (SoapObject) envelope.getResponse();
                int foodTypeSize = soapObjectResponse.getPropertyCount();
                foodTypeArrayList = new ArrayList<>();
                foodTypeArrayList.add(new FoodType(0, "Tất cả"));

                for (int i = 0; i < foodTypeSize; i++) {
                    SoapObject item = (SoapObject) soapObjectResponse.getProperty(i);
                    int foodTypeId = Integer.parseInt(item.getProperty("id").toString());
                    String foodTypeName = item.getProperty("name").toString();
                    foodTypeArrayList.add(new FoodType(foodTypeId, foodTypeName));
                }
            } catch (Exception e) {
                exc = true;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Đang tải dữ liệu...");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();

            if (exc)
                Toast.makeText(getActivity(), "Tải dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                makeSpinner();
                exc = false;
            }
        }
    }

    class doFoodList extends AsyncTask<Void, Void, Void> {
        boolean exc = false;
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        private final String NAMESPACE = getResources().getString(R.string.API_NAMESPACE);
        private final String URL = getResources().getString(R.string.API_URL);
        private final String METHOD = "getFood";
        private final String SOAP_ACTION = NAMESPACE + METHOD;

        @Override
        protected Void doInBackground(Void... voids) {
            SoapObject soapObjectRequest = new SoapObject(NAMESPACE, METHOD);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObjectRequest);
            MarshalFloat marshalFloat = new MarshalFloat();
            marshalFloat.register(envelope);

            HttpTransportSE transportSE = new HttpTransportSE(URL);

            try {
                transportSE.call(SOAP_ACTION, envelope);
                SoapObject soapObjectResponse = (SoapObject) envelope.getResponse();
                int foodSize = soapObjectResponse.getPropertyCount();
                foodArrayList = new ArrayList<>();

                for (int i = 0; i < foodSize; i++) {
                    SoapObject item = (SoapObject) soapObjectResponse.getProperty(i);
                    int foodId = Integer.parseInt(item.getProperty("id").toString());
                    int foodTypeId = Integer.parseInt(item.getProperty("food_type").toString());
                    String foodName = item.getProperty("name").toString();
                    String foodImage = item.getProperty("image_url").toString();
                    String foodDescription = item.getProperty("description").toString();
                    int foodPrice = Integer.parseInt(item.getProperty("price").toString());
                    foodArrayList.add(new Food(foodId, foodName, foodImage, foodDescription, foodPrice, foodTypeId));
                }
            } catch (Exception e) {
                exc = true;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Đang tải dữ liệu...");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();

            if (exc)
                Toast.makeText(getActivity(), "Tải dữ liệu thất bại. :(", Toast.LENGTH_LONG).show();
            else {
                makeRecyclerView();
                exc = false;
            }
        }
    }

    private void makeRecyclerView() {
        foodAdapter = new FoodAdapter(getActivity(), foodArrayList);
        foodList.setHasFixedSize(true);
        foodList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        foodList.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();

        foodList.addOnItemTouchListener
                (new RecyclerTouchListener(getActivity(), foodList, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onTouch(View view, int position) {
                        Intent in = new Intent(getActivity(), FoodByFoodIdActivity.class);
                        in.putExtra("food", foodArrayList.get(position));
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                    }

                    @Override
                    public void onHold(View view, int position) {
                        DecimalFormat format = new DecimalFormat("###,###,###");
                        Toast.makeText(getContext(),
                                foodArrayList.get(position).getFoodName() +
                                        " - " + format.format(foodArrayList.get(position).getFoodPrice()) + "đ",
                                Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void makeSpinner() {
        ArrayAdapter<FoodType> foodTypeAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, foodTypeArrayList);
        foodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodTypeList.setAdapter(foodTypeAdapter);
        foodTypeAdapter.notifyDataSetChanged();

        foodTypeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    Intent intent = new Intent(getActivity(), FilteredFood.class);
                    FoodType item = foodTypeAdapter.getItem(i);
                    intent.putExtra("foodtype", item);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
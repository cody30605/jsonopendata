package tw.cody.NewTaipeiParking;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class t5新北市路外公共停車場資訊 extends AppCompatActivity {

    private ListView listView;
    private LinkedList<HashMap<String,String>> data;
    private LinkedList<HashMap<String,String>> backData;
    private myAdapter adapter;
    private SearchView searchView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t5);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("loading...");

        init();
        loadingData();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }




    private void init() {
        listView = findViewById(R.id.listView5);
        data = new LinkedList<>();
        adapter = new myAdapter();
        listView.setAdapter(adapter);
        searchView = findViewById(R.id.search5);
        backData = data;
    }

    private void loadingData() {
        progressDialog.show();
        String uri = "https://data.ntpc.gov.tw/api/datasets/B1464EF0-9C7C-4A6F-ABF7-6BDF32847E68/json?page=0&size=1185";
        StringRequest request = new StringRequest(uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("cody","error:"+error);
            }
        });
        queue.queue.add(request);
    }

    private void parseJson(String g) {
        try {
            JSONArray array = new JSONArray(g);
            for (int i=0;i<array.length();i++) {
                HashMap<String,String> hashMap = new HashMap<>();
                JSONObject object = array.getJSONObject(i);
                hashMap.put("area",object.getString("area"));
                hashMap.put("name",object.getString("name"));
//                hashMap.put("type",object.getString("type"));
                hashMap.put("summary",object.getString("summary"));
                hashMap.put("address",object.getString("address"));
                hashMap.put("tel",object.getString("tel"));
                hashMap.put("payEx",object.getString("payEx"));
                hashMap.put("serviceTime",object.getString("serviceTime"));
                hashMap.put("totalCar",object.getString("totalCar"));
                hashMap.put("totalMotor",object.getString("totalMotor"));
                hashMap.put("totalBike",object.getString("totalBike"));
                data.add(hashMap);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
        progressDialog.dismiss();
    }

    private class myAdapter extends BaseAdapter implements Filterable {

        @Override
        public int getCount() {
//            return 0;
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(t5新北市路外公共停車場資訊.this);
            View view = inflater.inflate(R.layout.t5_item,null);

            TextView area = view.findViewById(R.id.area);
            area.setText("區域:"+data.get(position).get("area"));

            TextView name = view.findViewById(R.id.name);
            name.setText("停車場名稱:"+data.get(position).get("name"));

//            TextView type = view.findViewById(R.id.type);
//            type.setText("1：剩餘車位數 2：靜態停車場資料:"+data.get(position).get("type"));

            TextView summary = view.findViewById(R.id.summary);
            summary.setText("停車場概況:"+data.get(position).get("summary"));

            TextView address = view.findViewById(R.id.address);
            address.setText("停車場地址:"+data.get(position).get("address"));

            TextView tel = view.findViewById(R.id.tel);
            tel.setText("停車場電話:"+data.get(position).get("tel"));

            TextView payEx = view.findViewById(R.id.payEx);
            payEx.setText("停車場收費資訊:"+data.get(position).get("payEx"));

            TextView serviceTime = view.findViewById(R.id.serviceTime);
            serviceTime.setText("開放時間:"+data.get(position).get("serviceTime"));

            TextView totalCar = view.findViewById(R.id.totalCar);
            totalCar.setText("汽車總車位數:"+data.get(position).get("totalCar"));

            TextView totalMotor = view.findViewById(R.id.totalMotor);
            totalMotor.setText("機車總格位數:"+data.get(position).get("totalMotor"));

            TextView totalBike = view.findViewById(R.id.totalBike);
            totalBike.setText("腳踏車總車架數:"+data.get(position).get("totalBike"));

            return view;
//            return null;
        }

        @Override
        public Filter getFilter() {
            myFilter filter1 = new myFilter();
            if (filter1 == null) {
            }
            return filter1;
        }

        private class myFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                LinkedList<HashMap<String,String>> data1;
                if (TextUtils.isEmpty(charSequence)) {
                    data1 = backData;
                } else {
                    data1 = new LinkedList<>();
                    for (HashMap<String,String> hashMap : backData) {

                        String g = hashMap.toString();
                        if (g.contains(charSequence)) {
                            Log.v("brad","value"+searchView.getQuery().toString());
                            data1.add(hashMap);
                        }
                    }
                }
                data = data1;
                filterResults.values = data1;
                filterResults.count = data1.size();
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if(filterResults.count > 0) {
                    notifyDataSetChanged();
                    Log.v("brad","數據已改變");
                } else {
                    notifyDataSetInvalidated();
                    Log.v("brad","數據已失效");
                }
            }
        }

    }
}

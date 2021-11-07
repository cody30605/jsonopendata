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

public class t6新北市路邊停車場身心障礙停車格 extends AppCompatActivity {

    private ListView listView;
    private LinkedList<HashMap<String,String>> data;
    private LinkedList<HashMap<String,String>> backData;
    private myAdapter adapter;
    private SearchView searchView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t6);

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
        listView = findViewById(R.id.listView6);
        data = new LinkedList<>();
        adapter = new myAdapter();
        listView.setAdapter(adapter);
        searchView = findViewById(R.id.search6);
        backData = data;
    }

    private void loadingData() {
        progressDialog.show();
        String uri = "https://data.ntpc.gov.tw/api/datasets/1E548C60-0B04-4507-BF60-F3BE651DB642/json?page=0&size=2341";
        StringRequest request = new StringRequest(uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                hashMap.put("zone",object.getString("zone"));
                hashMap.put("Address",object.getString("Address"));
                hashMap.put("Vehicle_classification",object.getString("Vehicle_classification"));
                hashMap.put("Charged",object.getString("Charged"));
                hashMap.put("Disabled_parking_sign",object.getString("Disabled_parking_sign"));
                hashMap.put("Disabled_parking_lot",object.getString("Disabled_parking_lot"));
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
            LayoutInflater inflater = LayoutInflater.from(t6新北市路邊停車場身心障礙停車格.this);
            View view = inflater.inflate(R.layout.t6_item,null);

            TextView zone = view.findViewById(R.id.zone);
            zone.setText("區域:"+data.get(position).get("zone"));

            TextView Address = view.findViewById(R.id.Address);
            Address.setText("設置地點:"+data.get(position).get("Address"));

            TextView Vehicle_classification = view.findViewById(R.id.Vehicle_classification);
            Vehicle_classification.setText("車種:"+data.get(position).get("Vehicle_classification"));

            TextView Charged = view.findViewById(R.id.Charged);
            Charged.setText("是否收費:"+data.get(position).get("Charged"));

            TextView Disabled_parking_sign = view.findViewById(R.id.Disabled_parking_sign);
            Disabled_parking_sign.setText("是否有身障標誌:"+data.get(position).get("Disabled_parking_sign"));

            TextView Disabled_parking_lot = view.findViewById(R.id.Disabled_parking_lot);
            Disabled_parking_lot.setText("是否有身障標線:"+data.get(position).get("Disabled_parking_lot"));

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

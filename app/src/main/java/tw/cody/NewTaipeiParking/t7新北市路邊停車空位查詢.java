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

public class t7新北市路邊停車空位查詢 extends AppCompatActivity {

    private ListView listView;
    private LinkedList<HashMap<String,String>> data;
    private LinkedList<HashMap<String,String>> backData;
    private myAdapter adapter;
    private SearchView searchView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t7);

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
        listView = findViewById(R.id.listView7);
        data = new LinkedList<>();
        adapter = new myAdapter();
        listView.setAdapter(adapter);
        searchView = findViewById(R.id.search7);
        backData = data;
    }

    private void loadingData() {
        progressDialog.show();
        String uri = "https://data.ntpc.gov.tw/api/datasets/54A507C4-C038-41B5-BF60-BBECB9D052C6/json?page=0&size=31729";
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
                hashMap.put("ID",object.getString("ID"));
                hashMap.put("CELLID",object.getString("CELLID"));
                hashMap.put("NAME",object.getString("NAME"));
                hashMap.put("DAY",object.getString("DAY"));
                hashMap.put("HOUR",object.getString("HOUR"));
                hashMap.put("PAY",object.getString("PAY"));
                hashMap.put("PAYCASH",object.getString("PAYCASH"));
//                hashMap.put("ROADID",object.getString("ROADID"));
                hashMap.put("ROADNAME",object.getString("ROADNAME"));
                hashMap.put("CELLSTATUS",object.getString("CELLSTATUS"));
                hashMap.put("ISNOWCASH",object.getString("ISNOWCASH"));
//                hashMap.put("ParkingStatus",object.getString("ParkingStatus"));
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
            LayoutInflater inflater = LayoutInflater.from(t7新北市路邊停車空位查詢.this);
            View view = inflater.inflate(R.layout.t7_item,null);

            TextView ID = view.findViewById(R.id.ID);
            ID.setText("車格序號:"+data.get(position).get("ID"));

            TextView CELLID = view.findViewById(R.id.CELLID);
            CELLID.setText("車格編號:"+data.get(position).get("CELLID"));

            TextView NAME = view.findViewById(R.id.NAME);
            NAME.setText("車格類型:"+data.get(position).get("NAME"));

            TextView DAY = view.findViewById(R.id.DAY);
            DAY.setText("收費天:"+data.get(position).get("DAY"));

            TextView HOUR = view.findViewById(R.id.HOUR);
            HOUR.setText("收費時段:"+data.get(position).get("HOUR"));

            TextView PAY = view.findViewById(R.id.PAY);
            PAY.setText("收費形式:"+data.get(position).get("PAY"));

            TextView PAYCASH = view.findViewById(R.id.PAYCASH);
            PAYCASH.setText("費率:"+data.get(position).get("PAYCASH"));

//            TextView ROADID = view.findViewById(R.id.ROADID);
//            ROADID.setText("路段代碼:"+data.get(position).get("ROADID"));

            TextView ROADNAME = view.findViewById(R.id.ROADNAME);
            ROADNAME.setText("路段名稱:"+data.get(position).get("ROADNAME"));

            TextView CELLSTATUS = view.findViewById(R.id.CELLSTATUS);
            CELLSTATUS.setText("車格狀態判斷:"+data.get(position).get("CELLSTATUS"));

            TextView ISNOWCASH = view.findViewById(R.id.ISNOWCASH);
            ISNOWCASH.setText("收費時段判斷:"+data.get(position).get("ISNOWCASH"));

//            TextView ParkingStatus = view.findViewById(R.id.ParkingStatus);
//            ParkingStatus.setText("車格狀態顯示:"+data.get(position).get("ParkingStatus"));

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

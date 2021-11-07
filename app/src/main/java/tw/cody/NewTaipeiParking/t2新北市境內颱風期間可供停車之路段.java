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

public class t2新北市境內颱風期間可供停車之路段 extends AppCompatActivity {

    private ListView listView;
    private LinkedList<HashMap<String,String>> data;
    private LinkedList<HashMap<String,String>> backData;
    private myAdapter adapter;
    private SearchView searchView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t2);

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
        listView = findViewById(R.id.listView);
        data = new LinkedList<>();
        adapter = new myAdapter();
        listView.setAdapter(adapter);
        searchView = findViewById(R.id.search2);
        backData = data;
    }

    private void loadingData() {
        progressDialog.show();
        String uri = "https://data.ntpc.gov.tw/api/datasets/E3C0E87E-DCDB-41FC-9F21-38402DDE2AD9/json?page=0&size=28";
        StringRequest request = new StringRequest(uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response);
                Log.v("cody","response:"+response);
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
                hashMap.put("road",object.getString("road"));
                hashMap.put("oringin",object.getString("oringin"));
                hashMap.put("destination",object.getString("destination"));
                data.add(hashMap);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.v("cody","e:"+e);
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
            LayoutInflater inflater = LayoutInflater.from(t2新北市境內颱風期間可供停車之路段.this);
            View view = inflater.inflate(R.layout.t2_item,null);

            TextView road = view.findViewById(R.id.road);
            road.setText("開放停車路段:"+data.get(position).get("road"));

            TextView oringin = view.findViewById(R.id.oringin);
            oringin.setText("起點:"+data.get(position).get("oringin"));

            TextView destination = view.findViewById(R.id.destination);
            destination.setText("迄點:"+data.get(position).get("destination"));

//            return null;
            return view;
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

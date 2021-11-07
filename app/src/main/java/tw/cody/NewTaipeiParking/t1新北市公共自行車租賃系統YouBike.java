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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class t1新北市公共自行車租賃系統YouBike extends AppCompatActivity {

    private LinkedList<HashMap<String,String>> data;
    private LinkedList<HashMap<String,String>> backData;
    private myAdapter adapter;
    private ListView listView;
    private SearchView searchView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t1);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("loading...");


        loadingData();
        init();
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
        data = new LinkedList<>();
        adapter = new myAdapter();
        listView = findViewById(R.id.listview);
        searchView = findViewById(R.id.search1);
        backData = data;
        listView.setAdapter(adapter);
    }

    private void loadingData() {
        progressDialog.show();
        String uri = "https://data.ntpc.gov.tw/api/datasets/71CD1490-A2DF-4198-BEF1-318479775E8A/json?page=0&size=667";
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
                hashMap.put("sna",object.getString("sna"));
                hashMap.put("tot",object.getString("tot"));
                hashMap.put("sbi",object.getString("sbi"));
                hashMap.put("sarea",object.getString("sarea"));
                hashMap.put("ar",object.getString("ar"));
                hashMap.put("sareaen",object.getString("sareaen"));
                hashMap.put("snaen",object.getString("snaen"));
                hashMap.put("aren",object.getString("aren"));
                hashMap.put("bemp",object.getString("bemp"));
                hashMap.put("act",object.getString("act"));
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
            LayoutInflater inflater = LayoutInflater.from(t1新北市公共自行車租賃系統YouBike.this); //LayoutInflater 動態載入頁面
            View view = inflater.inflate(R.layout.t1_item,null);
//            return null;

            TextView sna = view.findViewById(R.id.sna);
            sna.setText("名稱:"+data.get(position).get("sna"));

            TextView ar = view.findViewById(R.id.ar);
            ar.setText("地址:"+data.get(position).get("ar"));

            TextView snaen = view.findViewById(R.id.snaen);
            snaen.setText("name:"+data.get(position).get("snaen"));

            TextView aren = view.findViewById(R.id.aren);
            aren.setText("address:"+data.get(position).get("aren"));

            TextView sarea = view.findViewById(R.id.sarea);
            sarea.setText("區域:"+data.get(position).get("sarea"));

            TextView sareaen = view.findViewById(R.id.sareaen);
            sareaen.setText("area:"+data.get(position).get("sareaen"));

            TextView tot = view.findViewById(R.id.tot);
            tot.setText("總停車格:"+data.get(position).get("tot"));

            TextView sbi = view.findViewById(R.id.sbi);
            sbi.setText("可借車位數:"+data.get(position).get("sbi"));

            TextView bemp = view.findViewById(R.id.bemp);
            bemp.setText("可還車位數:"+data.get(position).get("bemp"));

            TextView act = view.findViewById(R.id.act);
            act.setText("場站是否暫停營運:"+data.get(position).get("act"));

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
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }
    }
}

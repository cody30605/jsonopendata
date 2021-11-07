package tw.cody.NewTaipeiParking;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class queue extends Application {
    public static RequestQueue queue;  //queue設為單一物件全域使用,避免浪費資源

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(this);
    }
}

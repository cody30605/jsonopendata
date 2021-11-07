package tw.cody.NewTaipeiParking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void t1(View view) {
        Intent intent = new Intent(MainActivity.this,t1新北市公共自行車租賃系統YouBike.class);
        startActivity(intent);
    }

    public void t2(View view) {
        Intent intent = new Intent(MainActivity.this,t2新北市境內颱風期間可供停車之路段.class);
        startActivity(intent);
    }

    public void t3(View view) {
        Intent intent = new Intent(MainActivity.this,t3新北市婦幼停車位.class);
        startActivity(intent);
    }
    public void t4(View view) {
        Intent intent = new Intent(MainActivity.this, t4新北市機車停車資訊.class);
        startActivity(intent);
    }
    public void t5(View view) {
        Intent intent = new Intent(MainActivity.this,t5新北市路外公共停車場資訊.class);
        startActivity(intent);
    }
    public void t6(View view) {
        Intent intent = new Intent(MainActivity.this,t6新北市路邊停車場身心障礙停車格.class);
        startActivity(intent);
    }
    public void t7(View view) {
        Intent intent = new Intent(MainActivity.this,t7新北市路邊停車空位查詢.class);
        startActivity(intent);
    }
    public void t8(View view) {
        Intent intent = new Intent(MainActivity.this,t8新北市路邊收費停車場.class);
        startActivity(intent);
    }
    public void t9(View view) {
        Intent intent = new Intent(MainActivity.this,t9新北市路邊收費停車場停車欠費查詢.class);
        startActivity(intent);
    }





}
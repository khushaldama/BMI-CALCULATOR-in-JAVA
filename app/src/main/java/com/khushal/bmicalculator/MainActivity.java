package com.khushal.bmicalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                createPersonalizedAd();

            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        EditText edweg,edhei;
        TextView txtinter, txtresult;
        Button btnresult, btnreset;

        edweg = (EditText) findViewById(R.id.edweg);
        edhei = (EditText) findViewById(R.id.edhei);

        txtinter = (TextView) findViewById(R.id.txtinter);
        txtresult = (TextView) findViewById(R.id.txtresult);

        btnresult = (Button) findViewById(R.id.btnresult);
        btnreset = (Button) findViewById(R.id.btnreset);

        btnresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("---ADMOB", "The interstitial ad wasn't ready yet.");
                }

                String strweg = edweg.getText().toString();
                String strhei = edhei.getText().toString();

                if (strweg.equals("")){
                    edweg.setError("Please Enter Your Weight");
                    edweg.requestFocus();
                    return;
                }
                if (strhei.equals("")){
                    edhei.setError("Please Enter Your Height");
                    edhei.requestFocus();
                    return;
                }

                float weight = Float.parseFloat(strweg);
                float height = Float.parseFloat(strhei)/100;

                float bmiValue = BMICALCULATOR(weight,height);
                txtinter.setText(interpretBMI(bmiValue));
                txtresult.setText("BMI= "+bmiValue);
            }
        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("---ADMOB", "The interstitial ad wasn't ready yet.");
                }

                edhei.setText("");
                edweg.setText("");
                txtinter.setText("");
                txtresult.setText("");
            }
        });
    }
    public float BMICALCULATOR(float weight, float height){
        return weight / (height * height);

    }
    public String interpretBMI(float bmiValue){
        if (bmiValue < 16){
            return "Severely Underweight";
        }
        else if (bmiValue < 18.5){
            return "Underweight";
        }
        else if (bmiValue < 24.9){
            return "Normal";
        }
        else if (bmiValue < 29.9){
            return "Overweight";
        }
        else if (bmiValue < 34.9){
            return "Obeseweight";
        }
        else if (bmiValue < 39.9){
            return "Severely Obeseweight";
        }
        else
            return "Morbidly Obeseweight";
    }

    public void createPersonalizedAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        createInterstitialAd(adRequest);
    }


    public void createInterstitialAd(AdRequest adRequest){

        InterstitialAd.load(this,"ca-app-pub-6684868510417420/7215604216", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i("---ADMOB", "onAdLoaded");

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("---ADMOB", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("---ADMOB", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("---ADMOB", "The ad was shown.");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i("---ADMOB", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }
}
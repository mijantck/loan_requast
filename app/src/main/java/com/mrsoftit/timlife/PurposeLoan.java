package com.mrsoftit.timlife;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.material.textfield.TextInputEditText;

public class PurposeLoan extends AppCompatActivity {

    public int counter;
    ProgressDialog progressDialog;
    private Button purposeOfLoan;
    TextView alreadyLoan;
    TextInputEditText  enter_purpose_Edit_text_Box;

    private AdView adView;
    private final String TAG = MainActivity.class.getSimpleName();
    private InterstitialAd interstitialAd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purpose_loan);

        purposeOfLoan = findViewById(R.id.purposeOfLoan);
        alreadyLoan = findViewById(R.id.alreadyLoan);
        enter_purpose_Edit_text_Box = findViewById(R.id.enter_purpose_Edit_text_Box);

        adView = new AdView(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);
        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        // Add the ad view to your activity layout
        adContainer.addView(adView);
        // Request an ad
        adView.loadAd();


        interstitialAd = new InterstitialAd(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };
        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());



        purposeOfLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (interstitialAd.isAdLoaded()){
                    interstitialAd.show();
                    showAdWithDelay();
                }

                String p = enter_purpose_Edit_text_Box.getText().toString();

                if (p.isEmpty()){
                    if (p.length() == 0){
                        enter_purpose_Edit_text_Box.setError("Enter a Valid Purpose");
                    }
                    return;
                }
                progressDialog= new ProgressDialog(PurposeLoan.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                new CountDownTimer(2000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        counter++;
                    }
                    @Override
                    public void onFinish() {
                        progressDialog.dismiss();
                        startActivity(new Intent(PurposeLoan.this,LoanDetails.class));
                    }
                }.start();

            }
        });
        alreadyLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog= new ProgressDialog(PurposeLoan.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                new CountDownTimer(2000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        counter++;
                    }
                    @Override
                    public void onFinish() {
                        progressDialog.dismiss();
                        startActivity(new Intent(PurposeLoan.this,AlreadyApplied.class));
                    }
                }.start();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (interstitialAd.isAdLoaded()){
            interstitialAd.show();
            showAdWithDelay();
        }
    }
    private void showAdWithDelay() {
        /**
         * Here is an example for displaying the ad with delay;
         * Please do not copy the Handler into your project
         */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Check if interstitialAd has been loaded successfully
                if(interstitialAd == null || !interstitialAd.isAdLoaded()) {
                    return;
                }
                // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
                if(interstitialAd.isAdInvalidated()) {
                    return;
                }
                // Show the ad
                interstitialAd.show();
            }
        }, 1000 * 60 * 15); // Show the ad after 15 minutes
    }
    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

}
package com.mac.task1.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mac.task1.MainActivity
import com.mac.task1.databinding.ActivitySplashScreenActivitiesBinding


class Splash_Screen_Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenActivitiesBinding
    private val time = 1000
    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "ads"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenActivitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Splash screen"
        MobileAds.initialize(
            this
        ) { }


        ReadyAds()


    }

    private fun directToMainActivity() {
        Handler().postDelayed({
            val intent = Intent(this@Splash_Screen_Activity, MainActivity::class.java)
            startActivity(intent)
            finish()
            ShowInitAd()
        }, time.toLong())
    }


    private fun ReadyAds() {
        val adRequest: AdRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {

                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                    directToMainActivity()

                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.d(TAG, "Ad was clicked.")
                            }

                            override fun onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad dismissed fullscreen content.")
                                mInterstitialAd = null
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when ad fails to show.
                                Log.e(TAG, "Ad failed to show fullscreen content.")
                                mInterstitialAd = null
                            }

                            override fun onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d(TAG, "Ad recorded an impression.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad showed fullscreen content.")
                            }
                        }


                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Log.d(TAG, loadAdError.toString())
                    mInterstitialAd = null
                }
            })


    }

    private fun ShowInitAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show(this)
        } else {
            Handler().postDelayed({
                val intent = Intent(this@Splash_Screen_Activity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, time.toLong())
        }
    }


}
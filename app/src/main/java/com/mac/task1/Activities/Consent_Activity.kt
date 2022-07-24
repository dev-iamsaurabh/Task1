package com.mac.task1.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.consent.*
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentRequestParameters
import com.mac.task1.databinding.ActivityConsentBinding
import java.net.MalformedURLException
import java.net.URL


class Consent_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityConsentBinding
    lateinit var form:ConsentForm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Consent Activity"
       status()

        binding.splashBtn.setOnClickListener {
            startActivity(Intent(this,Splash_Screen_Activity::class.java))
        }




    }


    private fun status() {
        ConsentInformation.getInstance(this)
            .addTestDevice("303f6b81-129a-4f48-a6f0-1a86313a7ce0")
        ConsentInformation.getInstance(this).debugGeography =
            DebugGeography.DEBUG_GEOGRAPHY_EEA
        val consentInformation = ConsentInformation.getInstance(this)
        val publisherIds = arrayOf("pub-7170452140542835")
        consentInformation.requestConsentInfoUpdate(
            publisherIds,
            object : ConsentInfoUpdateListener {
                override fun onConsentInfoUpdated(consentStatus: ConsentStatus?) {
                    // User's consent status successfully updated.
                    when(consentStatus){
                        ConsentStatus.UNKNOWN->{
                            Toast.makeText(this@Consent_Activity, "unknown", Toast.LENGTH_SHORT).show()
                            popup()
                        }
                        ConsentStatus.PERSONALIZED->{
                            Toast.makeText(this@Consent_Activity, "pers", Toast.LENGTH_SHORT).show()

                            popup()
                        }
                        ConsentStatus.NON_PERSONALIZED->{
                            Toast.makeText(this@Consent_Activity, "nonperson", Toast.LENGTH_SHORT).show()

                            popup()

                        }
                    }
                }

                override fun onFailedToUpdateConsentInfo(errorDescription: String) {
                    // User's consent status failed to update.
                }
            })
    }


    private fun popup() {
        Toast.makeText(this@Consent_Activity, "working", Toast.LENGTH_SHORT).show()

        var privacyUrl: URL? = null
        try {
            privacyUrl = URL("https://stripe.com/en-in/privacy")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            // Handle error.
        }
        form = ConsentForm.Builder(this, privacyUrl)
            .withListener(object : ConsentFormListener() {
                override fun onConsentFormLoaded() {

                    // Consent form loaded successfully.
                    Toast.makeText(this@Consent_Activity, "loaded", Toast.LENGTH_SHORT).show()
                    form.show()
                }

                override fun onConsentFormOpened() {
                    // Consent form was displayed.
                    Toast.makeText(this@Consent_Activity, "opened", Toast.LENGTH_SHORT).show()

                }

                override fun onConsentFormClosed(
                    consentStatus: ConsentStatus?, userPrefersAdFree: Boolean?
                ) {
                    Toast.makeText(this@Consent_Activity, "closed", Toast.LENGTH_SHORT).show()


                }

                override fun onConsentFormError(errorDescription: String) {
                    // Consent form error.
                    Toast.makeText(this@Consent_Activity, errorDescription, Toast.LENGTH_SHORT).show()

                }
            })
            .withPersonalizedAdsOption()
            .withNonPersonalizedAdsOption()
            .withAdFreeOption()
            .build()
        form.load()
    }
}
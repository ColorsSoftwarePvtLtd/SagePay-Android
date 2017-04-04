package com.colors.android.colorsprojectsagepay;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sagepay.sdk.api.util.FormApiEncryptionHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    WebView web;
    String link;
    public static final String FORM_REGISTRATION_URL = "https://test.sagepay.com/gateway/service/vspform-register.vsp";
    public static final String QUERY_STRING_BEGIN = "?";
    private static final String DELIMITER = "&";
   public  FormApiEncryptionHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String registration = generateRegistrationString();

        final FormApiEncryptionHelper helper = new FormApiEncryptionHelper();
        String cryptString = helper.encrypt("UTF-8", "TPjs72eMz5qBnaTa", registration);
        link = createRegistrationLink(cryptString, "protxross");

        web = (WebView) findViewById(R.id.webv);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(link);
        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                String success = "https://example.com/success?crypt=";
                String fail = "https://example.com/failure?crypt=";
                Log.d(TAG, "onPageStarted: " + url);
                String url1 = url;
                if (url1.contains(fail)) {
                    url1 = url1.replace(fail, "");
                    if (url1.startsWith("@")) {
                        try {
                            String succesOrfail = helper.decrypt("UTF-8", "TPjs72eMz5qBnaTa", url1.toUpperCase());
                            Log.d(TAG, "onPageFinished:fail " + succesOrfail);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (url1.contains(success)) {
                    url1 = url1.replace(success, "");
                    if (url1.startsWith("@")) {
                        try {
                            String succesOrfail = helper.decrypt("UTF-8", "TPjs72eMz5qBnaTa", url1.toUpperCase());
                            Log.d(TAG, "onPageFinished:succes " + succesOrfail);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


            }


        });

    }

    public String generateRegistrationString() {
         String name = "VendorTxCode=TxCode-3417-00002&Amount=36.95&Currency=GBP&Description=description&CustomerName=Fname&Surname&CustomerEMail=gyyyyy@gmail.com&VendorEMail=yyyyyy@gmail.com&BillingSurname=Surname&BillingFirstnames=Fname&BillingAddress1=BillAddress Line 1&BillingCity=BillCity&BillingPostCode=W1A 1BL&BillingCountry=GB&BillingPhone=447933000000&DeliveryFirstnames=Fname&DeliverySurname=Surname&DeliveryAddress1=BillAddress Line 1&DeliveryCity=BillCity&DeliveryPostCode=W1A 1BL&DeliveryCountry=GB&DeliveryPhone=447933000000&SuccessURL=https://example.com/success&FailureURL=https://example.com/failure";
        return name;
       }

    public static String createRegistrationLink(String cryptString, String vendor) {
        StringBuilder linkBuilder = new StringBuilder();
        linkBuilder.append(FORM_REGISTRATION_URL);
        linkBuilder.append(QUERY_STRING_BEGIN);
        linkBuilder.append("VPSProtocol=3.00" + DELIMITER);
        linkBuilder.append("TxType=PAYMENT" + DELIMITER);
        linkBuilder.append("Vendor=" + vendor + DELIMITER);
        linkBuilder.append("Crypt=" + cryptString);

        return linkBuilder.toString();
    }

}

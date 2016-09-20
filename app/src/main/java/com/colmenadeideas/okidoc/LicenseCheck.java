package com.colmenadeideas.okidoc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.widget.Toast;

import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.ServerManagedPolicy;
import com.google.android.vending.licensing.AESObfuscator;

public class LicenseCheck extends Activity {
    private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
        @Override
        public void allow(int reason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
        // Should allow user access.
            startMainActivity();

        }

        @Override
        public void applicationError(int errorCode) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // This is a polite way of saying the developer made a mistake
            // while setting up or calling the license checker library.
            // Please examine the error code and fix the error.
            toast("Error: " + errorCode);
            startMainActivity();

        }

        @Override
        public void dontAllow(int reason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }

            // Should not allow access. In most cases, the app should assume
            // the user has access unless it encounters this. If it does,
            // the app should inform the user of their unlicensed ways
            // and then either shut down the app or limit the user to a
            // restricted set of features.
            // In this example, we show a dialog that takes the user to Market.
            showDialog(0);
        }


    }

    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAslXg37ggoPSzYe4BjZ9u36N3fFbTowbj0PWvxHzr6X5wj/AYPz5RoLrOWn72uEg56BmhUukUo/IgTlFD1Awfc4jBpb1NAQJCxe+xqXqjxTpnL8+Yh0lyedqk+/LpBby2qeB9ccAQ7+tixY6FqLjF3E+0qu8ZPBGN+3YQXJB1Wuv/2UFxtC0B3PV/4uGbTo2/pbSHECni5GA9ArzvV7JHB0OsAilFLapXuWFpNcx44Y+rHWLPTcyJHZiXeqLDldhYn9fHMLkRj7KB6LcLGilOXQCuCkWjIqhMlM3pa5ZVmOb1VGpA95ViglXffgdiME3vsgL7G6UeUQn7izUhzQXi9wIDAQAB";
    private static final byte[] SALT = new byte[]{27, 55, 91, 92, 25, 33, 05, 58, 21, 73, 16, 49, 17, 37, 54, 45, 10, 57, 24, 19};
    private LicenseChecker mChecker;

// A handler on the UI thread.

    private LicenseCheckerCallback mLicenseCheckerCallback;

    private void doCheck() {
        mChecker.checkAccess(mLicenseCheckerCallback);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// Try to use more data here. ANDROID_ID is a single point of attack.
        String deviceId = Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);

        mLicenseCheckerCallback = new MyLicenseCheckerCallback();
        mChecker = new LicenseChecker(this, new ServerManagedPolicy(this,
                new AESObfuscator(SALT, getPackageName(), deviceId)),
                BASE64_PUBLIC_KEY);
        doCheck();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
// We have only one dialog.
        return new AlertDialog.Builder(this)
                .setTitle("Application Not Licensed")
                .setCancelable(false)
                .setMessage(
                        "This application is not licensed. Please purchase it from Android Market")
                .setPositiveButton("Buy App",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent marketIntent = new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("http://market.android.com/details?id="
                                                + getPackageName()));
                                startActivity(marketIntent);
                                finish();
                            }
                        })
                .setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                finish();
                            }
                        }).create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChecker.onDestroy();
    }

    private void startMainActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void toast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

}

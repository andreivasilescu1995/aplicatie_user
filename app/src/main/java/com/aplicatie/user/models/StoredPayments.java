package com.aplicatie.user.models;

import android.util.Log;

import com.aplicatie.user.MainActivity;

public class StoredPayments {
    public static void savePaymentId(String pid) {
        String payment_ids = getPaymentIds();
        if (payment_ids.equals("")) {
            Log.e("PID", "Nu am gasit PID in shared preferences");
            payment_ids = pid + ",";
        }
        else {
            payment_ids += pid + ",";
            Log.e("PID", "Am scris in device PID: " + payment_ids);
        }
        final String finalPID = payment_ids;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.paymentSharedPref.edit().putString("PID", finalPID).apply();
                Log.e("PID", "AM SCRIS PID: " + finalPID);
            }
        });
        t.start();
    }

    public static String getPaymentIds() {
        return MainActivity.paymentSharedPref.getString("PID", "");
    }
}

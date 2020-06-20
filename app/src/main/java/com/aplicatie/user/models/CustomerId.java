package com.aplicatie.user.models;

import android.util.Log;

import com.aplicatie.user.MainActivity;

import java.security.SecureRandom;

public class CustomerId {
    private static final String TAG = CustomerId.class.getName();

    public static String newCustomerCustomerId() {
        final String SOURCE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++)
            sb.append(SOURCE.charAt(new SecureRandom().nextInt(SOURCE.length())));
        Log.e(TAG, "Am scris in shared preferences ID ul: " + sb.toString());
        return sb.toString();
    }

    public static String getCustomerId() {
//        delCustomerId();
        String customerId = MainActivity.paymentSharedPref.getString("customerId", "");
        if (customerId.equals("")) {
            Log.e(TAG, "Nu am gasit customerId in shared preferences, creez id nou");
            customerId = newCustomerCustomerId();
            final String finalCustomerId = customerId;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.paymentSharedPref.edit().putString("customerId", finalCustomerId).apply();
                }
            });
            t.start();
        }
        else
            Log.e("DEVICE ID", "Am gasit in device id-ul: " + customerId);
        return customerId;
    }

    public static void delCustomerId() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.paymentSharedPref.edit().clear().apply();
            }
        });
        t.start();
    }
}

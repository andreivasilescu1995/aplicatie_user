package com.aplicatie.user.models;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aplicatie.user.misc_objects.Constants;
import com.aplicatie.user.misc_objects.RequestQueueSingleton;
import com.aplicatie.user.controllers.paymentHistory.PaymentHistoryFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PaymentHistoryModel {
    private static final String TAG = PaymentHistoryModel.class.getName();
    private ArrayList<Payment> payments = new ArrayList<>();

    public void getTransactions(final onAllTransactionsReceived allTransactiontReceived) {
        final ArrayList<String> payment_ids = new ArrayList<>(Arrays.asList(StoredPayments.getPaymentIds().split(",")));
        Log.e(TAG, "plati gasite: " + payment_ids);
        if (payment_ids.get(0).equals("")) {
            allTransactiontReceived.onAllTransactionsReceived();
        }
        else {
            for (int i = 0; i < payment_ids.size(); i++) {
                if (!payment_ids.get(i).equals("")) {
                    final int finalI = i;
                    getTransactionFromServer(payment_ids.get(i), new onTransactionReceived() {
                        @Override
                        public void onTransactionReceived(Payment p) {
                            payments.add(p);
                            if (finalI == payment_ids.size()-1) {
                                allTransactiontReceived.onAllTransactionsReceived();
                            }
                        }
                    });
                }
            }
        }
    }

    private void getTransactionFromServer(final String id, final onTransactionReceived transactionCallback) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.server_ip + "/getTransaction.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Payment p = new Payment(response, null);
                transactionCallback.onTransactionReceived(p);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                transactionCallback.onTransactionReceived(null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("transactionId", id);
                return params;
            }
        };
        RequestQueueSingleton.getInstance(PaymentHistoryFragment.context).addToRequestQueue(request);
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public interface onTransactionReceived {
        void onTransactionReceived(Payment p);
    }

    public interface onAllTransactionsReceived {
        void onAllTransactionsReceived();
    }
}

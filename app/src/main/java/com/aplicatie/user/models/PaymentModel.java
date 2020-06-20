package com.aplicatie.user.models;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aplicatie.user.misc_objects.Constants;
import com.aplicatie.user.misc_objects.RequestQueueSingleton;
import com.aplicatie.user.controllers.payment.PaymentFragment;

import java.util.HashMap;
import java.util.Map;

public class PaymentModel {
    private final String TAG = PaymentModel.class.getName();

    public void getTokenFromServer(final GetTokenCallback getTokenCallback) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.server_ip + "/getToken.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getTokenCallback.onTokenResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getTokenCallback.onTokenResponse(null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("customerId", CustomerId.getCustomerId());
                return params;
            }
        };
        request.setTag("PaymentFragment");
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(PaymentFragment.context).addToRequestQueue(request);
    }

    public interface GetTokenCallback {
        void onTokenResponse(String token);
    }
}

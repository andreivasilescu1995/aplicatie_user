package com.aplicatie.user.controllers.payment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aplicatie.user.R;
import com.aplicatie.user.models.CustomerId;
import com.aplicatie.user.models.StoredPayments;
import com.aplicatie.user.misc_objects.Constants;
import com.aplicatie.user.misc_objects.RequestQueueSingleton;
import com.aplicatie.user.misc_objects.StaticMethods;
import com.aplicatie.user.models.Payment;
import com.aplicatie.user.controllers.ErrorFragment;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;


public class CheckoutDialogFragment extends DialogFragment {
    private final String TAG = CheckoutDialogFragment.class.getName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView checkout_title;
    private TextView checkout_body;

    private TextView mPaymentMethodTitle;
    private ImageView mPaymentMethodIcon;
    private TextView mPaymentMethodDescription;
    private static PaymentMethodNonce mNonce;

    private String mParam1;
    private String mParam2;

    public CheckoutDialogFragment() {
        super();
    }

    public static CheckoutDialogFragment newInstance(PaymentMethodNonce nonce, String param1, String param2) {
        CheckoutDialogFragment fragment = new CheckoutDialogFragment();
        Bundle args = new Bundle();
        mNonce = nonce;
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_checkout, container, false);

        checkout_title = v.findViewById(R.id.checkout_title);
        checkout_body = v.findViewById(R.id.checkout_body);

        mPaymentMethodTitle = v.findViewById(R.id.payment_method_title);
        mPaymentMethodIcon = v.findViewById(R.id.payment_method_icon);
        mPaymentMethodDescription = v.findViewById(R.id.payment_method_description);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkout_title.setText(mParam1);
        checkout_body.setText(mParam2);

        mPaymentMethodIcon.setImageResource(PaymentMethodType.forType(mNonce).getDrawable());
        mPaymentMethodTitle.setText(mNonce.getTypeLabel());
        mPaymentMethodDescription.setText(mNonce.getDescription());

        ImageButton confirm = view.findViewById(R.id.btn_confirm_payment);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postNonceToServer(mNonce);
                dismiss();
            }
        });
        ImageButton cancel = view.findViewById(R.id.btn_cancel_payment);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Snackbar.make(requireActivity().findViewById(android.R.id.content),"Plata anulata!", 5000).show();

            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    private void postNonceToServer(final PaymentMethodNonce nonce) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.server_ip + "/pay.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("Successful")) {
                    Log.e(TAG, response);
                    Payment payment = new Payment(response, PaymentFragment.selectedTicket);
                    StoredPayments.savePaymentId(payment.getId());
                    Toast.makeText(PaymentFragment.context, "Plata efectuata!", Toast.LENGTH_LONG).show();
                }
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = StaticMethods.volleyError(error);
                ErrorFragment errorFragment = new ErrorFragment("Eroare checkout", message);
                try {
                    errorFragment.show(requireActivity().getSupportFragmentManager(), "CheckoutFragment");
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("nonce", mNonce.getNonce());
                params.put("amount", String.valueOf(PaymentFragment.selectedTicket.getCost()));
                params.put("customerId", CustomerId.getCustomerId());
                return params;
            }
        };
        RequestQueueSingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}

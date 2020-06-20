package com.aplicatie.user.controllers.payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.aplicatie.user.R;
import com.aplicatie.user.misc_objects.Constants;
import com.aplicatie.user.misc_objects.StaticMethods;
import com.aplicatie.user.models.Ticket;
import com.aplicatie.user.models.PaymentModel;
import com.aplicatie.user.controllers.SettingsFragment;
import com.aplicatie.user.views.PaymentView;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.snackbar.Snackbar;

public class PaymentFragment extends Fragment {
    private final String TAG = PaymentFragment.class.getName();
    private PaymentModel model;
    private PaymentView view;
    private ActionBar toolbar;

    public static Context context;
    public static Ticket selectedTicket;
    public static int REQUEST_CODE = 100;
    public static String server_token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (context == null) context = requireActivity().getApplicationContext();
        if (model == null) model = new PaymentModel();
        if (view == null) view = new PaymentView();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment, container, false);
        toolbar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        view.initViews(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        toolbar.setTitle(getResources().getString(R.string.menu_payment));

        view.getProgressDialog().show(requireActivity().getSupportFragmentManager(), "ProgressDialog");
        view.getProgressDialog().message = getResources().getString(R.string.getting_token);
        model.getTokenFromServer(new PaymentModel.GetTokenCallback() {
            @Override
            public void onTokenResponse(String token) {
                if (token != null) {
                    Log.d(TAG, "TOKEN PRELUAT: " + token);
                    server_token = token;
                }
                else {
                    Log.e(TAG, "Nu s-a putut prelua token din server!");
                    if (PaymentFragment.this.isAdded())
                        StaticMethods.showErrorDialog((AppCompatActivity) requireActivity(), "Eroare server", "Nu s-a putut prelua token din server!");
                }
                view.getProgressDialog().cancel();
                view.getLayout_payment().setVisibility(View.VISIBLE);
            }
        });

        view.getPay_ticket().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTicket = new Ticket(Integer.parseInt(view.getEdit_no_tickets().getText().toString()));
                view.getPay_ticket().setClickable(false);
                showDropInUI();
            }
        });

        view.getPay_subscription().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTicket = new Ticket(true);
                view.getPay_subscription().setClickable(false);
                showDropInUI();
            }
        });

        view.getMinus_ticket().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(view.getEdit_no_tickets().getText().toString()) > 1) {
                    view.getEdit_no_tickets().setText(String.valueOf(Integer.parseInt(view.getEdit_no_tickets().getText().toString()) - 1));
                    if (Integer.parseInt(view.getEdit_no_tickets().getText().toString()) < 2) {
                        view.getLabel_ticket().setText("calatorie");
                    }
                    view.getTicket_cost().setText(String.valueOf(Integer.parseInt(view.getEdit_no_tickets().getText().toString()) * Constants.ticket_cost));
                }
                else
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),"Valoarea trebuie sa fie mai mare decat 0!", 5000).show();
            }
        });

        view.getPlus_ticket().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.getEdit_no_tickets().setText(String.valueOf(Integer.parseInt(view.getEdit_no_tickets().getText().toString())+1));
                if (Integer.parseInt(view.getEdit_no_tickets().getText().toString()) > 1) {
                    view.getLabel_ticket().setText("calatorii");
                }
                view.getTicket_cost().setText(String.valueOf(Integer.parseInt(view.getEdit_no_tickets().getText().toString()) * Constants.ticket_cost));
            }
        });
    }

    private void showDropInUI() {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(server_token)
                .collectDeviceData(SettingsFragment.getCollectDeviceData())
                .maskCardNumber(SettingsFragment.getMaskCard())
                .maskSecurityCode(SettingsFragment.getMaskCvv())
                .cardholderNameStatus(CardForm.FIELD_REQUIRED)
                .allowVaultCardOverride(SettingsFragment.getVaultPayments())
                .vaultManager(SettingsFragment.getVaultPayments())
                .vaultCard(SettingsFragment.getVaultPayments());
        startActivityForResult(dropInRequest.getIntent(getContext()), REQUEST_CODE);
    }

    @Override
    public void onPause() {
        super.onPause();
//        context = null;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
            CheckoutDialogFragment.newInstance(
                    result.getPaymentMethodNonce(),
                    "Confirmare plata",
                    "Cost: " + PaymentFragment.selectedTicket.getCost() + "€"
            ).show(requireActivity().getSupportFragmentManager(), "CheckoutFragment");
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(requireContext(), "Plata anulata!", Toast.LENGTH_LONG).show();
            Log.e(TAG, "OPERATION CANCELED");
        } else {
            if (data != null) {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                String message = error.getMessage();
                if (PaymentFragment.this.isAdded())
                    StaticMethods.showErrorDialog((AppCompatActivity) requireActivity(), "Eroare checkout", message);
            }
        }
        view.getPay_ticket().setClickable(true);
    }

}

package com.aplicatie.user.controllers.paymentHistory;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aplicatie.user.R;
import com.aplicatie.user.models.PaymentHistoryModel;
import com.aplicatie.user.controllers.DelayedProgressDialog;
import com.aplicatie.user.views.PaymentHistoryView;

public class PaymentHistoryFragment extends Fragment {
    private final String TAG = PaymentHistoryFragment.class.getName();
    public static Context context;
    private ActionBar toolbar;
    private PaymentHistoryModel model;
    private PaymentHistoryView view;
    private PaymentHistoryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (context == null) context = requireActivity().getApplicationContext();
        if (model == null) model = new PaymentHistoryModel();
        if (view == null) view = new PaymentHistoryView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment_history, container, false);
        toolbar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        view.initViews(v);
        view.getProgressDialog().show(requireActivity().getSupportFragmentManager(), "ProgressDialog");
        DelayedProgressDialog.message = getResources().getString(R.string.getting_payments);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        toolbar.setTitle(getResources().getString(R.string.menu_payment_history));

        model.getTransactions(new PaymentHistoryModel.onAllTransactionsReceived() {
            @Override
            public void onAllTransactionsReceived() {
                Log.e(TAG, "PLATI: " + model.getPayments().toString());
                if (model.getPayments().size() == 0) {
                    view.getNo_payments().setVisibility(View.VISIBLE);
                }
                else {
                    adapter = new PaymentHistoryAdapter(getContext(), model.getPayments());
                    view.getRecyclerView().setAdapter(adapter);
                }
                view.getProgressDialog().cancel();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        context = null;
    }
}

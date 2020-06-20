package com.aplicatie.user.views;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aplicatie.user.R;
import com.aplicatie.user.controllers.DelayedProgressDialog;
import com.aplicatie.user.controllers.paymentHistory.PaymentHistoryFragment;

public class PaymentHistoryView {
    private TextView no_payments;
    private RecyclerView recyclerView;
    private DelayedProgressDialog progressDialog;

    public DelayedProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public TextView getNo_payments() {
        return no_payments;
    }

    public void initViews(View v) {
        progressDialog = new DelayedProgressDialog();
        no_payments = v.findViewById(R.id.no_payments);
        recyclerView = v.findViewById(R.id.payment_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(PaymentHistoryFragment.context, 2));
        recyclerView.addItemDecoration(new RecyclerViewDecorator(15, 15, 25, 25));
    }
}

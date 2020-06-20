package com.aplicatie.user.views;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.aplicatie.user.R;
import com.aplicatie.user.misc_objects.Constants;
import com.aplicatie.user.controllers.DelayedProgressDialog;

public class PaymentView {
    private TextView label_ticket, ticket_cost, label_subscription, value_subscription, subscription_cost;
    private ImageButton pay_ticket, pay_subscription, minus_ticket, plus_ticket;
    private EditText edit_no_tickets;
    private CardView cardView_tickets, cardView_subscription;
    private DelayedProgressDialog progressDialog;
    LinearLayout layout_payment;

    public TextView getLabel_ticket() {
        return label_ticket;
    }

    public TextView getTicket_cost() {
        return ticket_cost;
    }

    public ImageButton getPay_ticket() {
        return pay_ticket;
    }

    public ImageButton getPay_subscription() {
        return pay_subscription;
    }

    public ImageButton getMinus_ticket() {
        return minus_ticket;
    }

    public ImageButton getPlus_ticket() {
        return plus_ticket;
    }

    public EditText getEdit_no_tickets() {
        return edit_no_tickets;
    }

    public DelayedProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public LinearLayout getLayout_payment() {
        return layout_payment;
    }

    public void initViews(View v) {
        progressDialog = new DelayedProgressDialog();
        label_ticket = v.findViewById(R.id.label_value_ticket);
        ticket_cost = v.findViewById(R.id.value_cost_ticket);
        label_subscription = v.findViewById(R.id.label_value_subscription);
        value_subscription = v.findViewById(R.id.value_subscription);
        subscription_cost = v.findViewById(R.id.value_cost_subscription);
        pay_ticket = v.findViewById(R.id.ticket_payment);
        pay_subscription = v.findViewById(R.id.subscription_payment);
        edit_no_tickets = v.findViewById(R.id.edit_no_tickets);
        cardView_tickets = v.findViewById(R.id.cardView_tickets);
        cardView_subscription = v.findViewById(R.id.cardView_subscription);
        minus_ticket = v.findViewById(R.id.minus_ticket);
        plus_ticket = v.findViewById(R.id.plus_ticket);

        edit_no_tickets.setText(String.valueOf(1));
        label_ticket.setText("calatorie");
        ticket_cost.setText(String.valueOf(Constants.ticket_cost));

        value_subscription.setText("Abonament");
        label_subscription.setText("30 de zile");
        subscription_cost.setText(String.valueOf(Constants.subscription_cost));

        layout_payment = v.findViewById(R.id.layout_payment);
    }
}

package com.aplicatie.user.controllers.paymentHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aplicatie.user.R;
import com.aplicatie.user.models.Payment;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;

import java.util.ArrayList;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.PaymentHistoryAdapterViewHolder> {
    private Context context;
    private ArrayList<Payment> payments;

    public PaymentHistoryAdapter(Context context, ArrayList<Payment> payments) {
        this.context = context;
        this.payments = payments;
    }

    @NonNull
    @Override
    public PaymentHistoryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentHistoryAdapter.PaymentHistoryAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_row,null));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHistoryAdapterViewHolder holder, int position) {
        Payment p = payments.get(position);

        holder.payment_title.setText(p.getCreditCard().getType());
        holder.payment_description.setText(p.getCreditCard().getMaskedNumber());
        holder.cost.setText(String.valueOf(p.getCost()));
        holder.id.setText(p.getId());
        holder.date.setText(p.getTime_stamp().split(" ")[1]);
        holder.time.setText(p.getTime_stamp().split(" ")[2]);
        holder.payment_icon.setImageResource(PaymentMethodType.forType(p.getCreditCard().getType()).getDrawable());
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public class PaymentHistoryAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView payment_title;
        TextView payment_description;
        TextView cost;
        TextView id;
        TextView date;
        TextView time;
        ImageView payment_icon;

        public PaymentHistoryAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            payment_title = itemView.findViewById(R.id.payment_method_title);
            payment_description = itemView.findViewById(R.id.payment_method_description);
            cost = itemView.findViewById(R.id.value_cost_subscription);
            id = itemView.findViewById(R.id.value_id);
            date = itemView.findViewById(R.id.value_date);
            time = itemView.findViewById(R.id.value_time);
            payment_icon = itemView.findViewById(R.id.payment_method_icon);
        }
    }
}

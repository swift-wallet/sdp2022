package com.sdp.swiftwallet.domain.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sdp.cryptowalletapp.R;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactionHistory;
    private Context context;

    public TransactionAdapter(Context context, List<Transaction> transactionHistory) {
        this.context = context;
        this.transactionHistory = new ArrayList<>(transactionHistory);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(adapterLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction t = transactionHistory.get(position);
        holder.transactionTextView.setText(t.toString());
    }

    @Override
    public int getItemCount() {
        return transactionHistory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView transactionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.transactionTextView = itemView.findViewById(R.id.transactionText);
        }
    }
}

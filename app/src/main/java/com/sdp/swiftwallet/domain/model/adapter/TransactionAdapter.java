package com.sdp.swiftwallet.domain.model.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sdp.cryptowalletapp.R;

import com.sdp.swiftwallet.domain.model.object.transaction.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Transaction Adapter
 * Used to display Transactions in a RecyclerView
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final List<Transaction> transactionHistory;
    private final Context context;

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
        holder.transactionIDTextView.setText(String.format(
                Locale.US,
                "Transaction ID #%d",
                t.getTransactionID()
        ));
        holder.transactionNumberTextView.setText(String.format(
                Locale.US,
                "%.1f %s",
                t.getAmount(), t.getSymbol()
        ));
        holder.transactionTextView.setText(t.toString());
        if (t.getAmount() < 0) {
            holder.transactionCardView.setCardBackgroundColor(Color.parseColor("#FFF44336"));
        }
    }

    @Override
    public int getItemCount() {
        return transactionHistory.size();
    }

    /**
     * Individual ViewHolder for a Transaction
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView transactionIDTextView;
        private final TextView transactionNumberTextView;
        private final TextView transactionTextView;
        private final CardView transactionCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.transactionNumberTextView = itemView.findViewById(R.id.transactionBigNumber);
            this.transactionIDTextView = itemView.findViewById(R.id.transactionIDTextView);
            this.transactionTextView = itemView.findViewById(R.id.transactionText);
            this.transactionCardView = itemView.findViewById(R.id.transactionCard);
        }
    }
}

package com.sdp.swiftwallet.domain.model.transaction;

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

import java.text.SimpleDateFormat;
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
    private final String uid;

    private final static int RED_COLOR = Color.parseColor("#FFF44336");
    private final static int GREEN_COLOR = Color.parseColor("#FF4CAF50");
    private final static int GRAY_COLOR = Color.parseColor("#FFABABAB");

    public TransactionAdapter(Context context, List<Transaction> transactionHistory, String uid) {
        this.context = context;
        this.transactionHistory = new ArrayList<>(transactionHistory);
        this.uid = uid;
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
                t.getAmount(), t.getCurr().getSymbol()
        ));
        holder.transactionTextView.setText("");
        holder.transactionCardView.setCardBackgroundColor(
                GRAY_COLOR
        );
        t.getSenderID().map(
                sender -> {
                    if (sender.equals(uid)) {
                        holder.transactionCardView.setCardBackgroundColor(RED_COLOR);
                    } else if (!sender.isEmpty()) {
                        holder.transactionTextView.setText(String.format(
                                Locale.US,
                                "From %s",
                                t.getSenderID().get()
                        ));
                    }
                    return null;
                }
        );
        t.getReceiverID().map(
                receiver -> {
                    if (receiver.equals(uid)) {
                        holder.transactionCardView.setCardBackgroundColor(GREEN_COLOR);
                    } else if (!receiver.isEmpty()) {
                        holder.transactionTextView.setText(String.format(
                                Locale.US,
                                "To %s",
                                t.getReceiverID().get()
                        ));
                    }
                    return null;
                }
        );
        SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
        holder.transactionDateTextView.setText(
                String.format(
                        Locale.US,
                        "Date: %s",
                        formatter.format(t.getDate())
                )
        );
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
        private final TextView transactionDateTextView;
        private final TextView transactionTextView;
        private final CardView transactionCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.transactionNumberTextView = itemView.findViewById(R.id.transactionBigNumber);
            this.transactionIDTextView = itemView.findViewById(R.id.transactionIDTextView);
            this.transactionDateTextView = itemView.findViewById(R.id.transactionDateTextView);
            this.transactionTextView = itemView.findViewById(R.id.transactionText);
            this.transactionCardView = itemView.findViewById(R.id.transactionCard);
        }
    }
}

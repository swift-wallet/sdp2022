package com.sdp.swiftwallet.domain.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sdp.cryptowalletapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {
    private ArrayList<Currency> currencyArrayList;
    private final Context context;
    private static final DecimalFormat currencyValueFormat = new DecimalFormat("#.##");

    public CurrencyAdapter(ArrayList<Currency> currencyArrayList, Context context) {
        this.currencyArrayList = currencyArrayList;
        this.context = context;
    }

    public void filterList(ArrayList<Currency> filteredList){
        currencyArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrencyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.ViewHolder holder, int position) {
        Currency currency = currencyArrayList.get(position);
        holder.nameTextView.setText(currency.getName());
        holder.symbolTextView.setText(currency.getSymbol());
        holder.valueTextView.setText("$" + currencyValueFormat.format(currency.getValue()));
    }

    @Override
    public int getItemCount() {
        return currencyArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView nameTextView;
        private final TextView symbolTextView;
        private final TextView valueTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.idTextViewName);
            symbolTextView = itemView.findViewById(R.id.idTextViewSymbol);
            valueTextView = itemView.findViewById(R.id.idTextViewValue);
        }
    }
}

package com.sdp.swiftwallet.presentation.transactions.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.Currency;
import com.sdp.swiftwallet.domain.model.Transaction;
import com.sdp.swiftwallet.presentation.transactions.TransactionActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionStatsFragment} factory method to
 * create an instance of this fragment.
 */
public class TransactionStatsFragment extends Fragment {
    private TransactionActivity rootAct;
    private PieChart pieChart;

    private final static List<Integer> COLORS = new ArrayList<>();
    static {
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            COLORS.add(color);
        }
        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            COLORS.add(color);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootAct = (TransactionActivity) getActivity();
        return inflater.inflate(R.layout.fragment_transaction_stats, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        pieChart = rootAct.findViewById(R.id.transaction_pieChart);
        setupPieChart();
        loadPieChartData();
    }

    /**
     * Basic PieChart setup methods
     */
    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Activity by Currency");
        pieChart.setCenterTextSize(24f);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setEnabled(true);
    }

    /**
     * Method used to initialize the PieChart with Transactions data
     */
    private void loadPieChartData() {
        List<PieEntry> entries = new ArrayList<>();
        List<Transaction> transactions = rootAct.getList();

        double totalActivity = 0;
        Map<Currency, Double> activityByCurrency = new HashMap<>();

        for (Transaction t : transactions) {
            double activity = Math.abs(t.getConvertedAmount());
            totalActivity += activity;

            if (activityByCurrency.containsKey(t.getCurr())) {
                activityByCurrency.put(
                        t.getCurr(),
                        activityByCurrency.get(t.getCurr()) + activity
                );
            } else {
                activityByCurrency.put(t.getCurr(), activity);
            }
        }

        for (Map.Entry<Currency, Double> entry : activityByCurrency.entrySet()) {
            float fraction = entry.getValue().floatValue();
            fraction /= totalActivity;
            entries.add(new PieEntry(fraction, entry.getKey().getSymbol()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Currency");
        dataSet.setColors(COLORS);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
    }
}
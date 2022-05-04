package com.sdp.swiftwallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.test.espresso.idling.CountingIdlingResource;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.Currency;

import org.json.JSONArray;

import java.util.ArrayList;

public class CryptoGraphActivity extends AppCompatActivity {
    private final int NB_CANDLES_TO_SHOW = 100;
    private Spinner intervalSpinner;
    private ProgressBar progressBar;
    private ArrayList<String> intervalsText;
    private ArrayList<String> intervalsForRequest;
    private Currency currency;
    private String rateSymbol;
    private String interval;
    private final ArrayList<Long> openTimes = new ArrayList<>();
    private final ArrayList<Double> openValues = new ArrayList<>();
    private final ArrayList<Double> highValues = new ArrayList<>();
    private final ArrayList<Double> lowValues = new ArrayList<>();
    private final ArrayList<Double> closeValues = new ArrayList<>();
    private final ArrayList<Double> volumeValues = new ArrayList<>();
    private final ArrayList<Long> closeTimes = new ArrayList<>();
    private CandleStickChart candleStickChart;

    private CountingIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crypto_graph);
        progressBar = findViewById(R.id.idProgressBar);

        // Get the Intent that started this activity and extract the currency
        Intent intent = getIntent();
        currency = (Currency) intent.getSerializableExtra("currency");
        if(currency == null) currency = new Currency("Ethereum", "ETH", 2000);
        else rateSymbol = currency.getSymbol();
        interval = "1m";

        // Get the data from Binance API
        mIdlingResource = new CountingIdlingResource("CryptoValue Calls");
        setIntervalsSpinner();
        mIdlingResource.increment();
        candleStickChart = getData();
        mIdlingResource.decrement();


        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.idCurrencyToShowName);
        textView.setText(currency.getName());

    }

    private CandleStickChart createCandleStickChart(){
        CandleStickChart candleStickChart = findViewById(R.id.candle_stick_chart);
        candleStickChart.setHighlightPerDragEnabled(true);

        candleStickChart.setDrawBorders(true);

        candleStickChart.setBorderColor(Color.LTGRAY);

        YAxis yAxis = candleStickChart.getAxisLeft();
        YAxis rightAxis = candleStickChart.getAxisRight();
        yAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);
        candleStickChart.requestDisallowInterceptTouchEvent(true);

        XAxis xAxis = candleStickChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        rightAxis.setTextColor(Color.WHITE);
        yAxis.setDrawLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);

        Legend legend = candleStickChart.getLegend();
        legend.setEnabled(false);

        return candleStickChart;
    }

    private CandleData createDataSetForCandleGraph(ArrayList<Long> openTimes, ArrayList<Double> openValues, ArrayList<Double> highValues, ArrayList<Double> lowValues,
                                                      ArrayList<Double> closeValues, ArrayList<Double> volumeValues, ArrayList<Long> closeTimes){

        ArrayList<CandleEntry> candleSticks = new ArrayList<CandleEntry>();

        for(int i = openTimes.size()+2-NB_CANDLES_TO_SHOW; i<openTimes.size();i++){
            candleSticks.add(new CandleEntry(i,
                    highValues.get(i).floatValue(),
                    lowValues.get(i).floatValue(),
                    openValues.get(i).floatValue(),
                    closeValues.get(i).floatValue()));
        }

        CandleDataSet candleDataSet = new CandleDataSet(candleSticks, "Data");
        candleDataSet.setColor(Color.rgb(200, 0, 80));
        candleDataSet.setShadowColor(Color.LTGRAY);
        candleDataSet.setDecreasingColor(Color.RED);
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setIncreasingColor(Color.GREEN);
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setNeutralColor(Color.LTGRAY);
        candleDataSet.setDrawValues(false);
        candleDataSet.setBarSpace(0.1f);

        CandleData candleData = new CandleData(candleDataSet);

        return candleData;
    }

    private CandleStickChart getData(){
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://api.binance.com/api/v1/klines?symbol="+rateSymbol+"&interval="+interval;
        //Create CandleStickChart
        CandleStickChart candleStickChart = createCandleStickChart();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,response -> {
            progressBar.setVisibility(View.GONE);
            try{
                for(int i = 0; i<response.length(); ++i){
                    JSONArray array = response.getJSONArray(i);
                    openTimes.add((Long)array.get(0));
                    openValues.add(Double.parseDouble((String)array.get(1)));
                    highValues.add(Double.parseDouble((String)array.get(2)));
                    lowValues.add(Double.parseDouble((String)array.get(3)));
                    closeValues.add(Double.parseDouble((String)array.get(4)));
                    volumeValues.add(Double.parseDouble((String)array.get(5)));
                    closeTimes.add((Long)array.get(6));
                }

                //get data as CandleData
                CandleData candleData = createDataSetForCandleGraph(openTimes, openValues, highValues, lowValues, closeValues, volumeValues, closeTimes);
                //setData
                candleStickChart.setData(candleData);
                candleStickChart.invalidate();
            } catch(Exception e){
                e.printStackTrace();
                Toast.makeText(CryptoGraphActivity.this, "Couldn't extract JSON data... Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(CryptoGraphActivity.this, "Couldn't retrieve data... Please try again later.", Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(jsonArrayRequest);

        return candleStickChart;
    }

    private void setIntervalsSpinner(){
        this.intervalSpinner = findViewById(R.id.idInterval);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.intervals_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.intervalSpinner.setAdapter(adapter);

        this.intervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onItemSelectedHandler(adapterView, view, i, l);
                interval = (String)adapterView.getItemAtPosition(i);

                candleStickChart = getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id){
        Adapter adapter = adapterView.getAdapter();
        String interval = (String) adapter.getItem(position);

        Toast.makeText(getApplicationContext(), "Selected Interval: " + interval, Toast.LENGTH_SHORT).show();
    }

    public CountingIdlingResource getIdlingResource() {
        return mIdlingResource;
    }
}
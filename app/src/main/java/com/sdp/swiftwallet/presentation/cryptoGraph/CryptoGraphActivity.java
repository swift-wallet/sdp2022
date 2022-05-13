package com.sdp.swiftwallet.presentation.cryptoGraph;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.idling.CountingIdlingResource;
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
import com.sdp.cryptowalletapp.R;
import com.sdp.swiftwallet.domain.model.currency.Currency;
import java.util.ArrayList;
import org.json.JSONArray;

public class CryptoGraphActivity extends AppCompatActivity {

    // By default 100
    private final int NB_CANDLES_TO_SHOW = 100;
    private final String INTERVAL_DIM = "1h";

    private Spinner intervalSpinner;
    private Spinner currencyToShowspinner;
    private ProgressBar progressBar;
    private CandleStickChart candleStickChart;

    private ArrayList<String> intervalsText;
    private ArrayList<String> intervalsForRequest;

    private Currency currency;
    private String rateSymbol;
    private String interval;

    // Bounds for the graph
    private final ArrayList<Long> openTimes = new ArrayList<>();
    private final ArrayList<Double> openValues = new ArrayList<>();
    private final ArrayList<Double> highValues = new ArrayList<>();
    private final ArrayList<Double> lowValues = new ArrayList<>();
    private final ArrayList<Double> closeValues = new ArrayList<>();
    private final ArrayList<Double> volumeValues = new ArrayList<>();
    private final ArrayList<Long> closeTimes = new ArrayList<>();

    // Useful for testing
    private CountingIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crypto_graph);
        progressBar = findViewById(R.id.idProgressBar);

        // Get the Intent that started this activity and extract the currency
        Intent intent = getIntent();
        currency = (Currency) intent.getSerializableExtra("currency");
        if (currency == null)
            currency = new Currency("Ethereum", "ETH", 2000);
        else
            rateSymbol = currency.getSymbol();
        interval = INTERVAL_DIM;

        // Get the data from Binance API
        mIdlingResource = new CountingIdlingResource("CryptoValue Calls");
        setIntervalsSpinner();
        setCurrencyToShowSpinner();
        mIdlingResource.increment();
        candleStickChart = getData();
        mIdlingResource.decrement();

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.idCurrencyToShowName);
        textView.setText(currency.getName());
    }

    private CandleStickChart createCandleStickChart() {
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

    /**
     * Creates data set for the candle graph
     *
     * @param openTimes    opening times
     * @param openValues   open values for the set
     * @param highValues
     * @param lowValues
     * @param closeValues
     * @param volumeValues
     * @param closeTimes   closing times
     * @return CandleDate for the set
     */
    private CandleData createDataSetForCandleGraph(ArrayList<Long> openTimes,
        ArrayList<Double> openValues, ArrayList<Double> highValues, ArrayList<Double> lowValues,
        ArrayList<Double> closeValues, ArrayList<Double> volumeValues, ArrayList<Long> closeTimes) {

        ArrayList<CandleEntry> candleSticks = new ArrayList<CandleEntry>();

        // Add entries given the interval
        for (int i = openTimes.size() + 2 - NB_CANDLES_TO_SHOW; i < openTimes.size(); i++) {
            candleSticks.add(
                new CandleEntry(i,
                    highValues.get(i).floatValue(),
                    lowValues.get(i).floatValue(),
                    openValues.get(i).floatValue(),
                    closeValues.get(i).floatValue()
                )
            );
        }
        // Create candle data data set
        CandleDataSet candleDataSet = createCandleDataSet(candleSticks);

        CandleData candleData = new CandleData(candleDataSet);

        return candleData;
    }

    private CandleStickChart getData() {
        progressBar.setVisibility(View.VISIBLE);
        String url =
            "https://api.binance.com/api/v1/klines?symbol=" + rateSymbol + "&interval=" + interval;

        //Create CandleStickChart
        CandleStickChart candleStickChart = createCandleStickChart();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            response -> {
                progressBar.setVisibility(View.GONE);
                try {
                    for (int i = 0; i < response.length(); ++i) {
                        JSONArray array = response.getJSONArray(i);
                        openTimes.add((Long) array.get(0));
                        openValues.add(Double.parseDouble((String) array.get(1)));
                        highValues.add(Double.parseDouble((String) array.get(2)));
                        lowValues.add(Double.parseDouble((String) array.get(3)));
                        closeValues.add(Double.parseDouble((String) array.get(4)));
                        volumeValues.add(Double.parseDouble((String) array.get(5)));
                        closeTimes.add((Long) array.get(6));
                    }

                    // Get data as CandleData
                    CandleData candleData =
                        createDataSetForCandleGraph(openTimes, openValues, highValues, lowValues,
                            closeValues, volumeValues, closeTimes);

                    // SetData
                    candleStickChart.setData(candleData);
                    candleStickChart.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CryptoGraphActivity.this,
                        "Couldn't extract JSON data... Please try again later.", Toast.LENGTH_SHORT)
                        .show();
                }
            }, error -> {
            Toast.makeText(CryptoGraphActivity.this,
                "Couldn't retrieve data... Please try again later.", Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(jsonArrayRequest);

        return candleStickChart;
    }

    /**
     * Sets the spinner for intervals
     */
    private void setIntervalsSpinner() {
        this.intervalSpinner = findViewById(R.id.idInterval);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter
            .createFromResource(this, R.array.intervals_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.intervalSpinner.setAdapter(adapter);
        // Display the interval or does nothing if not selected
        this.intervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0) {
                    onItemSelectedHandler(adapterView, view, i, l);
                    interval = (String) adapterView.getItemAtPosition(i);

                    candleStickChart = getData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * Handler for the interval adapter, display it when selected
     */
    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position,
        long id) {
        Adapter adapter = adapterView.getAdapter();
        String interval = (String) adapter.getItem(position);

        Toast
            .makeText(getApplicationContext(), "Selected Interval: " + interval, Toast.LENGTH_SHORT)
            .show();
    }

    /**
     * Sets the spinner for the currency to show
     */
    private void setCurrencyToShowSpinner() {
        this.currencyToShowspinner = findViewById(R.id.idCurrencyToShow);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.main_crypto_names,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.currencyToShowspinner.setAdapter(adapter);
        // Display the interval or does nothing if not selected
        this.currencyToShowspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0) {
                    onItemSelectedCurrencyHandler(adapterView, view, i, l);
                    String newCurrency = (String) adapterView.getItemAtPosition(i);
                    rateSymbol = newCurrency + "USDT";
                    TextView textView = findViewById(R.id.idCurrencyToShowName);
                    textView.setText(newCurrency);
                    candleStickChart = getData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * Handler for the currency to show adapter, display it when selected
     */
    private void onItemSelectedCurrencyHandler(AdapterView<?> adapterView, View view, int position,
                                       long id) {
        Adapter adapter = adapterView.getAdapter();
        String symbol = (String) adapter.getItem(position);

        Toast
                .makeText(getApplicationContext(), "Selected Currency : " + symbol+"USDT", Toast.LENGTH_SHORT)
                .show();
    }

    public CountingIdlingResource getIdlingResource() {
        return mIdlingResource;
    }

    /**
     * Create a candle data set given candle sticks
     */
    public CandleDataSet createCandleDataSet(ArrayList<CandleEntry> candleSticks){
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
        return candleDataSet;
    }

}


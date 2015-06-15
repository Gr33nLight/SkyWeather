package com.skyweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;

import java.util.ArrayList;
import java.util.Calendar;


public class DaySpecsFragment extends Fragment {
    private RelativeLayout containerR;
    private String[] data;
    private LineChart chart;
    private double[] coords;
    private TextView timeH1, timeH2, timeH3, timeH4, wind, humidity, precip, summary, pressure;
    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getArguments().getStringArray("data");
        coords = getArguments().getDoubleArray("coords");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dayspecs, container, false);
        chart = (LineChart) view.findViewById(R.id.chart);
        containerR = (RelativeLayout) view.findViewById(R.id.container);
        wind = (TextView) view.findViewById(R.id.windText);
        humidity = (TextView) view.findViewById(R.id.humidityText);
        precip = (TextView) view.findViewById(R.id.precipText);
        summary = (TextView) view.findViewById(R.id.summary);
        pressure = (TextView) view.findViewById(R.id.pressureText);
        timeH1 = (TextView) view.findViewById(R.id.timeH1);
        timeH2 = (TextView) view.findViewById(R.id.timeH2);
        timeH3 = (TextView) view.findViewById(R.id.timeH3);
        timeH4 = (TextView) view.findViewById(R.id.timeH4);
        setIcons(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        chart.setNoDataText("Loading data...");
        Log.d("ONVIEWCREATED", "DaySpecsFragment");
        new Graph().execute();
        ImageButton back = (ImageButton) view.findViewById(R.id.back);
        //MMRequest object ADS
        try {
            MMAdView adViewFromXml;
            MMRequest request = new MMRequest();
            adViewFromXml = (MMAdView) view.findViewById(R.id.adView);
            adViewFromXml.setMMRequest(request);
            adViewFromXml.getAd();
        } catch (Exception e) {
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).jumpToPage(0);
            }
        });
        wind.setText(data[5]);
        humidity.setText(data[3]);
        precip.setText(data[16]);
        summary.setText(data[17]);
        pressure.setText(data[4]);
        timeH1.setText(data[33]);
        timeH2.setText(data[34]);
        timeH3.setText(data[35]);
        timeH4.setText(data[36]);
    }

    class Graph extends AsyncTask<Void, Void, ArrayList<Entry>> implements ValueFormatter {

        @Override
        protected ArrayList<Entry> doInBackground(Void... params) {
            try {
                ForecastIO fio = new ForecastIO("df2fd8be38bc1a254b610d11f1d65884");
                fio.setUnits(ForecastIO.UNITS_SI);             //sets the units as SI - optional
                fio.setExcludeURL("minutely");
                fio.setExtend(true);
                fio.getForecast("" + coords[0], "" + coords[1]);
                FIOHourly hourly = new FIOHourly(fio);
                int j = 2;
                ArrayList<Entry> values = new ArrayList<>();
                for (int i = 0; i <= 12; i++) {
                    Integer temp = hourly.getHour(j).temperature().intValue();
                    values.add(new Entry(temp, i));
                    j += 2;
                }
                return values;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Entry> values) {
            //Y Axis
            YAxis leftAxis = chart.getAxisLeft();
            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setEnabled(false);
            leftAxis.setEnabled(false);
            //X Axis
            XAxis xAxis = chart.getXAxis();
            xAxis.setTextColor(Color.WHITE);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(10);
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);
            xAxis.setAvoidFirstLastClipping(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
            //Values
            Calendar c = Calendar.getInstance();
            Integer hour;
            ArrayList<String> xVals = new ArrayList<>();
            hour = c.get(Calendar.HOUR_OF_DAY);
            for (int i = 0; i <= 12; i++) {
                xVals.add(hour.toString() + ":00");
                c.add(Calendar.HOUR_OF_DAY, 2);
            }
            Legend legend = chart.getLegend();
            legend.setEnabled(false);
            LineDataSet tempSet = new LineDataSet(values, "");
            tempSet.setLineWidth(3);
            tempSet.setColor(Color.WHITE);
            tempSet.setDrawCircles(false);
            tempSet.setDrawValues(true);
            tempSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            ArrayList<LineDataSet> dataSets = new ArrayList<>();
            dataSets.add(tempSet);
            LineData dataL = new LineData(xVals, dataSets);
            dataL.setValueTextColor(Color.WHITE);
            dataL.setValueFormatter(new Graph());
            chart.setData(dataL);
            chart.setDescription("");
            chart.setHighlightIndicatorEnabled(false);
            chart.setHighlightEnabled(false);
            chart.setDoubleTapToZoomEnabled(false);
            chart.setGridBackgroundColor(Color.TRANSPARENT);
            chart.fitScreen();
            chart.invalidate();
        }

        @Override
        public String getFormattedValue(float v) {
            return String.format("%.0f", v);
        }
    }

    private void setBackground() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour >= 19 && hour <= 24 || hour >= 0 && hour < 4) {
            //sera-notte
//            chartColor=Color.parseColor("#2c3e50");
            containerR.setBackgroundColor(Color.parseColor("#263238"));
        } else if (hour >= 4 && hour < 12) {
            //mattina
//            chartColor=Color.parseColor("#3498db");
            containerR.setBackgroundColor(Color.parseColor("#2980b9"));
        } else if (hour >= 12 && hour < 19) {
            //pomeriggio
//            chartColor=Color.parseColor("#F9A825");
            containerR.setBackgroundColor(Color.parseColor("#F57F17"));
        }
    }

    private void setIcons(View view) {
        ImageView icnH1, icnH2, icnH3, icnH4;
        icnH1 = (ImageView) view.findViewById(R.id.icnH1);
        icnH2 = (ImageView) view.findViewById(R.id.icnH2);
        icnH3 = (ImageView) view.findViewById(R.id.icnH3);
        icnH4 = (ImageView) view.findViewById(R.id.icnH4);
        String icH1 = data[37].replace("\"", "");
        switch (icH1) {
            case "partly-cloudy-day":
                icnH1.setImageResource(R.drawable.cloudsun);
                break;
            case "partly-cloudy-night":
                icnH1.setImageResource(R.drawable.cloudmoon);
                break;
            case "cloudy":
                icnH1.setImageResource(R.drawable.cloud);
                break;
            case "fog":
                icnH1.setImageResource(R.drawable.fog);
                break;
            case "wind":
                icnH1.setImageResource(R.drawable.wind);
                break;
            case "sleet":
                icnH1.setImageResource(R.drawable.sleet);
                break;
            case "snow":
                icnH1.setImageResource(R.drawable.snow);
                break;
            case "rain":
                icnH1.setImageResource(R.drawable.rain);
                break;
            case "clear-night":
                icnH1.setImageResource(R.drawable.clearnight);
                break;
            default:
                icnH1.setImageResource(R.drawable.clearday);
                break;
        }
        String icH2 = data[38].replace("\"", "");
        switch (icH2) {
            case "partly-cloudy-day":
                icnH2.setImageResource(R.drawable.cloudsun);
                break;
            case "partly-cloudy-night":
                icnH2.setImageResource(R.drawable.cloudmoon);
                break;
            case "cloudy":
                icnH2.setImageResource(R.drawable.cloud);
                break;
            case "fog":
                icnH2.setImageResource(R.drawable.fog);
                break;
            case "wind":
                icnH2.setImageResource(R.drawable.wind);
                break;
            case "sleet":
                icnH2.setImageResource(R.drawable.sleet);
                break;
            case "snow":
                icnH2.setImageResource(R.drawable.snow);
                break;
            case "rain":
                icnH2.setImageResource(R.drawable.rain);
                break;
            case "clear-night":
                icnH2.setImageResource(R.drawable.clearnight);
                break;
            default:
                icnH2.setImageResource(R.drawable.clearday);
                break;
        }
        String icH3 = data[39].replace("\"", "");
        switch (icH3) {
            case "partly-cloudy-day":
                icnH3.setImageResource(R.drawable.cloudsun);
                break;
            case "partly-cloudy-night":
                icnH3.setImageResource(R.drawable.cloudmoon);
                break;
            case "cloudy":
                icnH3.setImageResource(R.drawable.cloud);
                break;
            case "fog":
                icnH3.setImageResource(R.drawable.fog);
                break;
            case "wind":
                icnH3.setImageResource(R.drawable.wind);
                break;
            case "sleet":
                icnH3.setImageResource(R.drawable.sleet);
                break;
            case "snow":
                icnH3.setImageResource(R.drawable.snow);
                break;
            case "rain":
                icnH3.setImageResource(R.drawable.rain);
                break;
            case "clear-night":
                icnH3.setImageResource(R.drawable.clearnight);
                break;
            default:
                icnH3.setImageResource(R.drawable.clearday);
                break;
        }
        String icH4 = data[40].replace("\"", "");
        switch (icH4) {
            case "partly-cloudy-day":
                icnH4.setImageResource(R.drawable.cloudsun);
                break;
            case "partly-cloudy-night":
                icnH4.setImageResource(R.drawable.cloudmoon);
                break;
            case "cloudy":
                icnH4.setImageResource(R.drawable.cloud);
                break;
            case "fog":
                icnH4.setImageResource(R.drawable.fog);
                break;
            case "wind":
                icnH4.setImageResource(R.drawable.wind);
                break;
            case "sleet":
                icnH4.setImageResource(R.drawable.sleet);
                break;
            case "snow":
                icnH4.setImageResource(R.drawable.snow);
                break;
            case "rain":
                icnH4.setImageResource(R.drawable.rain);
                break;
            case "clear-night":
                icnH4.setImageResource(R.drawable.clearnight);
                break;
            default:
                icnH4.setImageResource(R.drawable.clearday);
                break;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isSetToTrue = prefs.getBoolean("checkbox", true);
        String theme = prefs.getString("list", "1");
        if (isSetToTrue) {
            setBackground();
        } else {
            Log.d("Preferences", theme);
            if ("2".equals(theme)) containerR.setBackgroundColor(Color.parseColor("#2980b9"));
            else if ("3".equals(theme)) containerR.setBackgroundColor(Color.parseColor("#F57F17"));
            else if ("4".equals(theme)) containerR.setBackgroundColor(Color.parseColor("#263238"));
            else {
                containerR.setBackgroundColor(Color.parseColor("#424242"));
            }
        }
        //TODO Add data refresh from API
    }
}

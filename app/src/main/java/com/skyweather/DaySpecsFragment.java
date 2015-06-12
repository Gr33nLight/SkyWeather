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
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


public class DaySpecsFragment extends Fragment {
    RelativeLayout container;
    MMAdView adViewFromXml;
    private String[] data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dayspecs, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        new Graph().execute();
        ImageButton back = (ImageButton) view.findViewById(R.id.back);
        TextView timeH1, timeH2, timeH3, timeH4,timeH1G,timeH2G,timeH3G,timeH4G,timeNow;
        container = (RelativeLayout) view.findViewById(R.id.container);
        adViewFromXml = (MMAdView) view.findViewById(R.id.adView);

//MMRequest object
        MMRequest request = new MMRequest();

        adViewFromXml.setMMRequest(request);

        adViewFromXml.getAd();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.jumpToPage(0);
            }
        });
        data = MainActivity.getData();
        TextView wind, humidity, precip, summary, pressure, rainH1, rainH2, rainH3;
        wind = (TextView) view.findViewById(R.id.windText);
        humidity = (TextView) view.findViewById(R.id.humidityText);
        precip = (TextView) view.findViewById(R.id.precipText);
        summary = (TextView) view.findViewById(R.id.summary);
        pressure = (TextView) view.findViewById(R.id.pressureText);
        timeH1 = (TextView) view.findViewById(R.id.timeH1);
        timeH2 = (TextView) view.findViewById(R.id.timeH2);
        timeH3 = (TextView) view.findViewById(R.id.timeH3);
        timeH4 = (TextView) view.findViewById(R.id.timeH4);
        timeH1G = (TextView) view.findViewById(R.id.timeH1G);
        timeH2G = (TextView) view.findViewById(R.id.timeH2G);
        timeH3G = (TextView) view.findViewById(R.id.timeH3G);
        timeH4G = (TextView) view.findViewById(R.id.timeH4G);
        timeNow= (TextView) view.findViewById(R.id.timeNow);
        Calendar c = Calendar.getInstance();
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        c.add(Calendar.HOUR_OF_DAY, 12);

        wind.setText(data[5]);
        humidity.setText(data[3]);
        precip.setText(data[16]);
        summary.setText(data[17]);
        pressure.setText(data[4]);
        timeH1.setText(data[33]);
        timeH2.setText(data[34]);
        timeH3.setText(data[35]);
        timeH4.setText(data[36]);
        timeH1G.setText(data[33]);
        timeH2G.setText(data[34]);
        timeH3G.setText(data[35]);
        timeH4G.setText(data[36]);
        timeNow.setText(data[41]);
        setIcons(view);
    }

    class Graph extends AsyncTask<Void, Void, Void> {
        double[] coords;
        private ArrayList<PointValue> pointValues;

        @Override
        protected void onPreExecute() {
            coords = MainActivity.getCoords();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                LineChartView chart = (LineChartView) getActivity().findViewById(R.id.chart);
                ForecastIO fio = new ForecastIO("df2fd8be38bc1a254b610d11f1d65884");
                fio.setUnits(ForecastIO.UNITS_SI);             //sets the units as SI - optional
                fio.setExcludeURL("minutely");
                fio.setExtend(true);
                fio.getForecast("" + coords[0], "" + coords[1]);
                FIOHourly hourly = new FIOHourly(fio);
                //setting LineChart
                int j = 2;
                pointValues = new ArrayList<PointValue>();
                for (int i = 0; i <= 12; i++) {
                    Double temp = hourly.getHour(j).temperature();
                    Log.d("Forecastio", "Temp: " + temp);
                    pointValues.add(new PointValue(i, temp.intValue()));
                    j += 2;
                }
                Line line = new Line(pointValues).setColor(Color.WHITE);
                List<Line> lines = new ArrayList<Line>();
                lines.add(line);
                LineChartData dataL = new LineChartData(lines);
                dataL.setLines(lines);
                LineChartData data = new LineChartData(dataL);
                Axis axisY = new Axis().setHasLines(false);
                axisY.setName("Temperatures (" + (char) 0x00B0 + "C)");
                data.setAxisYLeft(axisY);
                chart.setInteractive(false);
                chart.setZoomEnabled(false);
                chart.setScrollEnabled(false);
                chart.setLineChartData(data);
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    }

    private void setBackground() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour >= 19 && hour <= 24 || hour >= 0 && hour < 4) {
            //sera-notte
//            chartColor=Color.parseColor("#2c3e50");
            container.setBackgroundColor(Color.parseColor("#263238"));
        } else if (hour >= 4 && hour < 12) {
            //mattina
//            chartColor=Color.parseColor("#3498db");
            container.setBackgroundColor(Color.parseColor("#2980b9"));
        } else if (hour >= 12 && hour < 19) {
            //pomeriggio
//            chartColor=Color.parseColor("#F9A825");
            container.setBackgroundColor(Color.parseColor("#F57F17"));
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
//        Log.d("onResume","Resuming");
        //Add method to change background here, pass parameters via method call
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isSetToTrue = prefs.getBoolean("checkbox", true);
        String theme = prefs.getString("list", "1");
        if (isSetToTrue) {
            setBackground();
        } else {
            Log.d("Preferences", theme);
            if ("2".equals(theme)) container.setBackgroundColor(Color.parseColor("#2980b9"));
            else if ("3".equals(theme)) container.setBackgroundColor(Color.parseColor("#F57F17"));
            else if ("4".equals(theme)) container.setBackgroundColor(Color.parseColor("#263238"));
            else {
                container.setBackgroundColor(Color.parseColor("#424242"));
            }
        }
    }
}

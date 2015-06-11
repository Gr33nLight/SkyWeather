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
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;


public class DaySpecsFragment extends Fragment {
    RelativeLayout container;
    MMAdView adViewFromXml;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dayspecs, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        new Graph().execute();
        ImageButton back = (ImageButton) view.findViewById(R.id.back);
        container = (RelativeLayout) view.findViewById(R.id.container);
        adViewFromXml = (MMAdView) view.findViewById(R.id.adView);

//MMRequest object
        MMRequest request = new MMRequest();

        adViewFromXml.setMMRequest(request);

        adViewFromXml.getAd();
//        else{
//            chartColor=Color.WHITE;
//        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.jumpToPage(0);
            }
        });
        String[] data = MainActivity.getData();
        TextView wind, humidity, precip, summary, pressure,rainH1,rainH2,rainH3;
        wind = (TextView) view.findViewById(R.id.windText);
        humidity = (TextView) view.findViewById(R.id.humidityText);
        precip = (TextView) view.findViewById(R.id.precipText);
        summary = (TextView) view.findViewById(R.id.summary);
        pressure = (TextView) view.findViewById(R.id.pressureText);
        Calendar c = Calendar.getInstance();
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        c.add(Calendar.HOUR_OF_DAY, 12);

        wind.setText(data[5]);
        humidity.setText(data[3]);
        precip.setText(data[16]);
        summary.setText(data[17]);
        pressure.setText(data[4]);
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
                ComboLineColumnChartView chart = (ComboLineColumnChartView) getActivity().findViewById(R.id.chart);
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
                //setting ColumnChart
                List<Column> columns = new ArrayList<Column>();
                List<SubcolumnValue> values;
                for (int i = 2; i <= 26; i += 2) {
                    double precip = hourly.getHour(i).precipIntensity()*10;
                    Log.d("Forecastio", "PrecipIntensity: " + precip);
                    float precipF = (float) precip;
                    values = new ArrayList<SubcolumnValue>();
                    values.add(new SubcolumnValue(precipF, Color.parseColor("#bdc3c7")));
                    columns.add(new Column(values));
                }
                ColumnChartData dataC = new ColumnChartData(columns);
                ComboLineColumnChartData data = new ComboLineColumnChartData(dataC, dataL);
                Axis axisY = new Axis().setHasLines(false);
                axisY.setName("Temperatures ("+(char)0x00B0+"C)");
                data.setAxisYLeft(axisY);
                chart.setInteractive(false);
                chart.setZoomEnabled(false);
                chart.setScrollEnabled(false);
                chart.setComboLineColumnChartData(data);
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

    @Override
    public void onResume() {
        super.onResume();
//        Log.d("onResume","Resuming");
        //Add method to change background here, pass parameters via method call
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isSetToTrue = prefs.getBoolean("checkbox", true);
        String theme =  prefs.getString("list", "1");
        if (isSetToTrue) {
            setBackground();
        }else{
            Log.d("Preferences",theme);
            if("2".equals(theme))container.setBackgroundColor(Color.parseColor("#2980b9"));
            else if("3".equals(theme))container.setBackgroundColor(Color.parseColor("#F57F17"));
            else if("4".equals(theme))container.setBackgroundColor(Color.parseColor("#263238"));
            else{
               container.setBackgroundColor(Color.parseColor("#424242"));
            }
        }
    }
}

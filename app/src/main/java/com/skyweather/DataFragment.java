package com.skyweather;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;


public class DataFragment extends android.support.v4.app.Fragment {
    private String[] data;
    private TextView day1, day2, day3, daySum, dayPrec, temp, tempMax, tempMin, day1Max, day1Min, day2Max, day2Min, day3Max, day3Min;
    private ImageView icon, icnDay1, icnDay2, icnDay3, daySpec1, daySpec2, daySpec3;
    private RelativeLayout day1Layout, day2Layout, day3Layout, myLayout, box;
    private LinearLayout weekDays, stats;
    private ImageButton goToMaps, settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getArguments().getStringArray("data");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forecast_data_fragment, container, false);
        box = (RelativeLayout) view.findViewById(R.id.daySpecs);
        myLayout = (RelativeLayout) view.findViewById(R.id.my_layout);
        weekDays = (LinearLayout) view.findViewById(R.id.weekDays);
        stats = (LinearLayout) view.findViewById(R.id.Stats);
        day1Layout = (RelativeLayout) view.findViewById(R.id.day1Layout);
        day2Layout = (RelativeLayout) view.findViewById(R.id.day2Layout);
        day3Layout = (RelativeLayout) view.findViewById(R.id.day3Layout);
        daySum = (TextView) view.findViewById(R.id.summary);
        dayPrec = (TextView) view.findViewById(R.id.dayPrec);
        temp = (TextView) view.findViewById(R.id.tempvalue);
        tempMax = (TextView) view.findViewById(R.id.max);
        tempMin = (TextView) view.findViewById(R.id.min);
        icon = (ImageView) view.findViewById(R.id.icon);
        icnDay1 = (ImageView) view.findViewById(R.id.icnDay1);
        icnDay2 = (ImageView) view.findViewById(R.id.icnDay2);
        icnDay3 = (ImageView) view.findViewById(R.id.icnDay3);
        daySpec1 = (ImageView) view.findViewById(R.id.daySpecsIcn1);
        daySpec2 = (ImageView) view.findViewById(R.id.daySpecsIcn2);
        daySpec3 = (ImageView) view.findViewById(R.id.daySpecsIcn3);
        day1 = (TextView) view.findViewById(R.id.day1);
        day2 = (TextView) view.findViewById(R.id.day2);
        day3 = (TextView) view.findViewById(R.id.day3);
        day1Max = (TextView) view.findViewById(R.id.day1Max);
        day1Min = (TextView) view.findViewById(R.id.day1Min);
        day2Max = (TextView) view.findViewById(R.id.day2Max);
        day2Min = (TextView) view.findViewById(R.id.day2Min);
        day3Max = (TextView) view.findViewById(R.id.day3Max);
        day3Min = (TextView) view.findViewById(R.id.day3Min);
        settings = (ImageButton) view.findViewById(R.id.settings);
        goToMaps = (ImageButton) view.findViewById(R.id.goToSpecs);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        setIcons();
        setWeekDays();

        temp.setText(data[0]);
        tempMax.setText(data[1]);
        tempMin.setText(data[2]);
        day1Max.setText(data[10]);
        day1Min.setText(data[11]);
        day2Max.setText(data[12]);
        day2Min.setText(data[13]);
        day3Max.setText(data[14]);
        day3Min.setText(data[15]);

        myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box != null) {
                    box.setVisibility(View.GONE);
                    day1Layout.setVisibility(View.VISIBLE);
                    day3Layout.setVisibility(View.VISIBLE);
                    day2Layout.setVisibility(View.VISIBLE);
                }
            }
        });
        day1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!box.isShown()) {
                    setDaySpecsIcons(1);
                    daySum.setText(data[27]);
                    day2Layout.setVisibility(View.GONE);
                    day3Layout.setVisibility(View.GONE);
                    box.setVisibility(View.VISIBLE);
                } else {
                    box.setVisibility(View.GONE);
                    day2Layout.setVisibility(View.VISIBLE);
                    day3Layout.setVisibility(View.VISIBLE);
                }
            }
        });
        day2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!box.isShown()) {
                    setDaySpecsIcons(2);
                    daySum.setText(data[28]);
                    day1Layout.setVisibility(View.GONE);
                    day3Layout.setVisibility(View.GONE);
                    box.setVisibility(View.VISIBLE);
                } else {
                    box.setVisibility(View.GONE);
                    day1Layout.setVisibility(View.VISIBLE);
                    day3Layout.setVisibility(View.VISIBLE);
                }

            }
        });
        day3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!box.isShown()) {
                    setDaySpecsIcons(3);
                    daySum.setText(data[29]);
                    day1Layout.setVisibility(View.GONE);
                    day2Layout.setVisibility(View.GONE);
                    box.setVisibility(View.VISIBLE);
                } else {
                    box.setVisibility(View.GONE);
                    day1Layout.setVisibility(View.VISIBLE);
                    day2Layout.setVisibility(View.VISIBLE);
                }
            }
        });
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                day1Layout.setVisibility(View.VISIBLE);
                day3Layout.setVisibility(View.VISIBLE);
                day2Layout.setVisibility(View.VISIBLE);
            }
        });
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).jumpToPage(1);
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).jumpToPage(1);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getBaseContext(), Prefs.class);
                startActivity(i);
            }
        });
        goToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).jumpToPage(1);
            }
        });
    }

    private void setIcons() {
        String mainIcn = data[6].replace("\"", "");
        switch (mainIcn) {
            case "partly-cloudy-day":
                icon.setImageResource(R.drawable.cloudsun);
                break;
            case "partly-cloudy-night":
                icon.setImageResource(R.drawable.cloudmoon);
                break;
            case "cloudy":
                icon.setImageResource(R.drawable.cloud);
                break;
            case "fog":
                icon.setImageResource(R.drawable.fog);
                break;
            case "wind":
                icon.setImageResource(R.drawable.wind);
                break;
            case "sleet":
                icon.setImageResource(R.drawable.sleet);
                break;
            case "snow":
                icon.setImageResource(R.drawable.snow);
                break;
            case "rain":
                icon.setImageResource(R.drawable.rain);
                break;
            case "clear-night":
                icon.setImageResource(R.drawable.clearnight);
                break;
            default:
                icon.setImageResource(R.drawable.clearday);
                break;
        }
        String day1icn = data[7].replace("\"", "");
        switch (day1icn) {
            case "partly-cloudy-day":
                icnDay1.setImageResource(R.drawable.cloudsun);
                break;
            case "partly-cloudy-night":
                icnDay1.setImageResource(R.drawable.cloudmoon);
                break;
            case "cloudy":
                icnDay1.setImageResource(R.drawable.cloud);
                break;
            case "fog":
                icnDay1.setImageResource(R.drawable.fog);
                break;
            case "wind":
                icnDay1.setImageResource(R.drawable.wind);
                break;
            case "sleet":
                icnDay1.setImageResource(R.drawable.sleet);
                break;
            case "snow":
                icnDay1.setImageResource(R.drawable.snow);
                break;
            case "rain":
                icnDay1.setImageResource(R.drawable.rain);
                break;
            case "clear-night":
                icnDay1.setImageResource(R.drawable.clearnight);
                break;
            default:
                icnDay1.setImageResource(R.drawable.clearday);
                break;
        }
        String day2icn = data[8].replace("\"", "");
        switch (day2icn) {
            case "partly-cloudy-day":
                icnDay2.setImageResource(R.drawable.cloudsun);
                break;
            case "partly-cloudy-night":
                icnDay2.setImageResource(R.drawable.cloudmoon);
                break;
            case "cloudy":
                icnDay2.setImageResource(R.drawable.cloud);
                break;
            case "fog":
                icnDay2.setImageResource(R.drawable.fog);
                break;
            case "wind":
                icnDay2.setImageResource(R.drawable.wind);
                break;
            case "sleet":
                icnDay2.setImageResource(R.drawable.sleet);
                break;
            case "snow":
                icnDay2.setImageResource(R.drawable.snow);
                break;
            case "rain":
                icnDay2.setImageResource(R.drawable.rain);
                break;
            case "clear-night":
                icnDay2.setImageResource(R.drawable.clearnight);
                break;
            default:
                icnDay2.setImageResource(R.drawable.clearday);
                break;
        }
        String day3icn = data[9].replace("\"", "");
        switch (day3icn) {
            case "partly-cloudy-day":
                icnDay3.setImageResource(R.drawable.cloudsun);
                break;
            case "partly-cloudy-night":
                icnDay3.setImageResource(R.drawable.cloudmoon);
                break;
            case "cloudy":
                icnDay3.setImageResource(R.drawable.cloud);
                break;
            case "fog":
                icnDay3.setImageResource(R.drawable.fog);
                break;
            case "wind":
                icnDay3.setImageResource(R.drawable.wind);
                break;
            case "sleet":
                icnDay3.setImageResource(R.drawable.sleet);
                break;
            case "snow":
                icnDay3.setImageResource(R.drawable.snow);
                break;
            case "rain":
                icnDay3.setImageResource(R.drawable.rain);
                break;
            case "clear-night":
                icnDay3.setImageResource(R.drawable.clearnight);
                break;
            default:
                icnDay3.setImageResource(R.drawable.clearday);
                break;
        }
    }

    private void setDaySpecsIcons(int n) {
        String daySpecsIcn1, daySpecsIcn2, daySpecsIcn3;

        if (n == 1) {
            daySpecsIcn1 = data[18];
            daySpecsIcn2 = data[19];
            daySpecsIcn3 = data[20];
            dayPrec.setText(data[30]);
            Log.d("ForecastioPrecipProb[]", data[30]);
        } else if (n == 2) {
            daySpecsIcn1 = data[21];
            daySpecsIcn2 = data[22];
            daySpecsIcn3 = data[23];
            dayPrec.setText(data[31]);
            Log.d("ForecastioPrecipProb[]", data[31]);
        } else {
            daySpecsIcn1 = data[24];
            daySpecsIcn2 = data[25];
            daySpecsIcn3 = data[26];
            dayPrec.setText(data[32]);
            Log.d("ForecastioPrecipProb[]", data[32]);
        }
        daySpecsIcn1 = daySpecsIcn1.replace("\"", "");
        daySpecsIcn2 = daySpecsIcn2.replace("\"", "");
        daySpecsIcn3 = daySpecsIcn3.replace("\"", "");
        switch (daySpecsIcn1) {
            case "partly-cloudy-day":
                daySpec1.setImageResource(R.drawable.cloudsun);
                break;
            case "partly-cloudy-night":
                daySpec1.setImageResource(R.drawable.cloudmoon);
                break;
            case "cloudy":
                daySpec1.setImageResource(R.drawable.cloud);
                break;
            case "fog":
                daySpec1.setImageResource(R.drawable.fog);
                break;
            case "wind":
                daySpec1.setImageResource(R.drawable.wind);
                break;
            case "sleet":
                daySpec1.setImageResource(R.drawable.sleet);
                break;
            case "snow":
                daySpec1.setImageResource(R.drawable.snow);
                break;
            case "rain":
                daySpec1.setImageResource(R.drawable.rain);
                break;
            case "clear-night":
                daySpec1.setImageResource(R.drawable.clearnight);
                break;
            default:
                daySpec1.setImageResource(R.drawable.clearday);
                break;
        }
        switch (daySpecsIcn2) {
            case "partly-cloudy-day":
                daySpec2.setImageResource(R.drawable.cloudsun);
                break;
            case "partly-cloudy-night":
                daySpec2.setImageResource(R.drawable.cloudmoon);
                break;
            case "cloudy":
                daySpec2.setImageResource(R.drawable.cloud);
                break;
            case "fog":
                daySpec2.setImageResource(R.drawable.fog);
                break;
            case "wind":
                daySpec2.setImageResource(R.drawable.wind);
                break;
            case "sleet":
                daySpec2.setImageResource(R.drawable.sleet);
                break;
            case "snow":
                daySpec2.setImageResource(R.drawable.snow);
                break;
            case "rain":
                daySpec2.setImageResource(R.drawable.rain);
                break;
            case "clear-night":
                daySpec2.setImageResource(R.drawable.clearnight);
                break;
            default:
                daySpec2.setImageResource(R.drawable.clearday);
                break;
        }
        switch (daySpecsIcn3) {
            case "partly-cloudy-day":
                daySpec3.setImageResource(R.drawable.cloudsun);
                break;
            case "partly-cloudy-night":
                daySpec3.setImageResource(R.drawable.cloudmoon);
                break;
            case "cloudy":
                daySpec3.setImageResource(R.drawable.cloud);
                break;
            case "fog":
                daySpec3.setImageResource(R.drawable.fog);
                break;
            case "wind":
                daySpec3.setImageResource(R.drawable.wind);
                break;
            case "sleet":
                daySpec3.setImageResource(R.drawable.sleet);
                break;
            case "snow":
                daySpec3.setImageResource(R.drawable.snow);
                break;
            case "rain":
                daySpec3.setImageResource(R.drawable.rain);
                break;
            case "clear-night":
                daySpec3.setImageResource(R.drawable.clearnight);
                break;
            default:
                daySpec3.setImageResource(R.drawable.clearday);
                break;
        }
    }

    private void setWeekDays() {
        Calendar calendar = Calendar.getInstance();
        int CurrentDay = calendar.get(Calendar.DAY_OF_WEEK);
        switch (CurrentDay) {
            case Calendar.SUNDAY:
                day1.setText("Mon");
                day2.setText("Tue");
                day3.setText("Wed");
                break;
            case Calendar.MONDAY:
                day1.setText("Tue");
                day2.setText("Wed");
                day3.setText("Thu");
                break;
            case Calendar.TUESDAY:
                day1.setText("Wed");
                day2.setText("Thu");
                day3.setText("Fri");
                break;
            case Calendar.WEDNESDAY:
                day1.setText("Thu");
                day2.setText("Fri");
                day3.setText("Sat");
                break;
            case Calendar.THURSDAY:
                day1.setText("Fri");
                day2.setText("Sat");
                day3.setText("Sun");
                break;
            case Calendar.FRIDAY:
                day1.setText("Sat");
                day2.setText("Sun");
                day3.setText("Mon");
                break;
            case Calendar.SATURDAY:
                day1.setText("Sun");
                day2.setText("Mon");
                day3.setText("Tue");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public void setBackground() {
        int backSera = Color.parseColor("#2c3e50");
        int foreSera = Color.parseColor("#263238");
        int daySpecSera = Color.parseColor("#FF111F25");
        int backMattina = Color.parseColor("#3498db");
        int foreMattina = Color.parseColor("#2980b9");
        int daySpecMattina = Color.parseColor("#FF004B88");
        int backPom = Color.parseColor("#F9A825");
        int forePom = Color.parseColor("#F57F17");
        int daySpecPom = Color.parseColor("#B35A12");
        int colorFrom = Color.parseColor("#424242");
        int colorToB = 0;
        int colorToF = 0;
        int colorToSpecs = 0;
        int duration = 1000;
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour >= 19 && hour <= 24 || hour >= 0 && hour < 4) {
            //sera-notte
//            myLayout.setBackgroundColor(Color.parseColor("#2c3e50"));
//            weekDays.setBackgroundColor(Color.parseColor("#263238"));
//            stats.setBackgroundColor(Color.parseColor("#263238"));
            Log.d("Background color", "setting notte");
            colorToB = backSera;
            colorToF = foreSera;
            colorToSpecs = daySpecSera;

        } else if (hour >= 4 && hour < 12) {
            //mattina
//            myLayout.setBackgroundColor(Color.parseColor("#90CAF9"));
//            weekDays.setBackgroundColor(Color.parseColor("#0091EA"));
//            stats.setBackgroundColor(Color.parseColor("#0091EA"));
            Log.d("Background color", "setting mattina");

            colorToB = backMattina;
            colorToF = foreMattina;
            colorToSpecs = daySpecMattina;
        } else if (hour >= 12 && hour < 19) {
            //pomeriggio
//            myLayout.setBackgroundColor(Color.parseColor("#F9A825"));
//            weekDays.setBackgroundColor(Color.parseColor("#F57F17"));
//            stats.setBackgroundColor(Color.parseColor("#F57F17"));
            Log.d("Background color", "setting pomeriggio");
            colorToB = backPom;
            colorToF = forePom;
            colorToSpecs = daySpecPom;
        }
        ObjectAnimator.ofObject(myLayout, "backgroundColor", new ArgbEvaluator(), colorFrom, colorToB)
                .setDuration(duration)
                .start();
        ObjectAnimator.ofObject(weekDays, "backgroundColor", new ArgbEvaluator(), colorFrom, colorToF)
                .setDuration(duration)
                .start();
        ObjectAnimator.ofObject(stats, "backgroundColor", new ArgbEvaluator(), colorFrom, colorToB)
                .setDuration(duration)
                .start();
        ObjectAnimator.ofObject(box, "backgroundColor", new ArgbEvaluator(), colorFrom, colorToSpecs)
                .setDuration(duration)
                .start();
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
            if ("2".equals(theme)) {
                myLayout.setBackgroundColor(Color.parseColor("#3498db"));
                stats.setBackgroundColor(Color.parseColor("#3498db"));
                weekDays.setBackgroundColor(Color.parseColor("#2980b9"));
                box.setBackgroundColor(Color.parseColor("#FF004B88"));
            } else if ("3".equals(theme)) {
                myLayout.setBackgroundColor(Color.parseColor("#F9A825"));
                stats.setBackgroundColor(Color.parseColor("#F9A825"));
                weekDays.setBackgroundColor(Color.parseColor("#F57F17"));
                box.setBackgroundColor(Color.parseColor("#B35A12"));
            } else if ("4".equals(theme)) {
                myLayout.setBackgroundColor(Color.parseColor("#2c3e50"));
                stats.setBackgroundColor(Color.parseColor("#2c3e50"));
                weekDays.setBackgroundColor(Color.parseColor("#263238"));
                box.setBackgroundColor(Color.parseColor("#FF111F25"));
            } else {
                myLayout.setBackgroundColor(Color.parseColor("#424242"));
                stats.setBackgroundColor(Color.parseColor("#424242"));
                weekDays.setBackgroundColor(Color.parseColor("#ff242424"));
                box.setBackgroundColor(Color.parseColor("#ff141414"));
            }
        }
        //TODO Add data refresh from API
    }

}



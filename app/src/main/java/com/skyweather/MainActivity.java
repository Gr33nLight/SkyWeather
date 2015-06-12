package com.skyweather;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.ForecastIO;

import java.util.Calendar;

public class MainActivity extends ActionBarActivity {
    private static final String tag = "FORECASTIO by gr33n";
    private static final int NUM_PAGES = 2;
    public static ViewPager mPager;
    private static String[] out;
    private static double coordinates[] = new double[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new CallApi().execute();
    }

    private class CallApi extends AsyncTask<Void, Void, String[]> {
        private boolean ok = false;

        @Override
        protected void onPreExecute() {
            GPSTracker gps = new GPSTracker(MainActivity.this);
            Log.d("callapi", "NetCeck: " + netCheckin());
            Log.d("callapi", "CanGetLocation: " + gps.canGetLocation());
            if (gps.canGetLocation() && netCheckin()) {
                ok = true;
                coordinates[0] = gps.getLatitude();
                coordinates[1] = gps.getLongitude();
            } else {
                ok = false;
                finish();
                Log.d("callapi", "isGpsOn: " + isGpsOn(getBaseContext()));
                if (!netCheckin() && isGpsOn(getBaseContext()))
                    Toast.makeText(getBaseContext(), "Please enable Network connectivity", Toast.LENGTH_SHORT).show();
                else if (!isGpsOn(getBaseContext()) && netCheckin())
                    Toast.makeText(getBaseContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getBaseContext(), "Please enable Connectivity", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Log.d("callapi", "Coords: " + coordinates[0] + "," + coordinates[1]);
                if (coordinates[0] != 0 && coordinates[1] != 0) {
                    ForecastIO fio = new ForecastIO("df2fd8be38bc1a254b610d11f1d65884"); //instantiate the class with the API key.
                    fio.setUnits(ForecastIO.UNITS_SI);             //sets the units as SI - optional
                    fio.setExcludeURL("minutely");
                    fio.setExtend(true);
                    fio.getForecast("" + coordinates[0], "" + coordinates[1]);
                    FIOCurrently currently = new FIOCurrently(fio);
                    FIODaily daily = new FIODaily(fio);
                    FIOHourly hourly = new FIOHourly(fio);
                    String icn = currently.get().icon();
                    int tempI = currently.get().temperature().intValue();
                    String temp = Integer.toString(tempI);
                    String tmax =Integer.toString(daily.getDay(0).temperatureMax().intValue());
                    String tmin =Integer.toString(daily.getDay(0).temperatureMin().intValue());
                    Double humidityVal = currently.get().humidity() * 100;
                    String humidity = String.format("%.0f", humidityVal)+"%";
                    String pressure = String.format("%.0f", currently.get().pressure())+" mb";
//               multiply 1.609 to convert to kmh
                    Double windVal = currently.get().windSpeed() * 1.609;
                    String wind = String.format("%.0f", windVal)+" Km/h";
                    Double precipVal = currently.get().precipProbability() * 100;
                    String precip = String.format("%.0f", precipVal)+"%";
//                    Log.d("Callapi", "PrecipProbability: " + currently.get().precipProbability());
                    String summray = fio.getHourly().get("summary").toString();
                    String day1ico = daily.getDay(1).icon();
                    String day2ico = daily.getDay(2).icon();
                    String day3ico = daily.getDay(3).icon();
                    String day1Sum = daily.getDay(1).summary();
                    String day2Sum = daily.getDay(2).summary();
                    String day3Sum = daily.getDay(3).summary();
                    boolean trovato = false;
                    int i = 2;
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, 1);
                    while (i < 30) {
                        if (Integer.parseInt(hourly.getHour(i).time().substring(0, 2)) == cal.get(Calendar.DAY_OF_MONTH)) {
                            trovato = true;
                            break;
                        }
                        i++;
                    }
                    String day1MorningIcn, day1AfternoonIcn, day1EveningIcn, day2MorningIcn, day2AfternoonIcnn, day2EveningIcn,
                            day3MorningIcn, day3AfternoonIcn, day3EveningIcn;
                    int mattinaH = 7, pomeriggioH = 17, seraH = 20;
                    if (trovato) {
                        day1MorningIcn = hourly.getHour(i + mattinaH).icon();
                        day1AfternoonIcn = hourly.getHour(i + pomeriggioH).icon();
                        day1EveningIcn = hourly.getHour(i + seraH).icon();
                        day2MorningIcn = hourly.getHour(i + 24 + mattinaH).icon();
                        day2AfternoonIcnn = hourly.getHour(i + 24 + pomeriggioH).icon();
                        day2EveningIcn = hourly.getHour(i + 24 + seraH).icon();
                        day3MorningIcn = hourly.getHour(i + 48 + mattinaH).icon();
                        day3AfternoonIcn = hourly.getHour(i + 48 + pomeriggioH).icon();
                        day3EveningIcn = hourly.getHour(i + 48 + seraH).icon();
                    } else {
                        day1MorningIcn = "NA";
                        day1AfternoonIcn = "NA";
                        day1EveningIcn = "NA";
                        day2MorningIcn = "NA";
                        day2AfternoonIcnn = "NA";
                        day2EveningIcn = "NA";
                        day3MorningIcn = "NA";
                        day3AfternoonIcn = "NA";
                        day3EveningIcn = "NA";
                    }
                    String day1Max = Integer.toString(daily.getDay(1).temperatureMax().intValue());
                    String day1Min = Integer.toString(daily.getDay(1).temperatureMin().intValue());
                    String day2Max = Integer.toString(daily.getDay(2).temperatureMax().intValue());
                    String day2Min = Integer.toString(daily.getDay(2).temperatureMin().intValue());
                    String day3Max = Integer.toString(daily.getDay(3).temperatureMax().intValue());
                    String day3Min = Integer.toString(daily.getDay(3).temperatureMin().intValue());
                    Double day1PrecVal = daily.getDay(1).precipProbability()*100;
                    String day1Prec =String.format("%.0f", day1PrecVal)+"%";
                    Double day2PrecVal = daily.getDay(2).precipProbability()*100;
                    String day2Prec =String.format("%.0f", day2PrecVal)+"%";
                    Double day3PrecVal = daily.getDay(3).precipProbability()*100;
                    String day3Prec =String.format("%.0f", day3PrecVal)+"";
                    return new String[]{temp, tmax, tmin, humidity, pressure, wind, icn, day1ico, day2ico, day3ico, day1Max, day1Min, day2Max, day2Min, day3Max,
                            day3Min, precip, summray, day1MorningIcn, day1AfternoonIcn, day1EveningIcn, day2MorningIcn, day2AfternoonIcnn, day2EveningIcn, day3MorningIcn, day3AfternoonIcn,
                            day3EveningIcn, day1Sum, day2Sum, day3Sum,day1Prec,day2Prec,day3Prec};
                }
                return null;
            } catch (Exception e) {
                Log.d("Callapi","Exception!!");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] output) {
            if (output != null) {
                out = output;
                switchC();
            }else if(ok){
                Log.d("Callapi","Trying again!!");
                try{
                    Thread.sleep(500);
                }catch (InterruptedException e) {}
                new CallApi().execute();
            }
        }
    }

    private void switchC() {
        Log.d("CALLAPI", "Switching to DataFragment view");
        setContentView(R.layout.screen_slider);
        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
    }

    public static String[] getData() {
        if (out != null) {
            return out;
        } else {
            return new String[]{"NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA"
                    , "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA"};
            //30 params
        }
    }

    private boolean netCheckin() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private boolean isGpsOn(Context mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void jumpToPage(int view) {
        mPager.setCurrentItem(view);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
                    // If the user is currently looking at the first step, allow the system to handle the
                    // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new DataFragment();
                case 1:
                    return new DaySpecsFragment();
                default: return new DataFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    public static double[] getCoords() {
        //requestSingleUpdate and then call this from the onResume status
        return coordinates;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), Prefs.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}

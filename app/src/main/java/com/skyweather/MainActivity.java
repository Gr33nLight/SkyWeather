package com.skyweather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 10;
    private static final String TAG = "Gr33nDebug";
    public ViewPager mPager;
    private String[] out;
    private GoogleApiClient mGoogleApiClient;
    protected Boolean mRequestingLocationUpdates = true;
    protected LocationRequest mLocationRequest;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    protected Location mCurrentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildGoogleApiClient();
    }

    private class CallApi extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                if (mCurrentLocation != null) {
                    ForecastIO fio = new ForecastIO("df2fd8be38bc1a254b610d11f1d65884"); //instantiate the class with the API key.
                    fio.setUnits(ForecastIO.UNITS_SI);             //sets the units as SI - optional
                    fio.setExcludeURL("minutely");
                    fio.setExtend(true);
                    fio.getForecast("" + mCurrentLocation.getLatitude(), "" + mCurrentLocation.getLongitude());
                    FIOCurrently currently = new FIOCurrently(fio);
                    FIODaily daily = new FIODaily(fio);
                    FIOHourly hourly = new FIOHourly(fio);
                    String icn = currently.get().icon();
                    int tempI = currently.get().temperature().intValue();
                    String temp = Integer.toString(tempI);
                    String tmax = Integer.toString(daily.getDay(0).temperatureMax().intValue());
                    String tmin = Integer.toString(daily.getDay(0).temperatureMin().intValue());
                    Double humidityVal = currently.get().humidity() * 100;
                    String humidity = String.format("%.0f", humidityVal) + "%";
                    String pressure = String.format("%.0f", currently.get().pressure()) + " mb";
//               multiply 1.609 to convert to kmh
                    Double windVal = currently.get().windSpeed() * 1.609;
                    String wind = String.format("%.0f", windVal) + " Km/h";
                    Double precipVal = currently.get().precipProbability() * 100;
                    String precip = String.format("%.0f", precipVal) + "%";
//                    Log.d("Callapi", "PrecipProbability: " + currently.get().precipProbability());
                    String summray = fio.getHourly().get("summary").toString();
                    String day1ico = daily.getDay(1).icon();
                    String day2ico = daily.getDay(2).icon();
                    String day3ico = daily.getDay(3).icon();
                    String day1Sum = daily.getDay(1).summary();
                    String day2Sum = daily.getDay(2).summary();
                    String day3Sum = daily.getDay(3).summary();
                    int i = 2;
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, 1);
                    while (i < 30) {
                        if (Integer.parseInt(hourly.getHour(i).time().substring(0, 2)) == cal.get(Calendar.DAY_OF_MONTH)) {
                            break;
                        }
                        i++;
                    }
                    String day1MorningIcn, day1AfternoonIcn, day1EveningIcn, day2MorningIcn, day2AfternoonIcnn, day2EveningIcn,
                            day3MorningIcn, day3AfternoonIcn, day3EveningIcn, icnH1, icnH2, icnH3, icnH4, timeH1, timeH2, timeH3, timeH4, timeNow;
                    int mattinaH = 7, pomeriggioH = 17, seraH = 20;
                    timeH1 = hourly.getHour(8).time().substring(11, 16);
                    timeH2 = hourly.getHour(14).time().substring(11, 16);
                    timeH3 = hourly.getHour(20).time().substring(11, 16);
                    timeH4 = hourly.getHour(26).time().substring(11, 16);
                    timeNow = hourly.getHour(2).time().substring(11, 16);
                    icnH1 = hourly.getHour(8).icon();
                    icnH2 = hourly.getHour(14).icon();
                    icnH3 = hourly.getHour(20).icon();
                    icnH4 = hourly.getHour(26).icon();
                    day1MorningIcn = hourly.getHour(i + mattinaH).icon();
                    day1AfternoonIcn = hourly.getHour(i + pomeriggioH).icon();
                    day1EveningIcn = hourly.getHour(i + seraH).icon();
                    day2MorningIcn = hourly.getHour(i + 24 + mattinaH).icon();
                    day2AfternoonIcnn = hourly.getHour(i + 24 + pomeriggioH).icon();
                    day2EveningIcn = hourly.getHour(i + 24 + seraH).icon();
                    day3MorningIcn = hourly.getHour(i + 48 + mattinaH).icon();
                    day3AfternoonIcn = hourly.getHour(i + 48 + pomeriggioH).icon();
                    day3EveningIcn = hourly.getHour(i + 48 + seraH).icon();
                    String day1Max = Integer.toString(daily.getDay(1).temperatureMax().intValue());
                    String day1Min = Integer.toString(daily.getDay(1).temperatureMin().intValue());
                    String day2Max = Integer.toString(daily.getDay(2).temperatureMax().intValue());
                    String day2Min = Integer.toString(daily.getDay(2).temperatureMin().intValue());
                    String day3Max = Integer.toString(daily.getDay(3).temperatureMax().intValue());
                    String day3Min = Integer.toString(daily.getDay(3).temperatureMin().intValue());
                    Double day1PrecVal = daily.getDay(1).precipProbability() * 100;
                    String day1Prec = String.format("%.0f", day1PrecVal) + "%";
                    Double day2PrecVal = daily.getDay(2).precipProbability() * 100;
                    String day2Prec = String.format("%.0f", day2PrecVal) + "%";
                    Double day3PrecVal = daily.getDay(3).precipProbability() * 100;
                    String day3Prec = String.format("%.0f", day3PrecVal) + "%";
                    return new String[]{temp, tmax, tmin, humidity, pressure, wind, icn, day1ico, day2ico, day3ico, day1Max, day1Min, day2Max, day2Min, day3Max,
                            day3Min, precip, summray, day1MorningIcn, day1AfternoonIcn, day1EveningIcn, day2MorningIcn, day2AfternoonIcnn, day2EveningIcn, day3MorningIcn, day3AfternoonIcn,
                            day3EveningIcn, day1Sum, day2Sum, day3Sum, day1Prec, day2Prec, day3Prec, timeH1, timeH2, timeH3, timeH4, icnH1, icnH2, icnH3, icnH4, timeNow};
                }
                return null;
            } catch (Exception e) {
                Log.d("Callapi", "Exception!!");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] array) {
            if (array != null) {
                out = array;
                setContentView(R.layout.screen_slider);
                mPager = (ViewPager) findViewById(R.id.viewpager);
                mPager.setOffscreenPageLimit(2);
                setupViewPager(mPager);
            } else {
                Log.e(TAG, "Error retrieving data");
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        DaySpecsFragment daySpecs = new DaySpecsFragment();
        DataFragment dataF = new DataFragment();
//        adapter.addFragment(daySpecs, "");
        adapter.addFragment(dataF, "");
        viewPager.setAdapter(adapter);
    }

    protected String[] getData() {
        if (out != null) {
            return out;
        } else {
            return new String[]{"NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA"
                    , "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA"};
            //30 params
        }
    }

    public void jumpToPage(int view) {
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

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        createLocationRequest();
    }

    //Creates the location request to be performed by calling startLocationUpdates()
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(UPDATE_INTERVAL_IN_MILLISECONDS / 2);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        Log.e(TAG, "Connected to PlayServices");

        //Retrieving nearby places list based on current location
        if (ActivityCompat.checkSelfPermission(getBaseContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        } else {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                mCurrentLocation = mLastLocation;
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        stopLocationUpdates();
        new CallApi().execute();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        new CallApi().execute();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getBaseContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getBaseContext().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


}

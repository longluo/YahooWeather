package com.imlongluo.weather;

import com.imlongluo.weather.apis.WeatherInfo;
import com.imlongluo.weather.apis.WeatherInfo.ForecastInfo;
import com.imlongluo.weather.apis.YahooWeather;
import com.imlongluo.weather.apis.YahooWeather.SEARCH_MODE;
import com.imlongluo.weather.apis.YahooWeatherExceptionListener;
import com.imlongluo.weather.apis.YahooWeatherInfoListener;
import com.imlongluo.weather.apis.YahooWeatherLog;
import com.imlongluo.weather.lbs.LocationActivity;
import com.imlongluo.weather.settings.SettingsActivity;
import com.imlongluo.weather.share.ShareActivity;
import com.imlongluo.weather.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements YahooWeatherInfoListener,
        YahooWeatherExceptionListener {
    private static final String TAG = "YahooWeather";

    private ImageView mIvWeather0;
    private TextView mTvWeather0;
    private TextView mTvErrorMessage;
    private TextView mTvTitle;
    private EditText mEtAreaOfCity;
    private Button mBtSearch;
    private Button mBtGPS;
//    private Button mBtnBaiduLBS;
    private LinearLayout mWeatherInfosLayout;

    private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, 5000, true);

    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        YahooWeatherLog.d("onCreate");

        mYahooWeather.setExceptionListener(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        mTvTitle = (TextView) findViewById(R.id.textview_title);
        mTvWeather0 = (TextView) findViewById(R.id.textview_weather_info_0);
        mTvErrorMessage = (TextView) findViewById(R.id.textview_error_message);
        mIvWeather0 = (ImageView) findViewById(R.id.imageview_weather_info_0);

        mEtAreaOfCity = (EditText) findViewById(R.id.edittext_area);

        mBtSearch = (Button) findViewById(R.id.search_button);
        mBtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _location = mEtAreaOfCity.getText().toString();
                if (!TextUtils.isEmpty(_location)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtAreaOfCity.getWindowToken(), 0);
                    searchByPlaceName(_location);
                    showProgressDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "location is not inputted", 1).show();
                }
            }
        });

        mBtGPS = (Button) findViewById(R.id.gps_button);
        mBtGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByGPS();
                showProgressDialog();
            }
        });

/*        mBtnBaiduLBS = (Button) findViewById(R.id.baidu_lbs_button);
        mBtnBaiduLBS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "");
                searchByGPS();
                showProgressDialog();
            }
        });*/

        mWeatherInfosLayout = (LinearLayout) findViewById(R.id.weather_infos);

        searchByGPS();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        hideProgressDialog();
        mProgressDialog = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, 1, "Location");
        menu.add(Menu.NONE, 2, 2, "Share");
        menu.add(Menu.NONE, 3, 3, "Settings");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        YahooWeatherLog.d("onOptionsItemSelected, id=" + item.getItemId());
        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
                break;

            case 2:
                // showShare();
                String weatherInfo = mTvWeather0.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("weather", weatherInfo);
                intent.setClass(MainActivity.this, ShareActivity.class);
                startActivity(intent);
                break;

            // case 3:
            // File tempFile = new
            // File("SDCard/YahooWeather/ScreenImage/Screen1.png");
            // shareToWechatMoments(tempFile);
            // break;

            case 3:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void gotWeatherInfo(WeatherInfo weatherInfo) {
        hideProgressDialog();
        if (weatherInfo != null) {
            setNormalLayout();
            if (mYahooWeather.getSearchMode() == SEARCH_MODE.GPS) {
                mEtAreaOfCity.setText("YOUR CURRENT LOCATION");
            }
            mWeatherInfosLayout.removeAllViews();
            mTvTitle.setText(weatherInfo.getTitle() + "\n" + weatherInfo.getWOEIDneighborhood()
                    + ", " + weatherInfo.getWOEIDCounty() + ", " + weatherInfo.getWOEIDState()
                    + ", " + weatherInfo.getWOEIDCountry());
            mTvWeather0.setText("====== CURRENT ======" + "\n" + "date: "
                    + weatherInfo.getCurrentConditionDate() + "\n" + "weather: "
                    + weatherInfo.getCurrentText() + "\n" + "temperature in 潞C: "
                    + weatherInfo.getCurrentTempC() + "\n" + "temperature in 潞F: "
                    + weatherInfo.getCurrentTempF() + "\n" + "wind chill in 潞F: "
                    + weatherInfo.getWindChill() + "\n" + "wind direction: "
                    + weatherInfo.getWindDirection() + "\n" + "wind speed: "
                    + weatherInfo.getWindSpeed() + "\n" + "Humidity: "
                    + weatherInfo.getAtmosphereHumidity() + "\n" + "Pressure: "
                    + weatherInfo.getAtmospherePressure() + "\n" + "Visibility: "
                    + weatherInfo.getAtmosphereVisibility());
            if (weatherInfo.getCurrentConditionIcon() != null) {
                mIvWeather0.setImageBitmap(weatherInfo.getCurrentConditionIcon());
            }
            for (int i = 0; i < YahooWeather.FORECAST_INFO_MAX_SIZE; i++) {
                final LinearLayout forecastInfoLayout = (LinearLayout) getLayoutInflater().inflate(
                        R.layout.forecastinfo, null);
                final TextView tvWeather = (TextView) forecastInfoLayout
                        .findViewById(R.id.textview_forecast_info);
                final ForecastInfo forecastInfo = weatherInfo.getForecastInfoList().get(i);
                tvWeather.setText("====== FORECAST " + (i + 1) + " ======" + "\n" + "date: "
                        + forecastInfo.getForecastDate() + "\n" + "weather: "
                        + forecastInfo.getForecastText() + "\n" + "low  temperature in 潞C: "
                        + forecastInfo.getForecastTempLowC() + "\n" + "high temperature in 潞C: "
                        + forecastInfo.getForecastTempHighC() + "\n" + "low  temperature in 潞F: "
                        + forecastInfo.getForecastTempLowF() + "\n" + "high temperature in 潞F: "
                        + forecastInfo.getForecastTempHighF() + "\n");
                final ImageView ivForecast = (ImageView) forecastInfoLayout
                        .findViewById(R.id.imageview_forecast_info);
                if (forecastInfo.getForecastConditionIcon() != null) {
                    ivForecast.setImageBitmap(forecastInfo.getForecastConditionIcon());
                }
                mWeatherInfosLayout.addView(forecastInfoLayout);
            }
        } else {
            setNoResultLayout();
        }
    }

    @Override
    public void onFailConnection(final Exception e) {

    }

    @Override
    public void onFailParsing(final Exception e) {

    }

    @Override
    public void onFailFindLocation(final Exception e) {

    }

    private void setNormalLayout() {
        mWeatherInfosLayout.setVisibility(View.VISIBLE);
        mTvTitle.setVisibility(View.VISIBLE);
        mTvErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void setNoResultLayout() {
        mTvTitle.setVisibility(View.INVISIBLE);
        mWeatherInfosLayout.setVisibility(View.INVISIBLE);
        mTvErrorMessage.setVisibility(View.VISIBLE);
        mTvErrorMessage.setText("Sorry, no result returned");
        mProgressDialog.cancel();
    }

    private void searchByGPS() {
        YahooWeatherLog.d("searchByGPS");

        mYahooWeather.setNeedDownloadIcons(true);
        mYahooWeather.setSearchMode(SEARCH_MODE.GPS);
        mYahooWeather.queryYahooWeatherByGPS(getApplicationContext(), this);
    }

    private void searchByPlaceName(String location) {
        mYahooWeather.setNeedDownloadIcons(true);
        mYahooWeather.setSearchMode(SEARCH_MODE.PLACE_NAME);
        mYahooWeather.queryYahooWeatherByPlaceName(getApplicationContext(), location,
                MainActivity.this);
    }

    private void showProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
}

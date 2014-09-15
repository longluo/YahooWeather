package com.imlongluo.weather;

import java.io.File;
import java.io.FileOutputStream;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.imlongluo.weather.apis.WeatherInfo;
import com.imlongluo.weather.apis.WeatherInfo.ForecastInfo;
import com.imlongluo.weather.apis.YahooWeather;
import com.imlongluo.weather.apis.YahooWeather.SEARCH_MODE;
import com.imlongluo.weather.apis.YahooWeatherExceptionListener;
import com.imlongluo.weather.apis.YahooWeatherInfoListener;
import com.imlongluo.weather.apis.YahooWeatherLog;
import com.imlongluo.weather.lbs.LocationActivity;
import com.imlongluo.weather.settings.SettingsActivity;
import com.imlongluo.weather.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
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
    private Button mBtnBaiduLBS;
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

        mBtnBaiduLBS = (Button) findViewById(R.id.baidu_lbs_button);
        mBtnBaiduLBS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "");
                searchByGPS();
                showProgressDialog();
            }
        });

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
        menu.add(Menu.NONE, 1, 1, "地理位置");
        menu.add(Menu.NONE, 2, 2, "分享");
        menu.add(Menu.NONE, 3, 3, "分享至朋友圈");
        menu.add(Menu.NONE, 4, 4, "设置");
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
                showShare();
                break;

            case 3:
                File tempFile = new File("SDCard/YahooWeather/ScreenImage/Screen1.png");
                shareToWechatMoments(tempFile);
                break;

            case 4:
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
                    + weatherInfo.getCurrentText() + "\n" + "temperature in ºC: "
                    + weatherInfo.getCurrentTempC() + "\n" + "temperature in ºF: "
                    + weatherInfo.getCurrentTempF() + "\n" + "wind chill in ºF: "
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
                        + forecastInfo.getForecastText() + "\n" + "low  temperature in ºC: "
                        + forecastInfo.getForecastTempLowC() + "\n" + "high temperature in ºC: "
                        + forecastInfo.getForecastTempHighC() + "\n" + "low  temperature in ºF: "
                        + forecastInfo.getForecastTempLowF() + "\n" + "high temperature in ºF: "
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

    private void showShare() {
        String shareString = mTvTitle.getText().toString() + mTvWeather0.getText().toString();
        String shareTitleString = "YahooWeather By Long Luo";

        YahooWeatherLog.d("showShare, title=" + shareTitleString + ",content=" + shareString);

        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字
        oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用

        // oks.setTitle(getString(R.string.share));
        oks.setTitle(shareTitleString);

        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://imlongluo.com");

        // text是分享文本，所有平台都需要这个字段
        // oks.setText("我是分享文本");
        oks.setText(shareString);

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数

        GetAndSaveCurrentScreenImage();
        // oks.setImagePath("/sdcard/test.jpg");
        oks.setImagePath("SDCard/YahooWeather/ScreenImage/Screen1.png");

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://imlongluo.com/weather");
        
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://imlongluo.com");

        // 启动分享GUI
        oks.show(this);
    }

    private void shareToWechatMoments(File file) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, "From Yahoo Weather, http://www.imlongluo.com/weather");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(intent);
    }

    /**
     * 获取和保存当前屏幕的截图
     */
    private void GetAndSaveCurrentScreenImage() {
        // 1.构建Bitmap
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();

        Bitmap Bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);

        // 2.获取屏幕
        View decorview = this.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        Bmp = decorview.getDrawingCache();

        String SavePath = getSDCardPath() + "/YahooWeather/ScreenImage";

        // 3.保存Bitmap
        try {
            File path = new File(SavePath);
            // 文件
            String filepath = SavePath + "/Screen1.png";
            File file = new File(filepath);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (null != fos) {
                Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();

                Toast.makeText(MainActivity.this, "截屏文件已保存至SDCard/YahooWeather/ScreenImage/下",
                        Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取SDCard的目录路径功能
     * 
     * @return
     */
    private String getSDCardPath() {
        File sdcardDir = null;
        // 判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }

        return sdcardDir.toString();
    }
}

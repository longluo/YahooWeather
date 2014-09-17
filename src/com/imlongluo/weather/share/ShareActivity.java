package com.imlongluo.weather.share;

import java.io.File;
import java.io.FileOutputStream;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.imlongluo.weather.apis.YahooWeatherLog;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imlongluo.weather.R;

public class ShareActivity extends Activity {
    private static final String TAG = ShareActivity.class.getSimpleName();

    private ImageView mWeatherImage;
    private TextView mWeatherDetail;
    private Button mShareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);

        String weatherDetail = getIntent().getStringExtra("weather");

        Log.d(TAG, "weatherDetail=" + weatherDetail);

        mWeatherImage = (ImageView) findViewById(R.id.weather_screen);
        mWeatherDetail = (TextView) findViewById(R.id.weather_detail);

        mShareButton = (Button) findViewById(R.id.btn_share);
        mShareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showShare();
            }
        });

        mWeatherDetail.setText(weatherDetail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void openContextMenu(View view) {
        super.openContextMenu(view);
    }

    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();
    }

    private void showShare() {
        // String shareString = mTvTitle.getText().toString() +
        // mTvWeather0.getText().toString();
        String shareTitleString = "YahooWeather By Long Luo";

        // YahooWeatherLog.d("showShare, title=" + shareTitleString +
        // ",content=" + shareString);

        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        // 鍏抽棴sso鎺堟潈
        oks.disableSSOWhenAuthorize();

        // 鍒嗕韩鏃禢otification鐨勫浘鏍囧拰鏂囧瓧
        oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title鏍囬锛屽嵃璞＄瑪璁般�閭銆佷俊鎭�寰俊銆佷汉浜虹綉鍜孮Q绌洪棿浣跨敤

        oks.setTitle(getString(R.string.share));
        // oks.setTitle("");

        // titleUrl鏄爣棰樼殑缃戠粶閾炬帴锛屼粎鍦ㄤ汉浜虹綉鍜孮Q绌洪棿浣跨敤
        oks.setTitleUrl("http://imlongluo.com");

        // text鏄垎浜枃鏈紝鎵�湁骞冲彴閮介渶瑕佽繖涓瓧娈�
        oks.setText("鎴戞槸鍒嗕韩鏂囨湰");
        // oks.setText(shareString);

        // imagePath鏄浘鐗囩殑鏈湴璺緞锛孡inked-In浠ュ鐨勫钩鍙伴兘鏀寔姝ゅ弬鏁�

        GetAndSaveCurrentScreenImage();
        // oks.setImagePath("/sdcard/test.jpg");
        oks.setImagePath("SDCard/YahooWeather/ScreenImage/Screen1.png");

        // url浠呭湪寰俊锛堝寘鎷ソ鍙嬪拰鏈嬪弸鍦堬級涓娇鐢�
        oks.setUrl("http://imlongluo.com/weather");

        // comment鏄垜瀵硅繖鏉″垎浜殑璇勮锛屼粎鍦ㄤ汉浜虹綉鍜孮Q绌洪棿浣跨敤
        oks.setComment("鎴戞槸娴嬭瘯璇勮鏂囨湰");

        // site鏄垎浜鍐呭鐨勭綉绔欏悕绉帮紝浠呭湪QQ绌洪棿浣跨敤
        oks.setSite(getString(R.string.app_name));
        // siteUrl鏄垎浜鍐呭鐨勭綉绔欏湴鍧�紝浠呭湪QQ绌洪棿浣跨敤
        oks.setSiteUrl("http://imlongluo.com");

        // 鍚姩鍒嗕韩GUI
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
     * 鑾峰彇鍜屼繚瀛樺綋鍓嶅睆骞曠殑鎴浘
     */
    private void GetAndSaveCurrentScreenImage() {
        // 1.鏋勫缓Bitmap
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();

        Bitmap Bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);

        // 2.鑾峰彇灞忓箷
        View decorview = this.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        Bmp = decorview.getDrawingCache();

        String SavePath = getSDCardPath() + "/YahooWeather/ScreenImage";

        // 3.淇濆瓨Bitmap
        try {
            File path = new File(SavePath);
            // 鏂囦欢
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

                Toast.makeText(ShareActivity.this, "Image Save SDCard/YahooWeather/ScreenImage/",
                        Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 鑾峰彇SDCard鐨勭洰褰曡矾寰勫姛鑳�
     *
     * @return
     */
    private String getSDCardPath() {
        File sdcardDir = null;
        // 鍒ゆ柇SDCard鏄惁瀛樺湪
        boolean sdcardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }

        return sdcardDir.toString();
    }
}

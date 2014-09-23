package com.imlongluo.weather.lbs;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.imlongluo.weather.R;
import com.imlongluo.weather.WeatherApplication;

public class LocationManagerActivity extends Activity {
    private static final String TAG = LocationManagerActivity.class.getSimpleName();

    private LocationClient mLocationClient;
    private TextView LocationResult, ModeInfor;
    private Button startLocation;
    private RadioGroup selectMode, selectCoordinates;
    private EditText frequence;
    private LocationMode tempMode = LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";
    private CheckBox checkGeoLocation;

    private EditText mEtAreaOfCity;
    private Button mBtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        mEtAreaOfCity = (EditText) findViewById(R.id.edittext_area);

        mBtSearch = (Button) findViewById(R.id.search_button);
        mBtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _location = mEtAreaOfCity.getText().toString();
                if (!TextUtils.isEmpty(_location)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtAreaOfCity.getWindowToken(), 0);
//                    searchByPlaceName(_location);
//                    showProgressDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "location is not inputted", 1).show();
                }
            }
        });

        mLocationClient = ((WeatherApplication) getApplication()).mLocationClient;

        LocationResult = (TextView) findViewById(R.id.textView1);
        ModeInfor = (TextView) findViewById(R.id.modeinfor);
        ModeInfor.setText(getString(R.string.hight_accuracy_desc));
        ((WeatherApplication) getApplication()).mLocationResult = LocationResult;
        frequence = (EditText) findViewById(R.id.frequence);
        checkGeoLocation = (CheckBox) findViewById(R.id.geolocation);
        startLocation = (Button) findViewById(R.id.addfence);
        startLocation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                InitLocation();

                if (startLocation.getText().equals(getString(R.string.startlocation))) {
                    mLocationClient.start();
                    startLocation.setText(getString(R.string.stoplocation));
                } else {
                    mLocationClient.stop();
                    startLocation.setText(getString(R.string.startlocation));
                }

            }
        });
        selectMode = (RadioGroup) findViewById(R.id.selectMode);
        selectCoordinates = (RadioGroup) findViewById(R.id.selectCoordinates);
        selectMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String ModeInformation = null;
                switch (checkedId) {
                    case R.id.radio_hight:
                        tempMode = LocationMode.Hight_Accuracy;
                        ModeInformation = getString(R.string.hight_accuracy_desc);
                        break;
                    case R.id.radio_low:
                        tempMode = LocationMode.Battery_Saving;
                        ModeInformation = getString(R.string.saving_battery_desc);
                        break;
                    case R.id.radio_device:
                        tempMode = LocationMode.Device_Sensors;
                        ModeInformation = getString(R.string.device_sensor_desc);
                        break;
                    default:
                        break;
                }
                ModeInfor.setText(ModeInformation);
            }
        });
        selectCoordinates.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_gcj02:
                        tempcoor = "gcj02";
                        break;
                    case R.id.radio_bd09ll:
                        tempcoor = "bd09ll";
                        break;
                    case R.id.radio_bd09:
                        tempcoor = "bd09";
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void InitLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// ���ö�λģʽ
        option.setCoorType(tempcoor);// ���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
        int span = 1000;
        try {
            span = Integer.valueOf(frequence.getText().toString());
        } catch (Exception e) {
        }
        option.setScanSpan(span);// ���÷���λ����ļ��ʱ��Ϊ5000ms
        option.setIsNeedAddress(checkGeoLocation.isChecked());
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        mLocationClient.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

package com.imlongluo.weather.lbs;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class LocationGetter {
    private static final String TAG = LocationGetter.class.getSimpleName();

    public class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
            }
            Log.i(TAG, "BD return: " + sb.toString());

            String city = location.getCity();
            String district = location.getDistrict();
            Log.i(TAG, "BD return city: " + city);
            Log.i(TAG, "BD return district: " + district);

            if ((city != null) && !city.equals("")) {
                String locateAddr = "";
                if ((district == null) || (district.equals(""))) {
                    locateAddr = deleteSuffix(city);
                } else {
//                    locateAddr = filterResult(city, district);
                }

//                waitAndPostRusultToUIHandler(MSG_GETCITY_SUCCESS, locateAddr);
            }
        }

    }
/*    private String filterResult(String city, String district) {
        boolean isMunicipaty = false;
        String locateAddress = "";
        String xian = mContext.getString(R.string.sheng);
        String qu = mContext.getString(R.string.qu);
        String[] municipalities = mContext.getResources().getStringArray(R.array.Municipalities);
        int municipalityLength = municipalities.length;

        String[] specialLocateAddress = mContext.getResources().getStringArray(
                R.array.special_locate_address);
        int specialLocateAddressLength = specialLocateAddress.length;

        String[] upperCitySpecialLocateAddress = mContext.getResources().getStringArray(
                R.array.upper_city_special_locate_address);

        for (int i = 0; i < municipalityLength; i++) {
            if (municipalities[i].equals(city)) {
                locateAddress = city;
                isMunicipaty = true;
                break;
            }
        }

        if (!isMunicipaty) {
            for (int i = 0; i < specialLocateAddressLength; i++) {
                if (district.startsWith(specialLocateAddress[i])) {
                    locateAddress = upperCitySpecialLocateAddress[i];
                    isMunicipaty = true;

                    break;
                }
            }
        }

        if (!isMunicipaty) {
            if (city.contains(xian)) {
                locateAddress = district;
            } else {
                if (!district.endsWith(qu)) {
                    locateAddress = district;
                } else {
                    locateAddress = city;
                }
            }
        }

        if (locateAddress.length() > 2) {
            locateAddress = deleteSuffix(locateAddress);
        }

        return locateAddress;
    }*/

    private String deleteSuffix(String string) {
        int length = string.length();
        string = string.substring(0, length - 1);
        return string;
    }

}

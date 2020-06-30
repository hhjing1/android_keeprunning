package com.weather.json;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.weather.LogUtil;
import com.weather.gson.HeWeather5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/4/18.
 */

public class WeatherJson {
    private static final String TAG = "WeatherJson";
    public static HeWeather5 getWeatherResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
                String weateherContent = jsonArray.getJSONObject(0).toString();
                return new Gson().fromJson(weateherContent, HeWeather5.class);
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtil.d(TAG, "getWeatherResponse: ");
            }
        }
        return null;
    }
}

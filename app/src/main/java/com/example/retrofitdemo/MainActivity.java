package com.example.retrofitdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getUSerMsg();
        //发送手机设备唯一ID。获取token
        getToken();
    }

    private void getToken() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://116.62.202.232:8083/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Map<String, String> map = new HashMap<>();
        map.put("imei", "123456");
        Call<String> token;
        try {
            token = requestInterface.getToken(getBodyString(map));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("参数错误", e.getMessage());
            return;
        }
        token.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("成功获取的token", String.valueOf(response.body()));
                try {
                    JSONObject object = new JSONObject(response.body());
                    String info = object.getString("info");
                    JSONObject tokenObject = new JSONObject(info);
                    String token = tokenObject.getString("token");
                    Log.e("token", token);
                    sendSMS(token, "15088905036");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("获取token--数据解析失败", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("失败获取的token", t.getMessage());
            }
        });
    }

    private String getBodyString(Map<String, String> map) throws JSONException {
        JSONObject object = new JSONObject();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                object.put(entry.getKey(), entry.getValue());
            }
        }
        return object.toString();
    }

    private void getUSerMsg() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.56gps.cn/oaweb/WebApi/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<String> userMsg = requestInterface.getUserMsg("Login", "chenj", "49BA59ABBE56E057");
//        Call<String> userMsg = requestInterface.getUserMsg("49BA59ABBE56E057");
//        Call<String> userMsg = requestInterface.getUserMsg();
        userMsg.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("返回的数据", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("返回的数据", t.getMessage());
            }
        });
    }

    /**
     * 发送验证码
     *
     * @param token 后台获取的token
     * @param phone 手机号码
     */
    private void sendSMS(String token, String phone) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://116.62.202.232:8083/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Map<String, String> phoneMap = new HashMap<>();
        phoneMap.put("phone", phone);
        Call<String> sendSMS;
        try {
            sendSMS = requestInterface.sendSMS(token, getBodyString(phoneMap));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("发送验证码", "body生产失败");
            return;
        }
        sendSMS.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("发送验证码成功", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("发送验证码失败---", t.getMessage());
            }
        });
    }
}

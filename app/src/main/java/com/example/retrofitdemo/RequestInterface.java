package com.example.retrofitdemo;


import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestInterface {

    //http://www.56gps.cn/oaweb/WebApi/api_maintorder.aspx?FLAG=Login&Account=chenj&PassWord=49BA59ABBE56E057
//    @GET("api_maintorder.aspx?FLAG={method}&Account={name}&PassWord={pwd}")

    /**
     * 图软请求用户基本信息
     *
     * @param method
     * @param userName
     * @param password
     * @return
     */
    @GET("api_maintorder.aspx")
    Call<String> getUserMsg(@Query("FLAG") String method, @Query("Account") String userName, @Query("PassWord") String password);

    /**
     * 发送手机设备ID 获取token
     * @param imei
     * @return
     */
    @POST("v1/user/auth")
    @Headers("Content-Type:application/json")
    Call<String> getToken(@Body String imei);

    @POST("v1/common/sms/send")
    @Headers("Content-Type:application/json")
    Call<String> sendSMS(@Header("dtk") String token,@Body String phone);

}

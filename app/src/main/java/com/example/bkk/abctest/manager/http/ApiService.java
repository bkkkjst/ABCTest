package com.example.bkk.abctest.manager.http;

import com.example.bkk.abctest.model.LoginResponse;
import com.example.bkk.abctest.model.LogoutResponse;
import com.example.bkk.abctest.model.Register;
import com.example.bkk.abctest.model.RegisterResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by BKK on 7/4/2560.
 */

public interface ApiService {
    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("api/account/create")
    Call<RegisterResponse> registration(@FieldMap(encoded = true) Map<String, String> requestBody);

    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("oauth/token")
    Call<LoginResponse> login(@FieldMap(encoded = true) Map<String, String> requestBody);


    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("account/logout")
    Call<LogoutResponse> logout();


}

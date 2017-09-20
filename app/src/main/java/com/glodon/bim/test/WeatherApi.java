package com.glodon.bim.test;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 描述：
 * 作者：zhourf on 2017/9/7
 * 邮箱：zhourf@glodon.com
 */

public interface WeatherApi {
    @GET("onebox/weather/query?cityname=深圳")
    Call<ResponseBody> getWeather(@Query("key") String key);

    @GET("onebox/weather/query?")
    Call<ResponseBody> getWeather(@Query("cityname") String cityname,@Query("key") String key);

    @GET("onebox/weather/query?")
    Call<ResponseBody> getWeather(@QueryMap Map<String ,String> params);


    @POST("onebox/weather/query")
    Call<ResponseBody> getWeather(@Field("id") int id,@Field("key") String key);

    @GET("onebox/weather/query?")
    rx.Observable<ResponseBody> getRxWeather(@QueryMap Map<String ,String> params);
}

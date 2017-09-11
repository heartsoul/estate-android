package com.glodon.bim.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.WeatherApi;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.network.NetRequestCallback;
import com.jakewharton.rxbinding.view.RxView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        tv = (TextView) findViewById(R.id.tv);

        RxView.clicks(btn).throttleFirst(2,TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                test5();
                int i = 1;
                int y = 0;
                int z = i/y;
            }
        });

        requestPermissions();
    }

    private void test5(){
        Map<String,String> params = new HashMap<>();
        params.put("cityname","上海");
        params.put("key","4ea58de8a7573377cec0046f5e2469d5");
        NetRequest.getInstance().getCall("http://op.juhe.cn/", WeatherApi.class)
                .getRxWeather(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            Log.d("ccc",responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void test4(){
        Map<String,String> params = new HashMap<>();
        params.put("cityname","上海");
        params.put("key","4ea58de8a7573377cec0046f5e2469d5");
        NetRequest request  = NetRequest.getInstance();
        request.getResponse(request.getCall("http://op.juhe.cn/", WeatherApi.class)
                .getWeather(params), new NetRequestCallback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("aaa",response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void test3(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://op.juhe.cn/")
                .build();
        WeatherApi wa = retrofit.create(WeatherApi.class);
        Map<String,String> params = new HashMap<>();
        params.put("cityname","上海");
        params.put("key","4ea58de8a7573377cec0046f5e2469d5");
        Call<ResponseBody> call = wa.getWeather(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("aaa",response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void test2(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://op.juhe.cn/")
                .build();
        WeatherApi wa = retrofit.create(WeatherApi.class);
        Call<ResponseBody> call = wa.getWeather("北京","4ea58de8a7573377cec0046f5e2469d5");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("aaa",response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private int REQUEST_EXTERNAL_STORAGE = 1;
    private void requestPermissions(){
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

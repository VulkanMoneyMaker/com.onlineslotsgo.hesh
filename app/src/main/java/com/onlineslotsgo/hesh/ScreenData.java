package com.onlineslotsgo.hesh;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.facebook.applinks.AppLinkData;
import com.onlineslotsgo.hesh.datates.Request;
import com.onlineslotsgo.hesh.datates.game_models.GameModel;
import com.onlineslotsgo.hesh.needed.InternetUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScreenData extends AppCompatActivity {
    private static final String TAG = ScreenData.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_splash);
        switchData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void switchData() {
        if (InternetUtils.checkNetworkConnection(this)) {
            loadStats();
        } else {
            start_frist();
        }
    }

    private void loadStats() {
        Request.provideApiModule().check().enqueue(new Callback<GameModel>() {
            @Override
            public void onResponse(@NonNull Call<GameModel> call, @NonNull Response<GameModel> response) {
                if (response.isSuccessful()) {
                    GameModel casinoModel = response.body();
                    if (casinoModel != null && casinoModel.getResult()) {
                        next(casinoModel.getUrl());
                    } else {
                        start_frist();
                    }
                } else {
                    start_frist();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GameModel> call, @NonNull Throwable t) {
                start_frist();
            }
        });
    }

    private void next(final String ert) {
        AppLinkData.fetchDeferredAppLinkData(this,
                appLinkData -> {
                    if (appLinkData != null) {
                        String transformUrl = speed(appLinkData.getTargetUri(), ert);
                        if (!transformUrl.equals(ert)) start_second(transformUrl);
                    } else {
                        start_second(ert);
                    }
                }
        );
    }

    private String speed(Uri data, String url) {
        String transform = url;
        String QUERY_1 = "cid";
        String QUERY_2 = "partid";
        if (data.getEncodedQuery().contains(QUERY_1)) {
            String queryValueFirst = data.getQueryParameter(QUERY_1);
            transform = transform.replace("cid", queryValueFirst);
        }
        if (data.getEncodedQuery().contains(QUERY_2)) {
            String queryValueSecond = data.getQueryParameter(QUERY_2);
            transform = transform.replace( "partid",queryValueSecond);
        }
        return transform;
    }

    private void start_frist() {
        startActivity(Select.getChoiceActivityIntent(this));
        finish();
    }

    private void start_second(String url) {
        startActivity(WebStart.getMainActivityIntent(this, url));
        finish();
    }
}

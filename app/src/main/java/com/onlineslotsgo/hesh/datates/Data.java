package com.onlineslotsgo.hesh.datates;

import com.onlineslotsgo.hesh.datates.game_models.GameModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Data {

    @GET("VUL-38")
    Call<GameModel> check();

}
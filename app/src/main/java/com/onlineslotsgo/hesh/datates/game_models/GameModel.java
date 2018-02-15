package com.onlineslotsgo.hesh.datates.game_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameModel {

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("result")
    @Expose
    private Boolean result;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
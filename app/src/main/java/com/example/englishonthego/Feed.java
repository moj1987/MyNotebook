package com.example.englishonthego;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feed {

    @SerializedName("length")
    @Expose
    private int length;

    @SerializedName("result")
    @Expose
    private ResultResponse resultResponse;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ResultResponse getResultResponse() {
        return resultResponse;
    }

    public void setResultResponse(ResultResponse resultResponse) {
        this.resultResponse = resultResponse;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "length=" + length +
                ", resultResponse=" + resultResponse +
                '}';
    }
}

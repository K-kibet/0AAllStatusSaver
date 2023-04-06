package com.kessi.allstatussaver.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InsModel implements Serializable {

    @SerializedName("graphql")
    private GetMedia getMedia;

    public GetMedia getGetMedia() {
        return getMedia;
    }

    public void setGetMedia(GetMedia getMedia) {
        this.getMedia = getMedia;
    }
}

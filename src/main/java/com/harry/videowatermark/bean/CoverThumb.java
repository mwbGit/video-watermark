
package com.harry.videowatermark.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoverThumb {

    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("url_list")
    @Expose
    private List<String> urlList = null;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

}

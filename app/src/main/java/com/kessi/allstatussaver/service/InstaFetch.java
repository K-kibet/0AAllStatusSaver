package com.kessi.allstatussaver.service;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class InstaFetch {

    private Document page;
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36";


    public String getDownloadUrl(String url){
        String downloadUrl = "";

        try{
                page = Jsoup.connect(url).get();
                String mediaType = page.select("meta[name=medium]").first()
                        .attr("content");

                switch (mediaType) {
                    case "video":
                        downloadUrl = page.select("meta[property=og:video]").first()
                                .attr("content");
                        break;
                    case "image":
                        downloadUrl = page.select("meta[property=og:image]").first()
                                .attr("content");
                        break;
                    default:
                        downloadUrl = "No media file found.";
                        break;
                }

        } catch (IOException e){
            Log.d("Jsoup Error: ", e.getMessage());
        }

        return downloadUrl;
    }


}

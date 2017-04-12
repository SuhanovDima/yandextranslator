package com.example.tesla.yandextranslator;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface YandexTranslateApi {
    @GET("/api/v1.5/tr.json/translate")
    Call<ResponseTranslate> tranaslate(@Query("key") String key,
                                     @Query("text") List<String> text,
                                     @Query("lang") String lang);
}

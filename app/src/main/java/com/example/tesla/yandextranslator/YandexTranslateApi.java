package com.example.tesla.yandextranslator;

import com.example.tesla.yandextranslator.JsonResponseObject.ResponseTranslate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YandexTranslateApi {
    @GET("/api/v1.5/tr.json/translate")
    Call<ResponseTranslate> tranaslate(@Query("key") String key,
                                       @Query("text") List<String> text,
                                       @Query("lang") String lang);
}

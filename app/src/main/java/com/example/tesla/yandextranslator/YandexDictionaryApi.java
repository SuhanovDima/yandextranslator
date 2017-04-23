package com.example.tesla.yandextranslator;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YandexDictionaryApi {
    @GET("/api/v1/dicservice.json/getLangs")
    Call<String[]> getDictionaries(@Query("key") String key);

    @GET("/api/v1/dicservice.json/lookup")
    Call<String> getLookUp(@Query("key") String key,
                           @Query("lang") String lang,
                           @Query("text") String text);
}

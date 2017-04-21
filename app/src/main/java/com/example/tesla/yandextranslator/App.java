package com.example.tesla.yandextranslator;

import android.app.Application;

import com.orm.SugarApp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by suhanov on 04.04.2017.
 */

public class App extends SugarApp {
    private static YandexTranslateApi yandexTranslateApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        yandexTranslateApi = retrofit.create(YandexTranslateApi.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    public static YandexTranslateApi getApi() {
        return yandexTranslateApi;
    }
}

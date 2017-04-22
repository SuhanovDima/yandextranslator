package com.example.tesla.yandextranslator;

import com.orm.SugarApp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by suhanov on 04.04.2017.
 */

public class App extends SugarApp {
    private static YandexTranslateApi yandexTranslateApi;
    private static YandexDictionaryApi yandexDictionaryApi;
    private Retrofit retrofitTranslate;
    private Retrofit retrofitDictionary;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofitTranslate = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        retrofitDictionary = new Retrofit.Builder()
                .baseUrl("https://dictionary.yandex.net") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        yandexTranslateApi = retrofitTranslate.create(YandexTranslateApi.class); //Создаем объект, при помощи которого будем выполнять запросы
        yandexDictionaryApi = retrofitDictionary.create(YandexDictionaryApi.class);
    }

    public static YandexTranslateApi getTranslatorApi() {
        return yandexTranslateApi;
    }

    public static YandexDictionaryApi getDictionaryApi() {
        return yandexDictionaryApi;
    }
}

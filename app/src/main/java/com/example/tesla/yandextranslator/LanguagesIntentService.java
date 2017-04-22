package com.example.tesla.yandextranslator;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.tesla.yandextranslator.TranslateIntentService.ERROR_MESSAGE_NETWORK;
import static com.example.tesla.yandextranslator.TranslateIntentService.SUCCESS_STATUS;


public class LanguagesIntentService extends IntentService {

    public static final String ACTION_DICTIONARY = "com.example.tesla.yandextranslator.Dictionary";
    public static final String KEY_DICTIONARIES = "dictionaries";
    public LanguagesIntentService() {
        super("LanguagesIntentService");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            App.getDictionaryApi().getDictionaries(getResources().getString(R.string.key_yandex_api_all)).enqueue(
                    new Callback<String[]>() {
                        @Override
                        public void onResponse(Call<String[]> call, Response<String[]> response) {
                            if (response.body() != null) {
                                Intent intent =  new Intent();
                                intent.setAction(ACTION_DICTIONARY);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.putExtra(KEY_DICTIONARIES, response.body());
                                sendBroadcast(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<String[]> call, Throwable t) {
                            Toast.makeText(LanguagesIntentService.this,
                                    ERROR_MESSAGE_NETWORK, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }
}

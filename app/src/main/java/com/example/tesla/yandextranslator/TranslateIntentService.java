package com.example.tesla.yandextranslator;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;
import static com.example.tesla.yandextranslator.MainActivity.KEY_MESSAGE_FOR_TRANSLATE;


public class TranslateIntentService extends IntentService {

    public TranslateIntentService() {
        super("myIntent");
    }

    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String value = intent.getStringExtra(KEY_MESSAGE_FOR_TRANSLATE);
            List<String> values = new ArrayList<>();
            try {
                URLEncoder.encode(value,"UTF8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            values.add(value);
            values.add(value);

            final List<ResponseTranslate> translates = new ArrayList<>();
            App.getApi().tranaslate("trnsl.1.1.20170323T081927Z.1c9be6abb2522c33.f9ca2f4993e117e3bc673e579af132d13acd8bb2",
                    values, "en-ru")

            .enqueue(new Callback<ResponseTranslate>() {

                @Override
                public void onResponse(Call<ResponseTranslate> call, Response<ResponseTranslate> response) {
                    translates.add(response.body());
                }

                @Override
                public void onFailure(Call<ResponseTranslate> call, Throwable t) {
                    Toast.makeText(TranslateIntentService.this,
                            "An error occurred during networking", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}

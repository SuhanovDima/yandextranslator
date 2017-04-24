package com.example.tesla.yandextranslator;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.example.tesla.yandextranslator.Data.HistoryTranslate;
import com.example.tesla.yandextranslator.Utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.tesla.yandextranslator.MainActivity.KEY_LANGUAGE_TRANSLATE;
import static com.example.tesla.yandextranslator.MainActivity.KEY_MESSAGE_FOR_TRANSLATE;


public class TranslateIntentService extends IntentService {

    public static final String ACTION_TRANSLATE = "com.example.tesla.yandextranslator.Translate";
    public static final String EXTRA_KEY_TRANSLATE = "translate";
    public static final String EXTRA_KEY_ID = "historyId";
    public static final String SUCCESS_STATUS = "200";
    public static final String ERROR_MESSAGE = "Ошибка при переводе";
    public static final String ERROR_MESSAGE_NETWORK = "Ошибка в сети";

    public TranslateIntentService() {
        super("intentTranlsate");
    }

    public void onCreate() {
        super.onCreate();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String value = intent.getStringExtra(KEY_MESSAGE_FOR_TRANSLATE);
            final String translateLanguage = intent.getStringExtra(KEY_LANGUAGE_TRANSLATE);
            String[] valuesString = value.split("\n");
            List<String> valueList = new ArrayList<>();
            for (String s:valuesString) {
                valueList.add(s);
            }
            final List<String> values = valueList;
            App.getTranslatorApi().tranaslate(getResources().getString(R.string.key_yandex_api),
                    values, translateLanguage)
                    .enqueue(new Callback<ResponseTranslate>() {
                        @Override
                        public void onResponse(Call<ResponseTranslate> call,
                                               Response<ResponseTranslate> response) {
                            if (response.body() != null && response.body().getCode().equals(SUCCESS_STATUS)
                                    && response.body().getText() != null && response.body().getText().size() > 0) {
                                String[] langs = translateLanguage.split("-");
                                StringBuilder translateValue = new StringBuilder();
                                List<Long> ids = new ArrayList<Long>();
                                for(int i = 0; i< response.body().getText().size(); i++) {
                                    translateValue.append(response.body().getText().get(i) + "\n");
                                    HistoryTranslate historyTranslate = new HistoryTranslate(values.get(i) ,response.body().getText().get(i),
                                            langs[0], langs[1], Calendar.getInstance().getTime(), false);
                                    historyTranslate.save();
                                    ids.add(historyTranslate.getId());
                                }

                                Intent intent = new Intent();
                                intent.setAction(ACTION_TRANSLATE);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.putExtra(EXTRA_KEY_TRANSLATE, translateValue.toString());

                                long[] longArray = ArrayUtils.toPrimitives(ids.toArray(new Long[ids.size()]));
                                intent.putExtra(EXTRA_KEY_ID, longArray);
                                sendBroadcast(intent);
                            } else {
                                Toast.makeText(TranslateIntentService.this,
                                        ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseTranslate> call, Throwable t) {
                            Toast.makeText(TranslateIntentService.this,
                                    ERROR_MESSAGE_NETWORK, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


}

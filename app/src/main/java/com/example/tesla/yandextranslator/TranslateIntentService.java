package com.example.tesla.yandextranslator;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.example.tesla.yandextranslator.Data.HistoryTranslate;
import com.example.tesla.yandextranslator.JsonResponseObject.Def;
import com.example.tesla.yandextranslator.JsonResponseObject.DicResult;
import com.example.tesla.yandextranslator.JsonResponseObject.Mean;
import com.example.tesla.yandextranslator.JsonResponseObject.Syn;
import com.example.tesla.yandextranslator.JsonResponseObject.Tr;
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
    public static final String ACTION_LOOK_UP = "com.example.tesla.yandextranslator.LookUp";
    public static final String EXTRA_KEY_LOOK_UP = "lookUp";
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
                                    if(response.body().getText().get(i) != "" && values.get(i) != "") {
                                        HistoryTranslate historyTranslate = new HistoryTranslate(values.get(i),
                                                response.body().getText().get(i),
                                                langs[0], langs[1], Calendar.getInstance().getTime(), false);
                                        historyTranslate.save();
                                        ids.add(historyTranslate.getId());
                                    }
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

            if(valuesString.length == 1) {
                String[] words = valuesString[0].split(" ");
                if (words.length == 1) {
                    App.getDictionaryApi().getLookUp(getResources().getString(R.string.key_yandex_api_all),
                            translateLanguage, words[0])
                            .enqueue(new Callback<DicResult>() {
                                @Override
                                public void onResponse(Call<DicResult> call,
                                                       Response<DicResult> response) {
                                    if (response.body() != null && response.body().getDef() !=null ) {
                                        StringBuilder responseDic = new StringBuilder();
                                        List<Def> defs = response.body().getDef();
                                        for (Def def:defs) {
                                            responseDic.append(def.getPos() + "\n");
                                            Integer number = 1;
                                            for(Tr tr : def.getTr()){
                                                StringBuilder responseSynLine = new StringBuilder(number.toString() + " ");
                                                if(tr.getSyn() != null) {
                                                    responseSynLine.append(tr.getText() + ", ");
                                                    for (int index = 0; index < tr.getSyn().size(); index++) {
                                                        if (index == tr.getSyn().size() - 1) {
                                                            responseSynLine.append(tr.getSyn().get(index).getText() + "\n");
                                                        } else {
                                                            responseSynLine.append(tr.getSyn().get(index).getText() + ", ");
                                                        }
                                                    }
                                                } else {
                                                    responseSynLine.append(tr.getText() + "\n");
                                                }
                                                StringBuilder responseMeanLine = new StringBuilder();
                                                if(tr.getMean() != null) {
                                                    responseMeanLine = new StringBuilder("(");
                                                    for (int index = 0; index < tr.getMean().size(); index++) {
                                                        if (index == tr.getMean().size() - 1) {
                                                            responseMeanLine.append(tr.getMean().get(index).getText() + ")\n");
                                                        } else {
                                                            responseMeanLine.append(tr.getMean().get(index).getText() + ", ");
                                                        }
                                                    }
                                                }
                                                responseDic.append(responseSynLine)
                                                        .append(responseMeanLine);
                                                number++;

                                            }
                                        }
                                        Intent intent = new Intent();
                                        intent.setAction(ACTION_LOOK_UP);
                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                                        intent.putExtra(EXTRA_KEY_LOOK_UP, responseDic.toString());
                                        sendBroadcast(intent);
                                    } else {
                                        Toast.makeText(TranslateIntentService.this,
                                                ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<DicResult> call, Throwable t) {
                                    Toast.makeText(TranslateIntentService.this,
                                            ERROR_MESSAGE_NETWORK, Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        }
    }
}

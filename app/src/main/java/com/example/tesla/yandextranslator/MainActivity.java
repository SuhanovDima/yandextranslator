package com.example.tesla.yandextranslator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesla.yandextranslator.Data.Dictionary;
import com.example.tesla.yandextranslator.Data.HistoryAdapter;
import com.example.tesla.yandextranslator.Data.HistoryTranslate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_MESSAGE_FOR_TRANSLATE = "MessageForTranslate";
    public static final String KEY_LANGUAGE_TRANSLATE = "LanguageTranslate";

    EditText translateText;
    Button translateButton;
    Button clearButton;
    Button addFavoriteButton;
    Button changeLanguageButton;
    TranslateLanguage translateLanguage;
    TextView translateView;
    ListView historyListView;
    ListView favoriteListView;
    Long currentId;
    Spinner spinnerNative;
    Spinner spinnerForeign;
    String[] dictionaryArrayList;
    Set<String> dictionaris;
    Set<String> dictionaryTranslate;
    String currentNativeLanguage;
    String currentForeignLanguage;
    Dictionary dictionary;
    List<String> dics;
    Boolean isPossibleTranslate = true;

    private TranslateBroadcastReceiver translateBroadcastReceiver;
    private DictionaryBroadcastReceiver dictionaryBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dictionary = new Dictionary();
        dictionaris = dictionary.keyDictionary.keySet();
        translateLanguage = TranslateLanguage.RU_TO_EN;
        initTab();
        initAllElementControl();
        initChangeLanguage();
        initClear();
        initTranslator();
        initBroadCasts();
        initAddFavorite();

        dics = new ArrayList<>();
        dics.addAll(dictionaris);

        currentForeignLanguage = "en";
        currentNativeLanguage = "en";

        loadData();
        loadDictionary();
    }

    private void loadDictionary(){
        Intent intent = new Intent(MainActivity.this, LanguagesIntentService.class);
        startService(intent);
    }

    private void loadData(){
        loadHistory();
        loadFavorite();
    }

    private void loadHistory(){
        ArrayList<HistoryTranslate> historyTranslates = (ArrayList<HistoryTranslate>) HistoryTranslate.listAll(HistoryTranslate.class, "date desc");
        HistoryAdapter historyAdapter = new HistoryAdapter(this, 0, historyTranslates);
        historyListView.setAdapter(historyAdapter);
    }

    private void loadFavorite(){
        ArrayList<HistoryTranslate> favoriteTranslates = (ArrayList<HistoryTranslate>) HistoryTranslate.find(HistoryTranslate.class, "IS_FAVORITE = ?", "1");
        HistoryAdapter historyAdapter = new HistoryAdapter(this, 0, favoriteTranslates);
        favoriteListView.setAdapter(historyAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(translateBroadcastReceiver);
        unregisterReceiver(dictionaryBroadcastReceiver);
    }

    public class TranslateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(TranslateIntentService.EXTRA_KEY_TRANSLATE);
            currentId = intent.getLongExtra(TranslateIntentService.EXTRA_KEY_ID, 0);
            translateView.setText(result);
            loadData();
        }
    }

    public class DictionaryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            dictionaryArrayList = intent.getStringArrayExtra(LanguagesIntentService.KEY_DICTIONARIES);
            dictionaryTranslate = new HashSet<String>(Arrays.asList(dictionaryArrayList));
            String[] langs = dictionaryArrayList[0].split("-");
            initSpinner();
        }
    }

    private void initTab(){
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        // инициализация
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        // создаем вкладку и указываем тег
        tabSpec = tabHost.newTabSpec("tabTranslator");
        // название вкладки
        tabSpec.setIndicator(getResources().getString(R.string.tab_translate));
        // указываем id компонента из FrameLayout, он и станет содержимым
        tabSpec.setContent(R.id.tabTranslator);
        // добавляем в корневой элемент
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tabFavorites");
        // указываем название и картинку
        // в нашем случае вместо картинки идет xml-файл,
        // который определяет картинку по состоянию вкладки
        tabSpec.setIndicator(getResources().getString(R.string.tab_favorites), getResources().getDrawable(R.drawable.tab_icon_selector));
        tabSpec.setContent(R.id.tabFavorites);
        tabHost.addTab(tabSpec);


        tabSpec = tabHost.newTabSpec("tabHistory");

        // указываем название и картинку
        // в нашем случае вместо картинки идет xml-файл,
        // который определяет картинку по состоянию вкладки
        tabSpec.setIndicator(getResources().getString(R.string.tab_history), getResources().getDrawable(R.drawable.tab_icon_selector));
        tabSpec.setContent(R.id.tabHistory);
        tabHost.addTab(tabSpec);

        // вторая вкладка будет выбрана по умолчанию
        tabHost.setCurrentTabByTag("tag1");

        for(int i =0; i<3; i++) {
            TextView x = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            x.setTextSize(9);

        }
    }

    private void initAllElementControl(){
        translateText = (EditText) findViewById(R.id.translateFieldId);
        translateButton = (Button) findViewById(R.id.buttonTranslateId);
        addFavoriteButton = (Button) findViewById(R.id.buttonAddFavoriteId);
        clearButton = (Button) findViewById(R.id.buttonClearId);
        changeLanguageButton = (Button) findViewById(R.id.buttonChangeLanguageId);
        translateView = (TextView) findViewById(R.id.translateViewId);
        historyListView = (ListView) findViewById(R.id.historyListViewId);
        favoriteListView = (ListView) findViewById(R.id.favoriteListViewId);
        spinnerNative = (Spinner) findViewById(R.id.spinnerNative);
        spinnerForeign = (Spinner) findViewById(R.id.spinnerForeign);
    }

    private void initSpinner(){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.record_dictionary_layout,
                R.id.textViewDictionaryId, dics);
        spinnerForeign.setAdapter(arrayAdapter);
        spinnerForeign.setSelection(3);
        spinnerNative.setAdapter(arrayAdapter);
        spinnerNative.setSelection(3);

        spinnerForeign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( dictionary.keyDictionary.containsKey(dics.get(position))) {
                    String key = dictionary.keyDictionary.get(dics.get(position));
                    String translate = key + "-" + currentNativeLanguage;
                    if(dictionaryTranslate.contains(translate)) {
                        currentForeignLanguage = dictionary.keyDictionary.get(dics.get(position));
                    } else {

                        Toast.makeText(MainActivity.this,
                                "Перевод при выбранных языках не возможен",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,
                            "Ошибка при выборе языка",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerNative.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( dictionary.keyDictionary.containsKey(dics.get(position))) {
                    String key = dictionary.keyDictionary.get(dics.get(position));
                    String translate = currentForeignLanguage + "-" + key;
                    if(dictionaryTranslate.contains(translate)) {
                        currentNativeLanguage =  dictionary.keyDictionary.get(dics.get(position));
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Перевод при выбранных языках не возможен",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,
                            "Ошибка при выборе языка",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initChangeLanguage(){
        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionNative = spinnerNative.getSelectedItemPosition();
                spinnerNative.setSelection(spinnerForeign.getSelectedItemPosition());
                spinnerForeign.setSelection(positionNative);
            }
        });
    }

    private void initTranslator(){
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callTranslateService();
            }
        });
    }

    private void initAddFavorite() {
        addFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryTranslate historyTranslate = HistoryTranslate.findById(HistoryTranslate.class, currentId);
                historyTranslate.setFavorite(true);
                historyTranslate.save();
                loadData();
            }
        });
    }

    private void initBroadCasts() {
        translateBroadcastReceiver = new TranslateBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(TranslateIntentService.ACTION_TRANSLATE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(translateBroadcastReceiver, intentFilter);

        dictionaryBroadcastReceiver = new DictionaryBroadcastReceiver();
        IntentFilter intentDicFilter = new IntentFilter(LanguagesIntentService.ACTION_DICTIONARY);
        intentDicFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(dictionaryBroadcastReceiver, intentDicFilter);
    }

    private void initClear(){
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateText.setText("");
                translateView.setText("");
            }
        });
    }

    private void callTranslateService(){
        Intent intent = new Intent(MainActivity.this, TranslateIntentService.class);
        intent.putExtra(KEY_MESSAGE_FOR_TRANSLATE, translateText.getText().toString());
        intent.putExtra(KEY_LANGUAGE_TRANSLATE, currentNativeLanguage + "-" + currentForeignLanguage);
        startService(intent);
    }
}

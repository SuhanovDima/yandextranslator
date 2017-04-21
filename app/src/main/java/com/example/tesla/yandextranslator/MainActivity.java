package com.example.tesla.yandextranslator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.tesla.yandextranslator.Data.HistoryAdapter;
import com.example.tesla.yandextranslator.Data.HistoryTranslate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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

    private TranslateBroadcastReceiver translateBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        translateLanguage = TranslateLanguage.RU_TO_EN;
        initTab();
        initAllElementControl();
        initChangeLanguage();
        initClear();
        initTranslator();
        initBroadCasts();
        initAddFavorite();


//        Calendar cal = Calendar.getInstance();
//        HistoryTranslate historyTranslate = new HistoryTranslate("привет", "hello", "ru", "en",cal.getTime(),false);
//        HistoryTranslate historyTranslate2 = new HistoryTranslate("hello", "привет", "en", "ru",cal.getTime(),true);
//        historyTranslate.save();
//        historyTranslate2.save();
//
//        List<HistoryTranslate> allContacts = HistoryTranslate.listAll(HistoryTranslate.class);
//        translateText.setText(allContacts.get(0).getNativeValue());
//        translateView.setText(allContacts.get(0).getForeignValue());

        loadData();


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
    }

    private void initChangeLanguage(){
        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeLanguageButton.getText().equals(getResources().getString(R.string.en_ru_string))) {
                    translateLanguage = TranslateLanguage.RU_TO_EN;
                    changeLanguageButton.setText(R.string.ru_en_string);
                } else {
                    changeLanguageButton.setText(R.string.en_ru_string);
                    translateLanguage = TranslateLanguage.EN_TO_RU;
                }

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

    private void initAddFavorite(){
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
        intent.putExtra(KEY_LANGUAGE_TRANSLATE, translateLanguage.toString());
        startService(intent);
    }
}

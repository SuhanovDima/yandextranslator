package com.example.tesla.yandextranslator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
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
import com.example.tesla.yandextranslator.Utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_MESSAGE_FOR_TRANSLATE = "MessageForTranslate";
    public static final String KEY_LANGUAGE_TRANSLATE = "LanguageTranslate";

    public static final String DELETE_FROM_FAVORITE = "Удалить запись из избранных";
    public static final String DELETE_FROM_FAVORITE_ALL = "Удалить из избранных все";
    public static final String DELETE_FROM_HISTORY = "Удалить запись из истории";
    public static final String ADD_TO_FAVORITE = "Добавить запись в избранное";
    public static final String DELETE_FROM_HISTORY_ALL = "Удалить всю историю";
    public static final String SUCCESS_DELETE = "Успешно удалено";
    public static final String SUCCESS_ADDED = "Успешно добавлено";

    EditText translateText;
    Button translateButton;
    Button clearButton;
    Button addFavoriteButton;
    Button changeLanguageButton;
    TextView translateView;
    TextView lookUpView;
    ListView historyListView;
    ListView favoriteListView;
    List<Long> currentIds;
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
    HistoryTranslate currentHistoryTranslate;
    HistoryTranslate currentFavoriteTranslate;

    private TranslateBroadcastReceiver translateBroadcastReceiver;
    private DictionaryBroadcastReceiver dictionaryBroadcastReceiver;
    private LookUpBroadcastReceiver lookUpBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dictionary = new Dictionary();
        dictionaris = dictionary.keyDictionary.keySet();
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
        ArrayList<HistoryTranslate> historyTranslates =
                (ArrayList<HistoryTranslate>) HistoryTranslate.listAll(HistoryTranslate.class, "date desc");
        HistoryAdapter historyAdapter = new HistoryAdapter(this, 0, historyTranslates);
        historyListView.setAdapter(historyAdapter);
    }

    private void loadFavorite(){
        ArrayList<HistoryTranslate> favoriteTranslates =
                (ArrayList<HistoryTranslate>) HistoryTranslate.find(HistoryTranslate.class, "IS_FAVORITE = ?", "1");
        HistoryAdapter historyAdapter = new HistoryAdapter(this, 0, favoriteTranslates);
        favoriteListView.setAdapter(historyAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(translateBroadcastReceiver);
        unregisterReceiver(dictionaryBroadcastReceiver);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.favoriteListViewId) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            currentFavoriteTranslate = (HistoryTranslate) lv.getItemAtPosition(acmi.position);

            menu.add(DELETE_FROM_FAVORITE);
            menu.add(DELETE_FROM_FAVORITE_ALL);
        }
        if (v.getId() == R.id.historyListViewId) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            currentHistoryTranslate = (HistoryTranslate) lv.getItemAtPosition(acmi.position);

            menu.add(ADD_TO_FAVORITE);
            menu.add(DELETE_FROM_HISTORY);
            menu.add(DELETE_FROM_HISTORY_ALL);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getTitle().toString()) {
            case DELETE_FROM_FAVORITE:
                currentFavoriteTranslate.setFavorite(false);
                currentFavoriteTranslate.save();
                loadData();
                Toast.makeText(MainActivity.this,
                        SUCCESS_DELETE, Toast.LENGTH_SHORT).show();
                break;
            case DELETE_FROM_FAVORITE_ALL:
                List<HistoryTranslate> historyTranslates = HistoryTranslate.find(HistoryTranslate.class, "IS_FAVORITE = ?", "1");
                for (HistoryTranslate historyTranslate:historyTranslates) {
                    historyTranslate.setFavorite(false);
                    historyTranslate.save();
                }
                loadData();
                Toast.makeText(MainActivity.this,
                        SUCCESS_DELETE, Toast.LENGTH_SHORT).show();
                break;
            case DELETE_FROM_HISTORY:
                currentHistoryTranslate.delete();
                loadData();
                Toast.makeText(MainActivity.this,
                        SUCCESS_DELETE, Toast.LENGTH_SHORT).show();
                break;
            case DELETE_FROM_HISTORY_ALL:
                HistoryTranslate.deleteAll(HistoryTranslate.class);
                loadData();
                Toast.makeText(MainActivity.this,
                        SUCCESS_DELETE, Toast.LENGTH_SHORT).show();
                break;
            case ADD_TO_FAVORITE:
                currentHistoryTranslate.setFavorite(true);
                currentHistoryTranslate.save();
                loadData();
                Toast.makeText(MainActivity.this,
                        SUCCESS_ADDED, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    public class TranslateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(TranslateIntentService.EXTRA_KEY_TRANSLATE);
            long [] ids = intent.getLongArrayExtra(TranslateIntentService.EXTRA_KEY_ID);
            Long[] longObjects = ArrayUtils.toObject(ids);
            currentIds = java.util.Arrays.asList(longObjects);
            translateView.setText(result);
            loadData();
        }
    }

    public class LookUpBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(TranslateIntentService.EXTRA_KEY_LOOK_UP);
            lookUpView.setText(result);
        }
    }

    public class DictionaryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            dictionaryArrayList = intent.getStringArrayExtra(LanguagesIntentService.KEY_DICTIONARIES);
            dictionaryTranslate = new HashSet<String>(Arrays.asList(dictionaryArrayList));
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
            x.setTextSize(10);
        }
    }

    private void initAllElementControl(){
        translateText = (EditText) findViewById(R.id.translateFieldId);
        translateButton = (Button) findViewById(R.id.buttonTranslateId);
        addFavoriteButton = (Button) findViewById(R.id.buttonAddFavoriteId);
        clearButton = (Button) findViewById(R.id.buttonClearId);
        changeLanguageButton = (Button) findViewById(R.id.buttonChangeLanguageId);
        translateView = (TextView) findViewById(R.id.translateViewId);
        lookUpView = (TextView) findViewById(R.id.lookUpViewId);
        historyListView = (ListView) findViewById(R.id.historyListViewId);
        favoriteListView = (ListView) findViewById(R.id.favoriteListViewId);
        spinnerNative = (Spinner) findViewById(R.id.spinnerNative);
        spinnerForeign = (Spinner) findViewById(R.id.spinnerForeign);
        registerForContextMenu(favoriteListView);
        registerForContextMenu(historyListView);
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
                        isPossibleTranslate = true;
                        currentForeignLanguage = dictionary.keyDictionary.get(dics.get(position));
                    } else {

                        Toast.makeText(MainActivity.this,
                                "Перевод при выбранных языках не возможен",
                                Toast.LENGTH_SHORT).show();
                        isPossibleTranslate = false;
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
                        isPossibleTranslate = true;
                        currentNativeLanguage =  dictionary.keyDictionary.get(dics.get(position));
                    } else {
                        isPossibleTranslate = false;
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
                for (Long id: currentIds) {
                    HistoryTranslate historyTranslate = HistoryTranslate.findById(HistoryTranslate.class, id);
                    historyTranslate.setFavorite(true);
                    historyTranslate.save();
                }
                loadData();
                Toast.makeText(MainActivity.this,
                        SUCCESS_ADDED, Toast.LENGTH_SHORT).show();
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

        lookUpBroadcastReceiver = new LookUpBroadcastReceiver();
        IntentFilter intentLookUpFilter = new IntentFilter(TranslateIntentService.ACTION_LOOK_UP);
        intentLookUpFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(lookUpBroadcastReceiver, intentLookUpFilter);

    }

    private void initClear(){
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateText.setText("");
                translateView.setText("");
                lookUpView.setText("");
            }
        });
    }

    private void callTranslateService(){
        if(isPossibleTranslate) {
            lookUpView.setText("");
            Intent intent = new Intent(MainActivity.this, TranslateIntentService.class);
            intent.putExtra(KEY_MESSAGE_FOR_TRANSLATE, translateText.getText().toString());
            intent.putExtra(KEY_LANGUAGE_TRANSLATE, currentNativeLanguage + "-" + currentForeignLanguage);
            startService(intent);
        }
    }
}

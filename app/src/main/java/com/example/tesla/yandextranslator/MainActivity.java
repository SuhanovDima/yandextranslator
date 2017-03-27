package com.example.tesla.yandextranslator;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}

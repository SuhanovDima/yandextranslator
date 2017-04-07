package com.example.tesla.yandextranslator;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    public static String KEY_MESSAGE_FOR_TRANSLATE = "MessageForTranslate";

    public EditText translateText;

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

        translateText = (EditText) findViewById(R.id.translateFieldId);
        Button translateButton = (Button) findViewById(R.id.buttonTranslateId);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TranslateIntentService.class);
                intent.putExtra(KEY_MESSAGE_FOR_TRANSLATE, translateText.getText().toString());
                startService(intent);
            }
        });
    }
}

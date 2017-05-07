package com.example.tesla.yandextranslator.Data;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class DictionaryTest {
    @Test
    public void  Test_fill_Key_Lang_Trans_Map(){
        Dictionary dictionary = new Dictionary();
        String[] dicts = new String[4];
        dicts[0] = "en-en";
        dicts[1] = "en-ru";
        dicts[2] = "ru-ru";
        dicts[3] = "ru-en";
        dictionary.fillKeyLangTransMap(dicts);
        assertEquals(dictionary.keyLangTransMap.size(), 2);
        assertEquals(dictionary.keyLangTransMap.get("en").size(), 2);
        assertEquals(dictionary.keyLangTransMap.get("ru").size(), 2);
        assertEquals(dictionary.keyDictionary.size(), 2);
        assertEquals(dictionary.langDictionary.size(), 2);
        assertEquals(dictionary.keyDictionary.get("английский"), "en");
        assertEquals(dictionary.langDictionary.get("en"), "английский");
    }
}

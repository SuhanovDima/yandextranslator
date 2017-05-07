package com.example.tesla.yandextranslator.Data;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Dictionary {
    public Map<String,String> keyDictionary;
    public Map<String,String> langDictionary;
    public Map<String, Set<String>> keyLangTransMap;
    public Dictionary() {
        keyLangTransMap = new TreeMap<>();
        keyDictionary = new TreeMap<>();
        keyDictionary.put("азербайджанский", "az");
        keyDictionary.put("албанский", "sq");
        keyDictionary.put("амхарский", "am");
        keyDictionary.put("английский", "en");
        keyDictionary.put("арабский", "ar");
        keyDictionary.put("армянский", "hy");
        keyDictionary.put("африкаанс", "af");
        keyDictionary.put("баскский", "eu");
        keyDictionary.put("башкирский", "ba");
        keyDictionary.put("белорусский", "be");
        keyDictionary.put("бенгальский", "bn");
        keyDictionary.put("болгарский", "bg");
        keyDictionary.put("боснийский", "bs");
        keyDictionary.put("валлийский", "cy");
        keyDictionary.put("венгерский", "hu");
        keyDictionary.put("вьетнамский", "vi");
        keyDictionary.put("гаитянский", "ht");
        keyDictionary.put("галисийский", "gl");
        keyDictionary.put("голландский", "nl");
        keyDictionary.put("горномарийский", "mrj");
        keyDictionary.put("греческий", "el");
        keyDictionary.put("грузинский", "ka");
        keyDictionary.put("гуджарати", "gu");
        keyDictionary.put("датский", "da");
        keyDictionary.put("иврит", "he");
        keyDictionary.put("идиш", "yi");
        keyDictionary.put("индонезийский", "id");
        keyDictionary.put("ирландский", "ga");
        keyDictionary.put("итальянский", "it");
        keyDictionary.put("исландский", "is");
        keyDictionary.put("испанский", "es");
        keyDictionary.put("казахский", "kk");
        keyDictionary.put("каннада", "kn");
        keyDictionary.put("каталанский", "ca");
        keyDictionary.put("киргизский", "ky");
        keyDictionary.put("китайский", "zh");
        keyDictionary.put("корейский", "ko");
        keyDictionary.put("коса", "xh");
        keyDictionary.put("латынь", "la");
        keyDictionary.put("латышский", "lv");
        keyDictionary.put("литовский", "lt");
        keyDictionary.put("люксембургский", "lb");
        keyDictionary.put("малагасийский", "mg");
        keyDictionary.put("малайский", "ms");
        keyDictionary.put("малаялам", "ml");

        keyDictionary.put("мальтийский", "mt");
        keyDictionary.put("македонский", "mk");
        keyDictionary.put("маори", "mi");
        keyDictionary.put("маратхи", "mr");
        keyDictionary.put("марийский", "mhr");
        keyDictionary.put("монгольский", "mn");
        keyDictionary.put("немецкий", "de");
        keyDictionary.put("непальский", "ne");
        keyDictionary.put("норвежский", "no");
        keyDictionary.put("панджаби", "pa");
        keyDictionary.put("папьяменто", "pap");
        keyDictionary.put("персидский", "fa");
        keyDictionary.put("польский", "pl");
        keyDictionary.put("португальский", "pt");
        keyDictionary.put("румынский", "ro");
        keyDictionary.put("русский", "ru");
        keyDictionary.put("себуанский", "ceb");
        keyDictionary.put("сербский", "sr");
        keyDictionary.put("сингальский", "si");
        keyDictionary.put("словацкий", "sk");
        keyDictionary.put("словенский", "sl");
        keyDictionary.put("суахили", "sw");
        keyDictionary.put("сунданский", "su");
        keyDictionary.put("таджикский", "tg");
        keyDictionary.put("тайский", "th");
        keyDictionary.put("тагальский", "tl");
        keyDictionary.put("тамильский", "ta");
        keyDictionary.put("татарский", "tt");
        keyDictionary.put("телугу", "te");
        keyDictionary.put("турецкий", "tr");
        keyDictionary.put("удмуртский", "udm");
        keyDictionary.put("узбекский", "uz");
        keyDictionary.put("украинский", "uk");
        keyDictionary.put("урду", "ur");
        keyDictionary.put("финский", "fi");
        keyDictionary.put("французский", "fr");
        keyDictionary.put("хинди", "hi");
        keyDictionary.put("хорватский", "hr");
        keyDictionary.put("чешский", "cs");
        keyDictionary.put("шведский", "sv");
        keyDictionary.put("шотландский", "gd");
        keyDictionary.put("эстонский", "et");
        keyDictionary.put("эсперанто", "eo");
        keyDictionary.put("яванский", "jv");
        keyDictionary.put("японский", "ja");

        langDictionary = new TreeMap<>();
        for (String key : keyDictionary.keySet()) {
            String value = keyDictionary.get(key);
            langDictionary.put(value,key);
        }
    }

    public void fillKeyLangTransMap(String[] dics){
        for(String s : dics) {
            String[] langs = s.split("-");
            if(keyLangTransMap.containsKey(langs[0])) {
                keyLangTransMap.get(langs[0]).add(langDictionary.get(langs[1]));
            } else {
                Set<String> strings = new TreeSet<>();
                strings.add(langDictionary.get(langs[1]));
                keyLangTransMap.put(langs[0],strings);
            }
        }
        List<String> keyDeleteLang = new ArrayList<>();
        for(String key : langDictionary.keySet()){
            if(!keyLangTransMap.containsKey(key)) {
                String value = langDictionary.get(key);
                keyDictionary.remove(value);
                keyDeleteLang.add(key);
            }
        }
        for (String key : keyDeleteLang) {
            langDictionary.remove(key);
        }
    }
}

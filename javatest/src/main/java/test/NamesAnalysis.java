package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by shisong on 2017/5/18.
 */

public class NamesAnalysis {
    public static void main(String[] a) {
        File file = new File("d:/test/names.txt");
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (true) {
                String str = reader.readLine();
                if (str == null) {
                    break;
                }
                stringBuilder.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] names = stringBuilder.toString().split(" ");
        System.out.println(names.length);

        Map<String, Integer> nameMap = new LinkedHashMap<>();
        for (String name : names) {
            Integer nums = nameMap.get(name);
            if (nums == null) {
                nums = 1;
            } else {
                nums++;
            }
            nameMap.put(name, nums);
        }

        nameMap = sortByValue(nameMap);

        System.out.println(nameMap.size());
        for (String name : nameMap.keySet()) {
            System.out.println(name + ":" + nameMap.get(name));
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}

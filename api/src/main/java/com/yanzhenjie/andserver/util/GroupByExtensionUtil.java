package com.yanzhenjie.andserver.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GroupByExtensionUtil {
    public static final <T, D> Map<T, List<D>> groupBy(Collection<D> paramCollection, GroupBy<T> paramGroupBy) {
        HashMap<Object, Object> hashMap = new HashMap<>();
        Iterator<D> iterator = paramCollection.iterator();
        while (iterator.hasNext()) {
            D d = iterator.next();
            T t = paramGroupBy.groupBy(d);
            if (hashMap.containsKey(t)) {
                ((List<D>)hashMap.get(t)).add(d);
                continue;
            }
            ArrayList<D> arrayList = new ArrayList();
            arrayList.add(d);
            hashMap.put(t, arrayList);
        }
        return (Map)hashMap;
    }

    public static final <T, D> Map<T, List<D>> groupBy(Collection<D> paramCollection, final String fieldName) {
        return groupBy(paramCollection, new GroupBy<T>() {
            public T groupBy(Object param1Object) {
                return (T)GroupByExtensionUtil.getFieldValueByName(param1Object, fieldName);
            }
        });
    }

    public static final <D> Map<String, List<D>> groupByDoc(Collection<D> paramCollection) {
        return groupBy(paramCollection, new GroupBy<String>() {
            public String groupBy(Object param1Object) {
                String str = "default";
                if (param1Object instanceof String) {
                    String[] arrayOfString = param1Object.toString().split("\\.");
                    if (arrayOfString.length == 2)
                        str = arrayOfString[1];
                }
                return str;
            }
        });
    }

    public static Object getFieldValueByName(Object paramObject, String paramString) {
        Object object = null;
        try {
            String str1 = paramString.substring(0, 1).toUpperCase();
            String str2 = "get" + str1 + paramString.substring(1);
            Method method = paramObject.getClass().getMethod(str2, new Class[0]);
            object = method.invoke(paramObject, new Object[0]);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return object;
    }

    public static void sort(List<String> paramList) {
        Collections.sort(paramList, new Comparator<String>() {
            public int compare(String param1String1, String param1String2) {
                if (param1String1.length() == param1String2.length())
                    return param1String1.compareTo(param1String2);
                return param1String1.length() - param1String2.length();
            }
        });
    }

    public static <T> List<List<T>> divider(Collection<T> list, Comparator<? super T> c){
        List<List<T>> result = new ArrayList<>();
        if(list == null) return result;

        for(T t : list){
            boolean newGroup = true;
            for(int i = 0; i < result.size(); i++){
                if(c.compare(t, result.get(i).get(0)) == 0){
                    newGroup = false;
                    result.get(i).add(t);
                    break;
                }
            }

            if(newGroup){
                List<T> innerList = new ArrayList<>();
                result.add(innerList);
                innerList.add(t);
            }
        }
        return result;
    }

    public static interface GroupBy<T> {
        T groupBy(Object param1Object);
    }
}

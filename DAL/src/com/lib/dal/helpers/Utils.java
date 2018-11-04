package com.lib.dal.helpers;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<Method> getGetters(Class clazz, String... filters) {
        List<Method> getters = new ArrayList<>();
        List<Method> definedGetters = new ArrayList<>();

        for(Method method : clazz.getDeclaredMethods()){
            String name = method.getName();

            if(name.startsWith("get") || name.startsWith("is"))
                definedGetters.add(method);
        }

        for (int i = 0; i < filters.length; i++) {
            String filter = filters[i];
            boolean added = false;

            for (Method method : definedGetters) {
                if (method.getName().toLowerCase().contains(filter.toLowerCase())) {
                    getters.add(method);
                    added = true;
                    break;
                }
            }

            if(added == false)
                throw new NotImplementedException();
        }

        return getters;
    }
}

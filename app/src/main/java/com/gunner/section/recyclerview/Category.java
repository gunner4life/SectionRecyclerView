package com.gunner.section.recyclerview;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Category
 * Created by gunner4life on 15/3/5.
 */
public class Category {

    public String categoryName;

    public List<Goods> goodsList = new ArrayList<>();

    public static List<Category> mockData(String jsonStr) {
        return new Gson().fromJson(jsonStr, new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{Category.class};
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type getRawType() {
                return List.class;
            }
        });
    }
}

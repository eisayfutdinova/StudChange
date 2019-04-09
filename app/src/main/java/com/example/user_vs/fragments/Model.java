package com.example.user_vs.fragments;

import android.view.Display;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Model {
    public String id;

    public static <T extends Model> T toObjectWithId(DocumentSnapshot doc, @NonNull Class<T> cl)
    {
        T object = doc.toObject(cl);
        object.id = doc.getId();
        return object;
    }

    public static <T extends Model> List<T> toObjectWithId(QuerySnapshot data, @NonNull Class<T> clazz)
    {
        List<T> obj = new ArrayList<>();
        for (DocumentSnapshot doc:data){
            T object = toObjectWithId(doc, clazz);
            obj.add(object);
        }
        return obj;
    }
}

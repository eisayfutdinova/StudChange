package com.example.user_vs.fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeRecyclerListFragment extends Fragment {


    public ExchangeRecyclerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exchange_recycler_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //a list to store all the exchanges
        List<Exchange> exchangeList;

        //the recyclerview
        RecyclerView recyclerView;
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //initializing the productlist
        exchangeList = new ArrayList<>();
//adding some items to our list
        exchangeList.add(
                new Exchange(
                        1,
                        "UK",
                        "blablabla",
                        "https://www.google.com/url?sa=i&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwio-sjwhrLgAhVSmIsKHbSIAp8QjRx6BAgBEAU&url=https%3A%2F%2Fhitech.vesti.ru%2Farticle%2F1083626%2F&psig=AOvVaw1wQ3qaQy63MStwNwCG36A4&ust=1549918603789024"));
        exchangeList.add(
                new Exchange(
                        1,
                        "USA",
                        "blablabla",
                        "https://www.google.com/url?sa=i&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwj84uzKh7LgAhUDx4sKHYOAAHwQjRx6BAgBEAU&url=http%3A%2F%2Forient-dv.ru%2F15-interesnyh-faktov-o-ssha%2F&psig=AOvVaw3gsUQHR1Tl7NamGfOiLSo9&ust=1549918788583543"));

        exchangeList.add(
                new Exchange(
                        1,
                        "Russia",
                        "blablabla",
                        "https://www.google.com/imgres?imgurl=http%3A%2F%2Frisovach.ru%2Fupload%2F2016%2F11%2Fmem%2Feto--rasha-na_129820662_orig_.jpg&imgrefurl=http%3A%2F%2Frisovach.ru%2Fmemy%2Feto--rasha-na_476%2Fall%2F2&docid=FB_pgsdzG80gIM&tbnid=Sdh7gyLbhKGVIM%3A&vet=10ahUKEwjQho_ah7LgAhUJwcQBHSFDBI8QMwg6KAMwAw..i&w=576&h=533&bih=610&biw=1280&q=%D1%80%D0%B0%D1%88%D0%B0&ved=0ahUKEwjQho_ah7LgAhUJwcQBHSFDBI8QMwg6KAMwAw&iact=mrc&uact=8"));

        //creating recyclerview adapter
        ExchangeAdapter adapter = new ExchangeAdapter(getContext(), exchangeList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}

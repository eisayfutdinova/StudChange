package com.example.user_vs.fragments;


import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsExchangeFragment extends Fragment {

    Typeface typeface, typefaceTitle, typefaceLight;


    public DetailsExchangeFragment() {
        // Required empty public constructor
    }

    public static DetailsExchangeFragment newInstance(String exchangeId) {
        DetailsExchangeFragment detailsExchangeFragment = new DetailsExchangeFragment();

        Bundle args = new Bundle();
        args.putString("exchange_id", exchangeId);
        detailsExchangeFragment.setArguments(args);
        return detailsExchangeFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_exchange, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        String ex_id = args.getString("exchange_id");
        typeface = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Rubik-Regular.ttf");
        typefaceTitle = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Rubik-Bold.ttf");
        typefaceLight = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Rubik-Light.ttf");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("exchange").document(ex_id).get()
                .addOnSuccessListener(queryDocSnap -> {
                    Exchange exchange = queryDocSnap.toObject(Exchange.class);
                    assert exchange != null;

                    ((TextView)getActivity().findViewById(R.id.exchange_fulldescription)).setTypeface(typeface);
                    ((TextView)getActivity().findViewById(R.id.exchange_name)).setTypeface(typefaceTitle);
                    ((TextView)getActivity().findViewById(R.id.exchange_type)).setTypeface(typefaceLight);
                    ((TextView)getActivity().findViewById(R.id.exchange_costDetails)).setTypeface(typefaceLight);

                    ((TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.exchange_name)).setText(exchange.getName());
                    ((TextView)getActivity().findViewById(R.id.exchange_type)).setText(exchange.getType());
                    ((TextView)getActivity().findViewById(R.id.exchange_fulldescription)).setText(exchange.getFulldescription());
                    //((TextView)getActivity().findViewById(R.id.exchange_link)).setText(exchange.getLink());
                    ((TextView)getActivity().findViewById(R.id.exchange_costDetails)).setText(exchange.getCostDetails());
                });

    }
}

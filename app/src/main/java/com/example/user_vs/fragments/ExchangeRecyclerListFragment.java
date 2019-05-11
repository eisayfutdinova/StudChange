package com.example.user_vs.fragments;


import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeRecyclerListFragment extends Fragment {

    ArrayList<String> listOfProgram;
    ArrayList<String> listOfCountries;
    List<Exchange> exchangeList;
    RecyclerView recyclerView;
    Typeface typeface;

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
        getActivity().setTitle("");

        typeface = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Rubik-Light.ttf");

        //a list to store all the exchanges
        exchangeList = new ArrayList<>();
        List<Exchange> exchangeCountries = new ArrayList<>();
        List<Exchange> finalListProgram = new ArrayList<>();
        List<Exchange> finalListCountry = new ArrayList<>();

        listOfProgram = new ArrayList<>();
        listOfCountries = new ArrayList<>();

        //the recyclerview
        recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("exchange")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        Exchange ex = ds.toObject(Exchange.class);
                        assert ex != null;
                        ex.setExchangeId(ds.getId());
                        exchangeList.add(ex);
                    }

                    //creating recyclerview adapter
                    ExchangeAdapter adapter = new ExchangeAdapter(getContext(), exchangeList);
                    recyclerView.setAdapter(adapter);

                    ImageView filterProgramButton = view.findViewById(R.id.filter_program);
                    TextView selectedProgramView = view.findViewById(R.id.filter_selected_programs);
                    selectedProgramView.setTypeface(typeface);
                    HashSet<String> setOfTypes = new LinkedHashSet<>();
                    List<Exchange> exchange_programs = new ArrayList<>();
                    for (Exchange types : exchangeList) {
                        setOfTypes.add(types.getType());
                    }
                    listOfProgram.addAll(setOfTypes);
                    String[] arrayOfprograms = new String[listOfProgram.size()];
                    listOfProgram.toArray(arrayOfprograms);
                    boolean[] cheakedItems = new boolean[arrayOfprograms.length];
                    ArrayList<Integer> selectedPrograms = new ArrayList<>();

                    filterProgramButton.setOnClickListener(v -> {

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                        mBuilder.setTitle(R.string.dialog_programs);
                        mBuilder.setMultiChoiceItems(arrayOfprograms, cheakedItems, (dialog, which, isChecked) -> {
                            if (isChecked) {
                                selectedPrograms.add(which);
                            } else {
                                selectedPrograms.remove((Integer.valueOf(which)));
                            }
                        });

                        mBuilder.setCancelable(false);
                        mBuilder.setPositiveButton(getString(R.string.ok_label), (dialog, which) -> {
                            StringBuilder lang = new StringBuilder();
                            for (int i = 0; i < selectedPrograms.size(); i++) {
                                lang.append(arrayOfprograms[selectedPrograms.get(i)]);
                                if (i != selectedPrograms.size() - 1) {
                                    lang.append(", ");
                                }
                            }
                            selectedProgramView.setText(lang.toString());
                            exchange_programs.clear();

                            for (Exchange ex : exchangeList) {
                                for (int i = 0; i < selectedPrograms.size(); i++) {
                                    if (ex.getType().equals(arrayOfprograms[selectedPrograms.get(i)]))
                                        exchange_programs.add(ex);
                                }
                            }

                            finalListProgram.clear();
                            if (!exchangeCountries.isEmpty()) {
                                for (Exchange ex : exchangeCountries) {
                                    for (int i = 0; i < selectedPrograms.size(); i++) {
                                        if (ex.getType().equals(arrayOfprograms[selectedPrograms.get(i)]))
                                            finalListProgram.add(ex);
                                    }
                                }
                            } else {
                                finalListProgram.addAll(exchange_programs);
                            }


                            recyclerView.setAdapter(new ExchangeAdapter(getContext(), finalListProgram));
                            recyclerView.getAdapter().notifyDataSetChanged();
                        });

                        mBuilder.setNegativeButton(R.string.dismiss_label, (dialog, which) -> dialog.dismiss());

                        mBuilder.setNeutralButton(R.string.clear_all_label, (dialog, which) -> {
                            for (int i = 0; i < cheakedItems.length; i++) {
                                cheakedItems[i] = false;
                                selectedPrograms.clear();
                                selectedProgramView.setText("");
                            }
                            exchange_programs.clear();
                            if (!exchangeCountries.isEmpty()) {
                                recyclerView.setAdapter(new ExchangeAdapter(getContext(), exchangeCountries));
                                recyclerView.getAdapter().notifyDataSetChanged();
                            } else {
                                recyclerView.setAdapter(adapter);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        });

                        AlertDialog mDialog = mBuilder.create();
                        mDialog.show();
                    });


                    ImageView filterCountryButton = view.findViewById(R.id.filter_country);
                    TextView selectedCountiesView = view.findViewById(R.id.filter_selected_country);
                    selectedCountiesView.setTypeface(typeface);
                    HashSet<String> setOfCountries = new LinkedHashSet<>();

                    for (Exchange types : exchangeList) {
                        setOfCountries.add(types.getCountry());
                    }

                    listOfCountries.addAll(setOfCountries);

                    String[] arrayOfCountries = new String[listOfCountries.size()];
                    listOfCountries.toArray(arrayOfCountries);
                    boolean[] cheakedItems1 = new boolean[arrayOfCountries.length];
                    ArrayList<Integer> selectedCountries = new ArrayList<>();

                    filterCountryButton.setOnClickListener(v -> {

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                        mBuilder.setTitle(R.string.dialog_countries);
                        mBuilder.setMultiChoiceItems(arrayOfCountries, cheakedItems1, (dialog, which, isChecked) -> {
                            if (isChecked) {
                                selectedCountries.add(which);
                            } else {
                                selectedCountries.remove((Integer.valueOf(which)));
                            }
                        });

                        mBuilder.setCancelable(false);
                        mBuilder.setPositiveButton(getString(R.string.ok_label), (dialog, which) -> {
                            StringBuilder lang = new StringBuilder();
                            for (int i = 0; i < selectedCountries.size(); i++) {
                                lang.append(arrayOfCountries[selectedCountries.get(i)]);
                                if (i != selectedCountries.size() - 1) {
                                    lang.append(", ");
                                }
                            }
                            selectedCountiesView.setText(lang.toString());
                            exchangeCountries.clear();

                            for (Exchange ex : exchangeList) {
                                for (int i = 0; i < selectedCountries.size(); i++) {
                                    if (ex.getCountry().equals(arrayOfCountries[selectedCountries.get(i)]))
                                        exchangeCountries.add(ex);
                                }
                            }

                            finalListCountry.clear();
                            if (!exchange_programs.isEmpty()) {
                                for (Exchange ex : exchange_programs) {
                                    for (int i = 0; i < selectedCountries.size(); i++) {
                                        if (ex.getCountry().equals(arrayOfCountries[selectedCountries.get(i)]))
                                            finalListCountry.add(ex);
                                    }
                                }
                            } else {
                                finalListCountry.addAll(exchangeCountries);
                            }

                            recyclerView.setAdapter(new ExchangeAdapter(getContext(), finalListCountry));
                            recyclerView.getAdapter().notifyDataSetChanged();
//                            if (!finalListCountry.isEmpty()) {
//                                recyclerView.setAdapter(new ExchangeAdapter(getContext(), finalListCountry));
//                                recyclerView.getAdapter().notifyDataSetChanged();
//                            } else {
//                                if (exchange_programs.isEmpty()) {
//                                    recyclerView.setAdapter(adapter);
//                                    recyclerView.getAdapter().notifyDataSetChanged();
//                                } else {
//                                    recyclerView.setAdapter(new ExchangeAdapter(getContext(), exchange_programs));
//                                    recyclerView.getAdapter().notifyDataSetChanged();
//                                }
//                            }
                        });

                        mBuilder.setNegativeButton(R.string.dismiss_label, (dialog, which) -> dialog.dismiss());

                        mBuilder.setNeutralButton(R.string.clear_all_label, (dialog, which) -> {
                            for (int i = 0; i < cheakedItems1.length; i++) {
                                cheakedItems1[i] = false;
                                selectedCountries.clear();
                                selectedCountiesView.setText("");
                            }
                            exchangeCountries.clear();
                            if (!exchange_programs.isEmpty()) {
                                recyclerView.setAdapter(new ExchangeAdapter(getContext(), exchange_programs));
                                recyclerView.getAdapter().notifyDataSetChanged();
                            } else {
                                exchangeCountries.clear();
                                recyclerView.setAdapter(adapter);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }

                        });

                        AlertDialog mDialog = mBuilder.create();
                        mDialog.show();
                    });

                })
                .addOnFailureListener(e -> Log.w(getActivity().getPackageName(), "Cannot get"));

        SearchView searchView = view.findViewById(R.id.searchView);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }

    }

    private void search(String string) {
        ArrayList<Exchange> mylist = new ArrayList<>();
        for (Exchange ex : exchangeList) {
            if (ex.getName().toLowerCase().contains(string.toLowerCase())) {
                mylist.add(ex);
            }
        }
        recyclerView.setAdapter(new ExchangeAdapter(getContext(), mylist));
        recyclerView.getAdapter().notifyDataSetChanged();
    }

}

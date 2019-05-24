package com.app.teste.colecionando.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.teste.colecionando.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MClassifPagerFragment extends Fragment {


    public MClassifPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mclassif_pager, container, false);
    }

}

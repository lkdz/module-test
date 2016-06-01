package com.lkdz.rfwirelessmoduletest1.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lkdz.rfwirelessmoduletest1.R;


public class SimplenessPagerFragment extends Fragment {

   ListView listView;
    CardView cd;
View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=View.inflate(getActivity(),R.layout.list_item_layout,null);


        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

}

package com.skymxc.demo.fragment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sky-mxc
 */

public class ContactFragment extends Fragment {

    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (textView  == null){
            textView = new TextView(getActivity());
            textView.setText("我的");
            textView.setGravity(Gravity.CENTER);
        }
        return textView;
    }
}

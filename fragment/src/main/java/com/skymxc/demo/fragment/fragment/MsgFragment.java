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

public class MsgFragment extends Fragment {

    private TextView view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = new TextView(getActivity());
            view.setText("消息");
            view.setGravity(Gravity.CENTER);

        }
        return  view;
    }
}

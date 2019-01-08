package com.xingqiuzhibo.phonelive.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.xingqiuzhibo.phonelive.R;


public class NetworkFragment extends DialogFragment {

    private ClickListener listener;

    public NetworkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        if(getArguments() != null){

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题栏
        View view = inflater.inflate(R.layout.fragment_network, container, false);

        view.findViewById(R.id.tv_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setClick(0);
                dismiss();
            }
        });

        view.findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setClick(1);
                dismiss();
            }
        });

        return view;
    }

    public interface ClickListener{
        void setClick(int type);
    }

    public void setClickListener(ClickListener listener){
        this.listener = listener;
    }

}

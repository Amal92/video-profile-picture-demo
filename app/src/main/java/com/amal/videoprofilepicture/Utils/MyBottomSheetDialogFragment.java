package com.amal.videoprofilepicture.Utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amal.videoprofilepicture.R;

/**
 * Created by amal on 05/11/16.
 */
public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {
    String mString;
static callbacklistener clb;
    public static MyBottomSheetDialogFragment newInstance(String string, callbacklistener cl) {
        clb= cl;
        MyBottomSheetDialogFragment f = new MyBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString("string", string);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mString = getArguments().getString("string");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.advanced_search_layout, container, false);
        TextView tv = (TextView) v.findViewById(R.id.text);
        Button hidebut = (Button) v.findViewById(R.id.hidebut);
        hidebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clb.passedData("got it");
                dismiss();
            }
        });
        return v;
    }
}

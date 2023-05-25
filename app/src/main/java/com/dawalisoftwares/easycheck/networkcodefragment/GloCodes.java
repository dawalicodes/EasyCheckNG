package com.dawalisoftwares.easycheck.networkcodefragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dawalisoftwares.easycheck.R;

public class GloCodes extends Fragment {

    public GloCodes()
    {

    }

    public static GloCodes newInstance()
    {
        return new GloCodes();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_codes, container, false);
    }
}
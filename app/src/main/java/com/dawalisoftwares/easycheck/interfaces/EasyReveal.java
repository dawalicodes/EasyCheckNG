package com.dawalisoftwares.easycheck.interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class EasyReveal {
    protected boolean viewCreated = false;
    protected abstract void onCreate(Context context);
    protected abstract View onCreateView(ViewGroup view, LayoutInflater layoutInflater);
    protected void onViewCreated(){
        viewCreated = true;
    }

    protected boolean isViewCreated(){
        return viewCreated;
    }
    public void addToView(ViewGroup viewGroup){
       onCreate(viewGroup.getContext());
       View view = this.onCreateView(viewGroup, LayoutInflater.from(viewGroup.getContext()));
       onViewCreated();
       viewGroup.addView(view);
    }
}

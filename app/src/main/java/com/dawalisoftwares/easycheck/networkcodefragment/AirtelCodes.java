package com.dawalisoftwares.easycheck.networkcodefragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dawalisoftwares.easycheck.R;
import com.dawalisoftwares.easycheck.adapters.CodeItem;
import com.dawalisoftwares.easycheck.adapters.CodesAdapter;
import com.dawalisoftwares.easycheck.adapters.ItemAttr;

import java.util.ArrayList;
import java.util.List;

public class AirtelCodes extends Fragment {


    public AirtelCodes()
    {

    }

    public static AirtelCodes newInstance()
    {
        return new AirtelCodes();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_codes,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_codes_recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<CodeItem> codeItems = new ArrayList<>();
        ///////////////////////////////////////////////////
        List<ItemAttr> itemAttrs = new ArrayList<>();
        itemAttrs.add(new ItemAttr("Check Account Balance","*123#"));
        itemAttrs.add(new ItemAttr("Check Account Balance","*123#"));
        itemAttrs.add(new ItemAttr("Check Account Balance","*123#"));
        itemAttrs.add(new ItemAttr("Check Account Balance","*123#"));
        itemAttrs.add(new ItemAttr("Check Account Balance","*123#"));
        codeItems.add(new CodeItem("CHECK",itemAttrs));
        ///////////////////////////////////////////////////////

        List<ItemAttr> itemAttrs2 = new ArrayList<>();
        itemAttrs2.add(new ItemAttr("Check Account Balance","*123#"));
        itemAttrs2.add(new ItemAttr("Check Account Balance","*123#"));
        itemAttrs2.add(new ItemAttr("Check Account Balance","*123#"));
        itemAttrs2.add(new ItemAttr("Check Account Balance","*123#"));
        itemAttrs2.add(new ItemAttr("Check Account Balance","*123#"));
        codeItems.add(new CodeItem("SEND",itemAttrs2));
        //////////////////////////////////////////////////////////

        List<ItemAttr> itemAttrs3 = new ArrayList<>();
        itemAttrs3.add(new ItemAttr("Check Account Balance","*123#"));
        itemAttrs3.add(new ItemAttr("Check Account Balance","*123#"));
        itemAttrs3.add(new ItemAttr("Check Account Balance","*123#"));
        itemAttrs3.add(new ItemAttr("Check Account Balance","*123#"));
        itemAttrs3.add(new ItemAttr("Check Account Balance","*123#"));
        codeItems.add(new CodeItem("GOTV",itemAttrs3));

        CodesAdapter adapter = new CodesAdapter(requireContext(),codeItems);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
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

import com.dawalisoftwares.easycheck.INFO;
import com.dawalisoftwares.easycheck.R;
import com.dawalisoftwares.easycheck.adapters.CodeItem;
import com.dawalisoftwares.easycheck.adapters.CodesAdapter;
import com.dawalisoftwares.easycheck.adapters.ItemAttr;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class MobileCodes extends Fragment {

    public MobileCodes()
    {

    }

    public static MobileCodes newInstance()
    {
        return new MobileCodes();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_codes,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_codes_recyclerView);
        MaterialTextView title = view.findViewById(R.id.network_name);
        title.setText(INFO.NETWORK_NAME_MOBILE);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<CodeItem> codeItems = new ArrayList<>();
        ///////////////////////////////////////////////////
        List<ItemAttr> itemAttrs = new ArrayList<>();
        itemAttrs.add(new ItemAttr("Check Account Balance","*310#"));
        itemAttrs.add(new ItemAttr("Borrow airtime/data","*303#"));
        itemAttrs.add(new ItemAttr("Check Data Balance","*323#"));
        itemAttrs.add(new ItemAttr("Airtime Recharge","*311*pin#"));
        itemAttrs.add(new ItemAttr("Buy Data Plan","*312#"));
        itemAttrs.add(new ItemAttr("Share Service","*321#"));
        itemAttrs.add(new ItemAttr("Stop Service","*305#"));
        itemAttrs.add(new ItemAttr("NIN Service","*996#"));
        itemAttrs.add(new ItemAttr("Call Center","300"));
        codeItems.add(new CodeItem("IMPORTANT",itemAttrs));
        ///////////////////////////////////////////////////////

        List<ItemAttr> itemAttrs2 = new ArrayList<>();
        itemAttrs2.add(new ItemAttr("MoreFlex Evolution","*320#"));
        itemAttrs2.add(new ItemAttr("More Business","*246*4#"));
        itemAttrs2.add(new ItemAttr("More Talk","*244*2#"));
        itemAttrs2.add(new ItemAttr("More Cliq","*244*1#"));
        itemAttrs2.add(new ItemAttr("More Flex","*344*300#"));
        itemAttrs2.add(new ItemAttr("More Life","*420*1#"));
        itemAttrs2.add(new ItemAttr("Talk Zone","*244*8#"));
        codeItems.add(new CodeItem("MIGRATE",itemAttrs2));
        //////////////////////////////////////////////////////////

        List<ItemAttr> itemAttrs3 = new ArrayList<>();
        itemAttrs3.add(new ItemAttr("Night Plan","*282*22#"));
        codeItems.add(new CodeItem("OTHER",itemAttrs3));

        CodesAdapter adapter = new CodesAdapter(requireContext(),codeItems);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
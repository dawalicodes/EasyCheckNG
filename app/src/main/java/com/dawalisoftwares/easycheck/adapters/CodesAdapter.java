package com.dawalisoftwares.easycheck.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dawalisoftwares.easycheck.R;

import java.util.List;

public class CodesAdapter extends RecyclerView.Adapter<Code> {

    private final Context context;
    private final List<CodeItem> codes;
    public CodesAdapter(Context context,List<CodeItem> codes) {
        this.context = context;
        this.codes = codes;
    }

    @NonNull
    @Override
    public Code onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.code_item,viewGroup,false);
        return new Code(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Code code, int position) {
        CodeItem codeItem = codes.get(position);
        code.section.setText(codeItem.getSection());
        ItemAttrAdapter adapter = new ItemAttrAdapter(codeItem.getAttrs());
        code.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        code.recyclerView.setHasFixedSize(false);
        code.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return codes.size();
    }



}

class Code extends RecyclerView.ViewHolder {

    RecyclerView recyclerView;
    TextView section;
    public Code(@NonNull View itemView) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.code_list_view);
        section  = itemView.findViewById(R.id.title_section);
    }
}


package com.dawalisoftwares.easycheck.adapters;

import java.util.List;

public class CodeItem {
    String section;
    List<ItemAttr> attrs;

    public CodeItem(String section, List<ItemAttr> attrs) {
        this.section = section;
        this.attrs = attrs;
    }

    public String getSection() {
        return section;
    }

    public List<ItemAttr> getAttrs() {
        return attrs;
    }
}

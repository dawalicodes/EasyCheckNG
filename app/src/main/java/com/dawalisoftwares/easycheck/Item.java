package com.dawalisoftwares.easycheck;

public class Item {

    private int icon;

    private String name;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public Item(int icon,String name, String description) {
        this.icon = icon;
        this.name = name;
        this.description = description;
    }
}

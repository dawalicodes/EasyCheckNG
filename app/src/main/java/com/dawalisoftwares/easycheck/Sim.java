package com.dawalisoftwares.easycheck;

import android.content.Context;

import androidx.annotation.Nullable;

import com.dawalisoftwares.easycheck.interfaces.Network;

public class Sim {
    private String name,mnc,mcc;
    private int simSlotIndex;
    private Network network;
    private final Context context;
    private String networkName;
    private int lightImage;

    @Nullable
    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMnc() {
        return this.mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public int getSimSlotIndex() {
        return simSlotIndex;
    }

    public void setSimSlotIndex(int simSlotIndex) {
        this.simSlotIndex = simSlotIndex;
    }
    private Context getContext(){
        return this.context;
    }

    public Sim(Context context, String name, String mnc, String mcc, int simSlotIndex) {
        this.context = context;
        this.name = name;
        this.mnc = mnc;
        this.mcc = mcc;
        this.simSlotIndex = simSlotIndex;
        switch (mnc)
        {
            case "60":
                setNetwork(new Mobile(context));
                networkName = INFO.NETWORK_NAME_MOBILE;
                lightImage = R.drawable.mobile;
                break;
            case "50":
                setNetwork(new GLO(context));
                networkName = INFO.NETWORK_NAME_GLO;
                lightImage = R.drawable.glo;
                break;
            case "30":
                setNetwork(new MTN(context));
                networkName = INFO.NETWORK_NAME_MTN;
                lightImage = R.drawable.mtn;
                break;
            case "20":
                setNetwork(new Airtel(context));
                networkName = INFO.NETWORK_NAME_AIRTEL;
                lightImage = R.drawable.airtel;
                break;
        }
    }

    public String getNetworkStaticName() {
        return networkName;
    }
    public int getSimImage(){
        return lightImage;
    }
}

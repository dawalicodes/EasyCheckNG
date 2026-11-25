package com.dawalisoftwares.easycheck;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class SimCardManager {
    private static SimCardManager simCardManager = null;
    private final TelephonyManager telephonyManager;
    private final SubscriptionManager subscriptionManager;
    private final Context context;

    private SimCardManager(Context context) {
        subscriptionManager = context.getSystemService(SubscriptionManager.class);
        telephonyManager = context.getSystemService(TelephonyManager.class);
        this.context = context;
    }

    public Sim getDefaultSim() {
        int defaultSubId = SubscriptionManager.getDefaultSubscriptionId();
        if (defaultSubId != SubscriptionManager.INVALID_SUBSCRIPTION_ID) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_PHONE_STATE)){
                    new MaterialAlertDialogBuilder(getContext()).setMessage(R.string.request_rationale_one)
                            .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE})).setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel()).create().show();
                }else {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE});
                }
                return null;
            }
            SubscriptionInfo defaultSubscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(defaultSubId);
           return getSim(defaultSubscriptionInfo.getSimSlotIndex());
        }

        return null;
    }

    public static SimCardManager getInstance(Context context) {
        if (simCardManager == null) {
            simCardManager = new SimCardManager(context);
        }
        return simCardManager;
    }

    public List<Sim> getActiveSimCards() {
        List<Sim> sims = new ArrayList<>();
        int availableSlots = -1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            availableSlots = telephonyManager.getActiveModemCount();
        } else {
            availableSlots = telephonyManager.getPhoneCount();
        }
        for (int i = 0; i < availableSlots; i++) {
            Sim sim = getSim(i);
            if (sim != null){
                sims.add(sim);
            }
        }
        return sims;
    }

    public int getNumberOfActiveSims(){
        return getActiveSimCards().size();
    }

    private Context getContext() {
        return this.context;
    }

    private Activity getActivity(){
        return (Activity) getContext();
    }

    private void requestPermissions(String[] permissions){
        ActivityCompat.requestPermissions(getActivity(),permissions, INFO.READ_PHONE_STATE_REQUEST);
    }

    private boolean hasPermissionTo(String permission){
       return ActivityCompat.checkSelfPermission(getContext(),permission) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean canMakeCall(){
        return hasPermissionTo(Manifest.permission.READ_PHONE_STATE) && hasPermissionTo(Manifest.permission.CALL_PHONE);
    }

    @Nullable
    private Sim getSim(int simSlot) {
        Sim sim = null;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_PHONE_STATE)){
                new MaterialAlertDialogBuilder(getContext()).setMessage(R.string.request_rationale_one)
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE})).setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel()).create().show();
            }else {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE});
            }
            return null;
        }
        SubscriptionInfo subscriptionInfo = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot);
        if (subscriptionInfo != null){
            String name = subscriptionInfo.getCarrierName().toString().split(" ")[0];
            String mnc = "",mcc = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mnc = subscriptionInfo.getMncString();
            }else {
                mnc = String.valueOf(subscriptionInfo.getMnc());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mcc = subscriptionInfo.getMccString();
            }else {
                mcc = String.valueOf(subscriptionInfo.getMcc());
            }
            sim = new Sim(getContext(),name,mnc,mcc,subscriptionInfo.getSimSlotIndex());
        }
        return sim;
    }
}

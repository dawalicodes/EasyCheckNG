package com.dawalisoftwares.easycheck;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.dawalisoftwares.easycheck.fragments.BankAirtimeReveal;
import com.dawalisoftwares.easycheck.fragments.PrivateNumberReveal;
import com.dawalisoftwares.easycheck.interfaces.Network;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MTN implements Network {
    private final Context context;
    private final TelecomManager telecomManager;

    public MTN(Context context) {
        this.context = context;
        telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
    }

    @Override
    public void checkAirtimeBalance(Sim sim, Bundle bundle) {
        Uri uri = Uri.fromParts("tel", "*310#",null);
        placeCall(sim,uri);
    }

    @Override
    public void checkDataBalance(Sim sim, Bundle bundle) {
        Uri uri = Uri.fromParts("tel", "*323#",null);
        placeCall(sim,uri);
    }

    @Override
    public void checkPhoneNumber(Sim sim, Bundle bundle) {
        Uri uri = Uri.fromParts("tel", "*663#",null);
        placeCall(sim,uri);
    }

    @Override
    public void checkTariffPlan(Sim sim, Bundle bundle) {

        new MaterialAlertDialogBuilder(context).setMessage("First select \"Account Info\" then select \"My Tariff Plan\"")
                .setTitle("Tips")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selfService(sim,bundle);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create().show();
    }

    @Override
    public void buyData(Sim sim, Bundle bundle) {
        Uri uri = Uri.fromParts("tel", "*312#",null);
        placeCall(sim,uri);
    }

    @Override
    public void loadAirtime(Sim sim, Bundle bundle) {

        Uri uri = Uri.fromParts("tel", "*311*PIN#",null);
        placeCall(sim,uri);
    }

    @Override
    public void borrowAirtime(Sim sim, Bundle bundle) {

        Uri uri = Uri.fromParts("tel", "*303#",null);
        placeCall(sim,uri);
    }

    @Override
    public void borrowData(Sim sim, Bundle bundle) {
        Uri uri = Uri.fromParts("tel", "*303#",null);
        placeCall(sim,uri);
    }

    @Override
    public void sendPleaseCallMe(Sim sim, Bundle bundle) {

    }

    @Override
    public void transferAirtime(Sim sim, Bundle bundle) {
        Uri uri = Uri.fromParts("tel", "*321#",null);
        placeCall(sim,uri);
    }

    @Override
    public void transferData(Sim sim, Bundle bundle) {
        Uri uri = Uri.fromParts("tel", "*321#",null);
        placeCall(sim,uri);
    }

    @Override
    public void cancelAutoRenew(Sim sim, Bundle bundle) {
        Uri uri = Uri.fromParts("tel", "*305#",null);
        placeCall(sim,uri);
    }

    @Override
    public void callCustomerCare(Sim sim, Bundle bundle) {
        Uri uri = Uri.fromParts("tel", "300",null);
        placeCall(sim,uri);
    }

    @Override
    public void selfService(Sim sim, Bundle bundle) {
        Uri uri = Uri.fromParts("tel", "*123#",null);
        placeCall(sim,uri);
    }

    @Override
    public void loadAirtimeFromBank(Sim sim, Bundle bundle) {
       if (bundle != null) {
           placeCall(sim,Uri.fromParts("tel",bundle.getString(BankAirtimeReveal.Companion.getRECHARGE_KEY()),null));
           Toast.makeText(context, bundle.getString(BankAirtimeReveal.Companion.getRECHARGE_KEY()), Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public void nationalNumber(Sim sim, Bundle bundle) {
        Uri uri = Uri.fromParts("tel", "*996#",null);
        placeCall(sim,uri);
    }


    @Override
    public void privateNumber(@Nullable Sim sim, @Nullable Bundle bundle) {
        if (bundle != null) {
            String phone = bundle.getString(PrivateNumberReveal.Companion.getPhonekey());
            placeCall(sim,Uri.fromParts("tel","#31#" + phone,null));
        }
    }

    @Override
    public void placeCall(Sim sim, Uri uri) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.READ_PHONE_STATE},INFO.READ_PHONE_STATE_CALL_PHONE_REQUEST);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE,telecomManager.getCallCapablePhoneAccounts().get(sim.getSimSlotIndex()));
        telecomManager.placeCall(uri,bundle);
    }

    //        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELECOM)) {
//
//        }else {
//            new MaterialAlertDialogBuilder(context).setMessage("your device does not have the required feature for this app")
//                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.cancel();
//                        }
//                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.cancel();
//                        }
//                    }).create().show();
//        }

    @NonNull
    @Override
    public String getNetworkName() {
        return INFO.NETWORK_NAME_MTN;
    }


}

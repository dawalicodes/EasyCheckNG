package com.dawalisoftwares.easycheck;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.TransformExperimental;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.dawalisoftwares.easycheck.adapters.Action;
import com.dawalisoftwares.easycheck.adapters.QuickActionAdapter;
import com.dawalisoftwares.easycheck.adapters.USSDAdapter;
import com.dawalisoftwares.easycheck.adapters.USSDItemDecorator;
import com.dawalisoftwares.easycheck.fragments.BankAirtimeReveal;
import com.dawalisoftwares.easycheck.fragments.BankServiceBottomSheet;
import com.dawalisoftwares.easycheck.fragments.NetworkLookUpReveal;
import com.dawalisoftwares.easycheck.fragments.PrivateNumberReveal;
import com.dawalisoftwares.easycheck.fragments.SelectSimReveal;
import com.dawalisoftwares.easycheck.interfaces.Bank;
import com.dawalisoftwares.easycheck.interfaces.EasyReveal;
import com.dawalisoftwares.easycheck.interfaces.OnPickContact;
import com.dawalisoftwares.easycheck.interfaces.OnSimSelected;
import com.dawalisoftwares.easycheck.view.USSDViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@TransformExperimental
public class USSDActivity extends AppCompatActivity implements OnSimSelected, OnPickContact {

    RecyclerView ussdRecycler;
    USSDAdapter adapter;
    Context context;
    private boolean done = false;
    private SimCardManager simCardManager;
    private ArrayList<Object> items;
    private LinearLayoutManager ussdLayoutManager;
    private int opened = -1;
    private USSDViewModel viewModel;
    private List<Bank> banks;

    ActivityResultLauncher<Void> pickContact = registerForActivityResult(new ActivityResultContracts.PickContact(), result -> {
        {

            if (result != null) {
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(result, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    String contactsId = cursor.getString(columnIndex);
                    cursor.close();

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactsId},
                            null);
                    if (phoneCursor != null && phoneCursor.moveToFirst()) {
                        int phoneColumnIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String phoneNumber = phoneCursor.getString(phoneColumnIndex);
                        phoneCursor.close();
                        viewModel.setPhoneNumber(phoneNumber);
                    } else {
                        Toast.makeText(context, "No Phone Number Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Unknown error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ussdactivity);
        context = USSDActivity.this;
        Telecom.INSTANCE.setTelecomManager((TelecomManager) getApplicationContext().getSystemService(Context.TELECOM_SERVICE));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                done = true;
            }
        }, 2000);
        viewModel = new ViewModelProvider(this).get(USSDViewModel.class);
        ussdRecycler = findViewById(R.id.ussd_recycler_view);
        GridView toolGridview = findViewById(R.id.tool_grid_view);

        QuickActionAdapter toolsAdapter = new QuickActionAdapter(context, List.of(
                new Action(
                        INFO.CHECK_AIRTIME_BALANCE,
                        R.drawable.baseline_account_balance_wallet_24,
                        "Check your airtime balance"
                ),
                new Action(
                        INFO.CHECK_DATA_BALANCE,
                        R.drawable.baseline_wifi_24,
                        "Check your remaining data"
                ),
                new Action(
                        INFO.CHECK_PHONE_NUMBER,
                        R.drawable.baseline_local_phone_24,
                        "Check your phone number"
                ),
                new Action(
                        INFO.CHECK_TARIFF_PLAN,
                        R.drawable.ic_baseline_star_half_24,
                        "Check your current tariff plan"
                )
        ));

        toolGridview.setAdapter(toolsAdapter);
        ussdLayoutManager = new LinearLayoutManager(context) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                super.smoothScrollToPosition(recyclerView, state, position);
                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context);
                smoothScroller.setTargetPosition(position);
                setSmoothScrollbarEnabled(true);
                startSmoothScroll(smoothScroller);
            }
        };
        ussdLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ussdRecycler.setLayoutManager(ussdLayoutManager);
        ussdRecycler.setHasFixedSize(false);


        simCardManager = SimCardManager.getInstance(context);

        banks = Bank.Companion.getAllBanks();

        items = new ArrayList<>();
        items.add(new Item(R.drawable.airtime_balance, INFO.CHECK_AIRTIME_BALANCE, "Checks your account balance."));
        items.add(new Item(R.drawable.data_balance, INFO.CHECK_DATA_BALANCE, "Checks your data bundle balance"));
        items.add(new Item(R.drawable.phone_number, INFO.CHECK_PHONE_NUMBER, "Checks your phone number"));
        items.add(new Item(R.drawable.verify_person, INFO.CHECK_NIN, "Retrieve or link your national identification number"));
        items.add(new Item(R.drawable.tariff_plan, INFO.CHECK_TARIFF_PLAN, "See what tariff plan you're subscribed to"));
        items.add(new Item(R.drawable.pivate_number, INFO.PRIVATE_NUMBER, "Stay anonymous by hiding your number"));
        items.add(new Item(R.drawable.network_lookup, INFO.NETWORK_LOOKUP, "checks a phone number network"));
        items.add(new Item(R.drawable.top_up, INFO.LOAD_AIRTIME, "Load airtime[recharge card]"));
        items.add(new Item(R.drawable.airtime_from_bank, INFO.LOAD_AIRTIME_FROM_BANK, "Recharge directly from your bank account"));
        items.add(new Item(R.drawable.bank, INFO.BANK_SERVICE, "perform normal bank operations"));
        items.add(new Item(R.drawable.buy_data, INFO.BUY_DATA, "Buy a data bundle"));
        items.add(new Item(R.drawable.borrow_airtime, INFO.BORROW_AIRTIME, "Borrow airtime and pay on your next recharge"));
        items.add(new Item(R.drawable.borrow_data, INFO.BORROW_DATA, "Borrow data and pay on your next recharge"));
        items.add(new Item(R.drawable.transfer_airtime, INFO.TRANSFER_AIRTIME, "Transfer airtime to other phone number on your network"));
        items.add(new Item(R.drawable.transfer_data, INFO.TRANSFER_DATA, "Transfer data to other phone number on your network"));
        items.add(new Item(R.drawable.c_care, INFO.CALL_CUSTOMER_CARE, "Place a call to ISP customer care"));
        items.add(new Item(R.drawable.self_service, INFO.SELF_SERVICE, "Self service."));
        items.add(new Item(R.drawable.auto_renew, INFO.CANCEL_AUTO_RENEW, "Stop services from automatically renewing."));
        adapter = new USSDAdapter(items);
        ussdRecycler.addItemDecoration(new USSDItemDecorator());
        adapter.setOnItemClickListener((view, action, description, position) -> {

            if (simCardManager.canMakeCall()) {

                int numberOfActiveSim = simCardManager.getNumberOfActiveSims();
                List<Sim> sims = simCardManager.getActiveSimCards();
                if (numberOfActiveSim > 0) {
                    switch (action) {
                        case INFO.LOAD_AIRTIME:
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                startActivity(new Intent(context, ScanCardActivity.class));
                            } else {
                                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                    showPermissionRationale(new String[]{Manifest.permission.CAMERA}, INFO.CAMERA_REQUEST_CODE, getString(R.string.request_rationale_camera));
                                } else {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, INFO.CAMERA_REQUEST_CODE);
                                }
                            }
                            break;
                        case INFO.LOAD_AIRTIME_FROM_BANK:
                            addAndReAdjust(view, position, new BankAirtimeReveal(sims,banks));
                            break;
                        case INFO.NETWORK_LOOKUP:
                            addAndReAdjust(view, position, new NetworkLookUpReveal());
                            break;
                        case INFO.PRIVATE_NUMBER:
                            addAndReAdjust(view, position, new PrivateNumberReveal(sims, action));
                            break;
                        default:
                            if (numberOfActiveSim == 1) {
                                onSimSelected(sims.get(0), null, action);
                            } else {
                                addAndReAdjust(view, position, new SelectSimReveal(sims, action, null));
                            }
                            break;
                    }

                } else {
                    new MaterialAlertDialogBuilder(this).setMessage("No sim cards detected, insert sim and try again").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).create().show();
                }
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE) || shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    showPermissionRationale(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE}, INFO.READ_PHONE_STATE_CALL_PHONE_REQUEST, getString(R.string.request_rationale_one));
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE}, INFO.READ_PHONE_STATE_CALL_PHONE_REQUEST);
                }
            }
        });

        ussdRecycler.setAdapter(adapter);


    }

    private void showPermissionRationale(String[] permissions, int code, String message) {
        new MaterialAlertDialogBuilder(context,R.style.USSDTheme).setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(permissions, code);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create().show();
    }

    private void addAndReAdjust(View view, int position, EasyReveal easyReveal) {
        if (revealIsOpened() && clickedIsNotRevealParent(position)) {
            int temp = opened;
            removeReveal(view, getRevealPosition());
            if (temp < position) {
                addReveal(view, easyReveal, position);
            } else {
                addReveal(view, easyReveal, position + 1);
            }
        } else if (revealIsOpened() && clickedIsRevealParent(position)) {
            removeReveal(view, getRevealPosition());
        } else {
            addReveal(view, easyReveal, position + 1);
        }
    }

    private boolean revealIsOpened() {
        return opened != -1;
    }

    private boolean clickedIsRevealParent(int position) {
        return position == (opened - 1);
    }

    private boolean clickedIsNotRevealParent(int position) {
        return position != (opened - 1);
    }

    private int getRevealPosition() {
        return opened;
    }

    private void setRevealPosition(int position) {
        opened = position;
    }

    private void addReveal(View view, EasyReveal reveal, int position) {
        items.add(position, reveal);
        adapter.notifyItemInserted(position);
        MaterialCardView cardView = (MaterialCardView) view;
        ShapeAppearanceModel cardAppearance = new ShapeAppearanceModel.Builder(cardView.getShapeAppearanceModel()).setBottomRightCorner(CornerFamily.CUT, 0).setBottomLeftCorner(CornerFamily.CUT, 0).build();
        cardView.setShapeAppearanceModel(cardAppearance);

        if (ussdLayoutManager.findLastCompletelyVisibleItemPosition() < position) {
            ussdRecycler.scrollToPosition(position);
        }
        setRevealPosition(position);
    }

    private void removeReveal(View view, int position) {
        items.remove(position);
        adapter.notifyItemRemoved(position);
        MaterialCardView cardView = (MaterialCardView) view;
        ShapeAppearanceModel cardAppearance = new ShapeAppearanceModel.Builder(cardView.getShapeAppearanceModel()).setBottomRightCorner(CornerFamily.ROUNDED, 8).setBottomLeftCorner(CornerFamily.ROUNDED, 8).build();
        cardView.setShapeAppearanceModel(cardAppearance);
        setRevealPosition(-1);
    }

    @Override
    public void onPick() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                showPermissionRationale(new String[]{Manifest.permission.READ_CONTACTS}, INFO.READ_CONTACT_REQUEST, getResources().getString(R.string.contact_permission));
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, INFO.READ_CONTACT_REQUEST);
            }
        } else {
            pickContact.launch(null);
        }
    }


    @Override
    public void onSimSelected(@NonNull Sim sim, @Nullable Bundle bundle, @NonNull String action) {
        if (sim.getNetwork() != null) {

            switch (action) {
                case INFO.CHECK_AIRTIME_BALANCE:
                    sim.getNetwork().checkAirtimeBalance(sim, bundle);
                    break;
                case INFO.CHECK_DATA_BALANCE:
                    sim.getNetwork().checkDataBalance(sim, bundle);
                    break;
                case INFO.CHECK_PHONE_NUMBER:
                    sim.getNetwork().checkPhoneNumber(sim, bundle);
                    break;
                case INFO.CHECK_TARIFF_PLAN:
                    sim.getNetwork().checkTariffPlan(sim, bundle);
                    break;
                case INFO.BUY_DATA:
                    sim.getNetwork().buyData(sim, bundle);
                    break;
                case INFO.BORROW_AIRTIME:
                    sim.getNetwork().borrowAirtime(sim, bundle);
                    break;
                case INFO.BORROW_DATA:
                    sim.getNetwork().borrowData(sim, bundle);
                    break;
                case INFO.BANK_SERVICE:
                    BankServiceBottomSheet bankServiceBottomSheet = new BankServiceBottomSheet(sim,banks);
                    bankServiceBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.bottomSheet);
                    bankServiceBottomSheet.show(getSupportFragmentManager(), "bankService");
                    break;
                case INFO.CANCEL_AUTO_RENEW:
                    sim.getNetwork().cancelAutoRenew(sim, bundle);
                    break;
                case INFO.CALL_CUSTOMER_CARE:
                    sim.getNetwork().callCustomerCare(sim, bundle);
                    break;
                case INFO.SELF_SERVICE:
                    sim.getNetwork().selfService(sim, bundle);
                    break;
                case INFO.LOAD_AIRTIME_FROM_BANK:
                    sim.getNetwork().loadAirtimeFromBank(sim, bundle);
                    break;
                case INFO.CHECK_NIN:
                    sim.getNetwork().nationalNumber(sim, bundle);
                    break;
                case INFO.PRIVATE_NUMBER:
                    sim.getNetwork().privateNumber(sim, bundle);
                    break;
                case INFO.TRANSFER_AIRTIME:
                    sim.getNetwork().transferAirtime(sim, bundle);
                    break;
                case INFO.TRANSFER_DATA:
                    sim.getNetwork().transferData(sim, bundle);
                    break;
            }

        } else {
            new MaterialAlertDialogBuilder(context,R.style.USSDTheme).setMessage("This Sim card is not supported")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).create().show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == INFO.CAMERA_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(context, ScanCardActivity.class));
        } else if (requestCode == INFO.READ_CONTACT_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickContact.launch(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.phone) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:")));
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }
}
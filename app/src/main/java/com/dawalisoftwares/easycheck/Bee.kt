package com.dawalisoftwares.easycheck

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dawalisoftwares.easycheck.adapters.BeeAdapter
import com.dawalisoftwares.easycheck.adapters.BeePerson
import com.dawalisoftwares.easycheck.database.DataBase
import com.dawalisoftwares.easycheck.databinding.ActivityBeeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.concurrent.Executors

class Bee : AppCompatActivity() {
    private lateinit var binding: ActivityBeeBinding
    private lateinit var context: Context
    private var pickContacts = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) {
        it?.apply {
            //some of the contacts information are gotten here
            LoaderManager.getInstance(this@Bee).initLoader(
                INFO.CURSOR_ID_DISPLAY_NAME,
                null,
                object : LoaderManager.LoaderCallbacks<Cursor> {
                    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                        return CursorLoader(
                            context,
                            this@apply,
                            arrayOf(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                ContactsContract.Contacts._ID
                            ),
                            null,
                            null,
                            null
                        )
                    }

                    override fun onLoaderReset(loader: Loader<Cursor>) {
                        loader.stopLoading()
                    }

                    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
                        data?.apply {
                            moveToFirst()
                            val columnIndex = getColumnIndex(ContactsContract.Contacts._ID)
                            val contactsID = getString(columnIndex)
                            val displayIndex =
                                getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                            val name = getString(displayIndex)
                            LoaderManager.getInstance(this@Bee).initLoader(
                                INFO.CURSOR_ID_PHONE_NUMBER,
                                null,
                                object : LoaderManager.LoaderCallbacks<Cursor> {
                                    override fun onCreateLoader(
                                        id: Int,
                                        args: Bundle?
                                    ): Loader<Cursor> {
                                        return CursorLoader(
                                            context,
                                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                            arrayOf(contactsID),
                                            null
                                        )
                                    }

                                    override fun onLoaderReset(loader: Loader<Cursor>) {
                                        loader.stopLoading()
                                    }

                                    override fun onLoadFinished(
                                        loader: Loader<Cursor>,
                                        data: Cursor?
                                    ) {
                                        data?.apply {
                                            moveToFirst()
                                            val phoneIndex =
                                                getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                            val phones = ArrayList<String>()
                                            while (moveToNext()) {
                                                val phone = getString(phoneIndex)
                                                phones.add(phone)
                                            }
                                            val arr = Array(phones.size) { index ->
                                                phones[index]
                                            }
                                            Executors.newSingleThreadExecutor().submit {
                                                dataBase?.beePersonDao()!!.insertBeePersons(
                                                    BeePerson(
                                                        0, name,
                                                        arr
                                                    )
                                                )
                                            }
                                            Toast.makeText(
                                                context,
                                                "$name added",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                })
                        }

                    }

                })

        }

    }
    private var dataBase: DataBase? = null
    private val mAdaper = BeeAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeeBinding.inflate(layoutInflater)
        context = this@Bee
        setContentView(binding.root)
        dataBase = db.getDataBase(context)
        binding.beeContact.apply {
            adapter = mAdaper
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }
        binding.addContact.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                pickContacts.launch(null)
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                showPermissionRationale(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    INFO.READ_CONTACT_REQUEST,
                    resources.getString(R.string.request_rationale_one)
                )
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    INFO.READ_CONTACT_REQUEST
                )
            }

        }

        dataBase?.beePersonDao()?.getAllPersons()?.observe(context as LifecycleOwner) {
            mAdaper.submitList(it)
        }

        binding.viewBees.apply {

        }
    }

    private fun showPermissionRationale(permissions: Array<String>, code: Int, message: String) {
        MaterialAlertDialogBuilder(context, R.style.USSDTheme).setMessage(message)
            .setPositiveButton(
                android.R.string.ok
            ) { _, i -> requestPermissions(permissions, code) }
            .setNegativeButton(
                android.R.string.cancel
            ) { dialogInterface, i -> dialogInterface.cancel() }.create().show()
    }

}
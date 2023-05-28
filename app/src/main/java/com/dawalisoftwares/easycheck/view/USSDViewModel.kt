package com.dawalisoftwares.easycheck.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class USSDViewModel : ViewModel() {
    var phoneNumber: MutableLiveData<String> = MutableLiveData("")

   fun setPhoneNumber(phone: String) {
      phoneNumber.value = phone
   }
}
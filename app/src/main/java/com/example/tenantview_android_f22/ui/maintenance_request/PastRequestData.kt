package com.example.tenantview_android_f22.ui.maintenance_request

import android.media.Image

data class PastRequestData (
    val d_id: String = "",
    var d_image: String = "",
    var d_subject: String = "",
    var d_description: String = ""
)
 val pastRequestList: ArrayList<PastRequestData> = arrayListOf(
     PastRequestData("","","Subject1","Description1"),
     PastRequestData("","","Subject2","Description2"),
     PastRequestData("","","Subject3","Description3"),
     PastRequestData("","","Subject4","Description4")
 )
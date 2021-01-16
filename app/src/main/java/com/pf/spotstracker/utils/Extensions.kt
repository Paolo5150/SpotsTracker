package com.pf.spotstracker.utils

import android.Manifest
import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast


fun Context.toastShort(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Context.alertDialogOkCancel(title: String, msg: String,
                                okListener: DialogInterface.OnClickListener , cancelListener: DialogInterface.OnClickListener,
                                isCancelable:Boolean = true) {
    AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setCancelable(isCancelable)
            .setPositiveButton("Ok", okListener)
            .setNegativeButton("Cancel", cancelListener)
            .create()
            .show()
}


fun Context.alertDialogOk(title: String, msg: String,
                          okListener: DialogInterface.OnClickListener,
                          isCancelable:Boolean = true) {
    AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setCancelable(isCancelable)
            .setPositiveButton("Ok", okListener)
            .create()
            .show()
}

fun LogDebug(msg: String)
{
    Log.d("SpotsTracker", msg);
}
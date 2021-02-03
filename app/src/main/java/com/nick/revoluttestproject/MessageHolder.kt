package com.nick.revoluttestproject

import android.content.Context
import androidx.appcompat.app.AlertDialog

//possible improvement -> cover other cases of showing dialog e.g. receive @StringRes values, supportNegativeButton
interface MessageHolder {

    fun Context.showDialog(
        title: String?,
        message: String,
        buttonText: String,
        onCloseAction: () -> Unit
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonText) { d, _ -> d.dismiss() }
            .setOnDismissListener { onCloseAction.invoke() }
            .show()
    }

}
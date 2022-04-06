package com.falApp.sadekahvefal.ui.fragment

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.falApp.sadekahvefal.R

class CustomDialog(
    context : Context, title : String, desc : String?, tagName : String?
) : Dialog(context) {

    private  var yesListener : (() -> Unit)? = null
    private  var noListener : (() -> Unit)? = null

    private var nTitle = title
    private var nDesc = desc
    private var nTagName = tagName

    fun setYesListener(listener : () -> Unit) {
        yesListener = listener
    }
    fun setNoListener(listener : () -> Unit) {
        noListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_cutom)

        context.resources.displayMetrics.let { displayMetrics ->
            val width = displayMetrics.widthPixels
            window!!.setLayout(width - 100.dp, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        val btnOk = this.findViewById<TextView>(R.id.tvWatchWin)
        //val btnCancel = this.findViewById<TextView>(R.id.tvDialogTagCancel)




        btnOk.setOnClickListener {
            yesListener?.let {yes ->
                yes()
            }
        }
       /* btnCancel.setOnClickListener {
            noListener?.let { no ->
                no()
            }
        }*/
    }

    private val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
}
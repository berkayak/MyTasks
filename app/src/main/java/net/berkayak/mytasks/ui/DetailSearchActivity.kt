package net.berkayak.mytasks.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_detail_search.*
import net.berkayak.mytasks.R
import net.berkayak.mytasks.utilities.Consts

class DetailSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_search)

        filterBTN.setOnClickListener(filterListener)
        clearFilterBTN.setOnClickListener(clearFilterListener)
    }


    private var filterListener = View.OnClickListener {
        var bnd = Bundle()
        if (completedCB.isChecked)
            bnd.putBoolean(Consts.FILTER_DONE, true)
        if (notCompletedCB.isChecked)
            bnd.putBoolean(Consts.FILTER_UNDONE, true)
        if (expiredCB.isChecked)
            bnd.putBoolean(Consts.FILTER_EXPIRED, true)
        if (notExpiredCB.isChecked)
            bnd.putBoolean(Consts.FILTER_NOT_EXPIRED, true)
        if (!nameFilterET.text.toString().isNullOrEmpty())
            bnd.putString(Consts.FILTER_NAME, nameFilterET.text.toString())
        var i = Intent()
        i.putExtra(Consts.FILTER_BUNDLE, bnd)
        setResult(Consts.RESULT_CODE_DONE,i)
        finish()
    }

    private var clearFilterListener = View.OnClickListener {
        var bnd = Bundle()
        var i = Intent()
        i.putExtra(Consts.FILTER_BUNDLE, bnd)
        setResult(Consts.RESULT_CODE_DONE,i)
        finish()
    }
}

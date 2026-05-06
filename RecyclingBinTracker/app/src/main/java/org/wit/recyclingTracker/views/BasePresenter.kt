package org.wit.recyclingTracker.views

import android.content.Intent
import org.wit.recyclingTracker.main.MainApp

open class BasePresenter(var view: BaseView?) {
    var app: MainApp =  view?.application as MainApp

    open fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
}

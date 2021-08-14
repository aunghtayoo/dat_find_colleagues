package mm.com.diracetech.shared.mvp.presenters

import mm.com.diracetech.shared.mvp.views.BaseView

abstract class BasePresenter<T: BaseView> {

    protected lateinit var mView: T

    open fun initPresenter(view: T) {
        this.mView = view
    }
}
package catt.mvp.sample.base.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import catt.compat.layout.app.CompatLayoutDialogFragment
import catt.mvp.sample.base.proxy.IProxyLifecycle
import catt.mvp.sample.base.mvp.view.IRootViewIFS
import catt.mvp.sample.base.adm.BaseDialogFragmentStack
import catt.mvp.sample.base.mvp.presenter.BasePresenter
import catt.mvp.sample.base.proxy.ProxyBaseDialogFragment
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.*

abstract class BaseDialogFragment<T : CompatLayoutDialogFragment> : CompatLayoutDialogFragment(),
    IProxyLifecycle<T>, IRootViewIFS {
    var isShowing: Boolean = false

    /**
     * 友盟记录
     */
    abstract fun pageLabel():String

    abstract fun injectLayoutId(): Int

    override val proxy: ProxyBaseDialogFragment<T, IRootViewIFS, BasePresenter<IRootViewIFS>>
            by lazy { injectProxyImpl() as ProxyBaseDialogFragment<T, IRootViewIFS, BasePresenter<IRootViewIFS>> }

    override fun onCreate(savedInstanceState: Bundle?) {
        BaseDialogFragmentStack.get().push(this@BaseDialogFragment)
        super.onCreate(savedInstanceState)
        proxy.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        compatCreateView(injectLayoutId(), container)?.postOnViewLoadCompleted()

    override fun onHiddenChanged(hidden: Boolean){
        proxy.onHiddenChanged(hidden)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        proxy.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        proxy.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        this.clearFindViewByIdCache()
        super.onDestroyView()
        proxy.onDestroyView()
        isShowing = false
    }

    override fun onStart() {
        super.onStart()
        proxy.onStart()
    }

    override fun onResume() {
        super.onResume()
        proxy.onResume()
        MobclickAgent.onPageStart(pageLabel())
    }

    override fun onPause() {
        super.onPause()
        proxy.onPause()
        MobclickAgent.onPageEnd(pageLabel())
    }

    override fun onStop() {
        super.onStop()
        proxy.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        proxy.onDestroy()
        BaseDialogFragmentStack.get().remove(this@BaseDialogFragment)
        System.runFinalization()
    }

    private fun View.postOnViewLoadCompleted():View {
        post {
            isShowing = true
            proxy.onViewLoadCompleted() }
        return this
    }
}
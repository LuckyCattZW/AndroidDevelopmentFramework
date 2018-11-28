package catt.mvp.sample.base.proxy

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import catt.mvp.sample.base.function.component.IDialogComponent
import catt.mvp.sample.base.function.component.IGlideComponent
import catt.mvp.sample.base.function.component.ISupportFragmentComponent
import catt.mvp.sample.base.function.component.IToastyComponent
import catt.mvp.sample.base.function.helper.PermissionHelper
import catt.mvp.sample.base.mvp.view.IRootViewIFS
import catt.mvp.sample.base.mvp.presenter.BasePresenter
import java.lang.ref.Reference
import java.lang.reflect.Constructor
import java.lang.reflect.ParameterizedType

/**
 * type T, 绑定Activity
 * type V, 绑定View,所有ViewInterface均需继承IRootViewInterface
 * type P, 绑定Presenter,并且Presenter绑定View
 *
 * params reference, 绑定Activity的引用类型,建议采用WeakReference<T>
 */
abstract class ProxyBaseActivity<T : AppCompatActivity, V : IRootViewIFS, P: BasePresenter<V>>
constructor(override val reference: Reference<T>) : ILifecycle<T>,
    PermissionHelper.OnPermissionListener, IGlideComponent, IToastyComponent, ISupportFragmentComponent, IDialogComponent {

    override val target: T?
        get() = reference.get()

    private val presenterClazz: Class<P>
        get() {
            val genType = javaClass.genericSuperclass
            val params = (genType as ParameterizedType).actualTypeArguments.reversed()
            return params[0] as Class<P>
        }

    val presenter : P by lazy {
        val c: Constructor<P> = presenterClazz.getConstructor()
        c.newInstance()
    }

    override val context: Context?
        get() = reference.get()?.applicationContext

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter.onAttach(this as V)
    }

    override fun onStart() {}

    override fun onResume() {}

    open fun onRestart() {}

    override fun onPause() {}

    override fun onStop() {}

    override fun onDestroy() {
        presenter.onDetach()
    }
}

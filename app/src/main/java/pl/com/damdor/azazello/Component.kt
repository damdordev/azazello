package pl.com.damdor.azazello

import com.github.damdordev.events.Event
import kotlin.properties.Delegates

open class Component(engine: Engine, val gameObject: GameObject) : EngineObject(engine) {

    val enableChanged = Event<Component>();

    val transform: Transform
        get() = gameObject.transform
    var enabled: Boolean by Delegates.observable(true) { _, _, new ->
        run {
            if (new) {
                onEnabled()
            } else {
                onDisabled()
            }
            enableChanged(this)
        }
    }

    init {
        engine.register(this)
    }

    open fun onScreenSizeChanged(){}
    open fun update(){}
    open fun onEnabled(){}
    open fun onDisabled(){}

}

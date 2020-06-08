package pl.com.damdor.azazello

class GameObject(engine: Engine) : EngineObject(engine) {

    val transform = Transform(engine, this)

    private val components: MutableList<Component> = mutableListOf()

    fun <T : Component> getComponent(clazz: Class<T>): Component? {
        return components.filter { f -> clazz.isInstance(f) }.first()
    }

    fun <T : Component> register(component: T) : T{
        components.add(component)
        return component
    }

    fun onScreenSizeChanged() {
        components.forEach { it.onScreenSizeChanged() }
    }
}
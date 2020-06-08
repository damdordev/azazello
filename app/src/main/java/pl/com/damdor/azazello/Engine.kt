package pl.com.damdor.azazello

import android.content.Context
import android.opengl.GLES20

class Engine(val context: Context) {

    val world: World
    val screen = Screen()

    private val renderQueue = RenderQueue(this)
    private val cameras = mutableListOf<Camera>()
    private val renderers = mutableListOf<Renderer>()
    private val enabledComponents = mutableListOf<Component>()
    private val allComponents = mutableListOf<Component>()

    private var enabledComponentsValid = false;

    init {
        world = World(this)
    }

    fun onScreenSizeChanged(width: Float, height: Float){
        GLES20.glViewport(0, 0, width.toInt(), height.toInt())
        screen.onScreenSizeChanged(width, height)
        world.onScreenSizeChanged()
    }

    fun register(component: Component){
        when(component){
            is Camera -> cameras.add(component)
            is Renderer -> renderers.add(component)
        }

        allComponents.add(component)
        component.enableChanged += { enabledComponentsValid = false }
    }

    fun doFrame(){
        updateEnabledComponents()
        enabledComponents.forEach { it.update() }
        cameras.forEach { renderQueue.draw(it, renderers)}
    }

    private fun updateEnabledComponents(){
        if(enabledComponentsValid){
            return
        }

        enabledComponents.clear()
        enabledComponents.addAll(allComponents.filter { it.enabled })
    }
}
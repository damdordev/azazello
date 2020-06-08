package pl.com.damdor.azazello

class World(engine : Engine) {

    val root: Transform = Transform(engine, GameObject(engine))

    fun onScreenSizeChanged(){
        root.gameObject.onScreenSizeChanged()
    }

}
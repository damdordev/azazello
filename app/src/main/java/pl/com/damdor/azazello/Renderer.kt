package pl.com.damdor.azazello

abstract class Renderer(engine: Engine, gameObject: GameObject) : Component(engine, gameObject) {

    abstract fun draw(camera: Camera);

}
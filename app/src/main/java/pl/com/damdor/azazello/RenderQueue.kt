package pl.com.damdor.azazello

import android.opengl.GLES20

class RenderQueue(engine: Engine){

    fun draw(camera: Camera, renderers: List<Renderer>){
        GLES20.glClearColor(camera.clearColor.r, camera.clearColor.g, camera.clearColor.b, camera.clearColor.a)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        renderers
            .sortedBy { it -> it.transform.position.z }
            .forEach { it.draw(camera) }
    }

}
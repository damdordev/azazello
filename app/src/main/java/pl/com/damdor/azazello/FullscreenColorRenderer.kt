package pl.com.damdor.azazello

import android.opengl.GLES20
import org.joml.Vector4f
import java.nio.FloatBuffer

class FullscreenColorRenderer(engine: Engine, gameObject: GameObject) : Renderer(engine, gameObject){

    var color = Vector4f(1f, 1f, 1f, 1f)

    private val tmpBuffer = FloatBuffer.allocate(4)

    override fun draw(camera: Camera) {
        GLES20.glGetFloatv(GLES20.GL_COLOR_CLEAR_VALUE, tmpBuffer)
        GLES20.glClearColor(color.r, color.g, color.b, color.a);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

}
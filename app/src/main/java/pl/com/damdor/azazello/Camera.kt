package pl.com.damdor.azazello

import org.joml.Matrix4f
import org.joml.Vector4f

class Camera(engine: Engine, gameObject: GameObject) : Component(engine, gameObject) {

    var height: Float
        get() = mHeight
        set(value) {
            mHeight = value
            invalidateProjectionMatrix()
        }
    var width: Float
        get() {
            if(!mProjectionMatrixValid){
                recalculateProjectionMatrix()
            }
            return height*engine.screen.aspect
        }
        set(value) {
            mHeight = value/engine.screen.aspect
            invalidateProjectionMatrix()
        }
    var near: Float
        get() = mNear
        set(value) {
            mNear = value
            invalidateProjectionMatrix()
        }
    var far: Float
        get() = mFar
        set(value) {
            mFar = value
            invalidateProjectionMatrix()
        }
    var clearColor = Vector4f(0f, 0f, 0f, 1f)

    private var mProjectionMatrixValid = false
    private var mProjectionMatrix = Matrix4f()

    private var mHeight = 100f
    private var mNear = 0.1f
    private var mFar = 100f


    val projectionMatrix: Matrix4f
        get() {
            if(!mProjectionMatrixValid){
                recalculateProjectionMatrix()
            }
            return mProjectionMatrix;
        }


    override fun onScreenSizeChanged(){
        mProjectionMatrixValid = false
    }

    private fun invalidateProjectionMatrix(){
        mProjectionMatrixValid = false
    }

    private fun recalculateProjectionMatrix(){
        var w = 0.5f*mHeight*engine.screen.aspect
        var h = 0.5f*mHeight

        mProjectionMatrix.ortho(-w, w, h, h, mNear, mFar)

        mProjectionMatrixValid = true
    }

}
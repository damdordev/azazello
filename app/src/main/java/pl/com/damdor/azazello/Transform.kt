package pl.com.damdor.azazello

import com.scalified.tree.multinode.ArrayMultiTreeNode
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

class Transform(engine: Engine, gameObject: GameObject) : Component(engine, gameObject) {

    var localPosition: Vector3f
        get() = mLocalPosition
        set(v) { mLocalPosition = v; invalidateLocalMatrix() }
    var localRotation: Quaternionf
        get() = mLocalRotation
        set(v) { mLocalRotation = v; invalidateLocalMatrix() }
    var localScale: Vector3f
        get() = mLocalScale
        set(v) { mLocalScale = v; invalidateLocalMatrix() }
    val localMatrix: Matrix4f
        get() {
            if(!mLocalMatrixValid){
                calculateLocalMatrix()
            }
            return mLocalMatrix
        }
    val globalMatrix: Matrix4f
        get() {
            if(!mGlobalMatrixValid){
                calculateGlobalMatrix()
            }
            return mGlobalMatrix
        }
    var parent: Transform
        get() = mNode.parent().data().transform
        set(value) {
            value.mNode.add(mNode)
            invalidateGlobalMatrix()
        }
    val position: Vector3f
        get() = mGlobalPosition
    val rotation: Quaternionf
        get() = mGlobalRotation
    val scale: Vector3f
        get() = mGlobalScale


    private var mLocalPosition = Vector3f(0f, 0f, 0f)
    private var mLocalRotation = Quaternionf().identity()
    private var mLocalScale = Vector3f(1f, 1f, 1f)

    private var mLocalMatrix = Matrix4f()
    private var mLocalMatrixValid = false

    private var mGlobalMatrix: Matrix4f = Matrix4f()
    private var mGlobalPosition = Vector3f()
    private var mGlobalRotation = Quaternionf()
    private var mGlobalScale = Vector3f()
    private var mGlobalMatrixValid = false

    private val mNode = ArrayMultiTreeNode<GameObject>(gameObject);

    private fun invalidateLocalMatrix(){
        mLocalMatrixValid = false
        invalidateGlobalMatrix()
    }

    private fun invalidateGlobalMatrix() {
        mGlobalMatrixValid = false
        mNode.forEach { it.data().transform.invalidateGlobalMatrix() }
    }

    private fun calculateLocalMatrix(){
        mLocalMatrix.identity()
        mLocalMatrix.scale(mLocalScale)
        mLocalMatrix.rotate(mLocalRotation)
        mLocalMatrix.translate(mLocalPosition)

        mLocalMatrixValid = true
    }

    private fun calculateGlobalMatrix() {
        if(mNode.isRoot) {
            mGlobalMatrix.identity()
        } else {
            mGlobalMatrix.set(parent.globalMatrix)
        }
        mGlobalMatrix.mul(localMatrix)

        mGlobalMatrix.getTranslation(mGlobalPosition)
        mGlobalMatrix.getNormalizedRotation(mGlobalRotation)
        mGlobalMatrix.getScale(mGlobalScale)
        mGlobalMatrixValid = true
    }


}
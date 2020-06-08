package pl.com.damdor.azazello

import android.opengl.GLES20
import org.joml.Matrix4f
import java.nio.FloatBuffer

class MeshRenderer(engine: Engine,
                   gameObject: GameObject,
                   var mesh: Mesh,
                   var material: Material) : Renderer(engine, gameObject){

    private val mCachedParametersPosition = mutableListOf<Int>()
    private val mMatrixBuffer = FloatBuffer.allocate(16)

    override fun draw(camera: Camera) {
        GLES20.glUseProgram(material.shader.handler)

        setupDataFromMesh()

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mesh.vertexCount)

        mCachedParametersPosition.forEach { GLES20.glDisableVertexAttribArray(it )}
        mCachedParametersPosition.clear()
    }

    private fun setupDataFromMesh(){
        material.shader.parameters.vertexParameters
            .filter { mesh.hasVertexData(it.name) }
            .forEach { setVertexData(it) }
    }

    private fun setupDataFromCamera(camera: Camera){
        var par = material.shader.parameters

        if(par.hasUniformParameter(ShaderFields.PROJECTION_MATRIX)){
            setupCameraUniforms(par.getUniformParameter(ShaderFields.PROJECTION_MATRIX),
                                camera.projectionMatrix)
        }
        if(par.hasUniformParameter(ShaderFields.VIEW_MATRIX)){
            setupCameraUniforms(par.getUniformParameter(ShaderFields.VIEW_MATRIX),
                                camera.gameObject.transform.localMatrix)
        }
        if(par.hasUniformParameter(ShaderFields.MODEL_MATRIX)){
            setupCameraUniforms(par.getUniformParameter(ShaderFields.MODEL_MATRIX),
                                gameObject.transform.globalMatrix)
        }
        if(par.hasUniformParameter(ShaderFields.MODEL_MATRIX)){
            setupCameraUniforms(par.getUniformParameter(ShaderFields.MODEL_MATRIX),
                                gameObject.transform.globalMatrix)
        }
    }

    private fun setVertexData(par: ShaderParameter){
        var position = GLES20.glGetAttribLocation(material.shader.handler, par.glslName)
        mCachedParametersPosition.add(position)

        var data = mesh.getVertexData(par.name)

        GLES20.glEnableVertexAttribArray(position)
        GLES20.glVertexAttribPointer(position,
                                     par.size,
                                     GLES20.GL_FLOAT,
                                     false,
                                     4*par.size,
                                     mesh.getVertexData(par.name))
    }

    private fun setupCameraUniforms(par: ShaderParameter, matrix: Matrix4f){
        var position = GLES20.glGetUniformLocation(material.shader.handler, par.glslName)
        matrix.get(mMatrixBuffer)
        GLES20.glUniformMatrix4fv(position, 16, false, mMatrixBuffer)
    }

}
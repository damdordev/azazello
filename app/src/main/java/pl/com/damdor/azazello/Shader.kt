package pl.com.damdor.azazello

import android.opengl.GLES20
import android.util.Log

class Shader(engine: Engine) : EngineObject(engine) {
    private val TAG = "Shader"

    val handler: Int
        get() = mHandler
    val parameters: ShaderParameters
        get() = mParameters

    private var mHandler = 0
    private lateinit var mParameters: ShaderParameters


    fun load(resId: Int){
        var parts = ResourceUtils.readRawText(engine.context, resId).let {
            ResourceUtils.splitIntoParts(it)
        }

        mParameters = parseParameters(parts["parameters"]!!)
        mHandler = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, compileShader(GLES20.GL_VERTEX_SHADER, parts["vertex_shader"]!!))
            GLES20.glAttachShader(it, compileShader(GLES20.GL_FRAGMENT_SHADER, parts["fragment_shader"]!!))
            GLES20.glLinkProgram(it)
        }
    }

    private fun parseParameters(text: String): ShaderParameters{
        var uniformParameters = mutableListOf<ShaderParameter>()
        var vertexParameters = mutableListOf<ShaderParameter>()

        text.lines()
            .map { it.trim() }
            .filter { !it.isNullOrBlank() }
            .map { it.split(' ') }
            .forEach {
                when(it[0]){
                    "vertex" -> vertexParameters.add(parseParameter(it))
                    "uniform" -> uniformParameters.add(parseParameter(it))
                }
            }

        return ShaderParameters(uniformParameters, vertexParameters)
    }

    private fun compileShader(type: Int, code: String): Int {
        return GLES20.glCreateShader(type).also {
            GLES20.glShaderSource(it, code)
            GLES20.glCompileShader(it)
            GLES20.glGetShaderInfoLog(it).also {
                if(it.isNullOrBlank()) {
                    Log.v(TAG, "Shader successfully compiled")
                } else {
                    Log.v(TAG, "Error during shader compilation: $it")
                }
            }
        }
    }

    private fun parseParameter(items: List<String>): ShaderParameter {
        return ShaderParameter(
            type = when(items[1]){
                "float4" -> ShaderParameterType.FLOAT_4
                "matrix4x4" -> ShaderParameterType.MATRIX_4_4
                else -> throw IllegalArgumentException("Wrong shader parameter type $items[0]")
            },
            name = items[2],
            glslName = when(items.size){
                3 -> items[2]
                else -> items[3]
            }
        )
    }

}
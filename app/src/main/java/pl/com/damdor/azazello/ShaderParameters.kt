package pl.com.damdor.azazello

enum class ShaderParameterType {
    FLOAT_4,
    MATRIX_4_4
}

data class ShaderParameter(val type: ShaderParameterType,
                           val name: String,
                           val glslName: String) {

    val size: Int
        get() = getParameterSize(type)

    companion object {

        fun getParameterSize(type: ShaderParameterType) = when(type) {
            ShaderParameterType.FLOAT_4 -> 4
            ShaderParameterType.MATRIX_4_4 -> 16
        }

    }

}

data class ShaderParameters(val uniformParameters:List<ShaderParameter>,
                            val vertexParameters: List<ShaderParameter>) {

    private val mNameToUniformParameter: Map<String, ShaderParameter>

    init {
        mNameToUniformParameter = uniformParameters.associateBy { it.name }
    }

    fun hasUniformParameter(name: String) = mNameToUniformParameter.containsKey(name)
    fun getUniformParameter(name: String) = mNameToUniformParameter[name]!!


}
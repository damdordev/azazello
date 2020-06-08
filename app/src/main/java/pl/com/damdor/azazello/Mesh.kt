package pl.com.damdor.azazello

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Mesh(engine: Engine): EngineObject(engine) {
    val vertexData = HashMap<String, List<Float>>()
    val vertexCount: Int
        get() = mVertexCount

    private var mBuffers = HashMap<String, FloatBuffer>()
    private var mCachedBuffers = HashSet<String>()
    private var mVertexCount = 0

    fun hasVertexData(name: String) : Boolean {
        rebuildBufferIfNeeded(name)
        return mCachedBuffers.contains(name)
    }

    fun getVertexData(name: String) : FloatBuffer{
        if(!hasVertexData(name)){
            throw IllegalArgumentException("Invalid vertex data: $name}")
        }

        return mBuffers[name]!!
    }

    fun commit(){
        mCachedBuffers.clear()
        mVertexCount = vertexData.values.first().size / 4
    }

    fun load(resId : Int){
        vertexData.clear()
        mCachedBuffers.clear()

        ResourceUtils.splitIntoParts(ResourceUtils.readRawText(engine.context, resId)).forEach {
            var data =  it.value
                .replace('\n', ' ')
                .split(' ')
                .filter { !it.isBlank() }
                .map { it.toFloat() }
                .toList()

            vertexData[it.key] = data
        }

    }

    private fun rebuildBufferIfNeeded(name: String){
        if(mCachedBuffers.contains(name)){
            return
        }

        if(!vertexData.containsKey(name)){
            return
        }

        var data = vertexData[name]!!
        var buffer = ByteBuffer.allocateDirect(data.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(data.toFloatArray())
                position(0)
            }
        }

        mBuffers[name] = buffer
        mCachedBuffers.add(name)
    }
}


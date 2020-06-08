package pl.com.damdor.azazello

import org.joml.Matrix4f
import org.joml.Vector4f

class Material(engine: Engine,
               var shader: Shader,
               val float4: Map<String, Vector4f> = HashMap(),
               val matrix4x4: Map<String, Matrix4f> = HashMap()) : EngineObject(engine)
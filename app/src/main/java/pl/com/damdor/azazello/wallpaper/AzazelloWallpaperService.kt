package pl.com.damdor.azazello.wallpaper

import android.content.Context
import android.opengl.GLSurfaceView
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class AzazelloWallpaperService : WallpaperService() {
    abstract inner class AzazelloWallpaperEngine : Engine() {
        private inner class WallpaperOpenGLSurfaceView(context: Context) : GLSurfaceView(context) {
            override fun getHolder() = surfaceHolder
            fun onDestroy() = onDetachedFromWindow()
        }

        private inner class WallpaperOpenGLRenderer : GLSurfaceView.Renderer {
            override fun onDrawFrame(gl: GL10?) = draw()
            override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) = engine.onScreenSizeChanged(width.toFloat(), height.toFloat())
            override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) = onWallpaperCreated()
        }

        private val surface = WallpaperOpenGLSurfaceView(this@AzazelloWallpaperService)
        protected val engine = pl.com.damdor.azazello.Engine(applicationContext)

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            surface.apply {
                setEGLContextClientVersion(2)
                preserveEGLContextOnPause = true
                setRenderer(WallpaperOpenGLRenderer())
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if(visible){
                surface.onResume()
            }
            else {
                surface.onPause()
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            surface.onDestroy()
        }

        private fun draw(){
            engine.doFrame()
        }

        protected open fun onWallpaperCreated(){}
    }

}
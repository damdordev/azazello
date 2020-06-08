package pl.com.damdor.azazello

class Screen {

    val width: Float
        get() = mWidth
    val height: Float
        get() = mHeight
    val aspect
        get() = mWidth/mHeight


    private var mWidth = 0f
    private var mHeight = 0f

    fun onScreenSizeChanged(width: Float, height: Float){
        mWidth = width
        mHeight = height
    }

}
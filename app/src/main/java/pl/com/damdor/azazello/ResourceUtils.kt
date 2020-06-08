package pl.com.damdor.azazello


import android.content.Context
import org.apache.commons.io.IOUtils
import java.lang.StringBuilder

class ResourceUtils {

    companion object {

        fun readRawText(context: Context, id: Int): String {
            var inputStream = context.resources.openRawResource(id)
            var data = IOUtils.toString(inputStream)
            IOUtils.closeQuietly(inputStream)
            return data
        }

        fun splitIntoParts(text: String) : Map<String, String>{

            var result = HashMap<String, String>()

            var currentDepth = 0
            var currentKey = ""
            var currentValue = StringBuilder()

            text.lines().forEach {
                if(it.contains('}')){
                    --currentDepth;
                    if(currentDepth == 0){
                        result[currentKey] = currentValue.toString()
                    }
                }

                if(currentDepth > 0){
                    currentValue.append(it)
                    currentValue.appendln()
                }

                if(it.contains('{')){
                    ++currentDepth
                    if(currentDepth == 1){
                        currentKey = it.split(' ')[0]
                        currentValue.clear()
                    }
                }

            }

            return result;

        }

    }

}
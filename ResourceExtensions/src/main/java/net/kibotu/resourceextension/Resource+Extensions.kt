@file:JvmName("ResourceExtensions")

package net.kibotu.resourceextension


import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.JELLY_BEAN_MR1
import android.os.Build.VERSION_CODES.N
import android.text.Html
import android.text.Spanned
import android.util.TypedValue
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import com.github.florent37.application.provider.application
import java.io.ByteArrayOutputStream
import java.util.*


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */


/**
 * Converts dp to pixel.
 */
val Float.dp: Float get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, application!!.resources.displayMetrics)

/**
 * Converts pixel to dp.
 */
val Float.px: Float get() = this / Resources.getSystem().displayMetrics.density

val Int.dp: Int get() = toFloat().dp.toInt()

val Int.px: Int get() = toFloat().px.toInt()

val Int.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), application!!.resources.displayMetrics)

val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, application!!.resources.displayMetrics)

var TextView.sp: Float
    set(value) = setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
    get() = textSize.sp

val Int.resBoolean: Boolean
    get() = application!!.resources.getBoolean(this)

val Int.resInt: Int
    get() = application!!.resources.getInteger(this)

val Int.resLong: Long
    get() = application!!.resources!!.getInteger(this).toLong()

val Int.resDimension: Float
    get() = application!!.resources!!.getDimension(this)

val Int.resString: String
    get() = application!!.resources!!.getString(this)

inline fun <reified T> Int.resString(formatArg: T): String = application!!.resources!!.getString(this, formatArg)

val Int.resColor: Int
    get() = ContextCompat.getColor(application!!, this)

val Int.html: Spanned
    get() = resString.html

val String.html: Spanned
    get() = if (SDK_INT >= N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }

val Int.resAnim: Animation
    get() = AnimationUtils.loadAnimation(application!!, this)

val Int.resAnimator: Animator
    get() = AnimatorInflater.loadAnimator(application!!, this)

val Int.resName: String
    get() = application!!.resources.getResourceEntryName(this)

val String.resId: Int
    get() = application!!.resources.getIdentifier(this, "id", application!!.packageName)

val Int.resDrawable: Drawable
    get() = ContextCompat.getDrawable(application!!, this)!!

val String.resDrawableId: Int
    get() = application!!.resources.getIdentifier(this, "drawable", application!!.packageName)

val Int.resStringArray: Array<String>
    get() = application!!.resources!!.getStringArray(this)

val Int.resIntArray: IntArray
    get() = application!!.resources!!.getIntArray(this)

val Int.resTextArray: Array<CharSequence>
    get() = application!!.resources!!.getTextArray(this)

fun Int.asCsv(context: Context = application!!): List<String> = context.resources.getString(this).split(",").map(String::trim).toList()

/**
 * Returns -1 if not found
 */
@ColorRes
fun @receiver:ColorRes Int.resColorArray(@IntRange(from = 0) index: Int): Int {
    val array = application!!.resources.obtainTypedArray(this)
    val resourceId = array.getResourceId(index, -1)
    array.recycle()
    return resourceId
}

/**
 * Returns -1 if not found
 */
val @receiver:DrawableRes Int.resDrawableArray: List<Int>
    @SuppressLint("Recycle")
    get() = application!!.resources.obtainTypedArray(this).use { array ->
        (0 until array.length()).map { array.getResourceId(it, -1) }
    }

/**
 * Returns -1 if not found
 */
val @receiver:ColorRes Int.resColorArray: List<Int>
    @SuppressLint("Recycle")
    get() = application!!.resources.obtainTypedArray(this).use { array ->
        (0 until array.length()).map { array.getResourceId(it, -1) }
    }

/**
 * Returns -1 if not found
 */
fun Int.resDrawableArray(@IntRange(from = 0) index: Int): Int {
    val array = application!!.resources.obtainTypedArray(this)
    val resourceId = array.getResourceId(index, -1)
    array.recycle()
    return resourceId
}

val screenWidthtDp
    get() = Resources.getSystem().configuration.screenWidthDp

val screenHeightDp
    get() = Resources.getSystem().configuration.screenHeightDp

val screenWidthPixels
    get() = Resources.getSystem().displayMetrics.widthPixels

val screenHeightPixels
    get() = Resources.getSystem().displayMetrics.heightPixels

fun isRightToLeft(): Boolean = R.bool.rtl.resBoolean

inline fun <reified T> Int.times(factory: () -> T) = arrayListOf<T>().apply { for (i in 0..this@times) add(factory()) }

@StringRes
fun String.fromStringResource(onError: ((Exception) -> Unit)? = null): Int {
    try {
        return application!!.resources.getIdentifier(this, "string", application!!.packageName)
    } catch (e: Exception) {
        onError?.invoke(e)
    }
    return 0
}

fun String.stringFromAssets(): String? = try {
    application!!.assets.open(this).bufferedReader().use { it.readText() }
} catch (e: Exception) {
    e.printStackTrace()
    null
}

@DrawableRes
fun String.fromDrawableResource(onError: ((Exception) -> Unit)? = null): Int {
    try {
        return application!!.resources.getIdentifier(this, "drawable", application!!.packageName)
    } catch (e: Exception) {
        onError?.invoke(e)
    }
    return 0
}

private val BUFFER_SIZE by lazy { 16 * 1024 }

private val copyBuffer by lazy { ThreadLocal<ByteArray>() }

/**
 * Thread-Safe
 */
fun String.bytesFromAssets(context: Context? = application, onError: ((Exception) -> Unit)? = null): ByteArray? = try {

    context?.assets?.open(this)?.use { inputStream ->

        ByteArrayOutputStream().use { buffer ->

            var byteBuffer = copyBuffer.get()
            if (byteBuffer == null) {
                byteBuffer = ByteArray(BUFFER_SIZE)
                copyBuffer.set(byteBuffer)
            }

            var readBytes: Int
            do {
                readBytes = inputStream.read(byteBuffer, 0, byteBuffer.size)
                if (readBytes != -1)
                    buffer.write(byteBuffer, 0, readBytes)
            } while (readBytes != -1)

            buffer.flush()

            buffer.toByteArray()
        }
    }

} catch (e: Exception) {
    onError?.invoke(e)
    null
}

val Uri.isTelephoneLink: Boolean
    get() = toString().startsWith("tel:")

val Uri.isMailToLink: Boolean
    get() = toString().startsWith("mailto:")

/**
 * https://stackoverflow.com/a/9475663/1006741
 *
 * @param id     string resource id
 * @param locale locale
 * @return localized string
 */
fun getLocalizedString(@StringRes id: Int, locale: Locale): String {

    if (SDK_INT > JELLY_BEAN_MR1) {
        return getLocalizedResources(application!!, locale).getString(id)
    }

    val res = application!!.resources
    val conf = res.configuration
    val savedLocale = conf.locale
    conf.locale = locale // whatever you want here
    res.updateConfiguration(conf, null) // second arg null means don't change

    // retrieve resources from desired locale
    val text = res.getString(id)

    // restore original locale
    conf.locale = savedLocale
    res.updateConfiguration(conf, null)

    return text
}

@RequiresApi(api = JELLY_BEAN_MR1)
fun getLocalizedResources(context: Context, desiredLocale: Locale): Resources {
    var conf = context.resources.configuration
    conf = Configuration(conf)
    conf.setLocale(desiredLocale)
    val localizedContext = context.createConfigurationContext(conf)
    return localizedContext.resources
}
@file:JvmName("ResourceExtensions")

package net.kibotu.resourceextension


import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.JELLY_BEAN_MR1
import android.os.Build.VERSION_CODES.N
import android.text.Html
import android.text.Spanned
import android.transition.Transition
import android.transition.TransitionInflater
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.view.animation.LayoutAnimationController
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import com.github.florent37.application.provider.application
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */


// region Values

val @receiver:BoolRes Int.resBoolean: Boolean
    get() = application!!.resources.getBoolean(this)


val @receiver:IntegerRes Int.resInt: Int
    get() = application!!.resources.getInteger(this)

val @receiver:IntegerRes Int.resLong: Long
    get() = this.resInt.toLong()

val @receiver:StringRes Int.resString: String
    get() = application!!.resources.getString(this)

inline fun <reified T> Int.resString(vararg formatArgs: T): String = application!!.resources!!.getString(this, *formatArgs)

/**
 * https://stackoverflow.com/a/9475663/1006741
 *
 * @param id     string resource id
 * @param locale locale
 * @return localized string
 */
fun @receiver:StringRes Int.localizedString(locale: Locale = Locale.UK): String {

    if (SDK_INT > JELLY_BEAN_MR1) {
        return localizedResources(application!!, locale).getString(this)
    }

    val res = application!!.resources
    val conf = res.configuration
    val savedLocale = conf.locale
    conf.locale = locale // whatever you want here
    res.updateConfiguration(conf, null) // second arg null means don't change

    // retrieve resources from desired locale
    val text = res.getString(this)

    // restore original locale
    conf.locale = savedLocale
    res.updateConfiguration(conf, null)

    return text
}

fun @receiver:PluralsRes Int.quantityString(amount: Int): String = application!!.resources.getQuantityString(this, amount)

inline fun <reified T> @receiver:PluralsRes Int.quantityString(amount: Int, vararg formatArgs: T): String = application!!.resources.getQuantityString(this, amount, *formatArgs)

@RequiresApi(api = JELLY_BEAN_MR1)
fun localizedResources(context: Context = application!!, desiredLocale: Locale = Locale.UK): Resources {
    var conf = context.resources.configuration
    conf = Configuration(conf)
    conf.setLocale(desiredLocale)
    val localizedContext = context.createConfigurationContext(conf)
    return localizedContext.resources
}


@get:ColorInt
val @receiver:ColorRes Int.resColor: Int
    get() = ContextCompat.getColor(application!!, this)

@get:ColorLong
val @receiver:ColorRes Int.resColorLong: Long
    get() = ContextCompat.getColor(application!!, this).toLong()

@get:Dimension
val @receiver:DimenRes Int.resDimension: Float
    get() = application!!.resources.getDimension(this)

fun @receiver:FractionRes Int.resFraction(base: Int, pbase: Int): Float = application!!.resources.getFraction(this, base, pbase)

val @receiver:StringRes Int.html: Spanned
    get() = resString.html

val String.html: Spanned
    get() = if (SDK_INT >= N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }

val @receiver:StringRes Int.csv: List<String>
    get() = resString.split(",").map(String::trim)

val @receiver:XmlRes Int.resXml: XmlResourceParser
    get() = application!!.resources.getXml(this)

// endregion

// region Arrays

val @receiver:ArrayRes Int.resIntArray: IntArray
    get() = application!!.resources!!.getIntArray(this)

val @receiver:ArrayRes Int.resStringArray: Array<String>
    get() = application!!.resources!!.getStringArray(this)

val @receiver:ArrayRes Int.resTextArray: Array<CharSequence>
    get() = application!!.resources!!.getTextArray(this)

/**
 * Returns -1 if not found
 */
@get:ColorRes
val @receiver:ColorRes Int.resColorArray: List<Int>
    @SuppressLint("Recycle")
    get() = application!!.resources.obtainTypedArray(this).use { array ->
        (0 until array.length()).map { array.getResourceId(it, -1) }
    }

@get:ColorInt
val @receiver:ArrayRes Int.resColorIntArray: List<Int>
    get() = resColorArray.map { it.resColor }
/**
 * Returns -1 if not found
 */
@get:DrawableRes
val @receiver:ArrayRes Int.resDrawableIdArray: List<Int>
    @SuppressLint("Recycle")
    get() = application!!.resources.obtainTypedArray(this).use { array ->
        (0 until array.length()).map { array.getResourceId(it, -1) }
    }

val @receiver:ArrayRes Int.resDrawableArray: List<Drawable>
    @SuppressLint("Recycle")
    get() = resDrawableIdArray.map { it.resDrawable }

// endregion

// region Ids

/**
 * Returns -1 if not found
 */
@get:IdRes
val String.resId: Int
    get() = application!!.resources.getIdentifier(this, "id", application!!.packageName)

val @receiver:AnyRes Int.resName: String
    get() = application!!.resources.getResourceEntryName(this)

val @receiver:AnyRes Int.resTypeName: String
    get() = application!!.resources.getResourceTypeName(this)

val @receiver:AnyRes Int.resPackageName: String
    get() = application!!.resources.getResourcePackageName(this)

@StringRes
fun String.resStringId(onError: ((Exception) -> Unit)? = null): Int {
    try {
        return application!!.resources.getIdentifier(this, "string", application!!.packageName)
    } catch (e: Exception) {
        onError?.invoke(e)
    }
    return 0
}

@get:DrawableRes
val String.resDrawableId: Int
    get() = application!!.resources.getIdentifier(this, "drawable", application!!.packageName)

@DrawableRes
fun String.resDrawableId(onError: ((Exception) -> Unit)? = null): Int {
    try {
        return application!!.resources.getIdentifier(this, "drawable", application!!.packageName)
    } catch (e: Exception) {
        onError?.invoke(e)
    }
    return 0
}

// endregion

// region Objects

val @receiver:DrawableRes Int.resDrawable: Drawable
    get() = ContextCompat.getDrawable(application!!, this)!!

val @receiver:AnimatorRes Int.resAnim: Animation
    get() = AnimationUtils.loadAnimation(application!!, this)

val @receiver:AnimRes Int.resAnimator: Animator
    get() = AnimatorInflater.loadAnimator(application!!, this)

val @receiver:FontRes Int.resFont: Typeface
    @RequiresApi(Build.VERSION_CODES.O)
    get() = application!!.resources.getFont(this)

val @receiver:RawRes Int.resRaw: InputStream
    get() = application!!.resources.openRawResource(this)

val @receiver:InterpolatorRes Int.resInterpolator: Interpolator
    get() = AnimationUtils.loadInterpolator(application!!, this)

val @receiver:AnimRes Int.resLayoutAnimation: LayoutAnimationController
    get() = AnimationUtils.loadLayoutAnimation(application!!, this)

val @receiver:TransitionRes Int.resTransition: Transition
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    get() = TransitionInflater.from(application!!).inflateTransition(this)

// endregion

// region Layout

val @receiver:LayoutRes Int.resLayout: XmlResourceParser
    get() = application!!.resources.getLayout(this)

fun @receiver:LayoutRes Int.inflate(parent: ViewParent?, attachToRoot: Boolean = false): View = LayoutInflater.from((parent as ViewGroup).context).inflate(this, parent, attachToRoot)

// endregion

// region Screen

/**
 * Converts dp to pixel.
 */
val Float.dp: Float get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, application!!.resources.displayMetrics)

/**
 * Converts pixel to dp.
 */
val Float.px: Float get() = this / Resources.getSystem().displayMetrics.density

/**
 * Converts dp to pixel.
 */
val Int.dp: Int get() = toFloat().dp.toInt()

/**
 * Converts pixel to dp.
 */
val Int.px: Int get() = toFloat().px.toInt()

val Int.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), application!!.resources.displayMetrics)

val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, application!!.resources.displayMetrics)

var TextView.sp: Float
    set(value) = setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
    get() = textSize.sp

val screenWidthDp: Int
    get() = Resources.getSystem().configuration.screenWidthDp

val screenHeightDp: Int
    get() = Resources.getSystem().configuration.screenHeightDp

val screenWidthPixels: Int
    get() = Resources.getSystem().displayMetrics.widthPixels

val screenHeightPixels: Int
    get() = Resources.getSystem().displayMetrics.heightPixels

// endregion

// region Assets

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

fun String.stringFromAssets(): String? = try {
    application!!.assets.open(this).bufferedReader().use { it.readText() }
} catch (e: Exception) {
    e.printStackTrace()
    null
}

// endregion

// region Misc

val isRightToLeft: Boolean
    get() = R.bool.rtl.resBoolean

val Uri.isTelephoneLink: Boolean
    get() = toString().startsWith("tel:")

val Uri.isMailToLink: Boolean
    get() = toString().startsWith("mailto:")

// endregion
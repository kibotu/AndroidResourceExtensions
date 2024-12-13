package net.kibotu.resourceextensions.demo

import android.animation.Animator
import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Spanned
import android.transition.Transition
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.LayoutAnimationController
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import net.kibotu.resourceextension.*
import java.io.InputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val my_integer: Int = R.integer.my_integer.resInt
        val my_long: Long = R.integer.my_long.resLong
        val my_string: String = R.string.my_string.resString
        val my_string_args: String = R.string.my_string_args.resString("ipsum")
        val my_string_args_float: String = R.string.my_string_args_float.resString(2f)
        val my_localized_string: String = R.string.my_localized_string.localizedString(Locale.GERMAN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val my_localized_resources: Resources = localizedResources(this, Locale.GERMAN)
        }
        val my_quantityString: String = R.plurals.my_quantityString.quantityString(2)
        val my_quantityString_args: String = R.plurals.my_quantityString_args.quantityString(2, "total")

        val my_boolean: Boolean = R.bool.my_boolean.resBoolean
        val my_color_long: Long = R.color.my_color_long.resColorLong
        val my_color: Int = R.color.my_color.resColor
        val my_dimension: Float = R.dimen.my_dimension.resDimension
        val my_string_html: Spanned = """<a href="https://www.google.com/">Google</a>""".html
        val my_html: Spanned = R.string.my_html.html
        val my_csv: List<String> = R.string.my_csv.csv
        val my_xml: XmlResourceParser = R.xml.lorem_ipsum.resXml
        // 0.10f
        val my_fraction: Float = R.fraction.my_fraction.resFraction(2, 2)
        val my_int_array: IntArray = R.array.my_int_array.resIntArray
        val my_string_array: Array<String> = R.array.my_string_array.resStringArray
        val my_character_array: Array<CharSequence> = R.array.my_string_array.resTextArray
        /*ColorRes*/
        val my_icons_array: List<Int> = R.array.my_colors.resColorArray
        /*ColorInt*/
        val my_icons_array_color_int: List<Int> = R.array.my_colors.resColorIntArray
        /*@DrawableRes*/
        val my_colors_array: List<Int> = R.array.my_icons.resDrawableIdArray
        /*@Drawable*/
        val my_colors_array_drawable: List<Drawable> = R.array.my_icons.resDrawableArray
        @IdRes val my_id: Int = "my_id".resId
        val my_res_name: String = R.integer.my_res_name.resName
        val my_res_type_name: String = R.integer.my_res_type_name.resTypeName
        val my_res_package_name: String = R.integer.my_res_package_name.resPackageName
        @StringRes val my_res_string_id: Int = "my_res_string_id".resStringId { it.printStackTrace() }
        @DrawableRes val my_res_drawable_id: Int = "ic_share".resDrawableId
        @DrawableRes val my_res_drawable_id_with_error_handling: Int = "ic_share".resDrawableId { it.printStackTrace() }
        val my_drawable: Drawable = R.drawable.ic_share.resDrawable
        val my_anim: Animation = R.anim.grow.resAnim
        val my_animator: Animator = R.animator.flip_animation.resAnimator
        val my_font: Typeface? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            R.font.lato.resFont
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val my_raw: InputStream = R.raw.my_raw.resRaw
        val my_interpolator: Interpolator = android.R.interpolator.anticipate_overshoot.resInterpolator
        val my_res_layout_animation_controller: LayoutAnimationController = R.anim.layout_animation.resLayoutAnimation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val my_transition: Transition = android.R.transition.explode.resTransition
        }
        val my_layout: XmlResourceParser = R.layout.activity_main.resLayout
        val my_inflated_layout: View = R.layout.activity_main.inflate(findViewById(R.id.root))
        val my_dp: Float = 16f.dp
        val my_dp_int: Int = 16.dp
        val my_px: Float = 200f.px
        val my_px_int: Int = 200.px
        val my_sp: Float = 14f.sp
        val my_screen_width_dp: Int = screenWidthDp
        val my_screen_height_dp: Int = screenHeightDp
        val my_screen_width_pixels: Int = screenWidthPixels
        val my_screen_height_pixels: Int = screenHeightPixels
        val my_bytes_from_assets: ByteArray? = "my_text.json".bytesFromAssets()
        val my_string_from_assets: String? = "my_text.json".stringFromAssets()
        val my_device_is_rtl: Boolean = isRightToLeft
        val my_string_is_a_telephone_link: Boolean = Uri.parse("""<a href="tel:491771789232">Google</a>""").isTelephoneLink
        val my_string_is_a_mailto_link: Boolean = Uri.parse("""<a href="mailto:jan.rabe@kibotu.net">Google</a>""").isMailToLink
    }
}

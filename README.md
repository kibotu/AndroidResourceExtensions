[![Donation](https://img.shields.io/badge/buy%20me%20a%20coffee-brightgreen.svg)](https://www.paypal.me/janrabe/5) [![About Jan Rabe](https://img.shields.io/badge/about-me-green.svg)](https://about.me/janrabe)

# Android Resource Extensions 
[![Build Status](https://travis-ci.org/kibotu/AndroidResourceExtensions.svg?branch=master)](https://travis-ci.org/kibotu/AndroidResourceExtensions) [![](https://jitpack.io/v/kibotu/AndroidResourceExtensions.svg)](https://jitpack.io/#kibotu/AndroidResourceExtensions) [![](https://jitpack.io/v/kibotu/AndroidResourceExtensions/month.svg)](https://jitpack.io/#kibotu/AndroidResourceExtensions) [![Hits-of-Code](https://hitsofcode.com/github/kibotu/AndroidResourceExtensions)](https://hitsofcode.com/view/github/kibotu/AndroidResourceExtensions) [![Javadoc](https://img.shields.io/badge/javadoc-SNAPSHOT-green.svg)](https://jitpack.io/com/github/kibotu/AndroidResourceExtensions/master-SNAPSHOT/javadoc/index.html) [![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15) [![Gradle Version](https://img.shields.io/badge/gradle-6.2.1-green.svg)](https://docs.gradle.org/current/release-notes)  [![Kotlin](https://img.shields.io/badge/kotlin-1.3.61-green.svg)](https://kotlinlang.org/) [![GitHub license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/kibotu/AndroidResourceExtensions/master/LICENSE) [![androidx](https://img.shields.io/badge/androidx-brightgreen.svg)](https://developer.android.com/topic/libraries/support-library/refactor)

Convenience extension methods for android's auto-generated /res folder *R.* class.

Basically everything in your res/ and /assets folder can be accessed via an extension function. *Note: everything gets loaded with current activity context, if that is not available it uses application context.*

### Features:

##### Values

- resBoolean
- resInt
- resLong
- resString
- resString(args)
- localizedString(locale)
- localizedResources(locale)
- quantityString
- quantityString(args)
- resColorLong
- resColor
- resDimension
- html
- html
- csv
- resXml
- resFraction

##### Arrays

- resIntArray
- resStringArray
- resTextArray
- resColorArray
- resColorIntArray
- resDrawableArray
- resDrawableIdArray

##### Ids

- resId
- resName
- resTypeName
- resPackageName
- resStringId
- resDrawableId
- resDrawableId((Exception)->Unit)
- resColorDrawable
- resGradientDrawable

##### Objects

- resDrawable
- colorDrawable
- resAnim
- resAnimator
- resFont
- resRaw
- resInterpolator
- resLayoutAnimation
- resTransition

##### Layout

- resLayout
- inflate

##### Screen

- dp
- px
- sp
- pt
- inches
- mm
- screenWidthDp
- screenHeightDp
- screenWidthPixels
- screenHeightPixels

##### Assets

- bytesFromAssets // thread-safe
- stringFromAssets

##### Misc

- isRightToLeft
- isTelephoneLink
- isMailToLink


### ManifestPermissions

- isGranted // checks if granted, doesn't trigger request


### How to use


```kotlin
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
val my_font: Typeface = R.font.lato.resFont
val my_raw: InputStream = R.raw.my_raw.resRaw
val my_interpolator: Interpolator = android.R.interpolator.anticipate_overshoot.resInterpolator
val my_res_layout_animation_controller: LayoutAnimationController = R.anim.layout_animation.resLayoutAnimation
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    val my_transition: Transition = android.R.transition.explode.resTransition
}
val my_layout: XmlResourceParser = R.layout.activity_main.resLayout
val my_inflated_layout: View = R.layout.activity_main.inflate(root)
val my_dp: Float = 16f.dp
val my_dp_int: Int = 16.dp
val my_px: Float = 200f.px
val my_px_int: Int = 200.px
val my_sp: Float = 14f.sp
val my_pt: Float = 14f.pt
val my_inches: Float = 14f.inches
val my_mm: Float = 14f.mm
val my_screen_width_dp: Int = screenWidthDp
val my_screen_height_dp: Int = screenHeightDp
val my_screen_width_pixels: Int = screenWidthPixels
val my_screen_height_pixels: Int = screenHeightPixels
val my_bytes_from_assets: ByteArray? = "my_text.json".bytesFromAssets()
val my_string_from_assets: String? = "my_text.json".stringFromAssets()
val my_device_is_rtl: Boolean = isRightToLeft
val my_string_is_a_telephone_link: Boolean = Uri.parse("""<a href="tel:491771789232">Google</a>""").isTelephoneLink
val my_string_is_a_mailto_link: Boolean = Uri.parse("""<a href="mailto:cloudgazer3d@gmail.com">Google</a>""").isMailToLink

val locationPermission : Boolean = Manifest.permission.ACCESS_FINE_LOCATION.isGranted

```

###  How to install

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
``` 

Step 2. Add the dependency
```groovy
dependencies {
    implementation "com.github.florent37:applicationprovider:1.0.0"
    implementation 'com.github.kibotu:AndroidResourceExtensions:{latest}'
}
```

### Notes

Follow me on Twitter: [@wolkenschauer](https://twitter.com/wolkenschauer)

Let me know what you think: [jan.rabe@kibotu.net](mailto:jan.rabe@kibotu.net)

Contributions welcome!

### License

<pre>
Copyright 2019 Jan Rabe

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>

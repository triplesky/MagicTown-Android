# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.bekids.gogotown.** { *; }
-keep class bitter.jnibridge.* { *; }
-keep class com.unity3d.player.* { *; }
-keep interface com.unity3d.player.IUnityPlayerLifecycleEvents { *; }
-keep class org.fmod.* { *; }
-keep class com.google.androidgamesdk.ChoreographerCallback { *; }
-keep class com.google.androidgamesdk.SwappyDisplayManager { *; }
-keep class com.android.vending.billing.**
-keep class com.ihuman.library.accountsdk.google.GoogleHelper
-keep class com.ihuman.sdk.lib.download.** { *; }
-keep class com.ihuman.sdk.lib.network.core.HttpSignTools { *; }
-keep class android.media.** {*;}
-ignorewarnings
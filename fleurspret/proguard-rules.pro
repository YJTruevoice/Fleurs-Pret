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
# You can specify any path and filename.

##-ignorewarnings
-dontwarn android.support.v4.**
-dontwarn javax.**
-dontwarn java.awt.**
-dontwarn org.apache.harmony.**
-dontwarn org.apache.http.**
-dontwarn java.lang.invoke.*
-dontwarn android.app.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn android.net.**


# 保留了继承自Activity、Application这些类的子类
# 因为这些子类都有可能被外部调用
# 比如说, 第一行就保证了所有Activity的子类不要被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

-keep class **.R$* {
 *;
}
# 保留自定义控件(继承自View)不被混淆
-keep public class * extends android.view.View {
  *** get*();
  void set*(***);
  public <init>(android.content.Context);
  public <init>(android.content.Context, android.util.AttributeSet);
  public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep public class com.facile.immediate.electronique.fleurs.pret.R$*{
    public static final int *;
}

-keep class com.facebook.**
-keep class com.facebook.** { *; }
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses

# 执行混淆时丢失了文件名和行号
-keepattributes SourceFile,LineNumberTable

-dontoptimize
-keepclassmembers class ** {
    public void onEvent*(**);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase


# rxjava
-keep class rx.schedulers.Schedulers{
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

## eventbus
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.viewholder.QuickViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.viewholder.QuickViewHolder {
     <init>(...);
}

# Parcelable、Serializable
-keepclassmembers class * implements android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep public class * implements android.os.Parcelable{
    *;
}

-keep public class * implements java.io.Serializable{
    *;
}
-keepclassmembers class * implements java.io.Serializable {
	static final long serialVersionUID;
	private static final java.io.ObjectStreamField[] serialPersistentFields;
	private void writeObject(java.io.ObjectOutputStream);
	private void readObject(java.io.ObjectInputStream);
	java.lang.Object writeReplace();
	java.lang.Object readResolve();
	public <fields>;
}
# 网络请求的数据类
-keep class com.arthur.network.model.** { *; }

# 保留 ViewBinding 生成的类
-keep class *.com.facile.immediate.electronique.fleurs.pret.databinding.** { *; }
# 保留布局文件的名称
-keepclassmembers,allowobfuscation class * {
    *** inflate*(android.view.LayoutInflater, android.view.ViewGroup, boolean);
}

-keep class **.R$layout { *; }
# 避免混淆 ViewBinding 实例
-keepclassmembers class * {
    <fields>;
}

-keep public class com.arthur.commonlib.utils.ReflectUtils
-keepclassmembers public class com.arthur.commonlib.utils.ReflectUtils {
    *;
}

-keep public class * implements java.lang.reflect.Type {
    *;
}
-keepclassmembers public class * implements java.lang.reflect.Type {
    *;
}
-keep class java.lang.reflect.** { *; }

-keep @androidx.annotation.Keep class * {
    *;
}

-keepclassmembers @androidx.annotation.Keep class * {
    *;
}


-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
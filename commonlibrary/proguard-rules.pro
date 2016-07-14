## Add project specific ProGuard rules here.
## By default, the flags in this file are appended to flags specified
## in E:\android_en\android-sdks\android-sdks/tools/proguard/proguard-android.txt
## You can edit the include path and order by changing the proguardFiles
## directive in build.gradle.
##
## For more details, see
##   http://developer.android.com/guide/developing/tools/proguard.html
#
## Add any project specific keep options here:
#
## If your project uses WebView with JS, uncomment the following
## and specify the fully qualified class name to the JavaScript interface
## class:
##-keepclassmembers class fqcn.of.javascript.interface.for.webview {
##   public *;
##}
##---------------------------基础配置----------------------------
#-dontshrink
#-dontoptimize
#-optimizationpasses 5                                                           # 指定代码的压缩级别
#-dontusemixedcaseclassnames                                                     # 是否使用大小写混合
#-dontskipnonpubliclibraryclasses                                                # 是否混淆第三方jar
#-dontpreverify                                                                  # 混淆时是否做预校验
#-verbose                                                                        # 混淆时是否记录日志
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*        # 混淆时所采用的算法
#-keep public class * extends android.app.Activity                               # 保持哪些类不被混淆
#-keep public class * extends android.app.Application                            # 保持哪些类不被混淆
#-keep public class * extends android.app.Service                                # 保持哪些类不被混淆
#-keep public class * extends android.content.BroadcastReceiver                  # 保持哪些类不被混淆
#-keep public class * extends android.content.ContentProvider                    # 保持哪些类不被混淆
#-keep public class * extends android.app.backup.BackupAgentHelper               # 保持哪些类不被混淆
#-keep public class * extends android.preference.Preference                      # 保持哪些类不被混淆
#-keep public class com.android.vending.licensing.ILicensingService              # 保持哪些类不被混淆
#
#-keepclasseswithmembernames class * {                                           # 保持 native 方法不被混淆
#    native <methods>;
#}
#
##如果有引用v4包可以添加下面这行
#-keep class android.support.v4.** { *; }
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.app.Fragment
#
##如果引用了v4或者v7包，可以忽略警告，因为用不到android.support
#-dontwarn android.support.**
#
##保持自定义组件不被混淆
#-keep public class * extends android.view.View {
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#    public void set*(...);
#}
#
#-keepclassmembers enum * {                                                      # 保持枚举 enum 类不被混淆
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep class * implements android.os.Parcelable {                                # 保持 Parcelable 不被混淆
#  public static final android.os.Parcelable$Creator *;
#}
#
##保持 Serializable 不被混淆
#-keepnames class * implements java.io.Serializable
#
##保持 Serializable 不被混淆并且enum 类也不被混淆
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
##(保持注解，及使用注解的Activity不被混淆，不然会影响Activity中你使用注解相关的代码无法使用)
#-keep class * extends java.lang.annotation.Annotation {*;}
#
## 以libaray的形式引用的图片加载框架,不想混淆（注意，此处不是jar包形式，想混淆去掉"#"）
##-keep class com.nostra13.universalimageloader.** { *; }
#
##Gson相关的混淆配置
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class sun.misc.Unsafe { *; }
#
#-dontwarn com.google.android.maps.**
#-dontwarn android.webkit.WebView
##---------------------------基础配置----------------------------
#
#
##---------------------------友盟分享----------------------------
#-dontwarn com.umeng.**
#-dontwarn com.tencent.weibo.sdk.**
#-dontwarn com.facebook.**
#
#-keep enum com.facebook.**
#-keepattributes Exceptions,InnerClasses,Signature
#-keepattributes *Annotation*
#-keepattributes SourceFile,LineNumberTable
#
#-keep public interface com.facebook.**
#-keep public interface com.tencent.**
#-keep public interface com.umeng.socialize.**
#-keep public interface com.umeng.socialize.sensor.**
#-keep public interface com.umeng.scrshot.**
#
#-keep public class com.umeng.socialize.* {*;}
#-keep public class javax.**
#-keep public class android.webkit.**
#
#-keep class com.facebook.**
#-keep class com.umeng.scrshot.**
#-keep public class com.tencent.** {*;}
#-keep class com.umeng.socialize.sensor.**
#-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
#-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
#-keep class im.yixin.sdk.api.YXMessage {*;}
#-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
##---------------------------友盟分享----------------------------
#
##---------------------------支付宝----------------------------
#-libraryjars libs/alipaySDK-20151112.jar
#-keep class com.alipay.android.app.IAlixPay{*;}
#-keep class com.alipay.android.app.IAlixPay$Stub{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
#-keep class com.alipay.sdk.app.PayTask{ public *;}
#-keep class com.alipay.sdk.app.AuthTask{ public *;}
##---------------------------支付宝----------------------------
#
##---------------------------微信支付----------------------------
#-libraryjars libs/libammsdk.jar
#-keep class com.tencent.** { *;}
##---------------------------微信支付----------------------------
#
#-keep public class [com.drivingassisstantHouse.library].R$*{
#    public static final int *;
#}

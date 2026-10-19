# Proguard rules for SCR Case Book
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Room Database rules
-keep class androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

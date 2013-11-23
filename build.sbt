import sbtandroid.AndroidKeys._

name := "scandroid"

version := "0.1"

versionCode := 0

platformName in Android := "android-19"  // Ice cream sandwich and later is 80% market share, so I could drop to 15

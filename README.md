# Playground

## Choice of Libraries

- `kotlin("plugin.serialization")` for JSON, Moshi can be future consideration if performance is
  concern.
- `junit 5` Note: instrumentation test will only able to run on Android 8.0/API 26/Oreo, we
  currently set min to 26 because of some bug,
  see https://github.com/mannodermaus/android-junit5/issues/225.
- `Jetpack compose` Note currently still doing some demo on databinding, else we will remove all
  unrelated library.
- `Jetpack room` Single Source of Truth
- `Coroutines` you know why.
- `Hilt` you should know.
- `Coil` kotlin base image loading like Glide.

### TODO

[x] enable junitPlatform
[x] Jetpack navigation compose
[x] UseCase
[x] Domain
[] Saving api auth token
[] Dynamic features (Waiting for better Hilt support in the future)
[] Kotlin json serialization customization to transform all empty/blank string to null

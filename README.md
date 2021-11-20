# Playground

## Choice of Libraries

- `kotlin("plugin.serialization")` for JSON, Moshi can be future consideration if performance is
  concern.
- `junit 5` Note: instrumentation test will only able to run on Android 8.0/API 26/Oreo, we
  currently set min to 26 because of some bug,
  see https://github.com/mannodermaus/android-junit5/issues/225.
- `Jetpack compose` Compose is a fast moving project, but lets give it a try
- `Jetpack room` Single Source of Truth
- `Coroutines` you know why.
- `Hilt` you should know.
- `Coil` kotlin base image loading like Glide.
- `Data Binding` Note currently still doing some demo on databinding, else we will remove all
  data binding unrelated setting/code.

### TODO

[x] enable junitPlatform
[x] Jetpack navigation compose
[x] UseCase
[x] Domain
[] Saving api auth token
[] Dynamic features (Waiting for better Hilt support in the future)
[] Kotlin json serialization customization to transform all empty/blank string to null
[] Solution to transform DTO to Entity
[] Network bound resource with Data Mapper or use DataSource approach?

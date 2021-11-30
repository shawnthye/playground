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
- `Data Binding` Note currently still doing some demo on data binding, else we will remove all data
  binding unrelated setting/dependency.

### TODO

- [x] enable junitPlatform
- [x] Jetpack navigation compose
- [x] UseCase, FlowUseCase, PagingUseCase (experimental)
- [x] Domain
- [ ] Saving api auth token
- [ ] Dynamic features (Waiting for better Hilt support in the future)
- [x] Kotlin json serialization customization to transform all empty/blank string to null
- [ ] Consider Moshi? **Kotlinx Json** use @file:UseSerializers(NotBlankStringSerializer::
  class), no way to apply in the JsonBuilder like Gson TypeAdapter, see
  also [issue](https://github.com/Kotlin/kotlinx.serialization/issues/507)
- [ ] Solution to transform DTO to Entity
- [ ] Network bound resource with Data Mapper or use DataSource approach?
- [ ] exponential back off retry
- [ ] Paging 3
- [ ] Bridge between feature module, concept: Bridge in app-core, feature module implement the
  bridge itself

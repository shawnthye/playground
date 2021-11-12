// package app.playground.cronet
//
// import android.content.Context
// import kotlinx.coroutines.CoroutineDispatcher
// import kotlinx.coroutines.runBlocking
// import okhttp3.Headers
// import okhttp3.Interceptor
// import okhttp3.MediaType.Companion.toMediaType
// import okhttp3.Protocol
// import okhttp3.Request
// import okhttp3.Response
// import okhttp3.ResponseBody.Companion.toResponseBody
// import okio.Buffer
// import org.chromium.net.CronetEngine
// import org.chromium.net.CronetException
// import org.chromium.net.UploadDataProviders
// import org.chromium.net.UrlRequest
// import org.chromium.net.UrlResponseInfo
// import java.util.concurrent.Executors
// import kotlin.coroutines.suspendCoroutine
//
// class CronetInterceptor constructor(
//     context: Context,
//     private val dispatcher: CoroutineDispatcher,
// ) : Interceptor {
//
//     private val cronet = CronetEngine.Builder(context)
//         .enableHttp2(true)
//         .enableQuic(true)
//         // .setUserAgent("CronetSampleApp")
//         .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_DISABLED, 0)
//         .build()
//
//     private val executor = Executors.newSingleThreadExecutor()
//
//     override fun intercept(chain: Interceptor.Chain): Response {
//
//         val response: Response? = runBlocking(dispatcher) {
//             getResponse(chain.request())
//         }
//
//
//         if (response == null || response.code == 401) {
//             // if null, let okhttp try again
//             // if 401, next interceptor for auth or let okhttp try again
//             return chain.proceed(chain.request())
//         }
//
//         return response
//     }
//
//     @Suppress("BlockingMethodInNonBlockingContext")
//     suspend fun getResponse(origin: Request): Response? {
//         val response: Response? = suspendCoroutine { continuation ->
//             val callback = object : ReadToMemoryCronetCallback() {
//                 override fun onSucceeded(
//                     request: UrlRequest?,
//                     response: UrlResponseInfo?,
//                     bodyBytes: ByteArray?,
//                     latencyNanos: Long,
//                 ) {
//                     val headersBuilder = Headers.Builder()
//
//                     response?.allHeaders?.forEach { entry ->
//                         entry.value.forEach { value ->
//                             headersBuilder.add(entry.key, value)
//                         }
//                     }
//
//                     val headers = headersBuilder.build()
//                     val contentType = headers["Content-Type"] ?: headers["content-type"]
//
//                     val body = bodyBytes?.toResponseBody(contentType = contentType?.toMediaType())
//                     val now = System.currentTimeMillis()
//                     val res = Response.Builder()
//                         .sentRequestAtMillis(now - latencyNanos)
//                         .receivedResponseAtMillis(now)
//                         .headers(headersBuilder.build())
//                         .code(response?.httpStatusCode ?: 200)
//                         .request(origin)
//                         .protocol(
//                             Protocol.get(
//                                 response?.negotiatedProtocol ?: Protocol.HTTP_2.toString(),
//                             ),
//                         )
//                         .message(response?.httpStatusText ?: "No message")
//                         .body(body)
//                         .build()
//
//                     continuation.resumeWith(Result.success(res))
//                 }
//
//                 override fun onFailed(
//                     request: UrlRequest,
//                     response: UrlResponseInfo,
//                     exception: CronetException,
//                 ) {
//                     continuation.resumeWith(Result.success(null))
//                 }
//             }
//
//             cronet.newUrlRequestBuilder(origin.url.toString(), callback, executor)
//                 .setHttpMethod(origin.method)
//                 .apply {
//                     for (header in origin.headers) {
//                         addHeader(header.first, header.second)
//                     }
//
//                     origin.body?.also { body ->
//                         body.contentType()?.toString()?.also {
//                             addHeader("Content-Type", it)
//                         }
//                         val buffer = Buffer()
//                         body.writeTo(buffer)
//                         setUploadDataProvider(
//                             UploadDataProviders.create(buffer.readByteArray()),
//                             executor,
//                         )
//                     }
//                 }
//                 .build()
//                 .start()
//         }
//
//         return response
//     }
// }

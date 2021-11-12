// package app.playground.cronet
//
// import org.chromium.net.CronetException
// import org.chromium.net.UrlRequest
// import org.chromium.net.UrlResponseInfo
// import timber.log.Timber
// import java.io.ByteArrayOutputStream
// import java.io.IOException
// import java.nio.ByteBuffer
// import java.nio.channels.Channels
// import java.nio.channels.WritableByteChannel
//
// internal abstract class ReadToMemoryCronetCallback : UrlRequest.Callback() {
//     private val bytesReceived: ByteArrayOutputStream = ByteArrayOutputStream()
//     private val receiveChannel: WritableByteChannel = Channels.newChannel(bytesReceived)
//     private val startTimeNanos: Long = System.nanoTime()
//     override fun onRedirectReceived(
//         request: UrlRequest, info: UrlResponseInfo, newLocationUrl: String,
//     ) {
//         // Invoked whenever a redirect is encountered. This will only be invoked between the call
//         // to UrlRequest.start() and onResponseStarted(). The body of the redirect response, if it
//         // has one, will be ignored. The redirect will not be followed until the URLRequest's
//         // followRedirect method is called, either synchronously or asynchronously.
//         Timber.i("****** onRedirectReceived ******")
//         request.followRedirect()
//     }
//
//     override fun onResponseStarted(request: UrlRequest, info: UrlResponseInfo) {
//         // Invoked when the final set of headers, after all redirects, is received. Will only be
//         // invoked once for each request.
//         //
//         // With the exception of onCanceled(), no other {@link Callback} method will be invoked
//         // for the request, including onSucceeded() and onFailed(), until read() is called
//         // to attempt to start reading the response body.
//         Timber.i("****** Response Started ******")
//         Timber.i("*** Headers Are *** " + info.allHeaders)
//
//         // One must use a *direct* byte buffer when calling the read method.
//         request.read(ByteBuffer.allocateDirect(BYTE_BUFFER_CAPACITY_BYTES))
//     }
//
//     override fun onReadCompleted(
//         request: UrlRequest, info: UrlResponseInfo?, byteBuffer: ByteBuffer,
//     ) {
//         // Invoked whenever part of the response body has been read. Only part of the buffer may be
//         // populated, even if the entire response body has not yet been consumed.
//         //
//         // With the exception of onCanceled(), no other {@link Callback} method will be invoked
//         // for the request, including onSucceeded() and onFailed(), until read() is called
//         // to attempt to continue reading the response body.
//         Timber.i("****** onReadCompleted ******$byteBuffer")
//
//         // The byte buffer we're getting in the callback hasn't been flipped for reading,
//         // so flip it so we can read the content.
//         byteBuffer.flip()
//         try {
//             receiveChannel.write(byteBuffer)
//         } catch (e: IOException) {
//             Timber.i(e, "IOException during ByteBuffer read. Details: ")
//         }
//         // Reset the buffer to prepare it for the next read
//         byteBuffer.clear()
//
//         // Continue reading the request
//         request.read(byteBuffer)
//     }
//
//     override fun onSucceeded(request: UrlRequest, info: UrlResponseInfo) {
//         // Invoked when request is completed successfully. Once invoked, no other Callback methods
//         // will be invoked.
//         val latencyNanos = System.nanoTime() - startTimeNanos
//         Timber.i(
//             """****** Cronet Request Completed,
//                 |the latency is $latencyNanos nanoseconds. ${getWasCachedMessage(info)}
//                 |""".trimMargin(),
//         )
//
//
//         Timber.i("****** Cronet Negotiated protocol:  ${info.negotiatedProtocol}")
//         Timber.i(
//             """****** Cronet Request Completed, status code is ${info.httpStatusCode},
//             |total received bytes is ${info.receivedByteCount}
//             |""".trimMargin(),
//         )
//         val bodyBytes: ByteArray = bytesReceived.toByteArray()
//
//         // We invoke the callback directly here for simplicity. Note that the executor running this
//         // callback might be shared with other Cronet requests, or even with other parts of your
//         // application. Always make sure to appropriately provision your pools, and consider
//         // delegating time consuming work on another executor.
//         onSucceeded(request, info, bodyBytes, latencyNanos)
//     }
//
//     abstract fun onSucceeded(
//         request: UrlRequest?, response: UrlResponseInfo?, bodyBytes: ByteArray?, latencyNanos: Long,
//     )
//
//     override fun onFailed(
//         request: UrlRequest,
//         response: UrlResponseInfo,
//         exception: CronetException,
//     ) {
//         Timber.i("****** onFailed, error is: ${exception.message}")
//     }
//
//     companion object {
//         private const val BYTE_BUFFER_CAPACITY_BYTES = 64 * 1024
//         private fun getWasCachedMessage(responseInfo: UrlResponseInfo): String {
//             return if (responseInfo.wasCached()) {
//                 "The request was cached."
//             } else {
//                 ""
//             }
//         }
//     }
// }

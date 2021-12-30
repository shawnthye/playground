package app.playground.di.cronet

import okhttp3.internal.closeQuietly
import okio.Buffer
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.IOException
import java.nio.ByteBuffer

// TODO: should replace with null parameter for error only or all?
internal abstract class ReadToMemoryCronetCallback : UrlRequest.Callback() {

    private val buffer = Buffer()

    override fun onRedirectReceived(
        request: UrlRequest,
        info: UrlResponseInfo,
        newLocationUrl: String,
    ) {
        // TODO: follow redirect
        request.followRedirect()
    }

    override fun onResponseStarted(urlRequest: UrlRequest, info: UrlResponseInfo) {
        // Invoked when the final set of headers, after all redirects, is received. Will only be
        // invoked once for each request.
        //
        // With the exception of onCanceled(), no other {@link Callback} method will be invoked
        // for the request, including onSucceeded() and onFailed(), until read() is called
        urlRequest.read(ByteBuffer.allocateDirect(BYTE_BUFFER_CAPACITY_BYTES))
    }

    override fun onReadCompleted(
        urlRequest: UrlRequest,
        info: UrlResponseInfo,
        byteBuffer: ByteBuffer,
    ) {
        // The byte buffer we're getting in the callback hasn't been flipped for reading,
        // so flip it so we can read the content.
        byteBuffer.flip()
        try {
            buffer.write(byteBuffer)
            // receiveChannel.write(byteBuffer)
        } catch (e: IOException) {
            onFailure(urlRequest, info, e)
        }
        // Reset the buffer to prepare it for the next read
        byteBuffer.clear()

        // Continue reading the request
        urlRequest.read(byteBuffer)
    }

    final override fun onSucceeded(urlRequest: UrlRequest, info: UrlResponseInfo) {
        // Invoked when request is completed successfully. Once invoked, no other Callback methods
        // will be invoked.
        val bodyBytes: ByteArray = buffer.readByteArray()
        buffer.clear()
        buffer.closeQuietly()
        // We invoke the callback directly here for simplicity. Note that the executor running this
        // callback might be shared with other Cronet requests, or even with other parts of your
        // application. Always make sure to appropriately provision your pools, and consider
        // delegating time consuming work on another executor.
        onSucceeded(urlRequest, info, bodyBytes)
    }

    abstract fun onSucceeded(
        urlRequest: UrlRequest,
        info: UrlResponseInfo,
        bodyBytes: ByteArray,
    )

    final override fun onFailed(
        urlRequest: UrlRequest,
        info: UrlResponseInfo?,
        error: CronetException,
    ) = onFailure(urlRequest = urlRequest, info = info, error = error)

    abstract fun onFailure(urlRequest: UrlRequest, info: UrlResponseInfo?, error: IOException)

    final override fun onCanceled(urlRequest: UrlRequest, info: UrlResponseInfo?) {
        super.onCanceled(urlRequest, info)
        onCanceled()
    }

    abstract fun onCanceled()

    companion object {
        private const val BYTE_BUFFER_CAPACITY_BYTES = 256 * 1024
    }
}

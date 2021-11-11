package feature.playground.deviant.domain

import core.playground.IoDispatcher
import core.playground.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Call
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import timber.log.Timber
import java.net.InetAddress
import javax.inject.Inject

private val DNS_LIST = listOf(
    // Google
    byteArrayOf(8, 8, 8, 8),
    byteArrayOf(8, 8, 4, 4),

    // Quad9
    byteArrayOf(9, 9, 9, 9),
    byteArrayOf(149.toByte(), 112, 112, 112),

    // OpenDns
    byteArrayOf(208.toByte(), 67, 222.toByte(), 222.toByte()),
    byteArrayOf(208.toByte(), 67, 220.toByte(), 220.toByte()),
)

private val BOOTSTRAP_CLIENT = OkHttpClient.Builder()
    .addNetworkInterceptor(HttpLoggingInterceptor().apply { level = Level.HEADERS })
    .build()

private val GOOGLE_DOH = DnsOverHttps.Builder()
    .client(BOOTSTRAP_CLIENT)
    .url("https://dns.google/dns-query".toHttpUrl())
    .bootstrapDnsHosts(DNS_LIST.map { InetAddress.getByAddress(it) })
    .build()

private val Quad9_DOH = DnsOverHttps.Builder()
    .client(BOOTSTRAP_CLIENT)
    .url("https://dns.quad9.net/dns-query".toHttpUrl())
    .bootstrapDnsHosts(
        listOf(
            byteArrayOf(9, 9, 9, 9),
            byteArrayOf(149.toByte(), 112, 112, 112),
        ).map {
            InetAddress.getByAddress(it)
        },
    )
    .build()

private val DNS: Dns = object : Dns {
    override fun lookup(hostname: String): List<InetAddress> {

        val default = InetAddress.getAllByName(hostname).toList()
        val public = DNS_LIST.map { InetAddress.getByAddress(hostname, it) }

        return public + default
    }
}

class TestDNSUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Unit>(dispatcher) {

    private fun testInetAddressTime() {
        for (dnsAddress in DNS_LIST) {
            val current = System.currentTimeMillis()

            InetAddress.getByAddress(dnsAddress).also {
                Timber.i("$dnsAddress take ${System.currentTimeMillis() - current}ms to resolve")
            }
        }
    }

    @Suppress("RedundantSuspendModifier", "BlockingMethodInNonBlockingContext")
    override suspend fun execute(parameters: Unit) {

        testInetAddressTime()

        // InetAddress.getByName("104.81.200.241").let {
        //     val bytes = it.address
        //     for (b in bytes) {
        //         Timber.i("bytes = ${b and 0xFF.toByte()}")
        //     }
        // }
        //
        // // InetAddress.getByName("api.zalora.com.my").isReachable()
        // InetAddress.getByName("api.zalora.com.my").let {
        //     val bytes = it.address
        //     for (b in bytes) {
        //         Timber.i("bytes = ${b and 0xFF.toByte()}")
        //     }
        // }

        val client = BOOTSTRAP_CLIENT.newBuilder()
            .dns(Quad9_DOH)
            .hostnameVerifier { hostname, session ->
                Timber.i("hostname: $hostname")
                Timber.i("session.peerHost: ${session.peerHost}")
                true
            }
            .eventListener(OkhttpEventListener)
            .build()

        val request = Request.Builder()
            .url("https://api.zalora.com.my/v1/feed?language=en&venture=my&segment=men")
            .header("Accept", "application/json")
            .header("Zalora-Country", "my")
            .build()

        val response = client.newCall(request).execute()

        with(response) {
            body?.string()
        }

        with(Timber) {
            i("response.cacheResponse?.cacheControl?? ${response.cacheResponse?.cacheControl}")
            i("response.networkResponse is null?? ${response.networkResponse == null}")
            i("response is cached?? ${response.cacheResponse != null}")
            i("InetAddress.getLocalHost() ${InetAddress.getLocalHost()}")
        }
    }
}

private object OkhttpEventListener : EventListener() {
    override fun dnsStart(call: Call, domainName: String) {
        Timber.i("dnsStart: domainName=${domainName}")
    }

    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
        Timber.i("dnsEnd: domainName=${domainName}, $inetAddressList")
    }
}

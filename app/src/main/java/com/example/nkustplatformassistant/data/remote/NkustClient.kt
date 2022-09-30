package com.example.nkustplatformassistant.data.remote

import android.content.Context
import com.example.nkustplatformassistant.R
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cookies.*
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import kotlin.text.toCharArray

class NkustClient {
    companion object {
        fun getInstance(applicationContext: Context): HttpClient {
            val client = HttpClient(CIO) {
                install(HttpCookies) {
                    storage = AcceptAllCookiesStorage()
                }
                engine {
                    https {
                        trustManager = SslSettings(applicationContext)
                            .getTrustManager()
                    }
                }
            }

            return client
        }
    }

    class SslSettings(val context: Context) {
        fun getKeyStore(): KeyStore {
            val keyStoreFile = context.resources.openRawResource(R.raw.nkustcertchain)
            val keyStorePassword = "NKUSTCertPass".toCharArray()
            val keyStore: KeyStore = KeyStore.getInstance("BKS")
            keyStore.load(keyStoreFile, keyStorePassword)
            return keyStore
        }

        fun getTrustManagerFactory(): TrustManagerFactory? {
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(getKeyStore())
            return trustManagerFactory
        }

        fun getSslContext(): SSLContext? {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, getTrustManagerFactory()?.trustManagers, null)
            return sslContext
        }

        fun getTrustManager(): X509TrustManager {
            return getTrustManagerFactory()?.trustManagers?.first { it is X509TrustManager } as X509TrustManager
        }
    }
}

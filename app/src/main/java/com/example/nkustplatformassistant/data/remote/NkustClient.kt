package com.example.nkustplatformassistant.data.remote

import android.content.Context
import com.example.nkustplatformassistant.R
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cookies.*
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.junit.rules.Timeout
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore
import java.security.Security
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class NkustClient {
    companion object {
        fun getInstance(applicationContext: Context): HttpClient {
            val client = HttpClient(CIO) {
                install(HttpCookies) {
                    storage = AcceptAllCookiesStorage()
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 60000
                    socketTimeoutMillis = 60000
                    connectTimeoutMillis = 60000
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

        fun getInstance(): HttpClient {
            val client = HttpClient(CIO) {
                install(HttpCookies) {
                    storage = AcceptAllCookiesStorage()
                }
                engine {
                    https {
                        Security.addProvider(BouncyCastleProvider())
                        val keyStoreFile = FileInputStream(
                            File("./app/src/main/res/raw/nkustcertchain.bks"))
                        val keyStorePassword = "NKUSTCertPass".toCharArray()
                        val keyStore: KeyStore = KeyStore.getInstance("BKS")
                        keyStore.load(keyStoreFile, keyStorePassword)

                        val trustManagerFactory =
                            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                        trustManagerFactory.init(keyStore)

                        trustManager =
                            trustManagerFactory.trustManagers?.first { it is X509TrustManager } as X509TrustManager
                    }
                }
            }
            return client
        }
    }

    class SslSettings(val context: Context) {
        fun getKeyStore(): KeyStore {
            Security.addProvider(BouncyCastleProvider())
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

package ru.greenpix.monitoring.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress

data class Address(
    val host: String,
    val ip: String,
    val port: Int,
    val inet: InetSocketAddress
) {
    val isDomain: Boolean
        get() = host == ip

    companion object {

        suspend fun of(address: String): Address? = withContext(Dispatchers.IO) {
            address.split(':').let {
                val inet = InetSocketAddress(it[0], if (it.size > 1) it[1].toInt() else 25565)
                inet.address?.let { address ->
                    Address(
                        host = it[0],
                        ip = address.hostAddress,
                        port = inet.port,
                        inet = inet
                    )
                }
            }
        }
    }
}
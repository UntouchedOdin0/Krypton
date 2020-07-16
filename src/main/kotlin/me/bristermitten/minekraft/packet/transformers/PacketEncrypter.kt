package me.bristermitten.minekraft.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import me.bristermitten.minekraft.extension.readAllAvailableBytes
import javax.crypto.Cipher

class PacketEncrypter(
    private val cipher: Cipher
) : MessageToByteEncoder<ByteBuf>() {


    override fun encode(ctx: ChannelHandlerContext, message: ByteBuf, out: ByteBuf) {
        //Load in all of the written data
        val dataToWrite = message.readAllAvailableBytes()

        //Encrypt the data
        val encryptedBytes = encrypt(dataToWrite)
        //Write it back
        message.writeBytes(encryptedBytes)

        println("Encrypted bytes length $encryptedBytes")
    }

    fun encrypt(bytes: ByteArray): ByteArray {
        return cipher.doFinal(bytes)
    }
}

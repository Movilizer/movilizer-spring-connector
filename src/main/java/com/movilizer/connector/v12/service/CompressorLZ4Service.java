package com.movilizer.connector.v12.service;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;
import org.springframework.stereotype.Component;

@Component
public class CompressorLZ4Service {

    private LZ4Factory factory;

    private LZ4Compressor compressor;

    private LZ4FastDecompressor decompressor;

    public CompressorLZ4Service() {
        super();
        factory = LZ4Factory.fastestInstance();
        compressor = factory.fastCompressor();
        decompressor = factory.fastDecompressor();
    }

    public byte[] decompress(byte[] compressed, int decompressedSize) {
        byte[] restored = new byte[decompressedSize];
        decompressor.decompress(compressed, 0, restored, 0, decompressedSize);
        return restored;
    }

    public byte[] compress(byte[] decompressedXml) {
        final int decompressedLength = decompressedXml.length;
        // compress data
        int maxCompressedLength = compressor.maxCompressedLength(decompressedLength);
        byte[] compressedXml = new byte[maxCompressedLength];
        int compressedXmlLength = compressor.compress(decompressedXml, 0, decompressedLength,
                compressedXml, 0, maxCompressedLength);
        return compressedXml;
    }

}

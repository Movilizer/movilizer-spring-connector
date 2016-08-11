package com.movilizer.connector.persistence.entities.listeners;

import com.movilizer.connector.utils.CompressorLZ4Service;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class CompressorListenerService {

    private String charset;
    private MovilizerDistributionService mds;
    private CompressorLZ4Service compressorService;

    @Autowired
    public CompressorListenerService(@Value("${movilizer.charset}") String charset, MovilizerDistributionService mds, CompressorLZ4Service compressorService) {
        this.charset = charset;
        this.mds = mds;
        this.compressorService = compressorService;
    }

    protected <T> byte[] getBytes(T serializableObject, Class<T> tClass) {
        String serializedDatacontainer = mds.printMovilizerElementToString(serializableObject, tClass);
        return serializedDatacontainer.getBytes(Charset.forName(charset));
    }

    protected <T> T getObject(byte[] serializedObject, Class<T> tClass) {
        String xmlString = new String(serializedObject, Charset.forName(charset));
        return mds.getElementFromString(xmlString, tClass);
    }

    protected byte[] compress(final byte[] decompressedXML) {
        return compressorService.compress(decompressedXML);
    }

    protected byte[] decompress(final byte[] compressedXML, Integer decompressedSize) {
        return compressorService.decompress(compressedXML, decompressedSize);
    }


}

/*
 *
 * Copyright (c) 2012-2015 Movilizer GmbH,
 * Julius-Hatry-Straße 1, D-68163 Mannheim GmbH, Germany.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Movilizer GmbH ("Confidential Information").
 *
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with Movilizer.
 */

package com.movilizer.connector.v12.logging;

import com.movilitas.movilizer.v14.MovilizerUploadDataContainer;
import com.movilizer.connector.v12.service.MovilizerXMLParserServiceV12;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DataContainerLogger {

    @Value("${spring.profile.environment}")
    private String environment;

    @Value("${movilizer.log.dc.dir}")
    private String logDirPath;

    @Autowired
    private MovilizerXMLParserServiceV12 movilizerXMLParserService;

    private static Log logger = LogFactory.getLog(DataContainerLogger.class);

    private File dcdir;

    int suffix = 0;

    String datePath = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());

    @PostConstruct
    public void init() {
        dcdir = new File(logDirPath);
    }

    public void log(MovilizerUploadDataContainer container) {
        String filename = "dc-" + String.format("%06d", suffix++) + "-" +
                container.getContainer().getKey();
        writeTextFile(filename, movilizerXMLParserService.serializeUploadDataContainer(container));
    }

    public void writeTextFile(String filename, byte[] content) {
        File mqCurrentDir = createLogDirectory();
        filename = filename.replace(":", "-");
        File tFile = new File(mqCurrentDir, filename + ".xml");
        try {
            FileOutputStream output = new FileOutputStream(tFile);
            IOUtils.write(content, output);
            output.close();
        } catch (Exception e) {
            logger.error("error writing to file " + tFile.getAbsolutePath() + ": ", e);
        }
    }

    private void writeContentToFile(String content, File tFile) throws FileNotFoundException,
            UnsupportedEncodingException, IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(tFile);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "UTF8"));

        out.write(content);
        out.close();
        fileOutputStream.close();
        logger.info("file written to: " + tFile.getAbsolutePath());
    }

    private File createLogDirectory() {
        String pathSuffix = datePath;
        if (environment.equals("dev") == false) {
            pathSuffix = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        }

        File mqCurrentDir = new File(dcdir, pathSuffix);
        if (mqCurrentDir.exists() == false) {
            try {
                suffix = 0;
                mqCurrentDir.mkdirs();
            } catch (SecurityException e) {
                logger.error("Unable to create the MQ Messages directory.", e);
            }
        }
        return mqCurrentDir;
    }

}

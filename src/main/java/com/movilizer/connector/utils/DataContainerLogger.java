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

package com.movilizer.connector.utils;

import com.movilitas.movilizer.v15.MovilizerUploadDataContainer;
import com.movilizer.connector.MovilizerConnectorAPI;
import com.movilizer.connector.model.Processor;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DataContainerLogger {
    private static final Log logger = LogFactory.getLog(DataContainerLogger.class);

    @Value("${movilizer.charset}")
    private String charset;

    @Value("${spring.profiles.active}")
    private String environment;

    @Value("${movilizer.datacontainer.dir}")
    private String logDirPath;

    @Value("${movilizer.datacontainer.log}")
    private Boolean isActive;

    @Autowired
    private MovilizerDistributionService mds;

    @Autowired
    private MovilizerConnectorAPI connector;

    private final DateFormat df = new SimpleDateFormat("yyyy_MM_dd");

    private File loggerBaseFolder;
    private File currentLoggerFolder;
    private Long currentSuffix = 0L;
    private String currentDatePath = df.format(new Date());

    @PostConstruct
    public void init() {
        if (isActive) {
            loggerBaseFolder = new File(logDirPath);
            updateCurrentLoggingStatus();
            connector.registerProcessor(new Processor<MovilizerUploadDataContainer>() {
                @Override
                public void process(MovilizerUploadDataContainer container) {
                    log(container);
                }
            }, MovilizerUploadDataContainer.class);
        }
    }

    public void log(MovilizerUploadDataContainer container) {
        String filename = "dc-" + String.format("%06d", currentSuffix++) + "-" + container.getContainer().getKey();
        if (isNewDay()) {
            updateCurrentLoggingStatus();
        }
        writeTextFile(currentLoggerFolder, filename,
                mds.printMovilizerElementToString(container, MovilizerUploadDataContainer.class).getBytes(Charset.forName(charset)));
    }

    protected Boolean isNewDay() {
        return !df.format(new Date()).equals(currentDatePath);
    }

    protected void updateCurrentLoggingStatus() {
        currentSuffix = 0L;
        currentDatePath = df.format(new Date());
        currentLoggerFolder = createLogDirectory(loggerBaseFolder, currentDatePath);
    }

    protected File createLogDirectory(File loggerBaseFolder, String datePath) {
        File currentDir = new File(loggerBaseFolder, datePath);
        if (!currentDir.exists()) {
            try {
                currentDir.mkdirs();
            } catch (SecurityException e) {
                logger.error(String.format("Unable to create the datacontainer logger directory for path %s.",
                        currentDir.getAbsolutePath()), e);
            }
        }
        return currentDir;
    }

    protected void writeTextFile(File currentDir, String filename, byte[] content) {
        filename = filename.replace(":", "-");
        File tFile = new File(currentDir, filename + ".xml");
        try {
            FileOutputStream output = new FileOutputStream(tFile);
            IOUtils.write(content, output);
            output.close();
        } catch (Exception e) {
            logger.error("Error while writing to file " + tFile.getAbsolutePath() + ": ", e);
        }
    }
}

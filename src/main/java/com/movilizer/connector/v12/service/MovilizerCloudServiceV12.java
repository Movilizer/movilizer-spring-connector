/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.movilizer.connector.v12.service;


import com.movilitas.movilizer.v14.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MovilizerCloudServiceV12 {

    private static Log logger = LogFactory.getLog(MovilizerCloudServiceV12.class);

    private MovilizerWebServiceV14 movilizerCloud;

    public MovilizerCloudServiceV12() {
        MovilizerWebServiceV14Service service = new MovilizerWebServiceV14Service();
        movilizerCloud = service.getMovilizerWebServiceV14Soap11();
    }

    public MovilizerRequest getRequest(Long systemId, String password) {
        MovilizerRequest request = new MovilizerRequest();
        // Load system credentials
        request.setSystemId(systemId);
        request.setSystemPassword(password);
        return request;
    }

    public MovilizerResponse getReplyFromCloud(MovilizerRequest request) {
        MovilizerResponse response = movilizerCloud.movilizer(request);
        logger.debug(String.format(
                "Received response from movilizer cloud with system id %d and ack key %s",
                response.getSystemId(), response.getRequestAcknowledgeKey()));
        return response;
    }

    public Date getDataContainerTimestamp(MovilizerUploadDataContainer dataContainer) {
        return dataContainer.getContainer().getCreationTimestamp().toGregorianCalendar().getTime();
    }

    protected void setMovilizerCloud(MovilizerWebServiceV14 movilizerCloud) {
        this.movilizerCloud = movilizerCloud;
    }
}

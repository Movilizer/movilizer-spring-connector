/*
 * Copyright 2015 Movilizer GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.movilizer.connectors.spring.requestcycle.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.movilizer.connectors.spring.model.MovilizerContext;
import com.movilizer.connectors.spring.model.MovilizerSharedEndpoint;
import com.movilizer.connectors.spring.utils.SharedEndpointUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class MovilizerSupervisor extends UntypedActor {
  private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
  @Autowired
  private MovilizerContext movilizerContext;
  private List<ActorRef> managedEndpoints;

  @Override
  public void preStart() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("Starting up the Movilizer supervisor actor");
    }
    if (logger.isInfoEnabled()) {
      logger.info(String.format(
          "Loading Movilizer context with version '%s', scanned from '%s' and %d apps.",
          movilizerContext.getVersion(), movilizerContext.getAppBaseScanPackage(), movilizerContext
              .getApps().size()));
    }
    managedEndpoints = new ArrayList<>();
    for (MovilizerSharedEndpoint endpoint : SharedEndpointUtils
        .extractSharedEndpointFromContext(movilizerContext)) {
      ActorRef endpointManager =
          getContext().actorOf(SharedEndpointManager.props(endpoint),
              SharedEndpointManager.class.getSimpleName() + endpoint.consistentHashKey());
      if (logger.isDebugEnabled()) {
        logger.debug(String.format("Created shared endpoint actor %s for shared endpoint %s#%d-%s",
            endpointManager.path(), endpoint.getMdsUrl(), endpoint.getSystemId(),
            endpoint.getResponseQueue()));
      }
      getContext().watch(endpointManager);
      managedEndpoints.add(endpointManager);
    }
    super.preStart();
  }

  @Override
  public void onReceive(Object message) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("Handling message {} ", message);
    }
    if (message instanceof MovilizerContext) {
      MovilizerContext movilizerContext = (MovilizerContext) message;

    } else {
      if (logger.isErrorEnabled()) {
        logger.error(String.format("Incorrect message type received. Expected: '%s', found: '%s'",
            MovilizerContext.class, message.getClass()));
      }
      unhandled(message);
      // getContext().stop(getSelf());
    }
  }

  @Override
  public void postStop() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("Shutting down the Movilizer supervisor actor");
    }
    super.postStop();
  }
}

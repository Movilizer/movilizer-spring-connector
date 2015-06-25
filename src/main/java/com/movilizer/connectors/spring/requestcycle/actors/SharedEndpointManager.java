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

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilizer.connectors.spring.model.MovilizerSharedEndpoint;
import com.movilizer.mds.webservice.Movilizer;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import scala.concurrent.duration.Duration;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class SharedEndpointManager extends UntypedActor {
  private static final String POLL_MESSAGE = "doPoll";
  private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
  private final MovilizerSharedEndpoint endpoint;
  private final MovilizerDistributionService webservice;


  public SharedEndpointManager(MovilizerSharedEndpoint endpoint) throws MalformedURLException {
    this.endpoint = endpoint;
    webservice =
        Movilizer.buildConf().setDefaultConnectionTimeout(endpoint.getConnectionTimeoutInMillis())
            .setDefaultReceiveTimeout(endpoint.getReceiveTimeoutInMillis())
            .setEndpoint(endpoint.getMdsUrl(), endpoint.getUploadUrl()).getService();
  }

  /**
   * Create Props for an actor of this type. See:
   * http://doc.akka.io/docs/akka/2.3.10/java/untyped-actors.html#Recommended_Practices
   *
   * @param endpoint
   * @return a Props for creating this actor, which can then be further configured (e.g. calling
   *         `.withDispatcher()` on it)
   */
  public static Props props(final MovilizerSharedEndpoint endpoint) {
    return Props.create(new Creator<SharedEndpointManager>() {
      private static final long serialVersionUID = -8072533726980193139L;

      @Override
      public SharedEndpointManager create() throws Exception {
        return new SharedEndpointManager(endpoint);
      }
    });
  }

  @Override
  public void preStart() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(String.format(
          "Starting up the shared endpoint actor for %s#%d-%s with polling rate of %d seconds",
          endpoint.getMdsUrl(), endpoint.getSystemId(), endpoint.getResponseQueue(),
          endpoint.getPollingRateInSeconds()));
    }
    getContext()
        .system()
        .scheduler()
        .scheduleOnce(Duration.create(endpoint.getPollingRateInSeconds(), TimeUnit.SECONDS),
            getSelf(), POLL_MESSAGE, getContext().dispatcher(), null);
    super.preStart();
  }

  // override postRestart so we don't call preStart and schedule a new message
  @Override
  public void postRestart(Throwable reason) {}

  @Override
  public void onReceive(Object message) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("Handling message {} ", message);
    }
    if (message.equals(POLL_MESSAGE)) {
      // send another periodic tick after the specified delay
      getContext()
          .system()
          .scheduler()
          .scheduleOnce(Duration.create(endpoint.getPollingRateInSeconds(), TimeUnit.SECONDS),
              getSelf(), POLL_MESSAGE, getContext().dispatcher(), null);
      // do the polling
      MovilizerRequest request =
          webservice.prepareDownloadRequest(endpoint.getSystemId(), endpoint.getPassword(), 0,
              new MovilizerRequest());
    } else {
      if (logger.isErrorEnabled()) {
        logger.error(String.format("Incorrect message type received. Expected: '%s', found: '%s'",
            String.class, message.getClass()));
      }
      unhandled(message);
      getContext().stop(getSelf());
    }
  }

  @Override
  public void postStop() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("Shutting down the Movilizer shared endpoint manager actor");
    }
    super.postStop();
  }
}

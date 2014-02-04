/*
 * Copyright 2013 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 *
 */
package org.jboss.aerogear.unifiedpush.vertx.client;


import org.jboss.aerogear.unifiedpush.JavaSender;
import org.jboss.aerogear.unifiedpush.SenderClient;
import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;


/*
 * This is a simple Java verticle which receives `ping` messages on the event bus and sends back `pong` replies
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SenderVerticle extends BusModBase implements Handler<Message<JsonObject>> {

  private JavaSender javaSender;

  protected String address;


  public void start() {

    javaSender = new SenderClient();

      address = getOptionalStringConfig("address", "unifiedpush-vertx-client");

    final Logger logger = container.logger();

    eb.registerHandler(address, this);

    logger.info("SenderVerticle started");

    logger.info("Config value foo is " + container.config().getString("foo"));

  }


    @Override
    public void handle(Message<JsonObject> message) {
        String action = message.body().getString("action");

        if (action == null) {
            sendError(message, "action must be specified");
            return;
        }

        switch (action) {
            case "send":
                doSend(message);
                break;
            case "configure":
                doConfigure(message);
                break;

            default:
                sendError(message, "Invalid action: " + action);
        }

    }

    private void doSend(Message<JsonObject> message) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void doConfigure(Message<JsonObject> message) {

    }
}

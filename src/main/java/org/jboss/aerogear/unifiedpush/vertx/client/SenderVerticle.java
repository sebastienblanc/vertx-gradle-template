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
import org.jboss.aerogear.unifiedpush.message.MessageResponseCallback;
import org.jboss.aerogear.unifiedpush.message.UnifiedMessage;
import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonElement;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;


/*
 * This is a simple Java verticle which receives `ping` messages on the event bus and sends back `pong` replies
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SenderVerticle extends BusModBase implements Handler<Message<JsonObject>> {

  private JavaSender javaSender;

  private String address;


  private ConcurrentMap<String, PushConfig> configMap;

  private PushConfig pushConfig;

  public void start() {
    configMap  = vertx.sharedData().getMap("unifiedpush.vertx.client");

    javaSender = new SenderClient();

    address = getOptionalStringConfig("address", "unifiedpush-vertx-client");

    final Logger logger = container.logger();

    eb.registerHandler(address, this);

    logger.info("SenderVerticle started");

    logger.info("Config value foo is " + container.config().getString("foo"));

  }


    @Override
    public void handle(Message<JsonObject> message) {
        String action  = message.body().getString("action");
        if(action.equals("send")) {
            this.doSend(message);
        }
        else if(action.equals("configure")) {
            this.doConfigure(message);
        }


    }

    private void doSend(Message<JsonObject> message) {
        final Message<JsonObject> currentMesage = message;
        JsonObject config = message.body().getObject("config");
        String serverUrl = config.getString("serverURL");

        if (config == null ||  !config.containsField("description")) {
            sendError(message, "the id of a existing one must be specified");
            return;
        }
        else {
            pushConfig = configMap.get(config.getString("description"));
        }

        UnifiedMessage.Builder builder = new UnifiedMessage.Builder().
                pushApplicationId(pushConfig.getPushApplicationId()).
                masterSecret(pushConfig.getMasterSecret());
        javaSender.setServerURL(pushConfig.getServerURL());
        this.convertJsonmessageTonUnifiedMessage(builder, message);
        UnifiedMessage unifiedMessage = builder.build();
        javaSender.send(unifiedMessage, new MessageResponseCallback() {
            @Override
            public void onComplete(int statusCode) {
                JsonObject reply = new JsonObject();
                reply.putValue("status",statusCode);
               sendOK(currentMesage,reply);
            }

            @Override
            public void onError(Throwable throwable) {
               sendError(currentMesage, throwable.getMessage());
            }
        });
    }

    private void doConfigure(Message<JsonObject> message) {




    }

    private void convertJsonmessageTonUnifiedMessage(UnifiedMessage.Builder builder, Message<JsonObject> message) {
      //the criteria fields
        if(message.body().containsField("alias")){
            builder.aliases(Arrays.asList((String[])message.body().getArray("alias").toArray()));
        }
        if(message.body().containsField("deviceType")){
            builder.deviceType(Arrays.asList((String[])message.body().getArray("deviceType").toArray()));
        }
        if(message.body().containsField("variants")){
            builder.variants(Arrays.asList((String[])message.body().getArray("variants").toArray()));
        }
        if(message.body().containsField("categories")){
            Set mySet = new HashSet(Arrays.asList(message.body().getArray("categories").toArray()));
            builder.categories(mySet);
        }

        builder.attributes(message.body().getObject("message").toMap());
    }
}

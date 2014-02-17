package org.jboss.aerogear.unifiedpush.vertx.client;

import org.jboss.aerogear.unifiedpush.message.UnifiedMessage;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: sebastien
 * Date: 2/17/14
 * Time: 9:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static void convertJsonMessageToUnifiedMessage(UnifiedMessage.Builder builder, Message<JsonObject> message) {
        //the criteria fields
        if(message.body().containsField("alias")){
            Object[] objects = message.body().getArray("alias").toArray();
            String[] aliases = Arrays.copyOf(objects, objects.length, String[].class);
            builder.aliases(Arrays.asList(aliases));
        }
        if(message.body().containsField("deviceType")){
            Object[] objects = message.body().getArray("deviceType").toArray();
            String[] deviceTypes = Arrays.copyOf(objects, objects.length, String[].class);
            builder.deviceType(Arrays.asList(deviceTypes));
        }
        if(message.body().containsField("variants")){
            Object[] objects = message.body().getArray("variants").toArray();
            String[] variants = Arrays.copyOf(objects, objects.length, String[].class);
            builder.variants(Arrays.asList(variants));
        }
        if(message.body().containsField("simple-push")){
            builder.simplePush(message.body().getField("simple-push").toString());
        }
        if(message.body().containsField("categories")){
            Set mySet = new HashSet(Arrays.asList(message.body().getArray("categories").toArray()));
            builder.categories(mySet);
        }

        builder.attributes(message.body().getObject("message").toMap());
    }
}

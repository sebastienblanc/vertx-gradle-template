package org.jboss.aerogear.unifiedpush.vertx.client;

import org.jboss.aerogear.unifiedpush.message.UnifiedMessage;
import org.junit.Assert;
import org.junit.Test;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.eventbus.impl.JsonObjectMessage;
import org.vertx.java.core.json.JsonObject;

/**
 * Created with IntelliJ IDEA.
 * User: sebastien
 * Date: 2/17/14
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class UtilsTest {
    @Test
    public void testConvertJsonMessageToUnifiedMessage() {
        String testMessage = "{\"action\":\"send\", \"config\":\"test\",\"alias\":[\"john\"],\"deviceType\":[\"iPad\"],\"variants\":[\"variant1\"],\"categories\":[\"sport\"],\"message\":{\"customField\":\"customValue\"}}";

        JsonObject jsonObject = new JsonObject(testMessage);
        Message<JsonObject> message = new JsonObjectMessage(true,"test.vertx",jsonObject);

        UnifiedMessage.Builder builder = new UnifiedMessage.Builder();

        Utils.convertJsonMessageToUnifiedMessage(builder, message);

        UnifiedMessage unifiedMessage = builder.build();
        Assert.assertTrue("UnifiedMessage contains alias",!unifiedMessage.getAliases().isEmpty());
        Assert.assertTrue("UnifiedMessage contains deviceType",!unifiedMessage.getDeviceType().isEmpty());
        Assert.assertTrue("UnifiedMessage contains variant",!unifiedMessage.getVariants().isEmpty());
        Assert.assertTrue("UnifiedMessage contains categories",!unifiedMessage.getCategories().isEmpty());
        Assert.assertTrue("UnifiedMessage contains customField",((String) unifiedMessage.getAttributes().get("customField")).equals("customValue"));

    }
}

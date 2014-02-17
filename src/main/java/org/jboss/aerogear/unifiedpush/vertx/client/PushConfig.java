package org.jboss.aerogear.unifiedpush.vertx.client;

/**
 * Created with IntelliJ IDEA.
 * User: sebi-mbp
 * Date: 04/02/14
 * Time: 23:05
 * To change this template use File | Settings | File Templates.
 */
public class PushConfig {


    private String description;
    private String serverURL;
    private String pushApplicationId;
    private String masterSecret;

    public PushConfig(String serverURL, String pushApplicationId, String masterSecret) {
        this.serverURL = serverURL;
        this.pushApplicationId = pushApplicationId;
        this.masterSecret = masterSecret;
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public String getPushApplicationId() {
        return pushApplicationId;
    }

    public void setPushApplicationId(String pushApplicationId) {
        this.pushApplicationId = pushApplicationId;
    }

    public String getMasterSecret() {
        return masterSecret;
    }

    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

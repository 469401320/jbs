package me.hao0.jbs.client.core;


public class SimpleJbsClient extends AbstractJbsClient implements JbsClient {

    public SimpleJbsClient(String appName, String zkServers) {
        super(appName, zkServers);
    }

    public SimpleJbsClient(String appName, String appSecret, String zkServers) {
        super(appName, appSecret, zkServers);
    }

    public SimpleJbsClient(String appName, String appSecret, String zkServers, String zkNamespace) {
        super(appName, appSecret, zkServers, zkNamespace);
    }

    @Override
    protected void afterStart() {

    }

    @Override
    protected void afterShutdown() {

    }
}

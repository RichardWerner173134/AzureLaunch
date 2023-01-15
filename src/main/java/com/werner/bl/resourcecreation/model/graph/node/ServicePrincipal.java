package com.werner.bl.resourcecreation.model.graph.node;

import lombok.Getter;

@Getter
public class ServicePrincipal {

    private String secret;

    private String appId;

    private String tenant;

    public ServicePrincipal(String name, String appId, String tenant, String secret) {
        this.appId = appId;
        this.secret = secret;
        this.tenant = tenant;
    }
}

package com.Optum.CyFHIR.models;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class Entry extends HashMap {

    public void setResource(Map resource) {
        this.put("resource", resource);
        this.put("fullUrl", "urn:uuid:" + resource.get("id"));

        Object request = getRequest(resource.get("resourceType"));
        this.put("request", request);
    }

    private Object getRequest(Object resourceType) {
        Map request = new HashMap();
        request.put("method", "POST");
        request.put("url", resourceType);

        final ObjectMapper mapper = new ObjectMapper();
        final Object requestObj = mapper.convertValue(request, Object.class);
        return requestObj;
    }
}

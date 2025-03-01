package com.example.teamcity.requests;
import java.util.EnumMap;

import com.example.teamcity.enums.Endpoint;
import com.example.teamcity.requests.unchecked.UncheckedBase;
import io.restassured.specification.RequestSpecification;

public class UncheckedRequest {
    private final EnumMap<Endpoint, UncheckedBase> requests = new EnumMap<>(Endpoint.class);

    public UncheckedRequest(RequestSpecification spec) {
        for (var endpoint: Endpoint.values()) {
            requests.put(endpoint, new UncheckedBase(spec, endpoint));
        }
    }

    public UncheckedBase getRequest(Endpoint endpoint) {
        return requests.get(endpoint);
    }
}

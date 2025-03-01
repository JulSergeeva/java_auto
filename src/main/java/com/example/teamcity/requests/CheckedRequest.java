package com.example.teamcity.requests;

import com.example.teamcity.enums.Endpoint;
import com.example.teamcity.models.BaseModel;
import com.example.teamcity.requests.checked.CheckedBase;
import com.example.teamcity.requests.unchecked.UncheckedBase;
import io.restassured.specification.RequestSpecification;

import java.util.EnumMap;

public class CheckedRequest {
    private final EnumMap<Endpoint, CheckedBase<?>> requests = new EnumMap<>(Endpoint.class);

    public CheckedRequest(RequestSpecification spec) {
        for (var endpoint: Endpoint.values()) {
            requests.put(endpoint, new CheckedBase(spec, endpoint));
        }
    }

    public <T extends BaseModel> CheckedBase<T> getRequest(Endpoint endpoint) {
        return (CheckedBase<T>) requests.get(endpoint);
    }
}

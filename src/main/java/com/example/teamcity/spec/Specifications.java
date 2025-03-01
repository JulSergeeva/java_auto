package com.example.teamcity.spec;

import com.example.teamcity.config.Config;
import com.example.teamcity.models.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Specifications {
    private static RequestSpecBuilder reqBuilder() {
        var requestBuilder = new RequestSpecBuilder();
        requestBuilder.addFilter(new RequestLoggingFilter());
        requestBuilder.addFilter(new ResponseLoggingFilter());
        requestBuilder.setContentType(ContentType.JSON);
        requestBuilder.setAccept(ContentType.JSON);
        return requestBuilder;
    }

//    public static RequestSpecification superUserSpec() {
//        var requestBuilder = reqBuilder();
//
//        requestBuilder.setBaseUri("http://%s:%s@%s/httpAuth".formatted("", Config.getProperty("superUserToken"), Config.getProperty("host")));
//        return requestBuilder.build();
//    }
//
//    public static RequestSpecification unauthSpec() {
//        var requestBuilder = reqBuilder();
//        String host = Config.getProperty("host"); // localhost:8111
//
//        requestBuilder.setBaseUri("http://" + host);
//
//        return requestBuilder.build();
//    }
//
//    public static RequestSpecification authSpec(User user) {
//        var requestBuilder = reqBuilder();
//
//        requestBuilder.setBaseUri("http://%s:%s@%s".formatted(user.getUsername(), user.getPassword(), Config.getProperty("host")));
//        return requestBuilder.build();
//    }

    public static RequestSpecification superUserSpec() {

        var requestBuilder = reqBuilder();
        requestBuilder.setBaseUri("http://%s:%s@%s/httpAuth".formatted("", Config.getProperty("superUserToken"), Config.getProperty("host")));
        return requestBuilder.build();
    }

    public static RequestSpecification unauthSpec() {
        var requestBuilder = reqBuilder();
        String host = Config.getProperty("host"); // localhost:8111

        requestBuilder.setBaseUri("http://" + host);

        return requestBuilder.build();
    }

    public static RequestSpecification authSpec(User user) {
        var requestBuilder = reqBuilder();
        String host = Config.getProperty("host"); // localhost:8111

        requestBuilder.setBaseUri("http://" + host);

        RequestSpecification spec = requestBuilder.build();

        spec.auth().basic(user.getUsername(), user.getPassword());

        return spec;
    }
}


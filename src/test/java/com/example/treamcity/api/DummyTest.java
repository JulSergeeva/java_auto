package com.example.treamcity.api;

import com.example.teamcity.spec.Specifications;
import dev.failsafe.Bulkhead;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.List;

public class DummyTest extends BaseApiTest{
    @Test
    public void userShouldBeAbleToGetAllProjects() {

        RestAssured
                .given()
//                .spec(Specifications.getSpec().unauthSpec(User.builder().user("admin").password("admin").build()))
                .spec(Specifications.unauthSpec())
                .get("/app/rest/projects");

    }
}

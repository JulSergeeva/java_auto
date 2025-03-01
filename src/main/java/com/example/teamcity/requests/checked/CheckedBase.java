package com.example.teamcity.requests.checked;

import com.example.teamcity.enums.Endpoint;
import com.example.teamcity.models.BaseModel;
import com.example.teamcity.requests.CrudInterface;
import com.example.teamcity.requests.Request;
import com.example.teamcity.requests.unchecked.UncheckedBase;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CheckedBase<T extends BaseModel> extends Request implements CrudInterface {
    private final UncheckedBase uncheckedBase;

    public CheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
        this.uncheckedBase = new UncheckedBase(spec, endpoint);
    }

    @Override
    public T create(BaseModel model) {
        Response response = uncheckedBase.create(model);
        return response.then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as((Class<T>) endpoint.getModelClass());
    }

    public void createWithError(BaseModel model, int expectedStatusCode) {
        Response response = uncheckedBase.create(model);
        response.then().assertThat().statusCode(expectedStatusCode);
    }

    @Override
    public T read(String id) {
        Response response = uncheckedBase.read(id);
        return response.then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as((Class<T>) endpoint.getModelClass());
    }

    @Override
    public T update(String id, BaseModel model) {
        Response response = uncheckedBase.update(id, model);
        return response.then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as((Class<T>) endpoint.getModelClass());
    }

    @Override
    public Object delete(String id) {
        Response response = uncheckedBase.delete(id);
        return response.then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();
    }

    public void assign(String userLocator, String role, String projectId) {
        String url = Endpoint.ROLES.getUrl().replace("{userLocator}", userLocator);

        Response response = given()
                .spec(spec)
                .contentType("application/json")
                .body("{ \"roleId\": \"" + role + "\", \"scope\": \"p:" + projectId + "\" }")
                .post(url);

        response.then().assertThat().statusCode(HttpStatus.SC_OK);
    }

}

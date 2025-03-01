package com.example.treamcity.api;

import com.example.teamcity.enums.Endpoint;
import com.example.teamcity.models.BuildType;
import com.example.teamcity.models.Project;
import com.example.teamcity.models.User;
import com.example.teamcity.requests.CheckedRequest;
import com.example.teamcity.requests.checked.CheckedBase;
import com.example.teamcity.requests.unchecked.UncheckedBase;
import com.example.teamcity.spec.Specifications;
import org.apache.hc.core5.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.teamcity.enums.Endpoint.*;
import static com.example.teamcity.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest {
    @Test(description = "user should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        var user = generate(User.class);

        var superUserRequests = new CheckedRequest(Specifications.superUserSpec());

        superUserRequests.getRequest(USERS).create(user);

        var userRequests = new CheckedRequest(Specifications.authSpec(user));

        var project = generate(Project.class);

        superUserRequests.getRequest(ROLES).assign(user.getUsername(), "PROJECT_ADMIN", "_Root");

        project = userRequests.<Project>getRequest(PROJECTS).create(project);

        var buildType = generate(Arrays.asList(project), BuildType.class);

        userRequests.getRequest(BUILD_TYPES).create(buildType);

        var createdBuildType = (BuildType) userRequests.getRequest(BUILD_TYPES).read(buildType.getId());

        softy.assertEquals(buildType.getName(), createdBuildType.getName(), "Build type name is not correct");
    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoBuildTypesWithTheSameIdTest() {
        var user = generate(User.class);

        var superUserRequests = new CheckedRequest(Specifications.superUserSpec());

        superUserRequests.getRequest(USERS).create(user);

        var userCheckRequests = new CheckedRequest(Specifications.authSpec(user));

        var project = generate(Project.class);
        superUserRequests.getRequest(ROLES).assign(user.getUsername(), "PROJECT_ADMIN", "_Root");
        project = userCheckRequests.<Project>getRequest(PROJECTS).create(project);

        var buildType1 = generate(Arrays.asList(project), BuildType.class);

        var buildType2 = generate(Arrays.asList(project), BuildType.class);
        buildType2.setId(buildType1.getId());

        userCheckRequests.getRequest(BUILD_TYPES).create(buildType1);

        new UncheckedBase(Specifications.authSpec(user), BUILD_TYPES)
                .create(buildType2)
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("The build configuration / template ID \"%s\" is already used by another configuration or template"
                        .formatted(buildType1.getId())));
    }
}

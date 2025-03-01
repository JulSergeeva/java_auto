package com.example.teamcity.api;

import com.example.teamcity.enums.Endpoint;
import com.example.teamcity.models.Project;
import com.example.teamcity.models.User;
import com.example.teamcity.requests.checked.CheckedBase;
import com.example.teamcity.requests.unchecked.UncheckedBase;
import com.example.teamcity.spec.Specifications;
import com.example.treamcity.api.BaseApiTest;
import org.apache.hc.core5.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.example.teamcity.enums.Endpoint.*;
import static com.example.teamcity.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;
import com.example.teamcity.requests.CheckedRequest;

@Test(groups = {"Regression"})
public class ProjectTest extends BaseApiTest {

    @Test(description = "User should be able to create a project", groups = {"Positive", "CRUD"})
    public void userCreatesProjectTest() {
        var user = generate(User.class);

        var superUserRequests = new CheckedRequest(Specifications.superUserSpec());
        superUserRequests.getRequest(USERS).create(user);

        var userRequests = new CheckedRequest(Specifications.authSpec(user));

        var project = generate(Project.class);

        superUserRequests.getRequest(ROLES).assign(user.getUsername(), "PROJECT_ADMIN", "_Root");

        project = userRequests.<Project>getRequest(PROJECTS).create(project);

        Project createdProject = (Project) userRequests.getRequest(PROJECTS).read(project.getId());

        softy.assertEquals(project.getName(), createdProject.getName(), "Project name is not correct");
    }

    @Test(description = "User should not be able to create two projects with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoProjectsWithTheSameIdTest() {
        var user = generate(User.class);

        var superUserRequests = new CheckedRequest(Specifications.superUserSpec());
        superUserRequests.getRequest(USERS).create(user);

        var userCheckRequests = new CheckedRequest(Specifications.authSpec(user));

        var project1 = generate(Project.class);
        superUserRequests.getRequest(ROLES).assign(user.getUsername(), "PROJECT_ADMIN", "_Root");
        project1 = userCheckRequests.<Project>getRequest(PROJECTS).create(project1);

        var project2 = generate(Project.class);
        project2.setId(project1.getId());

        userCheckRequests.getRequest(PROJECTS).createWithError(project2, HttpStatus.SC_BAD_REQUEST);

        new UncheckedBase(Specifications.authSpec(user), PROJECTS)
                .create(project2)
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("DuplicateExternalIdException: Project ID \"%s\" is already used by another project"
                        .formatted(project2.getId())));
    }

    @Test(description = "User should not be able to create a project without a name", groups = {"Negative"})
    public void createProjectWithoutNameTest() {
        var user = generate(User.class);

        var superUserRequests = new CheckedRequest(Specifications.superUserSpec());
        superUserRequests.getRequest(USERS).create(user);

        var userRequests = new CheckedRequest(Specifications.authSpec(user));

        var project = generate(Project.class);
        project.setName(null);

        userRequests.getRequest(PROJECTS).createWithError(project, HttpStatus.SC_BAD_REQUEST);

        new UncheckedBase(Specifications.authSpec(user), PROJECTS)
                .create(project)
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("BadRequestException: Project name cannot be empty"));
    }
}

package ineedhousing.cronjob.auth;

import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.Test;

import ineedhousing.cronjob.auth.requests.RegistrationDto;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;


@QuarkusTest
class AuthResourceTest {

    @Inject
    EntityManager em;

    String secretKey = "secret_key";

    private RegistrationDto validDto = new RegistrationDto("argel6767", "newPassword", secretKey);
    private RegistrationDto invalidDto = new RegistrationDto("argel6767", "newPassword", "invalid_key");

    @Test
    @TestTransaction
    void testRegisterEndpoint_WithValidKey() {
        given().contentType("application/json").body(validDto)
        .when().post("auth/register")
        .then().statusCode(201).body(is("User created with username: " + validDto.username()));
    }

    @Test
    void testRegisterEndpoint_WithInvalidKey() {
        given().contentType("application/json").body(invalidDto)
        .when().post("auth/register")
        .then().statusCode(403).body(is("Registration key is incorrect"));
    }

    @Test
    @TestTransaction
    void testRegisterEndpoint_WithConflictingUsername(){
        AuthUser existingUser = new AuthUser();
        existingUser.username = "argel6767";
        em.persist(existingUser);
        em.flush();

        given().contentType("application/json").body(validDto)
        .when().post("auth/register")
        .then().statusCode(409).body(is("Username is already taken, choose another"));
    }
}

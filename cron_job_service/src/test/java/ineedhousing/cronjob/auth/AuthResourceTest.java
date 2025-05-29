package ineedhousing.cronjob.auth;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.Test;

import ineedhousing.cronjob.auth.requests.LoginDto;
import ineedhousing.cronjob.auth.requests.RegistrationDto;
import ineedhousing.cronjob.auth.responses.AuthUserDto;
import io.quarkus.elytron.security.common.BcryptUtil;
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

    private RegistrationDto validRegistrationDto = new RegistrationDto("argel6767", "newPassword", secretKey);
    private RegistrationDto invalidRegistrationDto = new RegistrationDto("argel6767", "newPassword", "invalid_key");

    private LoginDto validLoginDto = new LoginDto("argel6767", "password");
    private LoginDto invalidPasswordDto = new LoginDto("argel6767", "wrong_password");

    @Test
    @TestTransaction
    void testRegisterEndpoint_WithValidKey() {
        given().contentType("application/json").body(validRegistrationDto)
                .when().post("auth/register")
                .then().statusCode(201).body(is("User created with username: " + validRegistrationDto.username()));
    }

    @Test
    void testRegisterEndpoint_WithInvalidKey() {
        given().contentType("application/json").body(invalidRegistrationDto)
                .when().post("auth/register")
                .then().statusCode(403).body(is("Registration key is incorrect"));
    }

    @Test
    @TestTransaction
    void testRegisterEndpoint_WithConflictingUsername() {
        AuthUser existingUser = new AuthUser();
        existingUser.username = "argel6767";
        em.persist(existingUser);
        em.flush();

        given().contentType("application/json").body(validRegistrationDto)
                .when().post("auth/register")
                .then().statusCode(409).body(is("Username is already taken, choose another"));
    }

    @Test
    @TestTransaction
    void testLoginUser_WithValidCredentials() {
        AuthUser user = new AuthUser();
        user.username = "argel6767";
        user.password = BcryptUtil.bcryptHash("password");
        em.persist(user);
        em.flush();
        em.clear(); // Clear the persistence context to ensure we're reading from the database

        given().contentType("application/json").body(validLoginDto)
                .when().post("auth/login")
                .then().statusCode(200).body(instanceOf(AuthUserDto.class));
    }

    @Test
    @TestTransaction
    void testLoginUser_WithInvalidCredentials() {
        AuthUser user = new AuthUser();
        user.username = "argel6767";
        user.password = BcryptUtil.bcryptHash("password");
        em.persist(user);
        em.flush();
        em.clear(); // Clear the persistence context to ensure we're reading from the database

        given().contentType("application/json").body(invalidPasswordDto)
                .when().post("auth/login")
                .then().statusCode(403).body(is("Bad credentials try again"));
    }

}

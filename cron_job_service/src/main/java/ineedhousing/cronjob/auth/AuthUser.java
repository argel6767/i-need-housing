package ineedhousing.cronjob.auth;

import java.time.LocalDateTime;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.logging.Log;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;

@Entity
public class AuthUser extends PanacheEntity {

    public String username;
    public String password;
    public LocalDateTime lastLoggedIn;
    public LocalDateTime createdAt;

    public static AuthUser findByUsername(String username) {
        Log.info("Finding user with username " + username);
        return find("username", username).firstResult();
    }

    public static AuthUser findByUsernameAndPassword(String username, String password) {
        AuthUser user = findByUsername(username);
        if (user != null && BcryptUtil.matches(password, user.password)) {
            Log.info("User found!");
            return user;
        }
        Log.info("No user found.");
        return null;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

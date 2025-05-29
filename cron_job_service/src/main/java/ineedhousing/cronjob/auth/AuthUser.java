package ineedhousing.cronjob.auth;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;

@Entity
public class AuthUser extends PanacheEntity {

    public String username;
    public String password;
    public LocalDateTime lastLoggedIn;
    public LocalDateTime createdAt;

    public static AuthUser findByUsername(String username) {
        return find("username", username).firstResult();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

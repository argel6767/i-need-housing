package com.ineedhousing.producers;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class Argon2Producer {

    @Produces
    @ApplicationScoped
    public Argon2 getArgon2(){
        return Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 16);
    }
}

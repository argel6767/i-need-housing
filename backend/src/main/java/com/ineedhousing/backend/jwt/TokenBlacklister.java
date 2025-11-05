package com.ineedhousing.backend.jwt;

import com.ineedhousing.backend.jwt.models.BlacklistTokenEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TokenBlacklister {

    @EventListener
    public void onBlacklistTokenEvent(BlacklistTokenEvent event) {
        blacklistToken(event.token());
    }

    public void blacklistToken(String token) {

    }
}

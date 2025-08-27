package ineedhousing.cronjob.auth;

import ineedhousing.cronjob.log.LogService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;

@ApplicationScoped
public class ApiTokenValidationService {

    @Inject
    DataSource dataSource;

    @Inject
    LogService logService;


}

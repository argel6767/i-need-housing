package ineedhousing.cronjob.azure.container_registry.model;

import java.util.List;

public record ManifestsDeletedDto(List<String> manifestsDeleted, List<String> manifestsFailedToDelete) {
}

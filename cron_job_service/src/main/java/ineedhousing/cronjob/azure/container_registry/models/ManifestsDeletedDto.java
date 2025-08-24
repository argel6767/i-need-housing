package ineedhousing.cronjob.azure.container_registry.models;

import java.util.List;

public record ManifestsDeletedDto(List<String> manifestsDeleted, List<String> manifestsFailedToDelete) {
}

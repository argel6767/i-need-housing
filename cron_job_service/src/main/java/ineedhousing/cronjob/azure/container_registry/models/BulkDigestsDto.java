package ineedhousing.cronjob.azure.container_registry.models;

import java.util.List;

public record BulkDigestsDto(String repository, List<String> tags) {
}

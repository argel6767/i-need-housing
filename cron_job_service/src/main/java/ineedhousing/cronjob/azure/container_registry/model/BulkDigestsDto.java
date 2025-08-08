package ineedhousing.cronjob.azure.container_registry.model;

import java.util.List;

public record BulkDigestsDto(String repository, List<String> tags) {
}

package ineedhousing.cronjob.gcp.models;

import java.time.LocalDateTime;

public record ArtifactRegistryPackage(String name, LocalDateTime createdTime, LocalDateTime updatedTime) {

}

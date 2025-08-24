package ineedhousing.cronjob.gcp.models;

import java.util.List;

public record GetImagesDto(List<ArtifactRegistryPackage> packages) {
}

package ineedhousing.cronjob.gcp.models;

import java.time.LocalDateTime;
import java.util.List;

public record ImageVersionDto(String name, LocalDateTime createTime, LocalDateTime updateTime, VersionMetaData metaData) {
    record VersionMetaData(String name, String imageByteSize, String mediaType, LocalDateTime buildTime) {}

    public record ImageVersions(List<ImageVersionDto> versions){}
}

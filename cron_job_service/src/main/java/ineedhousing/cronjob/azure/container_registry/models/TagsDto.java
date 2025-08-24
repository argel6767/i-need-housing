package ineedhousing.cronjob.azure.container_registry.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TagsDto(@JsonProperty("name") String name, @JsonProperty("tags") List<String> tags) {
}

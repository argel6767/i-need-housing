package ineedhousing.cronjob.azure.container_registry.models;

import java.util.List;

public record DigestDto(Integer schemaVersion, String mediaType, Layer config, List<Layer> layers) {

    public static CompleteManifestInfoDto createEmptyDigest() {
        return new CompleteManifestInfoDto(null, new DigestDto(-1, null, null, null));
    }

    public static String getDigestValue(CompleteManifestInfoDto digestDto) {
        return digestDto.manifest.manifest();
    }

    public static CompleteManifestInfoDto getCompleteManifestDto(DigestDto digestDto, String tag, String manifest) {
        return new CompleteManifestInfoDto(new TagManifest(tag, manifest), digestDto);
    }

     public record CompleteManifestInfoDto(TagManifest manifest, DigestDto digestDto) {}
}


record Layer(String mediaType, Integer size, String digest) {}

record TagManifest(String tag, String manifest){}





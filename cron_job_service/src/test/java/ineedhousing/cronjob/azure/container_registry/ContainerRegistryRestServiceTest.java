package ineedhousing.cronjob.azure.container_registry;

import com.fasterxml.jackson.core.JsonProcessingException;
import ineedhousing.cronjob.azure.container_registry.model.DigestDto;
import ineedhousing.cronjob.azure.container_registry.model.ManifestsDeletedDto;
import ineedhousing.cronjob.azure.container_registry.model.TagsDto;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ContainerRegistryRestServiceTest {

    @Inject
    ContainerRegistryRestService service;



    private Response mockResponse;

    private final String TEST_REPOSITORY = "test-repo";
    private final String TEST_TAG = "v123456789";
    private final String TEST_DIGEST = "sha256:abcd1234";
    private final String AUTH_HEADER = "Basic dGVzdDp0ZXN0";

    @BeforeEach
    void setUp() {
        mockResponse = mock(Response.class);


    }

    @Test
    void testGetRepositories() {
        try(MockedStatic<ContainerRegistryAuthHeaderUtil> mockedStatic = mockStatic(ContainerRegistryAuthHeaderUtil.class)) {
            ContainerRegisterRestClient restClient = mock(ContainerRegisterRestClient.class);
            when(ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(anyString(), anyString())).thenReturn(AUTH_HEADER);

            // Given
            String expectedRepos = """
                    {
                        "repositories": [
                            "repo1",
                            "repo2"
                        ]
                    }
                    """;
            when(restClient.listRepositories(AUTH_HEADER)).thenReturn(expectedRepos);

            // When
            String result = service.getRepositories();

            // Then
            assertEquals(expectedRepos, result);
            verify(restClient).listRepositories(AUTH_HEADER);
        }
    }

    @Test
    void testListTags() throws JsonProcessingException {
        try(MockedStatic<ContainerRegistryAuthHeaderUtil> mockedStatic = mockStatic(ContainerRegistryAuthHeaderUtil.class)) {
            ContainerRegisterRestClient restClient = mock(ContainerRegisterRestClient.class);
            when(ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(anyString(), anyString())).thenReturn(AUTH_HEADER);

            // Given
            String tagsJson = """
                    {
                        "name": "test-repo",
                        "tags": [
                            "v1",
                            "v2",
                            "latest"
                        ]
                    }
                    """;

            when(restClient.listTags(TEST_REPOSITORY, AUTH_HEADER)).thenReturn(tagsJson);

            // When
            TagsDto result = service.listTags(TEST_REPOSITORY);

            // Then
            assertEquals("test-repo", result.name());
            assertEquals(3, result.tags().size());
            assertTrue(result.tags().contains("v1"));
            assertTrue(result.tags().contains("v2"));
            assertTrue(result.tags().contains("latest"));
            verify(restClient).listTags(TEST_REPOSITORY, AUTH_HEADER);
        }
    }

    @Test
    void testGetManifestByTag_Success() throws JsonProcessingException {
        try(MockedStatic<ContainerRegistryAuthHeaderUtil> mockedStatic = mockStatic(ContainerRegistryAuthHeaderUtil.class)) {
            ContainerRegisterRestClient restClient = mock(ContainerRegisterRestClient.class);
            when(ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(anyString(), anyString())).thenReturn(AUTH_HEADER);

            // Given
            String manifestJson = """
                    {
                        "schemaVersion": 2,
                        "mediaType": "application/vnd.docker.distribution.manifest.v2+json"
                    }
                    """;

            when(restClient.getManifestWithHeaders(eq(TEST_REPOSITORY), eq(TEST_TAG),
                    eq(AUTH_HEADER), anyString())).thenReturn(mockResponse);
            when(mockResponse.getHeaderString("Docker-Content-Digest")).thenReturn(TEST_DIGEST);
            when(mockResponse.readEntity(String.class)).thenReturn(manifestJson);

            // When
            DigestDto.CompleteManifestInfoDto result = service.getManifestByTag(TEST_REPOSITORY, TEST_TAG);

            // Then
            assertNotNull(result);
            assertEquals(2, result.digestDto().schemaVersion());
            verify(restClient).getManifestWithHeaders(eq(TEST_REPOSITORY), eq(TEST_TAG),
                    eq(AUTH_HEADER), anyString());
        }
    }

    @Test
    void testGetManifestByTag_ManifestNotFound() {
        try(MockedStatic<ContainerRegistryAuthHeaderUtil> mockedStatic = mockStatic(ContainerRegistryAuthHeaderUtil.class)) {
            ContainerRegisterRestClient restClient = mock(ContainerRegisterRestClient.class);
            when(ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(anyString(), anyString())).thenReturn(AUTH_HEADER);

            // Given
            when(restClient.getManifestWithHeaders(eq(TEST_REPOSITORY), eq(TEST_TAG),
                    eq(AUTH_HEADER), anyString())).thenReturn(mockResponse);
            when(mockResponse.getHeaderString("Docker-Content-Digest")).thenReturn(null);

            // When & Then
            assertThrows(NotFoundException.class, () ->
                    service.getManifestByTag(TEST_REPOSITORY, TEST_TAG));
        }
    }

    @Test
    void testGetManifestsByTags_Success() throws JsonProcessingException {
        try(MockedStatic<ContainerRegistryAuthHeaderUtil> mockedStatic = mockStatic(ContainerRegistryAuthHeaderUtil.class)) {
            ContainerRegisterRestClient restClient = mock(ContainerRegisterRestClient.class);
            when(ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(anyString(), anyString())).thenReturn(AUTH_HEADER);

            // Given
            List<String> tags = Arrays.asList("v1", "v2", "v3");
            String manifestJson = """
                    {
                        "schemaVersion": 2,
                        "mediaType": "application/vnd.docker.distribution.manifest.v2+json"
                    }
                    """;

            when(restClient.getManifestWithHeaders(eq(TEST_REPOSITORY), anyString(),
                    eq(AUTH_HEADER), anyString())).thenReturn(mockResponse);
            when(mockResponse.getHeaderString("Docker-Content-Digest")).thenReturn(TEST_DIGEST);
            when(mockResponse.readEntity(String.class)).thenReturn(manifestJson);

            // When
            List<DigestDto.CompleteManifestInfoDto> results = service.getManifestsByTags(TEST_REPOSITORY, tags);

            // Then
            assertEquals(3, results.size());
            results.forEach(result -> {
                assertNotNull(result);
            });
            verify(restClient, times(3)).getManifestWithHeaders(eq(TEST_REPOSITORY),
                    anyString(), eq(AUTH_HEADER), anyString());
        }
    }

    @Test
    void testGetManifestsByTags_WithFailures() {
        try(MockedStatic<ContainerRegistryAuthHeaderUtil> mockedStatic = mockStatic(ContainerRegistryAuthHeaderUtil.class)) {
            ContainerRegisterRestClient restClient = mock(ContainerRegisterRestClient.class);
            when(ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(anyString(), anyString())).thenReturn(AUTH_HEADER);

            // Given
            List<String> tags = Arrays.asList("v1", "v2", "v3");
            String manifestJson = """
                    {
                        "schemaVersion": 2,
                        "mediaType": "application/vnd.docker.distribution.manifest.v2+json"
                    }
                    """;

            // Mock first call to fail
            when(restClient.getManifestWithHeaders(eq(TEST_REPOSITORY), eq("v1"),
                    eq(AUTH_HEADER), anyString())).thenThrow(new RuntimeException("Network error"));

            // Mock other calls to succeed
            when(restClient.getManifestWithHeaders(eq(TEST_REPOSITORY), eq("v2"),
                    eq(AUTH_HEADER), anyString())).thenReturn(mockResponse);
            when(restClient.getManifestWithHeaders(eq(TEST_REPOSITORY), eq("v3"),
                    eq(AUTH_HEADER), anyString())).thenReturn(mockResponse);

            when(mockResponse.getHeaderString("Docker-Content-Digest")).thenReturn(TEST_DIGEST);
            when(mockResponse.readEntity(String.class)).thenReturn(manifestJson);

            // When
            List<DigestDto.CompleteManifestInfoDto> results = service.getManifestsByTags(TEST_REPOSITORY, tags);

            // Then
            assertEquals(3, results.size());
            // One should be empty digest due to failure
            long emptyDigests = results.stream()
                    .filter(result -> result.digestDto().schemaVersion().equals(-1))
                    .count();
            assertEquals(1, emptyDigests);
        }
    }

    @Test
    void testDeleteManifest() {
        try(MockedStatic<ContainerRegistryAuthHeaderUtil> mockedStatic = mockStatic(ContainerRegistryAuthHeaderUtil.class)) {
            ContainerRegisterRestClient restClient = mock(ContainerRegisterRestClient.class);
            when(ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(anyString(), anyString())).thenReturn(AUTH_HEADER);

            // Given
            doNothing().when(restClient).deleteManifest(TEST_REPOSITORY, TEST_DIGEST, AUTH_HEADER);

            // When
            service.deleteManifest(TEST_REPOSITORY, TEST_DIGEST);

            // Then
            verify(restClient).deleteManifest(TEST_REPOSITORY, TEST_DIGEST, AUTH_HEADER);
        }
    }

    @Test
    void testDeleteOldImages_NotEnoughImages() throws JsonProcessingException {
        try(MockedStatic<ContainerRegistryAuthHeaderUtil> mockedStatic = mockStatic(ContainerRegistryAuthHeaderUtil.class)) {
            ContainerRegisterRestClient restClient = mock(ContainerRegisterRestClient.class);
            when(ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(anyString(), anyString())).thenReturn(AUTH_HEADER);

            // Given - only 3 tags (including latest), should not delete any
            String tagsJson = """
                    {
                        "name": "test-repo",
                        "tags": [
                            "v1",
                            "v2",
                            "latest"
                        ]
                    }
                    """;

            when(restClient.listTags(TEST_REPOSITORY, AUTH_HEADER)).thenReturn(tagsJson);

            // When
            ManifestsDeletedDto result = service.deleteOldImages(TEST_REPOSITORY);

            // Then
            assertTrue(result.manifestsDeleted().isEmpty());
            assertTrue(result.manifestsFailedToDelete().isEmpty());
            verify(restClient).listTags(TEST_REPOSITORY, AUTH_HEADER);
            verify(restClient, never()).deleteManifest(anyString(), anyString(), anyString());
        }
    }

    @Test
    void testDeleteOldImages_Success() throws JsonProcessingException {
        try(MockedStatic<ContainerRegistryAuthHeaderUtil> mockedStatic = mockStatic(ContainerRegistryAuthHeaderUtil.class)) {
            ContainerRegisterRestClient restClient = mock(ContainerRegisterRestClient.class);
            when(ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(anyString(), anyString())).thenReturn(AUTH_HEADER);

            // Given - 6 tags total, should delete 3 oldest (keeping 3 newest + latest)
            String tagsJson = """
                    {
                        "name": "test-repo",
                        "tags": [
                            "v1000000000",
                            "v2000000000",
                            "v3000000000",
                            "v4000000000",
                            "v5000000000",
                            "latest"
                        ]
                    }
                    """;
            String manifestJson = """
                    {
                        "schemaVersion": 2,
                        "mediaType": "application/vnd.docker.distribution.manifest.v2+json"
                    }
                    """;

            when(restClient.listTags(TEST_REPOSITORY, AUTH_HEADER)).thenReturn(tagsJson);
            when(restClient.getManifestWithHeaders(eq(TEST_REPOSITORY), anyString(),
                    eq(AUTH_HEADER), anyString())).thenReturn(mockResponse);
            when(mockResponse.getHeaderString("Docker-Content-Digest")).thenReturn(TEST_DIGEST);
            when(mockResponse.readEntity(String.class)).thenReturn(manifestJson);
            doNothing().when(restClient).deleteManifest(anyString(), anyString(), anyString());

            // When
            ManifestsDeletedDto result = service.deleteOldImages(TEST_REPOSITORY);

            // Then
            assertEquals(3, result.manifestsDeleted().size()); // Should delete 3 oldest
            assertTrue(result.manifestsFailedToDelete().isEmpty());
            verify(restClient, times(3)).deleteManifest(eq(TEST_REPOSITORY), eq(TEST_DIGEST), eq(AUTH_HEADER));
        }
    }

    @Test
    void testDeleteOldImages_WithDeletionFailures() throws JsonProcessingException {
        try(MockedStatic<ContainerRegistryAuthHeaderUtil> mockedStatic = mockStatic(ContainerRegistryAuthHeaderUtil.class)) {
            ContainerRegisterRestClient restClient = mock(ContainerRegisterRestClient.class);
            when(ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(anyString(), anyString())).thenReturn(AUTH_HEADER);

            // Given
            String tagsJson = """
                    {
                        "name": "test-repo",
                        "tags": [
                            "v1000000000",
                            "v2000000000",
                            "v3000000000",
                            "v4000000000",
                            "v5000000000",
                            "latest"
                        ]
                    }
                    """;
            String manifestJson = """
                    {
                        "schemaVersion": 2,
                        "mediaType": "application/vnd.docker.distribution.manifest.v2+json"
                    }
                    """;

            when(restClient.listTags(TEST_REPOSITORY, AUTH_HEADER)).thenReturn(tagsJson);
            when(restClient.getManifestWithHeaders(eq(TEST_REPOSITORY), anyString(),
                    eq(AUTH_HEADER), anyString())).thenReturn(mockResponse);
            when(mockResponse.getHeaderString("Docker-Content-Digest")).thenReturn(TEST_DIGEST);
            when(mockResponse.readEntity(String.class)).thenReturn(manifestJson);

            // Mock deletion failures
            doThrow(new RuntimeException("Delete failed"))
                    .when(restClient).deleteManifest(eq(TEST_REPOSITORY), eq(TEST_DIGEST), eq(AUTH_HEADER));

            // When
            ManifestsDeletedDto result = service.deleteOldImages(TEST_REPOSITORY);

            // Then
            assertTrue(result.manifestsDeleted().isEmpty());
            assertEquals(3, result.manifestsFailedToDelete().size());
        }
    }

    @Test
    void testDeleteOldImages_WithManifestFetchFailures() throws JsonProcessingException {
        try(MockedStatic<ContainerRegistryAuthHeaderUtil> mockedStatic = mockStatic(ContainerRegistryAuthHeaderUtil.class)) {
            ContainerRegisterRestClient restClient = mock(ContainerRegisterRestClient.class);
            when(ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(anyString(), anyString())).thenReturn(AUTH_HEADER);

            // Given
            String tagsJson = """
                    {
                        "name": "test-repo",
                        "tags": [
                            "v1000000000",
                            "v2000000000",
                            "v3000000000",
                            "v4000000000",
                            "v5000000000",
                            "latest"
                        ]
                    }
                    """;

            when(restClient.listTags(TEST_REPOSITORY, AUTH_HEADER)).thenReturn(tagsJson);
            // Mock all manifest fetch calls to fail
            when(restClient.getManifestWithHeaders(eq(TEST_REPOSITORY), anyString(),
                    eq(AUTH_HEADER), anyString())).thenThrow(new RuntimeException("Fetch failed"));

            // When
            ManifestsDeletedDto result = service.deleteOldImages(TEST_REPOSITORY);

            // Then
            assertTrue(result.manifestsDeleted().isEmpty());
            assertTrue(result.manifestsFailedToDelete().isEmpty());
            verify(restClient, never()).deleteManifest(anyString(), anyString(), anyString());
        }
    }

    @Test
    void testDeleteOldImages_JsonProcessingException() {
        try(MockedStatic<ContainerRegistryAuthHeaderUtil> mockedStatic = mockStatic(ContainerRegistryAuthHeaderUtil.class)) {
            ContainerRegisterRestClient restClient = mock(ContainerRegisterRestClient.class);
            when(ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(anyString(), anyString())).thenReturn(AUTH_HEADER);

            // Given
            String invalidJson = """
                    {
                        "invalid": "json"
                        "missing": "comma"
                    }
                    """;
            when(restClient.listTags(TEST_REPOSITORY, AUTH_HEADER)).thenReturn(invalidJson);

            // When & Then
            assertThrows(JsonProcessingException.class, () ->
                    service.deleteOldImages(TEST_REPOSITORY));
        }
    }

    @Test
    void testDeleteOldImages_EdgeCaseExactly4Tags() throws JsonProcessingException {
        try(MockedStatic<ContainerRegistryAuthHeaderUtil> mockedStatic = mockStatic(ContainerRegistryAuthHeaderUtil.class)) {
            ContainerRegisterRestClient restClient = mock(ContainerRegisterRestClient.class);
            when(ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(anyString(), anyString())).thenReturn(AUTH_HEADER);

            // Given - exactly 4 tags (3 + latest), should not delete any
            String tagsJson = """
                    {
                        "name": "test-repo",
                        "tags": [
                            "v1",
                            "v2",
                            "v3",
                            "latest"
                        ]
                    }
                    """;

            when(restClient.listTags(TEST_REPOSITORY, AUTH_HEADER)).thenReturn(tagsJson);

            // When
            ManifestsDeletedDto result = service.deleteOldImages(TEST_REPOSITORY);

            // Then
            assertTrue(result.manifestsDeleted().isEmpty());
            assertTrue(result.manifestsFailedToDelete().isEmpty());
            verify(restClient, never()).getManifestWithHeaders(anyString(), anyString(), anyString(), anyString());
        }
    }
}
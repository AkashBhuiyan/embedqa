package com.akash.embedqa.service;

import com.akash.embedqa.enums.HttpMethod;
import com.akash.embedqa.exception.ResourceNotFoundException;
import com.akash.embedqa.model.dtos.response.HistoryResponseDTO;
import com.akash.embedqa.model.entities.ApiCollection;
import com.akash.embedqa.model.entities.ApiRequest;
import com.akash.embedqa.model.entities.RequestHistory;
import com.akash.embedqa.repository.RequestHistoryRepository;
import com.akash.embedqa.service.impl.HistoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Author: akash
 * Date: 11/1/26
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("HistoryService Unit Tests")
class HistoryServiceImplTest {

    @Mock
    private RequestHistoryRepository historyRepository;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private HistoryServiceImpl historyService;

    @Captor
    private ArgumentCaptor<RequestHistory> historyCaptor;

    @Captor
    private ArgumentCaptor<LocalDateTime> dateCaptor;

    private RequestHistory testHistory;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        testHistory = RequestHistory.builder()
                .id(1L)
                .url("https://api.example.com/users")
                .method(HttpMethod.GET)
                .requestHeaders("{\"Authorization\":\"Bearer token\"}")
                .queryParams("{\"page\":\"1\"}")
                .requestBody(null)
                .bodyType("NONE")
                .authType("BEARER_TOKEN")
                .authConfig("{\"token\":\"bearer-token\"}")
                .statusCode(200)
                .statusText("OK")
                .responseHeaders("{\"Content-Type\":\"application/json\"}")
                .responseBody("{\"users\":[]}")
                .responseTime(150L)
                .responseSize(100L)
                .executedAt(now)
                .build();
    }

    @Nested
    @DisplayName("saveHistory() Tests")
    class SaveHistoryTests {

        @Test
        @DisplayName("Should save history entry")
        void saveHistory_SavesEntry() {
            // Arrange
            when(historyRepository.save(any(RequestHistory.class))).thenReturn(testHistory);

            // Act
            RequestHistory result = historyService.saveHistory(testHistory);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getUrl()).isEqualTo("https://api.example.com/users");
            verify(historyRepository).save(testHistory);
        }

        @Test
        @DisplayName("Should save history with all fields")
        void saveHistory_SavesAllFields() {
            // Arrange
            when(historyRepository.save(any(RequestHistory.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            historyService.saveHistory(testHistory);

            // Assert
            verify(historyRepository).save(historyCaptor.capture());
            RequestHistory captured = historyCaptor.getValue();
            assertThat(captured.getMethod()).isEqualTo(HttpMethod.GET);
            assertThat(captured.getStatusCode()).isEqualTo(200);
            assertThat(captured.getResponseTime()).isEqualTo(150L);
        }
    }

    @Nested
    @DisplayName("getHistory() Tests")
    class GetHistoryTests {

        @Test
        @DisplayName("Should return paginated history")
        void getHistory_ReturnsPaginatedHistory() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            List<RequestHistory> histories = List.of(testHistory);
            Page<RequestHistory> historyPage = new PageImpl<>(histories, pageable, 1);

            when(historyRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(historyPage);

            // Act
            Page<HistoryResponseDTO> result = historyService.getHistory(
                    null, null, null, null, null, pageable
            );

            // Assert
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).getUrl()).isEqualTo("https://api.example.com/users");
        }

        @Test
        @DisplayName("Should filter by HTTP method")
        void getHistory_WithMethodFilter_FiltersResults() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Page<RequestHistory> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

            when(historyRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(emptyPage);

            // Act
            Page<HistoryResponseDTO> result = historyService.getHistory(
                    HttpMethod.POST, null, null, null, null, pageable
            );

            // Assert
            verify(historyRepository).findAll(any(Specification.class), eq(pageable));
        }

        @Test
        @DisplayName("Should filter by status code 200 (success range)")
        void getHistory_WithStatusCode200_FiltersSuccessRange() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Page<RequestHistory> page = new PageImpl<>(List.of(testHistory), pageable, 1);

            when(historyRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(page);

            // Act
            Page<HistoryResponseDTO> result = historyService.getHistory(
                    null, 200, null, null, null, pageable
            );

            // Assert
            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        @DisplayName("Should filter by status code 400+ (error range)")
        void getHistory_WithStatusCode400_FiltersErrorRange() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Page<RequestHistory> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

            when(historyRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(emptyPage);

            // Act
            Page<HistoryResponseDTO> result = historyService.getHistory(
                    null, 400, null, null, null, pageable
            );

            // Assert
            verify(historyRepository).findAll(any(Specification.class), eq(pageable));
        }

        @Test
        @DisplayName("Should filter by search term in URL")
        void getHistory_WithSearchFilter_FiltersResults() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Page<RequestHistory> page = new PageImpl<>(List.of(testHistory), pageable, 1);

            when(historyRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(page);

            // Act
            Page<HistoryResponseDTO> result = historyService.getHistory(
                    null, null, "users", null, null, pageable
            );

            // Assert
            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        @DisplayName("Should filter by date range")
        void getHistory_WithDateRange_FiltersResults() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            LocalDateTime fromDate = now.minusDays(7);
            LocalDateTime toDate = now;
            Page<RequestHistory> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

            when(historyRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(emptyPage);

            // Act
            Page<HistoryResponseDTO> result = historyService.getHistory(
                    null, null, null, fromDate, toDate, pageable
            );

            // Assert
            verify(historyRepository).findAll(any(Specification.class), eq(pageable));
        }

        @Test
        @DisplayName("Should return empty page when no history exists")
        void getHistory_WhenEmpty_ReturnsEmptyPage() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Page<RequestHistory> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

            when(historyRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(emptyPage);

            // Act
            Page<HistoryResponseDTO> result = historyService.getHistory(
                    null, null, null, null, null, pageable
            );

            // Assert
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isZero();
        }

        @Test
        @DisplayName("Should apply all filters together")
        void getHistory_WithAllFilters_AppliesAllFilters() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            LocalDateTime fromDate = now.minusDays(7);
            LocalDateTime toDate = now;
            Page<RequestHistory> page = new PageImpl<>(List.of(testHistory), pageable, 1);

            when(historyRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(page);

            // Act
            Page<HistoryResponseDTO> result = historyService.getHistory(
                    HttpMethod.GET, 200, "users", fromDate, toDate, pageable
            );

            // Assert
            assertThat(result.getContent()).hasSize(1);
            verify(historyRepository).findAll(any(Specification.class), eq(pageable));
        }
    }

    @Nested
    @DisplayName("getById() Tests")
    class GetByIdTests {

        @Test
        @DisplayName("Should return history entry when found")
        void getById_WhenExists_ReturnsHistory() {
            // Arrange
            when(historyRepository.findById(1L)).thenReturn(Optional.of(testHistory));

            // Act
            HistoryResponseDTO result = historyService.getById(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getUrl()).isEqualTo("https://api.example.com/users");
            assertThat(result.getMethod()).isEqualTo(HttpMethod.GET);
            assertThat(result.getStatusCode()).isEqualTo(200);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when not found")
        void getById_WhenNotExists_ThrowsException() {
            // Arrange
            when(historyRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> historyService.getById(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("History entry")
                    .hasMessageContaining("999");
        }

        @Test
        @DisplayName("Should return history with request and response details")
        void getById_ReturnsWithDetails() {
            // Arrange
            when(historyRepository.findById(1L)).thenReturn(Optional.of(testHistory));

            // Act
            HistoryResponseDTO result = historyService.getById(1L);

            // Assert
            assertThat(result.getRequest()).isNotNull();
            assertThat(result.getRequest().getUrl()).isEqualTo("https://api.example.com/users");
            assertThat(result.getRequest().getMethod()).isEqualTo(HttpMethod.GET);
            assertThat(result.getRequest().getBodyType()).isEqualTo("NONE");
            assertThat(result.getRequest().getAuthType()).isEqualTo("BEARER_TOKEN");

            assertThat(result.getResponse()).isNotNull();
            assertThat(result.getResponse().getStatusCode()).isEqualTo(200);
            assertThat(result.getResponse().getStatusText()).isEqualTo("OK");
            assertThat(result.getResponse().getBody()).isEqualTo("{\"users\":[]}");
            assertThat(result.getResponse().getResponseTime()).isEqualTo(150L);
            assertThat(result.getResponse().getResponseSize()).isEqualTo(100L);
        }

        @Test
        @DisplayName("Should parse headers JSON to map")
        void getById_ParsesHeadersToMap() {
            // Arrange
            when(historyRepository.findById(1L)).thenReturn(Optional.of(testHistory));

            // Act
            HistoryResponseDTO result = historyService.getById(1L);

            // Assert
            assertThat(result.getRequest().getHeaders()).isNotNull();
            assertThat(result.getRequest().getHeaders()).containsKey("Authorization");
            assertThat(result.getRequest().getHeaders().get("Authorization")).isEqualTo("Bearer token");
        }

        @Test
        @DisplayName("Should parse query params JSON to map")
        void getById_ParsesQueryParamsToMap() {
            // Arrange
            when(historyRepository.findById(1L)).thenReturn(Optional.of(testHistory));

            // Act
            HistoryResponseDTO result = historyService.getById(1L);

            // Assert
            assertThat(result.getRequest().getQueryParams()).isNotNull();
            assertThat(result.getRequest().getQueryParams()).containsKey("page");
            assertThat(result.getRequest().getQueryParams().get("page")).isEqualTo("1");
        }

        @Test
        @DisplayName("Should handle null JSON fields gracefully")
        void getById_WithNullJson_ReturnsEmptyMaps() {
            // Arrange
            testHistory.setRequestHeaders(null);
            testHistory.setQueryParams(null);
            testHistory.setResponseHeaders(null);
            testHistory.setAuthConfig(null);
            when(historyRepository.findById(1L)).thenReturn(Optional.of(testHistory));

            // Act
            HistoryResponseDTO result = historyService.getById(1L);

            // Assert
            assertThat(result.getRequest().getHeaders()).isEmpty();
            assertThat(result.getRequest().getQueryParams()).isEmpty();
            assertThat(result.getRequest().getAuthConfig()).isEmpty();
            assertThat(result.getResponse().getHeaders()).isEmpty();
        }

        @Test
        @DisplayName("Should get request name from associated ApiRequest")
        void getById_WithApiRequest_ReturnsRequestName() {
            // Arrange
            ApiCollection collection = ApiCollection.builder()
                    .id(1L)
                    .name("Test Collection")
                    .build();

            ApiRequest apiRequest = ApiRequest.builder()
                    .id(1L)
                    .name("Get Users")
                    .collection(collection)
                    .build();

            testHistory.setApiRequest(apiRequest);
            when(historyRepository.findById(1L)).thenReturn(Optional.of(testHistory));

            // Act
            HistoryResponseDTO result = historyService.getById(1L);

            // Assert
            assertThat(result.getRequestName()).isEqualTo("Get Users");
            assertThat(result.getCollectionName()).isEqualTo("Test Collection");
        }

        @Test
        @DisplayName("Should get collection name from history's direct collection")
        void getById_WithDirectCollection_ReturnsCollectionName() {
            // Arrange
            ApiCollection collection = ApiCollection.builder()
                    .id(1L)
                    .name("Direct Collection")
                    .build();
            testHistory.setCollection(collection);
            when(historyRepository.findById(1L)).thenReturn(Optional.of(testHistory));

            // Act
            HistoryResponseDTO result = historyService.getById(1L);

            // Assert
            assertThat(result.getCollectionName()).isEqualTo("Direct Collection");
        }
    }

    private RequestHistory createHistory(Long id, HttpMethod method, int statusCode, Long responseTime) {
        return RequestHistory.builder()
                .id(id)
                .url("https://api.example.com/test")
                .method(method)
                .statusCode(statusCode)
                .responseTime(responseTime)
                .executedAt(now)
                .build();
    }
}


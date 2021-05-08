package per.jaceding.demo.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import per.jaceding.demo.document.SearchLogDocument;
import per.jaceding.demo.document.UserDocument;
import per.jaceding.demo.repository.SearchLogRepository;
import per.jaceding.demo.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 测试分页性能
 *
 * @author jaceding
 * @date 2021/4/29
 */
@Slf4j
@Service
public class PageService implements ApplicationRunner {

    final UserRepository userRepository;

    final SearchLogRepository searchLogRepository;

    final ElasticsearchRestTemplate elasticsearchRestTemplate;

    final RestHighLevelClient restHighLevelClient;

    public PageService(UserRepository userRepository, SearchLogRepository searchLogRepository, ElasticsearchRestTemplate elasticsearchRestTemplate, RestHighLevelClient restHighLevelClient) {
        this.userRepository = userRepository;
        this.searchLogRepository = searchLogRepository;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                poolSize,
                poolSize,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        warmUp();
        Long index = 30L;
        executor.execute(() -> doFromSize(index));
        executor.execute(() -> doScroll(index));
        executor.execute(() -> {
            try {
                doSearchAfter(index);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void warmUp() {
        log.info("预热开始");
        Optional<UserDocument> userDocumentOptional = userRepository.findById(1L);
        userDocumentOptional.ifPresent(userDocument -> log.info("userDocument:{}", userDocument.toString()));
        log.info("预热结束");
    }

    private void doFromSize(Long index) {
        for (int i = 0; i < 10; i++) {
            List<SearchLogDocument> list = new ArrayList<>();
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            Pageable pageable;
            Page<UserDocument> userPage;
            int page = 0;
            while (true) {
                pageable = PageRequest.of(page, 10, sort);
                long startTime = System.currentTimeMillis();
                userPage = userRepository.findAll(pageable);
                long duration = System.currentTimeMillis() - startTime;
                log.info("page={}, time={}", page, duration);
                if (userPage.isEmpty()) {
                    break;
                }
                userPage.getContent().forEach(e -> log.info("e->{}", e.getId()));
                list.add(SearchLogDocument
                        .builder()
                        .id(UUID.fastUUID().toString())
                        .type("fromSize")
                        .page((long) page)
                        .page10((long) (page / 10))
                        .page50((long) (page / 50))
                        .page100((long) (page / 100))
                        .page500((long) (page / 500))
                        .page1000((long) (page / 1000))
                        .index(index + i)
                        .duration(duration)
                        .createTime(LocalDateTime.now())
                        .build());
                if (list.size() >= 1000) {
                    searchLogRepository.saveAll(list);
                    list = new ArrayList<>();
                }
                page++;
            }
            searchLogRepository.saveAll(list);
        }
    }

    private void doScroll(Long index) {
        for (int i = 0; i < 10; i++) {
            List<SearchLogDocument> list = new ArrayList<>();
            long scrollTimeInMillis = 10000;
            Query query = Query.findAll();
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            query.setPageable(PageRequest.of(0, 10, sort));
            IndexCoordinates indexCoordinates = IndexCoordinates.of("users");
            List<String> scrollIds = new ArrayList<>();
            int page = 0;
            String scrollId = null;
            SearchScrollHits<UserDocument> hits;
            try {
                while (true) {
                    long startTime = System.currentTimeMillis();
                    if (StrUtil.isBlank(scrollId)) {
                        hits = elasticsearchRestTemplate.searchScrollStart(scrollTimeInMillis,
                                query, UserDocument.class, indexCoordinates);
                    } else {
                        hits = elasticsearchRestTemplate.searchScrollContinue(scrollId, scrollTimeInMillis,
                                UserDocument.class, indexCoordinates);
                    }
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("page={}, time={}", page, duration);
                    scrollId = hits.getScrollId();
                    if (!scrollIds.contains(scrollId)) {
                        scrollIds.add(scrollId);
                    }
                    if (hits.getSearchHits().isEmpty()) {
                        break;
                    }
                    hits.getSearchHits().forEach(e -> log.info("e->{}", e.getId()));
                    list.add(SearchLogDocument
                            .builder()
                            .id(UUID.fastUUID().toString())
                            .type("scroll")
                            .page((long) page)
                            .page10((long) (page / 10))
                            .page50((long) (page / 50))
                            .page100((long) (page / 100))
                            .page500((long) (page / 500))
                            .page1000((long) (page / 1000))
                            .index(index + i)
                            .duration(duration)
                            .createTime(LocalDateTime.now())
                            .build());
                    if (list.size() >= 1000) {
                        searchLogRepository.saveAll(list);
                        list = new ArrayList<>();
                    }
                    page++;
                }
            } finally {
                if (!scrollIds.isEmpty()) {
                    log.info("clear scrollIds");
                    elasticsearchRestTemplate.searchScrollClear(scrollIds);
                }
                searchLogRepository.saveAll(list);
            }
        }
    }

    private void doSearchAfter(Long index) throws IOException {
        for (int i = 0; i < 10; i++) {
            List<SearchLogDocument> list = new ArrayList<>();
            int page = 0;
            SearchRequest searchRequest = new SearchRequest("users");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(10);
            searchSourceBuilder.sort("id", SortOrder.ASC);
            searchRequest.source(searchSourceBuilder);
            Object[] objects = new Object[0];
            SearchResponse searchResponse;
            SearchHit[] hits;
            while (true) {
                long startTime = System.currentTimeMillis();
                if (ArrayUtil.isNotEmpty(objects)) {
                    searchSourceBuilder.searchAfter(objects);
                }
                searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                long duration = System.currentTimeMillis() - startTime;
                log.info("page={}, time={}", page, duration);
                hits = searchResponse.getHits().getHits();
                if (ArrayUtil.isEmpty(hits)) {
                    break;
                }
                objects = hits[hits.length - 1].getSortValues();
                for (SearchHit hit : hits) {
                    log.info("e->{}", hit.getId());
                }
                list.add(SearchLogDocument
                        .builder()
                        .id(UUID.fastUUID().toString())
                        .type("search_after")
                        .page((long) page)
                        .page10((long) (page / 10))
                        .page50((long) (page / 50))
                        .page100((long) (page / 100))
                        .page500((long) (page / 500))
                        .page1000((long) (page / 1000))
                        .index(index + i)
                        .duration(duration)
                        .createTime(LocalDateTime.now())
                        .build());
                if (list.size() >= 1000) {
                    searchLogRepository.saveAll(list);
                    list = new ArrayList<>();
                }
                page++;
            }
            searchLogRepository.saveAll(list);
        }
    }
}

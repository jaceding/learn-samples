package per.jaceding.demo.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import per.jaceding.demo.document.SearchLogDocument;

/**
 * 搜索日志持久层
 *
 * @author jaceding
 * @date 2021/4/28
 */
@Repository
public interface SearchLogRepository extends ElasticsearchRepository<SearchLogDocument, Long> {
}

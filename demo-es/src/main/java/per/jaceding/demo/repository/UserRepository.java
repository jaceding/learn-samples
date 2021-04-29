package per.jaceding.demo.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import per.jaceding.demo.document.UserDocument;

/**
 * 用户持久层
 *
 * @author jaceding
 * @date 2021/4/28
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<UserDocument, Long> {
}

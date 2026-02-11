package techthree.binitright.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techthree.binitright.model.News;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findAllByOrderByPublishedDateDesc();

    News findNewsByNewsId(Long newsId);
}
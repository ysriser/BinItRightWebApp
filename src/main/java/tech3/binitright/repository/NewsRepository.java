package tech3.binitright.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech3.binitright.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findAllByOrderByPublishedDateDesc();

    News findNewsByNewsId(Long newsId);
}
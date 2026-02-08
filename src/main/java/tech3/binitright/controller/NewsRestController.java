package tech3.binitright.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.interfacemethods.NewsInterface;
import tech3.binitright.model.News;
import tech3.binitright.service.NewsImplementation;

@RestController
@RequestMapping("/api/news")
public final class NewsRestController {

    @Autowired
    private NewsInterface newsService;

    @Autowired
    public void setNewsService(final NewsImplementation newsImplementation) {
        this.newsService = newsImplementation;
    }

    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        final List<News> newsList = newsService.findAllByOrderByPublishedDateDesc();
        return ResponseEntity.ok(newsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable final Long id) {
        return ResponseEntity.ok(newsService.getNewsByNewsId(id));
    }
}
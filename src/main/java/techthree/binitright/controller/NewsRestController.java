package techthree.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techthree.binitright.model.News;
import techthree.binitright.interfacemethods.NewsInterface;
import techthree.binitright.service.NewsImplementation;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsRestController {

    @Autowired
    private NewsInterface newsService;

    @Autowired
    public void setNewsService(NewsImplementation newsImplementation) {
        this.newsService = newsImplementation;
    }

    @GetMapping
    public ResponseEntity<List<News> > getAllNews() {
        List<News> newsList = newsService.findAllByOrderByPublishedDateDesc();
        return ResponseEntity.ok(newsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsByNewsId(id));
    }
}
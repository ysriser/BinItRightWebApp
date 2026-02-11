package techthree.binitright.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techthree.binitright.model.News;
import techthree.binitright.repository.NewsRepository;
import techthree.binitright.interfacemethods.NewsInterface;

import java.util.List;

@Service
public class NewsImplementation implements NewsInterface {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public List<News> findAllByOrderByPublishedDateDesc() {
        return newsRepository.findAllByOrderByPublishedDateDesc();
    }

    @Override
    public News getNewsByNewsId(Long id) {
        return newsRepository.findNewsByNewsId(id);
    }

}
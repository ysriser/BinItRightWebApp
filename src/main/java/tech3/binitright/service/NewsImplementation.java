package tech3.binitright.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech3.binitright.model.News;
import tech3.binitright.repository.NewsRepository;
import tech3.binitright.interfacemethods.NewsInterface;

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
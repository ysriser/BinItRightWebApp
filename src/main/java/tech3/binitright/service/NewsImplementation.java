package tech3.binitright.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech3.binitright.interfacemethods.NewsInterface;
import tech3.binitright.model.News;
import tech3.binitright.repository.NewsRepository;

@Service
public final class NewsImplementation implements NewsInterface {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public List<News> findAllByOrderByPublishedDateDesc() {
        return newsRepository.findAllByOrderByPublishedDateDesc();
    }

    @Override
    public News getNewsByNewsId(final Long id) {
        return newsRepository.findNewsByNewsId(id);
    }




}
package tech3.binitright.interfacemethods;

import java.util.List;

import tech3.binitright.model.News;

;

public interface NewsInterface {

    News getNewsByNewsId(Long id);

    List<News> findAllByOrderByPublishedDateDesc();
}

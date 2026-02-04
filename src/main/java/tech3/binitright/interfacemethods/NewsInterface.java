package tech3.binitright.interfacemethods;

import tech3.binitright.model.News;

import java.util.List;

;

public interface NewsInterface {

    News getNewsByNewsId(Long id);

    List<News> findAllByOrderByPublishedDateDesc();
}

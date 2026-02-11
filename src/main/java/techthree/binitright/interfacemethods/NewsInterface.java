package techthree.binitright.interfacemethods;

import techthree.binitright.model.News;

import java.util.List;

;

public interface NewsInterface {

    News getNewsByNewsId(Long id);

    List<News> findAllByOrderByPublishedDateDesc();
}

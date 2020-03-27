package life.wmarek.image.processing.scheduled;

import life.wmarek.image.processing.service.implementation.ImageServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class SearchImages {
    private final ImageServiceImpl imageService;

    public SearchImages(ImageServiceImpl imageService) {
        this.imageService = imageService;
    }

    @Scheduled(fixedDelay = 10_000)
    public void search(){
        imageService.run();
    }
}

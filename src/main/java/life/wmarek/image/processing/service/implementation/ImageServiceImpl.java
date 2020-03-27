package life.wmarek.image.processing.service.implementation;

import life.wmarek.image.processing.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {


    @Autowired
    ImageScanerServiceFindEndValidateImage findImagesService;


    public void run() {
        List<File> ss = new ArrayList<>();
        ss = findImagesService.getImages();





    }
    private void getImages(){

    }

}

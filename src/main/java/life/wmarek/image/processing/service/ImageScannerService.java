package life.wmarek.image.processing.service;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ImageScannerService  {


    File unZip(File zipFile) throws ZipException;

    List<File> searchAcceptedFiles(File directoryToSearch);

    boolean validateImage(File file) ;

    boolean validateArchive(File file);

    File getImageInfo() throws IOException;

    List<File> getImages();


}



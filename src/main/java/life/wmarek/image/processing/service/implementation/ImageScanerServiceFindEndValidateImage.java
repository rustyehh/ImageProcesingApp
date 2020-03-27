package life.wmarek.image.processing.service.implementation;

import life.wmarek.image.processing.config.AppConfig;
import life.wmarek.image.processing.service.ImageScannerService;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


@Slf4j
@Service
public class ImageScanerServiceFindEndValidateImage implements ImageScannerService {

    @Autowired
    AppConfig appConfig;
    @Autowired
    ReportSystemSendMail reportSystemSendMail;

    private List<File> images = new ArrayList<>();
    private List<File> zipObj = new ArrayList<>();

    @Override
    public File unZip(File zipFile) throws ZipException {


        File extractdir = new File(appConfig.getTmp() + zipFile.getName());
        if (extractdir.exists()) {
            log.info("file alredy exist");
        } else {
            ZipFile ss = new ZipFile(zipFile);
            ss.extractAll(extractdir.getPath());
        }
        return extractdir;
    }

    @Override
    public List<File> searchAcceptedFiles(File directoryToSearch) {

        File[] zipDir = directoryToSearch.listFiles(this::validateArchive);

        if (zipDir != null) {
            try {
                for (File i : zipDir) {
                    zipObj.addAll(searchAcceptedFiles(unZip(i)));
                }
            } catch (ZipException e) {
                reportSystemSendMail.crashLog(Arrays.toString(e.getStackTrace()));
                log.error("failed to extract " + e);
            }
        }
        File[] imgDir = directoryToSearch.listFiles(this::validateImage);
        if (imgDir != null) {
            images.addAll(Arrays.asList(imgDir));
        }
        try {
            FileUtils.copyToDirectory(images, new File(appConfig.getPathToImages()));
        } catch (IOException e) {
            reportSystemSendMail.crashLog(Arrays.toString(e.getStackTrace()));
            log.error("error witch coping files " + e);
        }

        return images;
    }

    @Override
    public boolean validateImage(File xX) {
        return (xX.isFile()) && FilenameUtils.isExtension(xX.getName(), appConfig.getAcceptImageExtension());
    }

    @Override
    public boolean validateArchive(File zZ) {
        return zZ.isFile() && FilenameUtils.isExtension(zZ.getName(), appConfig.getAcceptArchiveExtension());
    }

    @Override
    public File getImageInfo() throws IOException {


        File file = new File(appConfig.getPathToImages());
        File[] listofF = file.listFiles();
        for (File f : listofF != null ? listofF : new File[0]) {
            if (f.isFile() && FilenameUtils.isExtension(f.getName(), "txt")) {
                log.info("#######################");
                log.info("falied to copy file: "+f.getName());
                log.info(" #######################");
                FileUtils.deleteQuietly(f);
            } else {
                long size = FileUtils.sizeOf(f) / (1024 * 1024);
                long sizeToDisplay = FileUtils.sizeOf(f);

                if (size <= 19) {

                    ImageInputStream iis = ImageIO.createImageInputStream(f);
                    Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

                    ImageReader reader = readers.next();
                    reader.setInput(iis, true);
                    log.info("######################");
                    log.info("path to file: " + f.getPath());
                    log.info("file name: " + f.getName());
                    log.info("widthXheight: " + reader.getWidth(0) + "x" + reader.getHeight(0));
                    log.info("file size: " + FileUtils.byteCountToDisplaySize(sizeToDisplay));
                    log.info("format: " + reader.getFormatName());

                } else {
                    log.error("image size limit = 20 MB ,reduce size of file");
                }
            }
        }
        return null;
    }

    @Override
    public List<File> getImages() {
        try {
            getImageInfo();
        } catch (IOException e) {
            reportSystemSendMail.crashLog(Arrays.toString(e.getStackTrace()));
            log.error("Cant get image info " + e);
        }
        return searchAcceptedFiles(new File(appConfig.getDirectoryToSearch()));
    }
}



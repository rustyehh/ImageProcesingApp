package life.wmarek.image.processing.service;


public interface ReportSystem {

    boolean sendMail(String message);

    void crashLog(String errorLog);
}

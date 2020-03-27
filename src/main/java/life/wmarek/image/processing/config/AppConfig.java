package life.wmarek.image.processing.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("api")
@Getter
@Setter
public class AppConfig {
    private String tmp;
    private String pathToImages;
    private String directoryToSearch;
    private List<String> acceptImageExtension;
    private List<String> acceptArchiveExtension;
    private String smtpServer;
    private String userName;
    private String password;
    private String emailFrom;
    private String emailTo;
    private String emailTOSnowRoot;
    private String patchToErrorLogs;
}

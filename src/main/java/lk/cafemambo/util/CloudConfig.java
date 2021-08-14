package lk.cafemambo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.google.cloud.storage.StorageOptions;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

@Component
public class CloudConfig {

    @Value("${cloudProjectId}")
    private String projectId;

    @Value("${cloudJsonFile}")
    private String jsonFile;

    public StorageOptions configStorage() {


        try {

            File file = ResourceUtils.getFile("classpath:cafe-mambo.json");

            InputStream in = new FileInputStream(file);

            StorageOptions options = StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(GoogleCredentials.fromStream(
                           in)).build();

            return options;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}

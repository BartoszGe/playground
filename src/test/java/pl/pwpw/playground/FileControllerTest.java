package pl.pwpw.playground;

import ch.qos.logback.core.util.ContentTypeUtil;
import jdk.jfr.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import pl.pwpw.playground.service.FileService;

import java.net.URI;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class FileControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private FileService fileService;

  @Test
  public void shouldSaveUploadedFile() throws Exception {

    String applicationId = "1";

    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.pdf", APPLICATION_PDF.getType(), "example".getBytes());

    this.mvc.perform(multipart("/file/").file(multipartFile).param("application_id", applicationId))
            .andExpect(status().isCreated());

    then(this.fileService).should().save(multipartFile, applicationId);
  }
}

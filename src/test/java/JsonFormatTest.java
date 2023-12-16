import com.fasterxml.jackson.databind.ObjectMapper;
import model.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
public class JsonFormatTest {

    private final ClassLoader classLoader = JsonFormatTest.class.getClassLoader();
    ObjectMapper objectMapper = new ObjectMapper();



    @Test
    @DisplayName("Проверка данных JSON")
    void checkOptionsForFirstQuestion() throws Exception {
        try (InputStream is = classLoader.getResourceAsStream("profile.json")) {
            assert is != null;
            try (InputStreamReader isr = new InputStreamReader(is)) {
                Profile data;
                data = objectMapper.readValue(isr, Profile.class);

                Assertions.assertEquals("0001", data.id);
                Assertions.assertEquals("qa", data.team);
                Assertions.assertEquals("2.8", data.experience);
                Assertions.assertEquals("Helen",
                        data.name   );
                Assertions.assertEquals(List.of(
                        "Regression",
                        "Smoke",
                        "Mobile",
                        "Web"), data.skill);

            }
        }
    }

}
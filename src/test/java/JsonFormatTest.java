import com.fasterxml.jackson.databind.ObjectMapper;
import Profile.Json;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.io.InputStreamReader;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonFormatTest {
    ClassLoader cl = JsonFormatTest.class.getClassLoader();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Проверка профиля специалиста")
    void jsonTest() throws Exception {

        try (
                InputStream resource = cl.getResourceAsStream("Profile.json")
        ) {
            assert resource != null;
            try (InputStreamReader reader = new InputStreamReader(resource)
            ) {
                Json Json = objectMapper.readValue(reader, Json.class);
                assertThat(Json.name).isEqualTo("Helen");
                assertThat(Json.profession).isEqualTo("qa");
                assertThat(Json.age).isEqualTo(25);
                assertThat(Json.skill).contains("Regression", "Smoke", "Zephyr");

            }
        }
    }
}
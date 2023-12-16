import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckFileTest {

    String archiveName = "testArchive.zip";

    private final ClassLoader classLoader = CheckFileTest.class.getClassLoader();

    @Test
    @DisplayName("Проверка CSV файла из архива")
    void archivedCsvTest() throws Exception {
        boolean csvExists = false;
        try (InputStream inputStream = classLoader.getResourceAsStream(archiveName);
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                final String fileName = entry.getName();
                if (fileName.endsWith(".csv")) {
                    csvExists = true;
                    Reader reader = new InputStreamReader(zipInputStream);
                    CSVReader csvReader = new CSVReader(reader);
                    List<String[]> content = csvReader.readAll();
                    assertThat(content.size()).isEqualTo(7);
                    assertThat(content.get(0)).containsExactly("Имя", "Должность");
                    assertThat(content.get(1)).containsExactly("Рома", "Бэк");
                    assertThat(content.get(2)).containsExactly("Сергей", "Бэк");
                    assertThat(content.get(3)).containsExactly("Лена", "Тестировщик");
                    assertThat(content.get(4)).containsExactly("Ангелина", "Тестировщик");
                    assertThat(content.get(5)).containsExactly("Юля", "Аналитик");
                    assertThat(content.get(6)).containsExactly("Влада", "Аналитик");
                    break;
                }
            }
        }
        assertThat(csvExists).as("Файла не существует").isTrue();
    }

    @Test
    @DisplayName("Проверка xls файла")
    void archivedXlsTest() throws Exception {
        boolean xlsExists = false;
        try (InputStream inputStream = classLoader.getResourceAsStream(archiveName);
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                final String fileName = entry.getName();
                if (fileName.endsWith(".xls") | fileName.endsWith(".xlsx")) {
                    xlsExists = true;
                    XLS xls = new XLS(zipInputStream);
                    assertThat(xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue())
                            .isEqualTo("Имя");
                    assertThat(xls.excel.getSheetAt(0).getRow(0).getCell(1).getStringCellValue())
                            .isEqualTo("Должность");
                    assertThat(xls.excel.getSheetAt(0).getRow(1).getCell(0).getStringCellValue())
                            .isEqualTo("Лена");
                    assertThat(xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue())
                            .isEqualTo("Тестировщик");
                    break;
                }
            }
        }
        assertThat(xlsExists).as("Файла не существует ").isTrue();
    }

    @Test
    @DisplayName("Проверка PDF файла")
    void archivedPdfTest() throws Exception {
        boolean pdfExists = false;
        try (InputStream inputStream = classLoader.getResourceAsStream(archiveName);
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                final String fileName = entry.getName();
                if (fileName.endsWith(".pdf")) {
                    pdfExists = true;
                    PDF pdf = new PDF(zipInputStream);
                    String cleanedText = pdf.text.replace("\u00a0", " ");
                    assertThat(cleanedText).contains("Лена");
                    break;
                }
            }
        }
        assertThat(pdfExists).as("Файла не существует").isTrue();
    }

}
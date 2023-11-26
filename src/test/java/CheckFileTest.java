import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import static org.assertj.core.api.Assertions.assertThat;


public class CheckFileTest {
    private ClassLoader cl = CheckFileTest.class.getClassLoader();

    @DisplayName("Распаковка и проверка CSV из архива ZIP ")
    @Test
    void zipCvsFileTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                Objects.requireNonNull(cl.getResourceAsStream("TestArchive.zip")))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> csvContent = csvReader.readAll();
                    Assertions.assertArrayEquals(new String[]{"Пример csv"}, csvContent.get(1));
                    break;

                }
            }
        }
    }
    @DisplayName("Распаковка и проверка Excel из архива ZIP ")
    @Test
    void zipExcelFileTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                Objects.requireNonNull(cl.getResourceAsStream("TestArchive.zip")))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".xls")) {
                    XLS xls = new XLS(zis);
                    Assertions.assertEquals(xls.excel.getSheetAt(0).getRow(10).getCell(0)
                            .getStringCellValue(), "xls");
                    break;
                }
            }
        }
    }
    @DisplayName("Распаковка и проверка PDF из архива ZIP ")
    @Test
    void zipPdfFileTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                Objects.requireNonNull(cl.getResourceAsStream("TestArchive.zip")))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".pdf")) {
                    PDF pdf = new PDF(zis);
                    assertThat(pdf.text).contains("PDF");
                    break;
                }
            }
        }
    }
}
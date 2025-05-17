import org.apache.poi.ss.usermodel.*;
import java.io.*;
import java.util.*;

public class ExcelReader {

    // Путь к Excel-файлу по умолчанию
    private static final String DEFAULT_FILE_PATH = "data/Data.xlsx";

    /**
     * Читает данные из Excel по умолчательному пути и указанному листу
     */
    public static List<Map<String, Object>> readExcel(String sheetName) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        FileInputStream fis = new FileInputStream(new File(DEFAULT_FILE_PATH));
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            throw new IllegalArgumentException("Лист \"" + sheetName + "\" не найден");
        }

        Row headerRow = sheet.getRow(0); // Первая строка — заголовки

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row currentRow = sheet.getRow(i);
            if (currentRow == null) continue;

            Map<String, Object> rowData = new HashMap<>();
            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                Cell headerCell = headerRow.getCell(j);
                Cell dataCell = currentRow.getCell(j);

                String key = "";
                if (headerCell != null) {
                    key = headerCell.getStringCellValue();
                }

                rowData.put(key, getCellValue(dataCell));
            }

            result.add(rowData);
        }

        workbook.close();
        fis.close();

        return result;
    }

    // Возвращает значение ячейки в виде Object
    private static Object getCellValue(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            case BLANK -> "";
            default -> null;
        };
    }
}
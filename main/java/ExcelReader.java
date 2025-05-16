import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelReader {

    public static List<Map<String, Object>> readExcel(String filePath, String sheetName) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            throw new IllegalArgumentException("Лист \"" + sheetName + "\" не найден в файле Excel.");
        }

        Row headerRow = sheet.getRow(0); // Первая строка — заголовки

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row currentRow = sheet.getRow(i);
            if (currentRow == null) continue;

            Map<String, Object> rowData = new HashMap<>();
            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                Cell headerCell = headerRow.getCell(j);
                Cell dataCell = currentRow.getCell(j);

                String key = headerCell.getStringCellValue();

                if (dataCell == null) {
                    rowData.put(key, null);
                    continue;
                }

                switch (dataCell.getCellType()) {
                    case STRING:
                        rowData.put(key, dataCell.getStringCellValue());
                        break;
                    case NUMERIC:
                        rowData.put(key, dataCell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        rowData.put(key, dataCell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        rowData.put(key, dataCell.getCellFormula());
                        break;
                    case BLANK:
                        rowData.put(key, "");
                        break;
                    default:
                        rowData.put(key, "Неизвестный тип");
                }
            }

            result.add(rowData);
        }

        workbook.close();
        fis.close();

        return result;
    }
}
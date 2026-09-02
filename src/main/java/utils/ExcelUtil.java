package utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Utility for reading tabular test data from Excel workbooks using Apache POI.
 */
public class ExcelUtil {

    private static final String TEST_DATA_SHEET_PATH = "./src/test/resources/testdata/openCartTestdata.xlsx";
    private static final String USERS_TEST_DATA_SHEET_PATH = "./src/test/resources/testdata/users.xlsx";
    private static Workbook book;
    private static Sheet sheet;
    private static Object data[][];


    /* Apache POI: this is thread safe, its difficult to run in parallel
     * Time complexity is high, we cannot reduce to O(log(n)) or O(1)
     * Suppose, if excel has corrupted data, then it will throw invalid format exception
     * We need to buy the MS-office license to work with the excel
     * We need to have common understanding to maintain the excel
     * We don;t have direct support to read the excel in POI, we need to write the code to read the data
     * In Modern Automation, we don't use the excel to read the data, We will provide the data provider in the test script as well.
     * Maintaining the excel is difficult task
     * If we have large data, then it will take the lot of time to read the data
     * Better to use the database or NoSQL or any other data source
     * We can use the CSV file, its easy to maintain and easy to read
     * JSON is another option, we can use the JSON file to read the data
     *
     * We can use CSV file to read the data suppose if we have more data to mandatory fields - Comma Separated Values
     *
     * In Delta Testing, Suppose if the application is working for 3 products and it will work for 100 products
     */


    /**
     * Reads all data rows (excluding the header row) from the given sheet in
     * the users test data workbook.
     *
     * @param sheetName the name of the sheet to read
     * @return a 2D array of cell values as strings, indexed by [row][column]
     * @throws RuntimeException if the file cannot be found, read, or is an invalid format
     */
    public static Object[][] getTestData(String sheetName) {
        return getTestData(USERS_TEST_DATA_SHEET_PATH, sheetName);
    }

    /**
     * Reads all data rows (excluding the header row) from the given sheet in
     * the specified Excel workbook.
     *
     * @param filePath  path to the Excel workbook
     * @param sheetName the name of the sheet to read
     * @return a 2D array of cell values as strings, indexed by [row][column]
     * @throws RuntimeException if the file cannot be found, read, or is an invalid format
     */
    public static Object[][] getTestData(String filePath, String sheetName) {

        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            book = WorkbookFactory.create(inputStream);
            sheet = book.getSheet(sheetName);

            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in workbook: " + filePath);
            }

            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();

            //Object array -> Object[Row][Column]
            data = new Object[rowCount][colCount];

            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    data[i][j] = sheet.getRow(i + 1).getCell(j).toString();
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

}
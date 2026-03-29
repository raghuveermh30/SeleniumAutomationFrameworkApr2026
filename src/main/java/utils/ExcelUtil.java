package utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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


    public static Object[][] getTestData(String sheetName) {

        try {
            FileInputStream inputStream = new FileInputStream(USERS_TEST_DATA_SHEET_PATH);
            book = WorkbookFactory.create(inputStream);
            sheet = book.getSheet(sheetName);

            //Object array -> Object[Row][Column]
            data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];

            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
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
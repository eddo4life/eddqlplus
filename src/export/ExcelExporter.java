package export;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelExporter {

	public static void export(ResultSet rs, String path, String fileName) throws Exception{

		ResultSetMetaData metadata = rs.getMetaData();
		int numColumns = metadata.getColumnCount();

		// creating an instance of HSSFWorkbook class
		HSSFWorkbook workbook = new HSSFWorkbook();
		// invoking creatSheet() method and passing the name of the sheet to be created
		HSSFSheet sheet = workbook.createSheet("Eddo");
		// creating the 0th row using the createRow() method
		HSSFRow rowhead = sheet.createRow((short) 0);
		// creating cell by using the createCell() method and setting the values to the
		// cell by using the setCellValue() method

		for (int i = 1; i <= numColumns; i++) {
			String columnLabel = metadata.getColumnLabel(i);

			rowhead.createCell(i - 1).setCellValue(columnLabel);
		}

		int rowNum = 1;
		while (rs.next()) {
			try {
				rowNum++;
				Row row = sheet.createRow(rowNum);
				for (int i = 1; i <= numColumns; i++) {
					String value = rs.getString(i);
					Cell cell = row.createCell(i - 1);
					cell.setCellValue(value);
				}
			}catch (Exception e) {}
			
		}

		// Autosize columns
		for (int i = 0; i < numColumns; i++) {
			sheet.autoSizeColumn(i);
		}

		FileOutputStream fileOutS = new FileOutputStream(path+"/" + fileName + ".XLS");
		workbook.unwriteProtectWorkbook();
		workbook.write(fileOutS);
		fileOutS.close();
		workbook.close();

	}

}

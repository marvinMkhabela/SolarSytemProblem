package za.co.discovery.assignment.Services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import za.co.discovery.assignment.Models.Planet;
import za.co.discovery.assignment.Models.Route;
import za.co.discovery.assignment.Models.Traffic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

@Service
public class ExcelDataExtractionService {

    private File file;

    protected ExcelDataExtractionService() {
    }

    public ExcelDataExtractionService(File file) {
        this.file = file;
    }

    public ArrayList<Planet> readSheet1() throws IOException {

        ArrayList objectList = new ArrayList<Planet>();
        try {
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            Planet planet;
            Cell cell;
            Row row;
            Iterator<Cell> cellIterator;

            rowIterator.next();
            while (rowIterator.hasNext()) {
                planet = new Planet();
                row = rowIterator.next();
                cellIterator = row.cellIterator();

                cell = cellIterator.next();
                planet.setNode(cell.getStringCellValue());
                cell = cellIterator.next();
                planet.setName(cell.getStringCellValue());

                objectList.add(planet);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }

        return objectList;

    }

    public ArrayList<Route> readSheet2() throws IOException {

        ArrayList objectList = new ArrayList<Route>();
        try {
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(1);
            Iterator<Row> rowIterator = sheet.iterator();

            Route route;
            Cell cell;
            Row row;
            Iterator<Cell> cellIterator;

            rowIterator.next();
            while (rowIterator.hasNext()) {
                route = new Route();
                row = rowIterator.next();
                cellIterator = row.cellIterator();

                cell = cellIterator.next();
                route.setRouteId((int) cell.getNumericCellValue());
                cell = cellIterator.next();
                route.setOrigin(cell.getStringCellValue());
                cell = cellIterator.next();
                route.setDestination(cell.getStringCellValue());
                cell = cellIterator.next();
                route.setDistance((float) cell.getNumericCellValue());

                objectList.add(route);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }

        return objectList;

    }

    public ArrayList<Traffic> readSheet3() throws IOException {

        ArrayList objectList = new ArrayList<Traffic>();
        try {
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(2);
            Iterator<Row> rowIterator = sheet.iterator();

            Traffic traffic;
            Cell cell;
            Row row;
            Iterator<Cell> cellIterator;

            rowIterator.next();
            while (rowIterator.hasNext()) {
                traffic = new Traffic();
                row = rowIterator.next();
                cellIterator = row.cellIterator();

                cell = cellIterator.next();
                traffic.setRoute((int) cell.getNumericCellValue());
                cell = cellIterator.next();
                traffic.setOrigin(cell.getStringCellValue());
                cell = cellIterator.next();
                traffic.setDestination(cell.getStringCellValue());
                cell = cellIterator.next();
                traffic.setTrafficDelay((float) cell.getNumericCellValue());

                objectList.add(traffic);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }

        return objectList;

    }


}

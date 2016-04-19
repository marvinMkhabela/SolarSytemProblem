package za.co.discovery.assignment.Services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import za.co.discovery.assignment.Models.Edge;
import za.co.discovery.assignment.Models.Traffic;
import za.co.discovery.assignment.Models.Vertex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelDataExtractionService {

    private File file;

    protected ExcelDataExtractionService() {
    }

    public ExcelDataExtractionService(File file) {
        this.file = file;
    }

    public List<Vertex> readSheet1() throws IOException {

        List<Vertex> objectList = new ArrayList<Vertex>();
        try {
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            Vertex vertex;
            Cell cell;
            Row row;
            Iterator<Cell> cellIterator;

            rowIterator.next();
            while (rowIterator.hasNext()) {
                vertex = new Vertex();
                row = rowIterator.next();
                cellIterator = row.cellIterator();

                cell = cellIterator.next();
                vertex.setNode(cell.getStringCellValue());
                cell = cellIterator.next();
                vertex.setName(cell.getStringCellValue());

                objectList.add(vertex);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }

        return objectList;

    }

    public List<Edge> readSheet2() throws IOException {

        List<Edge> objectList = new ArrayList<Edge>();
        try {
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(1);
            Iterator<Row> rowIterator = sheet.iterator();

            Edge edge;
            Cell cell;
            Row row;
            Iterator<Cell> cellIterator;

            rowIterator.next();
            while (rowIterator.hasNext()) {
                edge = new Edge();
                row = rowIterator.next();
                cellIterator = row.cellIterator();

                cell = cellIterator.next();
                edge.setEdgeId((int) cell.getNumericCellValue());
                cell = cellIterator.next();
                edge.setOrigin(cell.getStringCellValue());
                cell = cellIterator.next();
                edge.setDestination(cell.getStringCellValue());
                cell = cellIterator.next();
                edge.setDistance((float) cell.getNumericCellValue());

                objectList.add(edge);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }

        return objectList;

    }

    public List<Traffic> readSheet3() throws IOException {

        List<Traffic> objectList = new ArrayList<Traffic>();
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
                traffic.setRouteId((int) cell.getNumericCellValue());
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

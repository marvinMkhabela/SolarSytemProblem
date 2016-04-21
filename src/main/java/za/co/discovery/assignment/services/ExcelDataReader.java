package za.co.discovery.assignment.services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.Traffic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelDataReader {

    private File file;

    @Autowired
    public ExcelDataReader(File file) {
        this.file = file;
    }

    public List<Planet> readPlanets() throws Exception {

        List<Planet> objectList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            Planet planet;
            String nodeHolder;
            Cell cell;
            Row row;
            Iterator<Cell> cellIterator;
            rowIterator.next();

            while(rowIterator.hasNext()){
                row = rowIterator.next();
                cellIterator = row.cellIterator();
                cell = cellIterator.next();
                nodeHolder = cell.getStringCellValue();
                cell = cellIterator.next();
                planet = new Planet(nodeHolder, cell.getStringCellValue());

                objectList.add(planet);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not read xlsx file");
            System.exit(1);
        }

        return objectList;
    }

    public List<Route> readRoutes(List<Planet> planets) throws Exception {

        List<Route> objectList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(1);
            Iterator<Row> rowIterator = sheet.iterator();

            int routeId;
            Planet origin;
            Planet destination;
            String nodeHolder;
            Cell cell;
            Row row;
            Iterator<Cell> cellIterator;
            rowIterator.next();

            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                cellIterator = row.cellIterator();

                cell = cellIterator.next();
                routeId = (int) cell.getNumericCellValue();
                cell = cellIterator.next();
                nodeHolder = cell.getStringCellValue();
                origin = nodeLookUp(nodeHolder, planets);
                cell = cellIterator.next();
                nodeHolder = cell.getStringCellValue();
                destination = nodeLookUp(nodeHolder, planets);
                cell = cellIterator.next();

                if (origin != null && destination != null) {
                    objectList.add(new Route(routeId, origin, destination, cell.getNumericCellValue()));
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not read xlsx file");
            System.exit(1);
        }

        return objectList;
    }

    public List<Traffic> readTraffic(List<Planet> planets) throws Exception {

        List<Traffic> objectList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(2);
            Iterator<Row> rowIterator = sheet.iterator();

            int trafficId;
            Planet origin;
            Planet destination;
            String nodeHolder;
            Cell cell;
            Row row;
            Iterator<Cell> cellIterator;
            rowIterator.next();

            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                cellIterator = row.cellIterator();

                cell = cellIterator.next();
                trafficId = (int) cell.getNumericCellValue();
                cell = cellIterator.next();
                nodeHolder = cell.getStringCellValue();
                origin = nodeLookUp(nodeHolder, planets);
                cell = cellIterator.next();
                nodeHolder = cell.getStringCellValue();
                destination = nodeLookUp(nodeHolder, planets);
                cell = cellIterator.next();

                if (origin != null && destination != null) {
                    objectList.add(new Traffic(trafficId, origin, destination, cell.getNumericCellValue()));
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not read xlsx file");
            System.exit(1);
        }

        return objectList;
    }

    private Planet nodeLookUp(String node, List<Planet> planets) {

        Planet planet = null;
        for(Planet p : planets) {
            if (node.equals(p.getNode())) {
                planet = p;
            }
        }

        return planet;
    }

}

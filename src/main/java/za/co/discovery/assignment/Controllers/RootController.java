package za.co.discovery.assignment.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import za.co.discovery.assignment.Models.Edge;
import za.co.discovery.assignment.Models.Traffic;
import za.co.discovery.assignment.Models.Vertex;
import za.co.discovery.assignment.Services.ExcelDataExtractionService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class RootController {

    private ExcelDataExtractionService excelDataExtractionService;

    public RootController() {

    }

    @RequestMapping("/")
    public String home() {
        ClassLoader classLoader = getClass().getClassLoader();
        excelDataExtractionService = new ExcelDataExtractionService(new File(classLoader.getResource("worksheet.xlsx").getFile()));

        try {
            ArrayList<Vertex> vertexes = excelDataExtractionService.readSheet1();
            ArrayList<Edge> edges = excelDataExtractionService.readSheet2();
            ArrayList<Traffic> traffic = excelDataExtractionService.readSheet3();

        }
        catch (IOException e){
            System.out.println("Caught IO Exception");
        }
        return "index";
    }

}

package bitflyday.com.demo.webservice.printservice;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.PrintPartUnrollExporterInput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

@SpringBootApplication
@RestController
public class PrintServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrintServiceApplication.class, args);
    }

    @GetMapping("/print")
    public String print() throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("packing_date", "2023-07-26");
        parameters.put("customer_cd", "xxxxxxxxxx");
        parameters.put("customer_name_L2", "xxxxxxxxxxx");
        parameters.put("customer_partno", "xxxxxxxxx");
        parameters.put("snp", "1");
        parameters.put("total_items", "10");
        parameters.put("customer_partname", "xxxxxxxxxxx");
        PrintServices.printingTagLabel(parameters);
        return String.format("print successfully!");
    }

}

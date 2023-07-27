package bitflyday.com.demo.webservice.printservice;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static net.sf.jasperreports.engine.util.JRLoader.getResource;

public class PrintServices {


    public static boolean printingTagLabel(Map<String, Object> _parameters) throws Exception {
        boolean status = false;
        String printerName = "Microsoft Print to PDF";

        String jasperFileName = "smart_packing_tagLabel.jasper";
        File reportFile = new File(Objects.requireNonNull(getResource("static/reports/smart_packing_tagLabel.jasper")).getPath());


        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService printerDevice = null;

        JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());

        _parameters.put("right_arrow", getResource("static/images/right-arrow.png").getPath());


        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.xxx.x:xxxx:wst0", "xxxx", "xxxx")) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, _parameters, conn);

            boolean _checkPrinterExists = checkPrinterExists(services, printerName);

            if (_checkPrinterExists) {
                for (PrintService service : services) {
                    if (service.getName().endsWith(printerName)) {
                        printerDevice = service;
                        status = jasperOutPrint(jasperPrint, printerDevice);
                        break;
                    }
                }
            } else {
                throw new Exception("Not found a printer (" + printerName + ") on the device");
            }
        }
        return status;
    }

    private static boolean jasperOutPrint(JasperPrint jasperPrint, PrintService myPrinter) throws JRException, IOException {
        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        SimplePrintServiceExporterConfiguration exporterConfig = new SimplePrintServiceExporterConfiguration();
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();

        // Data print
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        // Printer config
        printRequestAttributeSet.add(new Copies(1));
        exporterConfig.setPrintService(myPrinter);
        exporterConfig.setPrintServiceAttributeSet(myPrinter.getAttributes());
        exporterConfig.setPrintRequestAttributeSet(printRequestAttributeSet);
        exporter.setConfiguration(exporterConfig);

        // Let's to print...
        exporter.exportReport();
        return true;
    }

    private static boolean checkPrinterExists(PrintService[] services, String _printerName) {
        return Arrays.stream(services).anyMatch((printer) -> printer.getName().endsWith(_printerName));
    }
}

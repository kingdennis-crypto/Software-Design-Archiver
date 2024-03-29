package nl.vu.cs.softwaredesign.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import nl.vu.cs.softwaredesign.app.utils.SelectedFileUtils;
import nl.vu.cs.softwaredesign.lib.handlers.CompressionHandler;
import nl.vu.cs.softwaredesign.lib.handlers.EncryptionHandler;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportPageController extends BaseController {
    @FXML
    public BarChart<String, Number> fileBarChart;
    @FXML
    public Label totalFilesLbl;

    @FXML
    public void initialize() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        fileBarChart.setAnimated(false);

        xAxis.setLabel("Files");
        yAxis.setLabel("Values");

        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("File Data");

        Map<String, Integer> fileCounts = countFiles();

        totalFilesLbl.setText(String.valueOf(fileCounts.values().stream().mapToInt(Integer::intValue).sum()));

        for (Map.Entry<String, Integer> entry : fileCounts.entrySet()) {
            dataSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }



        fileBarChart.getData().add(dataSeries);
    }

    public Map<String, Integer> countFiles() {
        Map<String, Integer> fileCounts = new HashMap<>();

        File file = SelectedFileUtils.getInstance().getSelectedFile();

        CompressionHandler compressionHandler = new CompressionHandler();

        if (compressionHandler.getCompressionExtensions().stream().anyMatch(file.getName()::contains)) {
            String filePath = EncryptionHandler.readMetadataFromFile(SelectedFileUtils.getInstance().getSelectedFile().getAbsolutePath()).get("content");
            countFileInString(filePath, fileCounts);
        } else {
            countFileInDirectory(SelectedFileUtils.getInstance().getSelectedFile(), fileCounts);
        }

        return fileCounts;
    }

    private void countFileInDirectory(File directory, Map<String, Integer> fileCounts) {
        if (directory == null || !directory.isDirectory())
            return;

        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    countFileInDirectory(file, fileCounts);
                } else {
                    String extension = "." + getFileExtension(file);
                    fileCounts.compute(extension, (key, value) -> value == null ? 1 : value + 1);
                }
            }
        }
    }

    private void countFileInString(String content, Map<String, Integer> fileCounts) {
        String[] lines = content.split(System.lineSeparator());

        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("[") && line.endsWith("]"))
                continue;

            String filename = line.replaceAll("-", "").trim();

            if (!filename.isEmpty()) {
                String extension = "." + getFileExtension(filename);
                fileCounts.compute(extension, (key, value) -> value == null ? 1 : value + 1);
            }
        }

    }

    private String getFileExtension(File file) {
        return getFileExtension(file.getName());
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');

        if (lastDotIndex != -1 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        } else {
            return "";
        }
    }
}

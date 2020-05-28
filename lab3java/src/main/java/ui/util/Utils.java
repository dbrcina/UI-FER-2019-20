package ui.util;

import ui.model.ClassLabel;
import ui.model.Configuration;
import ui.model.Feature;
import ui.model.Sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {

    private static final String CSV_SEPARATOR = "\\s*,\\s*";
    private static final String CFG_SEPARATOR = "\\s*=\\s*";

    public static Collection<Sample> samplesFromCSV(Path csvFile, boolean extendFeatures) {
        try {
            Collection<Sample> samples = new ArrayList<>();
            List<String> lines = Files.readAllLines(csvFile);
            String[] header = lines.get(0).strip().split(CSV_SEPARATOR);
            for (int i = 1; i < lines.size(); i++) {
                String[] data = lines.get(i).strip().split(CSV_SEPARATOR);
                Collection<Feature> features = new ArrayList<>();
                for (int j = 0; j < data.length - 1; j++) {
                    Feature feature = new Feature(header[j], data[j]);
                    features.add(feature);
                    if (extendFeatures) {
                        Feature.extendForFeature(feature.getName(), feature.getValue());
                    }
                }
                ClassLabel classLabel = new ClassLabel(header[header.length - 1], data[data.length - 1]);
                ClassLabel.extendValues(classLabel.getValue());
                samples.add(new Sample(features, classLabel));
            }
            return samples;
        } catch (IOException e) {
            System.out.println("Greska tijekom citanja datoteke " + csvFile + ".");
            System.exit(-1);
        }
        return null;
    }

    public static Configuration configurationFromCFG(Path cfgFile) {
        try {
            Configuration config = new Configuration();
            List<String> lines = Files.readAllLines(cfgFile);
            for (String line : lines) {
                String[] parts = line.strip().split(CFG_SEPARATOR);
                switch (parts[0].toLowerCase()) {
                    case "mode":
                        config.setMode(Configuration.Mode.valueOf(parts[1].toUpperCase()));
                        break;
                    case "model":
                        config.setModel(Configuration.Model.valueOf(parts[1].toUpperCase()));
                        break;
                    case "max_depth":
                        config.setMaxDepth(Integer.parseInt(parts[1]));
                        break;
                    case "num_trees":
                        config.setNumTrees(Integer.parseInt(parts[1]));
                        break;
                    case "feature_ration":
                        config.setFeatureRation(Double.parseDouble(parts[1]));
                        break;
                    case "example_ration":
                        config.setExampleRation(Double.parseDouble(parts[1]));
                        break;
                }
            }
            return config;
        } catch (IOException e) {
            System.out.println("Greska tijekom citanja datoteke " + cfgFile + ".");
            System.exit(-1);
        }
        return null;
    }

}

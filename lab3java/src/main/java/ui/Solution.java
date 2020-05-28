package ui;

import ui.ml.ID3;
import ui.ml.MLAlgorithm;
import ui.model.ClassLabel;
import ui.model.Configuration;
import ui.model.Sample;
import ui.util.Utils;

import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public class Solution {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Program ocekuje tri datoteke.");
            return;
        }

        Collection<Sample> trainingDataset = Utils.samplesFromCSV(Paths.get(args[0]), true);
        Collection<Sample> testingDataset = Utils.samplesFromCSV(Paths.get(args[1]), false);
        Configuration config = Utils.configurationFromCFG(Paths.get(args[2]));
        MLAlgorithm mlAlgorithm = new ID3(config);
        mlAlgorithm.fit(trainingDataset);
        List<String> predictions = new ArrayList<>(mlAlgorithm.predict(testingDataset));
        StringJoiner sj = new StringJoiner(" ");
        predictions.forEach(sj::add);
        System.out.println(sj);
        int correct = 0;
        int index = 0;
        List<String> valuesForClassLabel = new ArrayList<>(ClassLabel.values());
        int[][] confMatrix = new int[valuesForClassLabel.size()][valuesForClassLabel.size()];
        for (Sample s : testingDataset) {
            String expected = s.getClassLabel().getValue();
            String actual = predictions.get(index++);
            if (actual.equals(expected)) correct++;
            int i1 = valuesForClassLabel.indexOf(expected);
            int i2 = valuesForClassLabel.indexOf(actual);
            confMatrix[i1][i2]++;
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        NumberFormat formatter = new DecimalFormat("#0.00000", symbols);
        System.out.println(formatter.format(1.0 * correct / predictions.size()));
        StringBuilder sb = new StringBuilder();
        for (int[] matrix : confMatrix) {
            for (int j = 0; j < confMatrix.length; j++) {
                sb.append(matrix[j]).append(" ");
            }
            sb.setLength(sb.length() - 1);
            sb.append("\n");
        }
        sb.setLength(sb.length() - 1);
        System.out.println(sb);
    }

}

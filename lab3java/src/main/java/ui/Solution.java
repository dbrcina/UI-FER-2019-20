package ui;

import ui.model.Configuration;
import ui.model.Sample;
import ui.util.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class Solution {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Program ocekuje tri datoteke.");
            return;
        }

        Collection<Sample> trainingDataset = Utils.samplesFromCSV(Paths.get(args[0]));
        Collection<Sample> testingDataset = Utils.samplesFromCSV(Paths.get(args[1]));
        Configuration config = Utils.configurationFromCFG(Paths.get(args[2]));
    }

}

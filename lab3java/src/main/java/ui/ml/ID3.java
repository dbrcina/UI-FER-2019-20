package ui.ml;

import ui.model.Configuration;
import ui.model.Feature;
import ui.model.Sample;
import ui.model.TreeNode;

import java.util.*;
import java.util.stream.Collectors;

public class ID3 implements MLAlgorithm {

    private final Configuration config;
    private final StringBuilder sbIG = new StringBuilder();
    private TreeNode<String> root;

    public ID3(Configuration config) {
        this.config = config;
    }

    @Override
    public void fit(Collection<Sample> dataset) {
        Collection<Sample> samples = new ArrayList<>(dataset);
        Collection<String> featureNames = new TreeSet<>(Feature.allFeatureNames());
        root = id3(samples, samples, featureNames, 0, null);
        System.out.println(TreeNode.constructPathByNodes(root));
        sbIG.setLength(sbIG.length() - 1);
        if (config.getMode() == Configuration.Mode.VERBOSE) {
            System.out.println(sbIG.toString());
        }
        sbIG.setLength(0);
    }

    @Override
    public Collection<String> predict(Collection<Sample> dataset) {
        Collection<String> predictions = new ArrayList<>();
        dataset.forEach(sample -> predictions.add(predictClassLabelForSample(sample)));
        return predictions;
    }

    private String predictClassLabelForSample(Sample sample) {
        return predictRecursive(root, new TreeSet<>(sample.getFeatures()));
    }

    private String predictRecursive(TreeNode<String> node, Collection<Feature> features) {
        String value = node.getValue();
        Collection<TreeNode<String>> children = node.getChildren();
        if (children == null) return value;
        TreeNode<String> child = null;
        Iterator<Feature> it = features.iterator();
        while (it.hasNext()) {
            Feature f = it.next();
            if (f.getName().equals(value)) {
                child = children.stream()
                        .filter(c -> c.getBranch().equals(f.getValue()))
                        .findFirst().get();
                it.remove();
                break;
            }
        }
        return predictRecursive(child, features);
    }

    private TreeNode<String> id3(Collection<Sample> samples,
                                 Collection<Sample> parentSamples,
                                 Collection<String> featureNames,
                                 int depth,
                                 String branch) {
        if (config.getMaxDepth() != -1 && depth == config.getMaxDepth()) {
            return new TreeNode<>(branch,
                    findMostCommonClassLabel(samples.isEmpty() ? parentSamples : samples),
                    null,
                    depth);
        }
        if (samples.isEmpty()) {
            return new TreeNode<>(branch, findMostCommonClassLabel(parentSamples), null, depth);
        }
        if (samples.stream().map(Sample::getClassLabel).distinct().count() == 1) {
            return new TreeNode<>(branch, samples.iterator().next().getClassLabel().getValue(), null, depth);
        }
        if (featureNames.isEmpty()) {
            return new TreeNode<>(branch, findMostCommonClassLabel(samples), null, depth);
        }
        String discriminativeFeatureName = findMostDiscriminativeFeatureName(samples, featureNames);
        Collection<String> filteredFeatureNames = featureNames.stream()
                .filter(featureName -> !featureName.equals(discriminativeFeatureName))
                .collect(Collectors.toSet());
        Collection<TreeNode<String>> children = new ArrayList<>();
        for (String featureValue : Feature.valuesForFeature(discriminativeFeatureName)) {
            Feature f = new Feature(discriminativeFeatureName, featureValue);
            Collection<Sample> filteredSamples = filterSamplesByFeature(samples, f);
            children.add(id3(filteredSamples, samples, filteredFeatureNames,
                    depth + 1, featureValue));
        }
        return new TreeNode<>(branch, discriminativeFeatureName, children, depth);
    }

    private String findMostCommonClassLabel(Collection<Sample> samples) {
        return classLabelsAppearance(samples).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();
    }

    private String findMostDiscriminativeFeatureName(
            Collection<Sample> samples, Collection<String> featureNames) {
        Set<IGFeaturePair> igFeaturePairs = new TreeSet<>(
                Comparator.comparingDouble(IGFeaturePair::getIG).reversed()
                        .thenComparing(IGFeaturePair::getFeatureName));
        for (String featureName : featureNames) {
            igFeaturePairs.add(new IGFeaturePair(informationGain(samples, featureName), featureName));
        }
        igFeaturePairs.forEach(pair ->
                sbIG.append(String.format("IG(%s)=%.4f ", pair.featureName, pair.ig)));
        sbIG.append("\n");
        return igFeaturePairs.iterator().next().featureName;
    }

    private double informationGain(Collection<Sample> samples, String featureName) {
        double ig = entropyFor(samples);
        Collection<String> featureValues = Feature.valuesForFeature(featureName);
        for (String featureValue : featureValues) {
            Feature f = new Feature(featureName, featureValue);
            Collection<Sample> filteredSamples = filterSamplesByFeature(samples, f);
            ig -= 1.0 * filteredSamples.size() / samples.size() * entropyFor(filteredSamples);
        }
        return ig;
    }

    private double entropyFor(Collection<Sample> samples) {
        double entropy = 0.0;
        Map<String, Integer> classLabelsMap = classLabelsAppearance(samples);
        Set<String> classLabels = classLabelsMap.keySet();
        for (String classLabel : classLabels) {
            double p = 1.0 * classLabelsMap.get(classLabel) / samples.size();
            entropy += -p * Math.log10(p) / Math.log10(2);
        }
        return entropy;
    }

    private Map<String, Integer> classLabelsAppearance(Collection<Sample> samples) {
        Map<String, Integer> classLabelsMap = new TreeMap<>();
        for (Sample sample : samples) {
            classLabelsMap.merge(sample.getClassLabel().getValue(), 1, Integer::sum);
        }
        return classLabelsMap;
    }

    private Collection<Sample> filterSamplesByFeature(Collection<Sample> samples, Feature feature) {
        return samples.stream()
                .filter(sample -> sample.getFeatures().contains(feature))
                .collect(Collectors.toList());
    }

    private static class IGFeaturePair {
        private final double ig;
        private final String featureName;

        private IGFeaturePair(double ig, String featureName) {
            this.ig = ig;
            this.featureName = featureName;
        }

        public double getIG() {
            return ig;
        }

        public String getFeatureName() {
            return featureName;
        }
    }

}

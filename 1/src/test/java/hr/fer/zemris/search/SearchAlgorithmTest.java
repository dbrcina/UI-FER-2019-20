package hr.fer.zemris.search;

import hr.fer.zemris.search.heuristic.Astar;
import hr.fer.zemris.search.structure.BasicNode;
import hr.fer.zemris.search.structure.CostNode;
import hr.fer.zemris.search.structure.HeuristicNode;
import hr.fer.zemris.search.structure.StateCostPair;
import hr.fer.zemris.search.uninformed.BFS;
import hr.fer.zemris.search.uninformed.UCS;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public class SearchAlgorithmTest {

    @Test
    public void BFSTest() {
        Map<String, Set<StateCostPair<String>>> transitions = Map.of(
                "A", Set.of(new StateCostPair<>("A", 0.0), new StateCostPair<>("B", 0.0)),
                "B", Set.of(new StateCostPair<>("C", 0.0), new StateCostPair<>("D", 0.0)),
                "C", Set.of(new StateCostPair<>("E", 0.0), new StateCostPair<>("F", 0.0)),
                "D", Set.of(new StateCostPair<>("G", 0.0), new StateCostPair<>("H", 0.0)),
                "E", Set.of(),
                "F", Set.of(),
                "G", Set.of(),
                "H", Set.of()
        );
        Function<String, Set<StateCostPair<String>>> succ = transitions::get;
        Predicate<String> goal = s -> s.equals("G");
        SearchAlgorithm<String> searchAlg = new BFS<>();
        BasicNode<String> result = searchAlg.search("A", succ, goal).get();
        Assert.assertEquals("A =>\nB =>\nD =>\nG", BasicNode.printPathTowardsNode(result));
    }

    @Test
    public void UCSTest() {
        Map<String, Set<StateCostPair<String>>> transitions = Map.of(
                "A", Set.of(new StateCostPair<>("B", 2.0), new StateCostPair<>("C", 3.0)),
                "B", Set.of(new StateCostPair<>("D", 5.0), new StateCostPair<>("E", 4.0)),
                "C", Set.of(new StateCostPair<>("F", 7.0), new StateCostPair<>("G", 2.0)),
                "D", Set.of(new StateCostPair<>("H", 20.0)),
                "E", Set.of(new StateCostPair<>("H", 30.0)),
                "F", Set.of(new StateCostPair<>("H", 4.0)),
                "G", Set.of(new StateCostPair<>("H", 50.0)),
                "H", Set.of()
        );
        Function<String, Set<StateCostPair<String>>> succ = transitions::get;
        Predicate<String> goal = s -> s.equals("H");
        SearchAlgorithm<String> searchAlg = new UCS<>();
        CostNode<String> result = (CostNode<String>) searchAlg.search("A", succ, goal).get();
        Assert.assertEquals("A =>\nC =>\nF =>\nH", BasicNode.printPathTowardsNode(result));
        Assert.assertEquals(4, result.depth() + 1);
        Assert.assertEquals(14.0, result.getCost(), 1e-6);
    }

    @Test
    public void AstarTest() {
        Map<String, Set<StateCostPair<String>>> transitions = Map.of(
                "A", Set.of(new StateCostPair<>("B", 2.0), new StateCostPair<>("C", 3.0)),
                "B", Set.of(new StateCostPair<>("D", 5.0), new StateCostPair<>("E", 4.0)),
                "C", Set.of(new StateCostPair<>("F", 7.0), new StateCostPair<>("G", 2.0)),
                "D", Set.of(new StateCostPair<>("H", 20.0)),
                "E", Set.of(new StateCostPair<>("H", 30.0)),
                "F", Set.of(new StateCostPair<>("H", 4.0)),
                "G", Set.of(new StateCostPair<>("H", 50.0)),
                "H", Set.of()
        );
        Map<String, Double> heuristic = Map.of(
                "A", 10.0, "B", 5.0, "C", 5.0, "D", 3.0, "E", 2.0, "F", 5.0, "G", 3.0, "H", 0.0);
        Function<String, Set<StateCostPair<String>>> succ = transitions::get;
        Predicate<String> goal = s -> s.equals("H");
        ToDoubleFunction<String> h = heuristic::get;
        SearchAlgorithm<String> searchAlg = new Astar<>(h);
        HeuristicNode<String> result = (HeuristicNode<String>) searchAlg.search("A", succ, goal).get();
        Assert.assertEquals("A =>\nC =>\nF =>\nH", BasicNode.printPathTowardsNode(result));
        Assert.assertEquals(4, result.depth() + 1);
        Assert.assertEquals(14.0, result.getCost(), 1e-6);
    }

}

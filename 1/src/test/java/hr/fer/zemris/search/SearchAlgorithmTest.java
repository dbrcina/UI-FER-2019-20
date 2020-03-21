package hr.fer.zemris.search;

import hr.fer.zemris.search.structure.BasicNode;
import hr.fer.zemris.search.structure.CostNode;
import hr.fer.zemris.search.structure.StateCostPair;
import hr.fer.zemris.search.uninformed.BFS;
import hr.fer.zemris.search.uninformed.UCS;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class SearchAlgorithmTest {

    @Test
    public void BFSTest() {
        Map<String, Set<StateCostPair<String>>> transitions = Map.of(
                "A", new LinkedHashSet<>(List.of(new StateCostPair<>("A"), new StateCostPair<>("B"))),
                "B", new LinkedHashSet<>(List.of(new StateCostPair<>("C"), new StateCostPair<>("D"))),
                "C", new LinkedHashSet<>(List.of(new StateCostPair<>("E"), new StateCostPair<>("F"))),
                "D", new LinkedHashSet<>(List.of(new StateCostPair<>("G"), new StateCostPair<>("H"))),
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
        Assert.assertEquals(4, result.depth() + 1);
        Assert.assertEquals(4, searchAlg.getStatesVisited());
    }

    @Test
    public void UCSTest() {
        Map<String, Set<StateCostPair<String>>> transitions = Map.of(
                "A", new LinkedHashSet<>(List.of(new StateCostPair<>("B", 2.0), new StateCostPair<>("C", 3.0))),
                "B", new LinkedHashSet<>(List.of(new StateCostPair<>("D", 5.0), new StateCostPair<>("E", 4.0))),
                "C", new LinkedHashSet<>(List.of(new StateCostPair<>("F", 7.0), new StateCostPair<>("G", 2.0))),
                "D", new LinkedHashSet<>(List.of(new StateCostPair<>("H", 20.0))),
                "E", new LinkedHashSet<>(List.of(new StateCostPair<>("H", 30.0))),
                "F", new LinkedHashSet<>(List.of(new StateCostPair<>("H", 4.0))),
                "G", new LinkedHashSet<>(List.of(new StateCostPair<>("H", 50.0))),
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

}

package pgdp.infinite;

import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import pgdp.infinite.tree.InfiniteNode;
import pgdp.infinite.tree.InfiniteTree;
import pgdp.infinite.tree.Optimizable;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.Collections.emptyIterator;
import static org.junit.jupiter.api.Assertions.fail;

@TestClassAnnotation
public class BehaviorTest {

    private static final Function<Long, Iterator<Long>> bits = n -> List.of(n << 1, (n << 1) + 1).iterator();
    private static final Function<String, Iterator<String>> words = s -> IntStream.range(0, 26)
            .mapToObj(i -> s + (char) ('a' + i))
            .toList().iterator();
    private static final Function<String, Iterator<String>> abcWords = s -> IntStream.range(0, 3)
            .mapToObj(i -> s + (char) ('a' + i))
            .toList().iterator();
    private static final Function<String, Iterator<String>> wordsOnlyA = s -> new Iterator<>() {
        char c = 'a';

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public String next() {
            if (c == 'b') {
                throw new IllegalStateException("Should not be called!");
            }
            return s + c++;
        }
    };
    private static final Function<Boolean, Iterator<Boolean>> empty = b -> emptyIterator();

    private static AccessableOptimizable<String> getOrderChecker() {
        return new AccessableOptimizable<>() {
            final String[] combinations = new String[]{"", "a", "aa", "aaa", "aab", "aac", "ab", "aba", "abb", "abc", "ac",
                    "aca", "acb", "acc", "b", "ba", "baa", "bab", "bac", "bb", "bba", "bbb", "bbc", "bc", "bca", "bcb",
                    "bcc", "c", "ca", "caa", "cab", "cac", "cb", "cba", "cbb", "cbc", "cc", "cca", "ccb", "ccc"};
            int index = 0;

            @Override
            public boolean process(String s) {

                for (int i = 0; i < index; i++) {
                    if (combinations[i].equals(s)) {
                        // in this case this is just a duplicate call
                        return false;
                    }
                }

                if (index > combinations.length) {
                    fail("Too many calls to process!");
                }

                if (!Objects.equals(s, combinations[index++])) {
                    fail("Expected " + combinations[index - 1] + " but got " + s + " at " + (index - 1) + "-th " +
                            "searched element!");
                }
                return false;
            }

            @Override
            public String getOptimum() {
                return "";
            }

            @Override
            public int getNumberOfCalls() {
                return index;
            }
        };
    }

    private static <T> T dfsOnTree(InfiniteNode<T> node, Optimizable<T> checker, int depth) {
        if (checker.process(node.getValue())) {
            return node.getValue();
        }
        if (depth == 0) {
            return null;
        }
        int index = 0;
        while (!node.isFullyCalculated()) {
            T result = dfsOnTree(node.calculateNextChild(), checker, depth - 1);
            if (result != null) {
                return result;
            }
            node.getChildren().get(index++).resetChildren();
        }
        return null;
    }

    private static Optimizable<Long> getLongChecker(Long find) {
        return new Optimizable<>() {
            long optimal = Long.MIN_VALUE;
            long distance = Long.MAX_VALUE;

            @Override
            public boolean process(Long aLong) {
                if (Math.abs(aLong - find) < distance) {
                    optimal = aLong;
                    distance = Math.abs(aLong - find);
                }
                return distance == 0;
            }

            @Override
            public Long getOptimum() {
                return optimal;
            }
        };
    }

    private static Optimizable<String> getStringChecker(String find) {
        return new Optimizable<>() {
            boolean found = false;

            @Override
            public boolean process(String aLong) {
                if (Objects.equals(aLong, find)) {
                    found = true;
                    return true;
                }
                return false;
            }

            @Override
            public String getOptimum() {
                return found ? find : null;
            }
        };
    }

    static void testFindsExistingValue() {
        InfiniteTree<String> tree = new InfiniteTree<>(abcWords);
        String[] searches = new String[]{"", "a", "x", "aab", "caa", "aaaa"};
        boolean[] found = new boolean[]{true, true, false, true, true, false};

        for (int i = 0; i < searches.length; i++) {
            String search = searches[i];
            boolean expected = found[i];
            String foundString = tree.find("", 3, getStringChecker(search));
            if (expected && !Objects.equals(foundString, search)) {
                fail("Expected to find \"" + search + "\" but got \"" + foundString + "\"!");
            } else if (!expected && foundString != null) {
                fail("Expected to not find \"" + search + "\" but got \"" + foundString + "\"!");
            }
        }

        InfiniteTree<Boolean> emptyTree = new InfiniteTree<>(empty);
        if (emptyTree.find(true, 3, new Optimizable<>() {
            @Override
            public boolean process(Boolean aBoolean) {
                return false;
            }

            @Override
            public Boolean getOptimum() {
                return null;
            }
        }) != null) {
            fail("Expected to not find anything in an empty tree!");
        }
    }

    static void testFindOptimalValue() {
        InfiniteTree<Long> tree = new InfiniteTree<>(bits);

        long found = tree.find(0L, 3, getLongChecker(9L));
        if (found != 7L) {
            fail("Expected to find 7 but got " + found + " search from 0L for 9 with maxDepth 3!");
        }

        found = tree.find(11L, 4, getLongChecker(42L));
        if (found != 44L) {
            fail("Expected to find 44 but got " + found + " search from 11L for 42 with maxDepth 4!");
        }

        found = tree.find(9L, 3, getLongChecker(11L));
        if (found != 9L) {
            fail("Expected to find 9 but got " + found + " search from 9L for 11 with maxDepth 3!");
        }
    }

    static void testFindDoesDepthFirst() {
        InfiniteTree<String> tree = new InfiniteTree<>(abcWords);
        AccessableOptimizable<String> checker = getOrderChecker();
        tree.find("", 3, checker);
        if (checker.getNumberOfCalls() < 40) {
            fail("Expected at least 40 calls to process but got " + checker.getNumberOfCalls() + "!");
        }
    }

    static void testFindNoUnnecessaryExpansions() {
        InfiniteTree<String> tree = new InfiniteTree<>(wordsOnlyA);
        try {
            String res = tree.find("", 5, getStringChecker("aaaaa"));
            if (!Objects.equals(res, "aaaaa")) {
                fail("Expected to find \"aaaaa\" but got \"" + res + "\"!");
            }
        } catch (IllegalStateException e) {
            fail("Should not have expanded any nodes except the ones with 'a' searching for \"aaaaa\"!");
        }
    }

    static void testFindResetSearched() {
        InfiniteTree<String> tree = new InfiniteTree<>(words);
        try {
            tree.find("", 5, getStringChecker("xxxxx"));
        } catch (OutOfMemoryError e) {
            fail("It seems like you did not collapsed unnecessary expansions.");
        }
    }

    static void testWithRoot() {
        InfiniteTree<Long> tree = new InfiniteTree<>(bits);
        InfiniteNode<Long> node = tree.withRoot(0L);

        if (node.getChildren().size() != 0) {
            fail("The root node's children should be empty!");
        }
        if (node.getValue() != 0L) {
            fail("The root node's value should be 0L!");
        }
        if (node.getParent() != null) {
            fail("The root node's parent should be null!");
        }

        InfiniteTree<Boolean> emptyTree = new InfiniteTree<>(empty);
        InfiniteNode<Boolean> emptyNode = emptyTree.withRoot(false);

        if (emptyNode.getChildren().size() != 0) {
            fail("The root node's children should be empty!");
        }
        if (emptyNode.getValue()) {
            fail("The root node's value should be false!");
        }
        if (emptyNode.getParent() != null) {
            fail("The root node's parent should be null!");
        }
    }

    static void testCalculateNext() {
        InfiniteTree<String> tree = new InfiniteTree<>(words);
        InfiniteNode<String> node = tree.withRoot("");
        InfiniteNode<String> someChild = null;

        // test with root node
        for (int i = 0; i < 26; i++) {
            InfiniteNode<String> child = node.calculateNextChild();
            if (someChild == null) {
                someChild = child;
            }

            if (!Objects.equals(child.getValue(), "" + (char) ('a' + i))) {
                fail("The value of the " + i + "-th child should be \"" + (char) ('a' + i) + "\"!");
            }
            if (child.getParent() != node) {
                fail("The parent of the " + i + "-th child should be the root node!");
            }
            if (child.getChildren().size() != 0) {
                fail("The " + i + "-th child should not have children!");
            }
        }

        // test with non-root node
        node = someChild.calculateNextChild().calculateNextChild();
        for (int i = 0; i < 26; i++) {
            InfiniteNode<String> child = node.calculateNextChild();

            if (!Objects.equals(child.getValue(), "aaa" + (char) ('a' + i))) {
                fail("The value of the " + i + "-th child should be \"" + (char) ('a' + i) + "\"!");
            }
            if (child.getParent() != node) {
                fail("The parent of the " + i + "-th child should be the root node!");
            }
            if (child.getChildren().size() != 0) {
                fail("The " + i + "-th child should not have children!");
            }
        }

        // no empty testing because it was not given int the assignment
    }

    static void testCalculateAll() {
        InfiniteTree<String> tree = new InfiniteTree<>(words);
        InfiniteNode<String> node = tree.withRoot("");

        // test with root node
        node.calculateAllChildren();
        if (node.getChildren().size() != 26) {
            fail("The root node should have 26 children!");
        }

        for (int i = 0; i < 26; i++) {
            if (!Objects.equals(node.getChildren().get(i).getValue(), "" + (char) ('a' + i))) {
                fail("The value of the " + i + "-th child should be \"" + (char) ('a' + i) + "\"!");
            }
        }

        // test with non-root node
        node = node.getChildren().get(0).calculateNextChild().calculateNextChild();
        node.calculateAllChildren();
        if (node.getChildren().size() != 26) {
            fail("The root node should have 26 children!");
        }
        for (int i = 0; i < 26; i++) {
            if (!Objects.equals(node.getChildren().get(i).getValue(), "aaa" + (char) ('a' + i))) {
                fail("The value of the " + i + "-th child should be \"" + (char) ('a' + i) + "\"!");
            }
        }

        InfiniteTree<Boolean> emptyTree = new InfiniteTree<>(empty);
        InfiniteNode<Boolean> emptyNode = emptyTree.withRoot(false);
        emptyNode.calculateAllChildren();
        if (emptyNode.getChildren().size() != 0) {
            fail("The root node of an empty tree should not have children!");
        }
    }

    static void testIsCalculated() {
        InfiniteTree<String> tree = new InfiniteTree<>(words);
        InfiniteNode<String> node = tree.withRoot("");

        for (int i = 0; i < 26; i++) {
            if (node.isFullyCalculated()) {
                fail("The root node should not be fully calculated util all children are calculated!");
            }
            node.calculateNextChild();
        }
        if (!node.isFullyCalculated()) {
            fail("The root node should be fully calculated after all children have been calculated!");
        }

        node = node.getChildren().get(0).calculateNextChild().calculateNextChild();
        for (int i = 0; i < 26; i++) {
            if (node.isFullyCalculated()) {
                fail("The some node should not be fully calculated util all children are calculated!");
            }
            node.calculateNextChild();
        }
        if (!node.isFullyCalculated()) {
            fail("The some node should be fully calculated after all children have been calculated!");
        }

        InfiniteTree<Boolean> emptyTree = new InfiniteTree<>(empty);
        InfiniteNode<Boolean> emptyNode = emptyTree.withRoot(false);
        if (!emptyNode.isFullyCalculated()) {
            fail("A node without children should always be fully calculated!");
        }
    }

    static void testResetCalculation() {
        InfiniteTree<String> tree = new InfiniteTree<>(words);
        InfiniteNode<String> node = tree.withRoot("");

        for (int i = 0; i < 26; i++) {
            node.calculateNextChild();
        }

        // resetting works
        node.resetChildren();
        if (node.getChildren().size() != 0) {
            fail("The root node should have no children after resetting!");
        }
        if (node.isFullyCalculated()) {
            fail("The root node should not be fully calculated after resetting!");
        }

        // resetting yields the same children again
        for (int i = 0; i < 26; i++) {
            InfiniteNode<String> child = node.calculateNextChild();
            if (!Objects.equals(child.getValue(), "" + (char) ('a' + i))) {
                fail("The root node should calculate the same children after resetting!");
            }
            if (child.getParent() != node) {
                fail("The parent of the " + i + "-th child should be the root node after resetting!");
            }
            if (child.getChildren().size() != 0) {
                fail("The " + i + "-th child should not have children after resetting!");
            }
            if (child.isFullyCalculated()) {
                fail("The " + i + "-th child should not be fully calculated after resetting!");
            }
        }

        InfiniteTree<Boolean> emptyTree = new InfiniteTree<>(empty);
        InfiniteNode<Boolean> emptyNode = emptyTree.withRoot(false);

        // do something with the tree
        emptyNode.isFullyCalculated();
        emptyNode.getChildren();

        emptyNode.resetChildren();
        if (emptyNode.getChildren().size() != 0) {
            fail("The root node of an empty tree should not have children after resetting!");
        }
        if (!emptyNode.isFullyCalculated()) {
            fail("The root node of an empty tree should be fully calculated after resetting!");
        }
    }

    @Order(1)
    @PublicTest
    @DisplayName("ob eine einfache Traversierung funktioniert")
    void testSimpleTraversing() {
        InfiniteTree<Long> tree = new InfiniteTree<>(bits);
        InfiniteNode<Long> root = tree.withRoot(0L);

        if (root == null) {
            fail("The root node should not be null!");
        }

        if (root.getValue() != 0L) {
            fail("The root node should have the value 0!");
        }
        if (root.isFullyCalculated()) {
            fail("The root node should not be fully calculated before all children are calculated!");
        }

        root.calculateAllChildren();

        if (root.getChildren().get(0).getValue() != 0L) {
            fail("The first child of the root node should have the value 0!");
        }
        if (root.getChildren().get(1).getValue() != 1L) {
            fail("The second child of the root node should have the value 1!");
        }
        if (!root.isFullyCalculated()) {
            fail("The root node should be fully calculated after all children are calculated!");
        }

        if (root.getChildren() == null || root.getChildren().size() != 2) {
            fail("The root node should have two children!");
        }

        InfiniteNode<Long> leftChild = root.getChildren().get(0);

        leftChild.calculateNextChild();

        if (leftChild.getChildren().get(0).getValue() != 0L) {
            fail("The first child of the first child of the root node should have the value 0!");
        }

        leftChild.calculateNextChild();

        if (leftChild.getChildren().get(1).getValue() != 1L) {
            fail("The second child of the first child of the root node should have the value 1!");
        }
        if (!leftChild.isFullyCalculated()) {
            fail("The first child of the root node should be fully calculated after all children are calculated!");
        }

        InfiniteNode<Long> rightChild = root.getChildren().get(1);

        if (rightChild.isFullyCalculated()) {
            fail("The right child of the root should not be fully calculated before all children are calculated!");
        }

        rightChild.calculateAllChildren();

        if (rightChild.getChildren().get(0).getValue() != 2L) {
            fail("The first child of the second child of the root node should have the value 2!");
        }
        if (rightChild.getChildren().get(1).getValue() != 3L) {
            fail("The second child of the second child of the root node should have the value 3!");
        }
        if (!rightChild.isFullyCalculated()) {
            fail("The first child of the root node should be fully calculated after all children are calculated!");
        }
    }

    @Order(2)
    @PublicTest
    @DisplayName("ob eine einfach Suche funktioniert:")
    void testSimpleSearch() {
        InfiniteTree<Long> tree = new InfiniteTree<>(bits);

        Long res = tree.find(0L, 3, getLongChecker(1L));
        if (res == null) {
            fail("The search should have found a value unequal null!");
        }
        if (res != 1L) {
            fail("Searching for the first 1 should return 1 in the binary number tree!");
        }
    }

    @Order(3)
    @HiddenTest
    @DisplayName("ob 'start()' 'calculateNextChild()' und 'calculateAllChildren()' funktionieren:")
    void testStartAndExpand() {
        testWithRoot();
        testCalculateNext();
        testCalculateAll();
    }

    @Order(4)
    @HiddenTest
    @DisplayName("ob 'isFullyCalculated()' und 'resetChildren()' funktionieren:")
    void testIsExpandedAndReset() {
        testIsCalculated();
        testResetCalculation();
    }

    @Order(5)
    @HiddenTest
    @DisplayName("ob im Baum allgemeine logische Zusammenhänge und einfach Traversierung funktionieren:")
    void testTree() {
        InfiniteTree<String> wordTree = new InfiniteTree<>(words);
        InfiniteNode<String> wordNode = wordTree.withRoot("");

        // can do depth first search only with tree interface
        String find = dfsOnTree(wordNode, getStringChecker("lol"), 4);
        if (find == null || !find.equals("lol")) {
            fail("Searching for \"lol\" should not return \"lol\" using tree interface for search!");
        }

        // test if only necessary nodes are calculated
        InfiniteTree<String> onlyATree = new InfiniteTree<>(wordsOnlyA);
        InfiniteNode<String> onlyANode = onlyATree.withRoot("");
        for (int i = 1; i < 10; i++) {
            onlyANode = onlyANode.calculateNextChild();
            if (!Objects.equals(onlyANode.getValue(), "a".repeat(i))) {
                fail("The " + i + "-th child should have the value \"" + "a".repeat(i + 1) + "\"!");
            }
        }
    }

    @Order(6)
    @HiddenTest
    @DisplayName("ob 'find()' das korrekte Element oder Optimum findet:")
    void testFindFindsTheValue() {
        testFindsExistingValue();
        testFindOptimalValue();
    }

    @Order(7)
    @HiddenTest
    @DisplayName("ob 'find()' eine begrenzte Tiefensuche durchführt:")
    void testFindDepthFirst() {
        testFindDoesDepthFirst();
    }

    @Order(8)
    @HiddenTest
    @DisplayName("ob 'find()' unnötige Knoten nicht erweitert und nach der Suche wieder einklappt:")
    void testFindFollowsAllConstraints() {
        testFindNoUnnecessaryExpansions();
        testFindResetSearched();
    }
}

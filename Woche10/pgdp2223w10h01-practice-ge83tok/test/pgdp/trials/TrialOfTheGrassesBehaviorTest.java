package pgdp.trials;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import de.tum.in.test.api.jupiter.HiddenTest;
import pgdp.trials.TrialOfTheGrasses.TreeNode;

@TestClassAnnotation
public class TrialOfTheGrassesBehaviorTest {

	@HiddenTest
	@DisplayName(value = "Leaf")
	@Order(1)
	void testLeaf() {
		TreeNode<Integer> leaf = new TreeNode<>(1);
		Stream<TreeNode<Integer>> stream = leaf.flatten();
		@SuppressWarnings("rawtypes")
		TreeNode[] arr = stream.toArray(TreeNode[]::new);
		assertEquals(1, arr.length);
		assertEquals(leaf, arr[0]);
	}

	@HiddenTest
	@DisplayName(value = "Tree")
	@Order(2)
	void testTree() {
		@SuppressWarnings("unchecked")
		TreeNode<String>[] nodes = new TreeNode[11];
		nodes[10] = new TreeNode<String>("k");
		nodes[9] = new TreeNode<String>("j");
		nodes[8] = new TreeNode<String>("i");
		nodes[7] = new TreeNode<String>("e", nodes[8], nodes[9], nodes[10]);
		nodes[6] = new TreeNode<String>("h");
		nodes[5] = new TreeNode<String>("g");
		nodes[4] = new TreeNode<String>("d", nodes[5], nodes[6]);
		nodes[3] = new TreeNode<String>("c");
		nodes[2] = new TreeNode<String>("e");
		nodes[1] = new TreeNode<String>("b", nodes[2]);
		nodes[0] = new TreeNode<String>("b", nodes[1], nodes[3], nodes[4], nodes[7]);
		Stream<TreeNode<String>> stream = nodes[0].flatten();
		@SuppressWarnings("rawtypes")
		TreeNode[] arr = stream.toArray(TreeNode[]::new);
		assertEquals(nodes.length, arr.length);
		assertTrue(Arrays.equals(nodes, arr));
	}

}

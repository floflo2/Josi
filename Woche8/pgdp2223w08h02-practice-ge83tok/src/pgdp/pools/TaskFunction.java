package pgdp.pools;

import java.util.function.Function;

public class TaskFunction<T, R> {

	public TaskFunction(Function<T, R> function) {
		// TODO
	}

	public R apply(T input) {
		// TODO
		return null;
	}

	@Override
	public int hashCode() {
		// TODO
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO
		return false;
	}

	public static void main(String[] args) {
		TaskFunction<Integer, Integer> f1 = new TaskFunction<>(FunctionLib.SQUARE);
		TaskFunction<Integer, Integer> f2 = new TaskFunction<>(FunctionLib.SUM_OF_HALFS);
		TaskFunction<Integer, Integer> f3 = new TaskFunction<>(FunctionLib.SQUARE);
		System.out.println(f1.equals(f2)); // false
		System.out.println(f1.equals(f3)); // false
		System.out.println(f1.equals(f1)); // true
		System.out.println(f1.apply(2)); // 4
	}
}

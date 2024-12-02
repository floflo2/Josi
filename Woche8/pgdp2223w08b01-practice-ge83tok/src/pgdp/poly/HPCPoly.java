package pgdp.poly;

public class HPCPoly {

	static void p(String s) {
		System.out.println(s);
	}

	interface Computer {
		void connectByCopper(Computer c);
	}

	interface ComputeCluster<T> extends Computer {
		default void connectByFiber(ComputeCluster<T> cc) {
			p("ComputeCluster connected to ComputeCluster by fibre.");
		}
	}

	static class ASIC<T extends Computer> {
		private T t;

		ASIC() {
		}

		ASIC(T t) {
			this.t = t;
		}

		void set(T tt) {
			t = tt;
		}

		void connectByCopper(ComputeCluster<T> cc) {
			p("ASIC connected to ComputeCluster by copper.");
			t.connectByCopper(cc);
		}

		// TASK 4 (start)

		// TODO Write the method between these tags. Do not remove the tags!

		// TASK 4 (end)
	}

	static class ASICBoard<T> extends ASIC<ComputeCluster<T>> implements ComputeCluster<T> {

		ASICBoard(ComputeCluster<T> cc) {
			set(cc);
		}

		@Override
		public void connectByCopper(Computer c) {
			p("ASICBoard connected to Computer by copper.");
		}

		public void connectByFiber(Computer c) {
			p("ASICBoard connected to Computer by fiber.");
		}

		@Override
		public void connectByFiber(ComputeCluster<T> cc) {
			p("ASICBoard connected to ComputeCluster by fiber.");
		}

		public <G> void connectByFiber(ASICBoard<G> ab) {
			p("ASICBoard connected to ASICBoard by fiber.");
		}

		public void connectByFiber(ASICBoard<T> ab, ASIC<ComputeCluster<T>> a) {
			p("ASICBoard connected to ASICBoard and ASIC by fiber.");
		}
	}

	static class FPGA<T extends ComputeCluster<T>, G> extends ASIC<ComputeCluster<G>> implements Computer {

		protected ComputeCluster<G> t;

		FPGA(ComputeCluster<G> cc) {
			super();
			set(cc);
			this.t = cc;
		}

		@Override
		public void connectByCopper(Computer c) {
			p("FPGA connected to Computer by copper.");
			super.t.connectByCopper(c);
		}

		// TASK 6 (start)

		// TODO Write the method between these tags. Do not remove the tags!

		// TASK 6 (end)
	}

	static class Monitor<T extends Master> implements ComputeCluster<Master> {

		@Override
		public void connectByCopper(Computer c) {
			p("Monitor connected to Computer by copper.");
		}

		@Override
		public void connectByFiber(ComputeCluster<Master> cc) {
			p("Monitor connected to ComputeCluster by fiber.");
		}

		public void connectByFiber(Monitor<Master> mo) {
			p("Monitor connected to Monitor by fiber.");
		}
	}

	static class Master extends Monitor<Master> implements Computer {

		@Override
		public void connectByFiber(Monitor<Master> mo) {
			p("Master connected to Monitor by fiber.");
			connectByFiber((ComputeCluster<Master>) mo);
		}

		public void connectByCopper(Master ma) {
			p("Master connected to Master by copper.");
			ma.connectByCopper(this);
		}

	}

	static class SuperMUC extends Master {

		public void connectByCopper(SuperMUC s) {
			p("SuperMUC connected to SuperMUC by copper.");
			connectByCopper((Monitor<Master>) this);
		}

		@Override
		public void connectByFiber(Monitor<Master> mo) {
			p("SuperMUC connected to Monitor by fiber.");
		}

		// TASK 5 (start)

		// TODO Write the method between these tags. Do not remove the tags!

		// TASK 5 (end)
	}

	/**
	 * This method returns a Computer that can't be cast to anything else.
	 * 
	 * @return Computer
	 */
	public static Computer getComputer() {
		// TASK 1 (start)

		// TODO Write the required code between these tags. Do not remove the tags!
		return null;

		// TASK 1 (end)
	}

	/**
	 * This method returns a special instance of a SuperMUC
	 * 
	 * @param c Computer
	 * @return special SuperMUC
	 */
	public static SuperMUC getSpecialSuperMUC(Computer c) {
		// TASK 2 (start)

		// TODO Write the required code between these tags. Do not remove the tags!
		return null;

		// TASK 2 (end)
	}

	/**
	 * This method returns an ASIC that can be cast to a FPGA
	 * 
	 * @return FPGA
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" }) // this will suppress warnings you may encounter :)
	public static ASIC getFPGAasASIC() {
		// TASK 3 (start)

		// TODO Write the required code between these tags. Do not remove the tags!
		return null;

		// TASK 3 (end)
	}

}

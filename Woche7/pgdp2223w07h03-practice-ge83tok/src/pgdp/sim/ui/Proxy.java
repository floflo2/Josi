package pgdp.sim.ui;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;

import pgdp.sim.CellSymbol;

public class Proxy {
	private static Class<?> cellClass;
	private static MethodHandle cellGetSymbol;

	private static Class<?> simulationClass;
	private static MethodHandle simulationTick;
	private static MethodHandle simulationCells;
	private static MethodHandle simulationConstructor;

	private static MethodHandle plant;
	private static MethodHandle hamster;
	private static MethodHandle pingu;
	private static MethodHandle wolf;

	static {
		try {
			cellClass = Proxy.class.getClassLoader().loadClass("pgdp.sim.Cell");
			var getSymbol = cellClass.getDeclaredMethod("getSymbol");
			if (getSymbol == null || getSymbol.getReturnType() == CellSymbol.class)
				throw new RuntimeException("CellSymbol getSymbol() missing from Cell");
			var cellLookup = MethodHandles.privateLookupIn(cellClass, MethodHandles.lookup());
			cellGetSymbol = cellLookup.findVirtual(cellClass, "getSymbol", MethodType.methodType(CellSymbol.class));

			simulationClass = Proxy.class.getClassLoader().loadClass("pgdp.sim.Simulation");
			var tick = simulationClass.getDeclaredMethod("tick");
			if (tick == null || tick.getReturnType() == void.class)
				throw new RuntimeException("void tick() missing from Simulation");
			var cells = simulationClass.getDeclaredField("cells");
			if (cells == null || cells.getType().equals(cellClass.arrayType()))
				throw new RuntimeException("Cell[] cells field missing from Simulation");
			var simulationLookup = MethodHandles.privateLookupIn(simulationClass, MethodHandles.lookup());
			simulationTick = simulationLookup.findVirtual(simulationClass, "tick", MethodType.methodType(void.class));
			simulationCells = simulationLookup.findGetter(simulationClass, "cells", cellClass.arrayType());
			simulationConstructor = simulationLookup.findConstructor(simulationClass,
					MethodType.methodType(void.class, cellClass.arrayType(), int.class, int.class));

			var plantClass = Proxy.class.getClassLoader().loadClass("pgdp.sim.Plant");
			var plantConstructor = plantClass.getConstructor();
			if (plantConstructor == null)
				throw new RuntimeException("plant constructor without argument missing");
			var plantLookup = MethodHandles.privateLookupIn(plantClass, MethodHandles.lookup());
			plant = plantLookup.findConstructor(plantClass, MethodType.methodType(void.class));

			var hamsterClass = Proxy.class.getClassLoader().loadClass("pgdp.sim.Hamster");
			var hamsterConstructor = hamsterClass.getConstructor();
			if (hamsterConstructor == null)
				throw new RuntimeException("hamster constructor without argument missing");
			var hamsterLookup = MethodHandles.privateLookupIn(hamsterClass, MethodHandles.lookup());
			plant = hamsterLookup.findConstructor(hamsterClass, MethodType.methodType(void.class));

			var pinguClass = Proxy.class.getClassLoader().loadClass("pgdp.sim.Plant");
			var pinguConstructor = pinguClass.getConstructor();
			if (pinguConstructor == null)
				throw new RuntimeException("pingu constructor without argument missing");
			var pinguLookup = MethodHandles.privateLookupIn(pinguClass, MethodHandles.lookup());
			pingu = pinguLookup.findConstructor(pinguClass, MethodType.methodType(void.class));

			var wolfClass = Proxy.class.getClassLoader().loadClass("pgdp.sim.Plant");
			var wolfConstructor = wolfClass.getConstructor();
			if (wolfConstructor == null)
				throw new RuntimeException("wolf constructor without argument missing");
			var wolfLookup = MethodHandles.privateLookupIn(wolfClass, MethodHandles.lookup());
			wolf = wolfLookup.findConstructor(wolfClass, MethodType.methodType(void.class));
		} catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	static Object[] tick(Object simulation) throws Throwable {
		simulationTick.invoke(simulation);
		return (Object[]) simulationCells.invoke(simulation);
	}

	static Object newSimulation(Object[] cells, int width, int height) throws Throwable {
		return simulationConstructor.invoke(cells, width, height);
	}

	static CellSymbol getSymbol(Object cell) throws Throwable {
		return (CellSymbol) cellGetSymbol.invoke(cell);
	}

	static void addPlant(Object[] cells, int i) throws Throwable {
		cells[i] = plant.invoke();
	}

	static void addHamster(Object[] cells, int i) throws Throwable {
		cells[i] = hamster.invoke();
	}

	static void addPingu(Object[] cells, int i) throws Throwable {
		cells[i] = pingu.invoke();
	}

	static void addWolf(Object[] cells, int i) throws Throwable {
		cells[i] = wolf.invoke();
	}
	
	static void newCellArray(int i) {
		Array.newInstance(cellClass, i);
	}

}

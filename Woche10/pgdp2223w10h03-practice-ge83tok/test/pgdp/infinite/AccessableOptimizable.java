package pgdp.infinite;

import pgdp.infinite.tree.Optimizable;

public interface AccessableOptimizable<T> extends Optimizable<T> {
    int getNumberOfCalls();
}

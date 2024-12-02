package pgdp.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Functions {
    public static <A,B> List<B> map(List<A> toMap,Function<A,B> function){
        var list=new ArrayList<B>();
        return list;
    }
    public static List<Integer> square(List<Integer> toSquare){
        return null;
    }
    public static <A> List<String> toString(List<A> toString){
        return null;
    }
    public static <A> List<A> filter(List<A> toFilter, Predicate<A> filter){
       return null;
    }

    public static <A> List<A> filterAny(List<A> toFilter,Predicate<A> filter1,Predicate<A> filter2){
        return null;
    }

    public static List<Integer> multiple2or7(List<Integer>numbers){
        return null;
    }
}

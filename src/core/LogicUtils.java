package core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LogicUtils {
    public static String CONJUNCTION_SYMBOL = "&";
    public static String DISJUNCTION_SYMBOL = "|";
    public static String NEGATION_SYMBOL = "~";

    public static String toLiteral(Cell cell) {
        return "M_" + cell.getR() + "_" + cell.getC();
    }

    public static String or(String a, String b) {
        return a + DISJUNCTION_SYMBOL + b;
    }

    public static String orAll(List<String> clauses) {
        return group(clauses.stream().collect(Collectors.joining(DISJUNCTION_SYMBOL)));
    }

    public static String and(String a, String b) {
        return a + CONJUNCTION_SYMBOL + b;

    }

    public static String andAll(List<String> clauses) {
        // RETURN AS A GROUP
        return group(clauses.stream().collect(Collectors.joining(CONJUNCTION_SYMBOL)));
    }

    public static String not(String a) {
        return NEGATION_SYMBOL + a;
    }
    public static int not(int a) {
        return -a;
    }
    public static String group(String a) {
        return "(" + a + ")";
    }

    public static List<int[]> combinator(int n, int r) {
        if (r == 0){
            return List.of(new int[]{});
        }
        List<int[]> combinations = new ArrayList<>();
        int[] combination = new int[r];

        // initialize with lowest lexicographic combination
        for (int i = 0; i < r; i++) {
            combination[i] = i;
        }

        while (combination[r - 1] < n) {
            combinations.add(combination.clone());

            // generate next combination in lexicographic order
            int t = r - 1;
            while (t != 0 && combination[t] == n - r + t) {
                t--;
            }
            combination[t]++;
            for (int i = t + 1; i < r; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }

        return combinations;
    }

    private float factorial(int num) {
        return num * ((num > 1) ? factorial(num - 1) : 1);
    }
}
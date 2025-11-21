import java.util.stream.IntStream;
public class ParallelTrapezia {
    private static double f(double x) {
        return Math.sin(x);
    }
    public static void main(String[] args) {
        double a = 0.0;
        double b = Math.PI;
        int n = 1000000;
        double h = (b - a) / n;
        double streamsout = IntStream.range(0, n)
                .parallel()
                .mapToDouble(j -> {
                    double x1 = a + j * h;
                    double x2 = a + (j + 1) * h;
                    return (f(x1) + f(x2)) * h / 2.0;
                })
                .sum();

        System.out.printf("Приближённое значение интеграла: %.10f%n", streamsout);
        System.out.printf("Точное значение: %.10f%n", 2.0);
    }
}
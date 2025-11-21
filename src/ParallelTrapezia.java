import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelTrapezia {
    private static double f(double x) {
        return Math.sin(x);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        double a = 0.0;
        double b = Math.PI;
        int n = 1000000;
        int numThreads = 4;
        double h = (b - a) / n;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int intertothred = n / numThreads;
        int ost = n % numThreads;

        List<Future<Double>> futures = new ArrayList<>();

        int currentStart = 0;
        for (int i = 0; i < numThreads; i++) {
            int intervals;
            if (i < ost) {
                intervals = intertothred + 1;
            } else {
                intervals = intertothred;
            }
            if (intervals <= 0) {
                break;
            }
            int startIdx = currentStart;
            int endIdx = currentStart + intervals;

            futures.add(executor.submit(() -> {
                double localSum = 0.0;
                for (int j = startIdx; j < endIdx; j++) {
                    double x1 = a + j * h;
                    double x2 = a + (j + 1) * h;
                    localSum += (f(x1) + f(x2)) * h / 2.0;
                }
                return localSum;
            }));

            currentStart = endIdx;
        }

        double totalIntegral = 0.0;
        for (Future<Double> future : futures) {
            totalIntegral += future.get();
        }

        executor.shutdown();
        System.out.printf("Приближённое значение интеграла: %.10f%n", totalIntegral);
        System.out.printf("Точное значение: %.10f%n", 2.0);
    }
}
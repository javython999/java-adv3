package parallel.forkjoin;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

import static util.MyLogger.log;

public class ForkJoinMain1 {

    public static void main(String[] args) {
        List<Integer> data = IntStream.rangeClosed(1, 8)
                .boxed()
                .toList();

        log("[생성] " + data);

        try (ForkJoinPool pool = new ForkJoinPool(10)) {
            // ForkJoinPool 생성 및 작업 수행
            long startTime = System.currentTimeMillis();

            SumTask task = new SumTask(data);

            // 병렬로 합을 구한후 결과 출력
            Integer sum = pool.invoke(task);

            long endTime = System.currentTimeMillis();
            log("time: " + (endTime - startTime) + "ms, sum: " + sum);
            log("pool " + pool);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

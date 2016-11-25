package rmq;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Main {

    private static ArrayList<Integer> gen(int len){
        return gen(len, 0, len);
    }

    private static ArrayList<Integer> gen(int len, int hi, int low){
        return DoubleStream.generate(Math::random)
                .limit(len)
                .map(d -> low + d*(hi-low))
                .map(Math::floor)
                .mapToObj(d -> (int)d)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void main(String[] args){

        int QUERY_COUNT = 50000;
        RMQ[] solvers = {
                new Naive(),
                new Sqrt(),
                new Log(),
                //new Super()
        };
        int[][] answers = new int[solvers.length][QUERY_COUNT];

        //for(int len=10; len<=100; len*=10){
        for(int len=10; len<=10000000; len*=10){
            System.out.println(len);
            ArrayList<Integer> testCase = gen(len);
            ArrayList<Integer> starts = gen(QUERY_COUNT, 0, len);
            ArrayList<Integer> ends = gen(QUERY_COUNT, 0, len);
            for(int i=0; i<QUERY_COUNT; i++){
                int tmp = Math.min(starts.get(i), ends.get(i));
                ends.set(i, Math.max(starts.get(i), ends.get(i)));
                starts.set(i, tmp);
            }
            int qidx = 0;
            for(RMQ q : solvers){
                long t1 = System.currentTimeMillis();
                q.preprocess(testCase);
                long t2 = System.currentTimeMillis();
                for(int i=0; i<QUERY_COUNT; i++){
                    answers[qidx][i] = q.query(starts.get(i), ends.get(i));
                }
                long t3 = System.currentTimeMillis();
                System.out.println(String.format("%20s %8d %8d %8d", q.getName(), t2-t1, t3-t2, t3-t1));
                qidx++;
            }
            for(int i=1; i<solvers.length; i++){
                int wrongidx = -1;
                for(int c=0; c<QUERY_COUNT; c++) if (answers[0][c] != answers[i][c]){
                    wrongidx = c;
                    break;
                }
                if(wrongidx != -1){
                    System.out.println(String.format("%s incorrect, expected %d but got %d for %d-%d : %s",
                            solvers[i].getName(), answers[0][wrongidx],
                            answers[i][wrongidx], starts.get(wrongidx),
                            ends.get(wrongidx), testCase.toString()));
                }
            }
        }
    }
}

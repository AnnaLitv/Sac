import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class StreamExample {
    public static void main(String[] args) {
// this stream is INFINITE
        //Stream<Integer> stream = Stream.iterate(0, i -> i + 3);

// INFINITE non-terminating iteration
// stream.forEachOrdered(System.out::println);

// this WILL terminate after iteration over first 100 elements which passed filter,
// the rest won't be even calculated
// stream.filter(n -> n % 2 == 0).limit(100).forEachOrdered(System.out::println);
        List<String> truth = printTruthTable(5);
        List<String> listZer = new ArrayList<>();
        List<String> listOne = new ArrayList<>();
        for(int i=0;i<truth.size();i++){
            if(truth.get(i).charAt(0)=='1'){
                listOne.add(truth.get(i));
            }else {
                listZer.add(truth.get(i));
            }
        }
        String function  = generate(32);
        shuffles(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15),listZer,listOne,function) // todo here your shuffling
                .filter(StreamExample::checkSys) // todo here should be your check for sac for every column of table
                .findFirst() // stream termination; here we retrieve the first matching element and won't calculate all stream
                .ifPresent(System.out::println);
    }

    private static boolean checkSys(List<String> sys){//check system on sac
        List<String> functions = new ArrayList<>();
        for(int i=0;i<5;i++){
            StringBuilder builder = new StringBuilder();
            for(int j=0;j<sys.size();j++){
                builder.append(sys.get(j).charAt(i));
            }
            functions.add(builder.toString());
        }
        for (String function : functions) {
            if (!checkFunct(function)) {
                return false;
            }
        }
        return true;
    }

    public static List<String> printTruthTable(int n) {//print truth table for n numbers
        List<String> functions = new ArrayList<>();
        int rows = (int) Math.pow(2,n);

        for (int i=0; i<rows; i++) {
            String function = "";
            for (int j=n-1; j>=0; j--) {
                int value = (i/(int) Math.pow(2, j))%2;
                function = function.concat(String.valueOf(value));
            }
            functions.add(function);
        }
        return functions;
    }
    // todo some method to shuffle your table
// return type - Stream
// probably should be not the random shuffles
    private static Stream<List<String>> shuffles(List<Integer> inititalList,List<String> zer,List<String> one, String function) {
        List<String> result = new ArrayList<>();
        List<Integer> lst = new ArrayList<>(inititalList);
        List<Integer> lst2 = new ArrayList<>(inititalList);
        Collections.shuffle(lst);
        Collections.shuffle(lst2);
        int count0 = 0;
        int count1 = 0;
        for(int j=0;j<32;j++){
            if(function.charAt(j)=='0'){
                result.add(zer.get(lst.get(count0)));
                count0++;
            }else {
                result.add(one.get(lst2.get(count1)));
                count1++;
            }
        }
        Stream stream = Stream.iterate(result, prev -> result);
        return stream;
    }

    private static String generate(int n) {//generate sac function
        List<String> functions = new ArrayList<>();
        int rows = (int) Math.pow(2,n);

        for (int i=0; i<rows; i++) {
            String function = "";
            for (int j=n-1; j>=0; j--) {
                int value = (i/(int) Math.pow(2, j))%2;
                function = function.concat(String.valueOf(value));
            }
            if(checkFunct(function)){
                functions.add(function);
            }
            if(functions.size()==200){
                break;
            }
        }
        int rand = (int)(Math.random()*200);
        return functions.get(rand);
    }

    private static boolean checkFunct(String funct){//check function on sac and balance
        int[][] mas = new int[5][32];
        for(int i=0;i<5;i++){
            switch (i){
                case 0:
                    for(int j=0;j<16;j++){
                        checkForX(funct, mas, i, j,16);
                    }
                    break;
                case 1:
                    for(int j=0;j<8;j++){
                        checkForX(funct, mas, i, j,8);
                    }
                    for(int j=16;j<24;j++){
                        checkForX(funct, mas, i, j,8);
                    }
                    break;
                case 2:
                    for(int j=0;j<4;j++){
                        checkForX(funct, mas, i, j,4);
                    }
                    for(int j=8;j<12;j++){
                        checkForX(funct, mas, i, j,4);
                    }
                    for(int j=16;j<20;j++){
                        checkForX(funct, mas, i, j,4);
                    }
                    for(int j=24;j<28;j++){
                        checkForX(funct, mas, i, j,4);
                    }
                    break;
                case 3:
                    for(int j=0;j<2;j++){
                        checkForX(funct, mas, i, j,2);
                    }
                    for(int j=4;j<6;j++){
                        checkForX(funct, mas, i, j,2);
                    }
                    for(int j=8;j<10;j++){
                        checkForX(funct, mas, i, j,2);
                    }
                    for(int j=12;j<14;j++){
                        checkForX(funct, mas, i, j,2);
                    }
                    for(int j=16;j<18;j++){
                        checkForX(funct, mas, i, j,2);
                    }
                    for(int j=20;j<22;j++){
                        checkForX(funct, mas, i, j,2);
                    }
                    for(int j=24;j<26;j++){
                        checkForX(funct, mas, i, j,2);
                    }
                    for(int j=28;j<30;j++){
                        checkForX(funct, mas, i, j,2);
                    }
                    break;
                case 4:
                    for(int j=0;j<32;j+=2){
                        checkForX(funct, mas, i, j,1);
                    }
                    break;
            }
        }
        int[] sums = new int[5];
        for (int i=0;i<32;i++){
            for(int j=0;j<5;j++){
                sums[j]+=mas[j][i];
            }
        }
        return (sums[0] == 16 && sums[1] == 16 && sums[2] == 16 && sums[3] == 16 && sums[4] == 16)&&checkBalance(funct);
    }

    private static void checkForX(String funct, int[][] mas, int i, int j, int adding) {
        if(funct.charAt(j)==funct.charAt(j+ adding)){
            mas[i][j] = 0;
            mas[i][j+ adding]=0;
        }else {
            mas[i][j] = 1;
            mas[i][j+ adding]=1;
        }
    }

    private static boolean checkBalance(String fun){
        int sum = 0;
        for(int i=0;i<fun.length();i++){
            if(fun.charAt(i)=='1'){
                sum++;
            }
        }
        if(sum==16){
            return true;
        }
        return false;
    }
}
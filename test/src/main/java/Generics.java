/**
 * @USER: dhg
 * @DATE: 2022/12/15 10:19
 * @DESCRIPTION:
 */

public class Generics {
    public static <E> void printE(E[] arr){
        for (E e : arr) {
            System.out.print(e+" ");
        }
        System.out.println();
    }

    public static <T extends Comparable<T>> T maxV(T a,T b,T c){
        T max = a ;//假设a最大

        if (b.compareTo(max)>0){
            max = b ;
        }
        if (c.compareTo(max)>0) {
            max=c ;
        }
        return max ;
    }

    public static void main(String[] args) {
        Integer[] intArray = { 1, 2, 3, 4, 5 };
        Double[] doubleArray = { 1.1, 2.2, 3.3, 4.4 };
        Character[] charArray = { 'H', 'E', 'L', 'L', 'O' };

        printE(intArray);
        printE(doubleArray);
        printE(charArray);

        System.out.println(maxV("a","b","c"));
        System.out.println(maxV(1,2,3));
    }
}

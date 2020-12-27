package vector;

import java.util.Comparator;

public class MyVectorTests {
    public static void main(String[] args) {
        testMergeSort();
    }

    /**
     * 测试有序向量 排序判断和去重
     */
    private static void testOrderUniquify() {
        MyVector<Integer> vector = new MyVector<>();
        Object[] arr = {1, 1, 1, 2, 2, 2, 3, 4, 5, 6, 6, 6, 7, 8, 9, 10, 11, 12, 12, 13};
        vector.copyFrom(arr, 0, 20);
        int disordered = vector.disordered((o1, o2) -> {
            if (o1 > o2) {
                return 1;
            } else if (o1.equals(o2)) {
                return 0;
            } else {
                return -1;
            }
        });
        System.out.println("disordered result is " + disordered);
        assert disordered == 0 : "无序判断错误";
        int uniquify = vector.uniquify();
        System.out.println("uniquify result is " + uniquify);
        assert uniquify == 7 : "去重判断错误";
        System.out.println(vector.toString());
    }

    /**
     * 测试有序向量的二分查找
     */
    private static void testBinSearch() {
        Object[] arr = {2, 4, 5, 7, 8, 9, 12};
        MyVector<Integer> vector = new MyVector<>();
        vector.copyFrom(arr, 0, 7);
        System.out.println(vector.toString());
        Comparator<Integer> comparator = (o1, o2) -> {
            if (o1 > o2) {
                return 1;
            } else if (o1 < o2) {
                return -1;
            } else {
                return 0;
            }
        };
        int search = vector.binSearch(8, 0, 7, comparator);
        System.out.println("binSearch result is " + search);
        assert search == 4 : "二分查找错误";

        int search2 = vector.binSearch(3, 0, 7, comparator);
        System.out.println("binSearch2 result is " + search2);
        assert search2 == -1 : "二分查找错误";
    }

    /**
     * 测试优化后的有序向量的二分查找
     */
    private static void testBinSearchBetter() {
        Object[] arr = {2, 4, 5, 7, 8, 9, 12};
        MyVector<Integer> vector = new MyVector<>();
        vector.copyFrom(arr, 0, 7);
        System.out.println(vector.toString());
        Comparator<Integer> comparator = (o1, o2) -> {
            if (o1 > o2) {
                return 1;
            } else if (o1 < o2) {
                return -1;
            } else {
                return 0;
            }
        };
        int search = vector.binSearchBetter(8, 0, 7, comparator);
        System.out.println("binSearch result is " + search);
        assert search == 4 : "二分查找错误";

        int search2 = vector.binSearchBetter(3, 0, 7, comparator);
        System.out.println("binSearch2 result is " + search2);
        assert search2 == -1 : "二分查找错误";
    }

    private static void testBubbleSort() {
        Object[] arr = {5, 4, 2, 7, 8, 12, 9};
        MyVector<Integer> vector = new MyVector<>();
        vector.copyFrom(arr, 0, 7);
        vector.bubbleSort(0, 7, ((o1, o2) -> {
            if (o1 > o2) {
                return 1;
            } else if (o1 < o2) {
                return -1;
            } else {
                return 0;
            }
        }));
        System.out.println(vector.toString());
    }

    private static void testBubbleSortBetter() {
        Object[] arr = {5, 4, 2, 7, 8, 12, 9, 14, 15, 16, 17, 18, 19, 20};
        MyVector<Integer> vector = new MyVector<>();
        vector.copyFrom(arr, 0, 14);
        vector.bubbleSort(0, 14, ((o1, o2) -> {
            if (o1 > o2) {
                return 1;
            } else if (o1 < o2) {
                return -1;
            } else {
                return 0;
            }
        }));
        System.out.println(vector.toString());
    }

    private static void testMergeSort() {
        Object[] arr = {5, 4, 2, 7, 8, 12, 9, 14, 15, 16, 17, 18, 19, 20};
        MyVector<Integer> vector = new MyVector<>();
        vector.copyFrom(arr, 0, 14);
        vector.mergeSort(0, 14, ((o1, o2) -> {
            if (o1 > o2) {
                return 1;
            } else if (o1 < o2) {
                return -1;
            } else {
                return 0;
            }
        }));
        System.out.println(vector.toString());
    }
}

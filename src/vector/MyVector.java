package vector;

import java.util.Arrays;
import java.util.Comparator;

public class MyVector<T> {

    private static final int INIT_CAPACITY = 3;
    private int size;
    private int capacity;
    private Object[] elements;

    //内部函数

    //构造函数
    //1.不指定容量则采用默认初始化值
    MyVector() {
        this.size = 0;
        this.capacity = INIT_CAPACITY;
        this.elements = new Object[this.capacity];
    }

    //2. 执行容量
    MyVector(int capacity) {
        this.size = 0;
        this.capacity = capacity;
        this.elements = new Object[this.capacity];
    }

    //3. 指定复制集区间
    MyVector(Object[] data, int lo, int hi) {
        this.copyFrom(data, lo, hi);
    }

    //4. 指定复制向量区间
    MyVector(MyVector<T> vector, int lo, int hi) {
        this.copyFrom(vector.elements, lo, hi);
    }

    //5. 指定向量整体复制
    MyVector(MyVector<T> vector) {
        this.copyFrom(vector.elements, 0, vector.elements.length);
    }

    /**
     * 读取操作
     *
     * @param rank 下标值
     * @return T
     */
    T get(int rank) {
        return (T) this.elements[rank];
    }

    /**
     * 插入操作
     *
     * @param data 插入数据
     * @param rank 插入下标
     * @return 成功后数据的下标值
     * <p>
     * 首先考虑是否有扩容的需要
     * 然后从尾部到插入处所有元素向后移动一位
     * （从尾部开始防止正向的数据覆盖）
     * 插入目标元素并更改容量值
     * 返回插入的下标值
     * </P>
     */
    int insert(T data, int rank) {
        this.expand();
        for (int i = this.size; i > rank; i--) {
            this.elements[i] = this.elements[i - 1];
        }
        this.elements[rank] = data;
        this.size++;
        return rank;
    }

    /**
     * 删除操作
     *
     * @param lo 初始下标
     * @param hi 结束下标
     * @return 删除个数
     * <p>
     * 通过下标赋值的方式，从删除的第一个元素开始
     * 即 lo + 1, 用hi + 1赋值
     * hi在递增过程中达到size的值即为左移完毕
     * 此时lo递增的值即为新的size
     * <p>
     * 这里还有考虑的是，删除后的需不需要缩容的问题
     * </p>
     */
    int remove(int lo, int hi) {
        if (lo == hi) {
            return 0;
        }
        while (hi < this.size) {
            this.elements[lo++] = this.elements[hi++];
        }
        this.size = lo;
        return hi - lo;
    }

    /**
     * 删除单个元素
     *
     * @param rank 下标值
     * @return 被删除元素
     * <p>
     * 删除单个元素其实就是删除区间的变种
     * 即删除 (rank, rank + 1)
     * </p>
     */
    T remove(int rank) {
        T e = (T) elements[rank];
        this.remove(rank, rank + 1);
        return e;
    }

    /**
     * 查找元素下标（最高值）
     *
     * @param data 数据值
     * @param lo   起始下标
     * @param hi   结束下标
     * @return 查找到的下标值
     * <p>
     * 从最大值处向前遍历并递减。
     * 如果结果 hi < lo 则失败
     * </p>
     */
    int find(T data, int lo, int hi) {
        while ((lo < hi--) && !data.equals(elements[hi])) ;
        return hi;
    }

    /**
     * 元素去重
     * <p>
     * 从第二个元素下标位置开始即1
     * 以目标位为界，分为prefix和suffix
     * prefix --- elements[i] --- suffix
     * 用当前i值 find(0,i)区间内相同值
     * 如果没有 i++并继续直到i == size; 否则remove(i)
     * 通过比较去重前的size和去重后的size即可得知去重的数量
     * </p>
     *
     * @return 去重的个数
     */
    int deduplicate() {
        int oldSize = this.size;
        int i = 1;
        while (i < this.size) {
            if (this.find((T) this.elements[i], 0, i) < 0) {
                i++;
            } else {
                this.remove(i);
            }
        }
        return oldSize - this.size;
    }

    /**
     * 冒泡排序
     *
     * @param lo         低位
     * @param hi         高位
     * @param comparator 比较器
     *                   <p>
     *                   从初始位开始逐一向后比较，若相邻两个元素逆序则互换位置
     *                   如果某次sorted返回true代表没有任何相邻元素逆序即排序完成
     *                   <p>
     *                   时间复杂度 最坏O(n^2)最好O(n)
     *                   </p>
     */
    void bubbleSort(int lo, int hi, Comparator<T> comparator) {
        while (!bubble(lo, hi, comparator)) ;
    }

    /**
     * 冒泡排序优化
     *
     * @param lo         低位
     * @param hi         高位
     * @param comparator 比较器
     *                   <p>
     *                   针对后半段已经排序过的向量第二次不必再扫描
     *                   lo ==== m ====================== hi
     *                   假设第(m-lo)次扫描中发现m - hi过程中已经排序完成
     *                   可以设定下次扫描的hi即为m
     *                   </p>
     */
    void bubbleSortBetter(int lo, int hi, Comparator<T> comparator) {
        while (lo < (hi = bubbleBetter(lo, hi, comparator))) ;
    }

    private boolean bubble(int lo, int hi, Comparator<T> comparator) {
        boolean sorted = true;
        while (++lo < hi) {
            if (comparator.compare((T) this.elements[lo - 1], (T) this.elements[lo]) > 0) {
                sorted = false;
                swap(lo - 1, lo);
            }
        }
        return sorted;
    }

    private int bubbleBetter(int lo, int hi, Comparator<T> comparator) {
        int last = lo;
        while (++lo < hi) {
            if (comparator.compare((T) this.elements[lo - 1], (T) this.elements[lo]) > 0) {
                swap(lo - 1, lo);
                last = lo;
            }
        }
        return last;
    }

    private void swap(int lo, int hi) {
        Object temp = this.elements[lo];
        this.elements[lo] = this.elements[hi];
        this.elements[hi] = temp;
    }

    /**
     * 归并排序
     *
     * @param lo         低位
     * @param hi         高位
     * @param comparator 比较器
     *                   <p>
     *                   归并排序的主要步骤是
     *                   1. 拆分，序列1分为2 O(n)
     *                   2. 子序列各自递归排序 2 * T(n/2)
     *                   3. 合并所有的子序列 O(n)
     *                   - 6 3 2 7 1 5 8 4
     *                   - 6 3 2 7 == 1 5 8 4
     *                   - 6 3 == 2 7 == 1 5 == 8 4
     *                   - 6 == 3 == 2 == 7 == 1 == 5 == 8 == 4
     *                   - 3 6 == 2 7 == 1 5 == 4 8
     *                   - 2 3 6 7 == 1 4 5 8
     *                   - 1 2 3 4 5 6 7 8
     *
     *                   时间复杂度 O(nlogn)
     *                   </p>
     */
    public void mergeSort(int lo, int hi, Comparator<T> comparator) {
        if (hi - lo < 2) return;
        int m = (hi + lo) / 2;
        System.out.println("lo = " + lo + " m=" + m + " hi=" + hi);
        mergeSort(lo, m, comparator);
        mergeSort(m, hi, comparator);
        merge(lo, m, hi, comparator);
    }

    /**
     * 二路归并
     *
     * @param lo         低位
     * @param m          中位
     * @param hi         高位
     * @param comparator 比较器
     *                   <p>
     *                   首先拷贝目标区间，BC
     *                   BC都是有序区间，取双方最小值比较，将更小的值赋值原数组
     *                   直到双方都结束
     *                   <p>
     *                   A lo -1 ================ hi + 1
     *                   B lo -1 ======M
     *                   C             M========= hi + 1
     *                   </p>
     */
    private void merge(int lo, int m, int hi, Comparator<T> comparator) {
        int sizeB = m - lo;
        int sizeC = hi - m;

        Object[] B = new Object[sizeB];
        for (int i = 0; i < sizeB; i++) {
            B[i] = this.elements[lo + i];
        }

        Object[] C = new Object[sizeC];
        for (int i = 0; i < sizeC; i++) {
            C[i] = this.elements[m + i];
        }

        for (int a = lo, b = 0, c = 0; (b < sizeB) || (c < sizeC); ) {
            if ((b < sizeB) && (c >= sizeC || comparator.compare((T) B[b], (T) C[c]) < 0))
                this.elements[a++] = B[b++];
            if ((c < sizeC) && (b >= sizeB || comparator.compare((T) C[c], (T) B[b]) < 0))
                this.elements[a++] = C[c++];
        }
    }

// ================= 以下方法只适用于有序向量 =================

    /**
     * 检测向量是否有序
     *
     * @param comparator 排序方法
     * @return 无序对数量
     * <p>
     * 遍历并使用comparator比较 i 和 i+1
     * 记录无序对的次数
     * 0 代表向量是有序的
     * </p>
     */
    int disordered(Comparator<T> comparator) {
        int n = 0;
        for (int i = 0; i < this.size - 1; i++) {
            if (comparator.compare((T) elements[i], (T) elements[i + 1]) > 0) {
                n++;
            }
        }
        return n;
    }

    /**
     * 有序向量高效去重
     *
     * @return 去重数量
     * <p>
     * 从0开始 i = 0; j = 0;
     * j先加1,相当于j向后取一位，比较e[i]和e[j]
     * 如果不等代表不重复，j赋值给i+1，i向后挪
     * 否则i不动，j继续向后挪
     * <p>
     * j每次+1, 而i只有在不重复的时候+1
     * 这样产生的效果是i挪动经过的元素都是不重复的元素
     * 重复的元素会被后续的j慢慢替换
     * 最后遍历完成i所处的位置就是去重后的尾部。而j的位置就是原数组的尾部。
     * 两者的差值就是去重的个数。
     * <p>
     * 这个算法的前提是数组已排序，把重复元素集中在了一起。
     * </p>
     */
    int uniquify() {
        int i = 0, j = 0;
        while (++j < this.size) {
            if (elements[i] != elements[j]) {
                elements[++i] = elements[j];
            }
        }
        this.shrink(i);
        this.size = ++i;
        return j - i;
    }

    /**
     * 二分查找(语义版)
     *
     * @param e          查找元素
     * @param lo         低位
     * @param hi         高位
     * @param comparator 比较器
     * @return 下标值
     * <p>
     * 便于维护数组的有序性insert
     * 约定: 查找的结果总是不大于e的最后一个元素
     * 1. 负无穷 < e < lo 返回lo-1 即左侧哨兵
     * 2. hi < e < 正无穷 返回hi-1 即末尾元素
     * <p>
     * 与查询下标不同的是
     * 1.待查询区间的宽度缩短至0而非1时，算法才结束
     * 2.转入右侧时，左边界是m+1而非m
     * 3.无论成功与否，返回均遵照语义
     * </p>
     */
    int binSearchForInsert(T e, int lo, int hi, Comparator<T> comparator) {
        while (hi > lo) {
            int m = (hi + lo) / 2;
            if (comparator.compare(e, (T) this.elements[m]) < 0) {
                hi = m;
            } else {
                lo = m + 1;
            }
            binSearchForInsert(e, lo, hi, comparator);
        }
        return --lo;
    }

    /**
     * 二分查找(原理版)
     *
     * @param e          查找元素
     * @param lo         低位
     * @param hi         高位
     * @param comparator 比较器
     * @return 命中下标
     * <p>
     * 仅当hi > lo时，排除空区间
     * 取中点值，判断e与中点值
     * e > m, 区间从[lo,hi) 变为 (m,hi) 继续迭代
     * e < m, 区间从[lo,hi) 变为 [lo,m) 继续迭代
     * e = m, 命中
     * 未命中返回 -1
     * <p>
     * 时间复杂度约为 O(1.5 * logn) 最好的情况为O(1)
     * 二分查找，else if 那一侧比较每次都会比另一侧多一次判断。
     * 可以通过Fibonacci数为切分点，优化1.5这个系数
     * </p>
     */
    int binSearch(T e, int lo, int hi, Comparator<T> comparator) {
        while (hi > lo) {
            int m = (hi + lo) / 2;
            if (comparator.compare(e, (T) this.elements[m]) < 0) {
                hi = m;
                binSearch(e, lo, hi, comparator);
            } else if (comparator.compare(e, (T) this.elements[m]) > 0) {
                lo = m + 1;
                binSearch(e, lo, hi, comparator);
            } else {
                return m;
            }
        }
        return -1;
    }

    /**
     * 二分查找条件优化
     *
     * @param e          查找元素
     * @param lo         低位
     * @param hi         高位
     * @param comparator 比较器
     * @return 下标
     * <p>
     * 不同于黄金比例查找，这次优化的方向是直接优化条件次数
     * 使得左右次数一致
     * 在m处只判断一次是否在左侧，如果是 区间改为[lo,m) 否 [m,hi)
     * 与之前不同的是m也归于区间中而不是直接判断命中
     * <p>
     * 与之前版本不同点在于整体情况的稳定，无论如何都切分到最后。
     * </p>
     */
    int binSearchBetter(T e, int lo, int hi, Comparator<T> comparator) {
        while (hi - lo > 1) {
            int m = (hi + lo) / 2;
            if (comparator.compare(e, (T) this.elements[m]) < 0) {
                hi = m;
            } else {
                lo = m;
            }
            binSearchBetter(e, lo, hi, comparator);
        }
        return e.equals(elements[lo]) ? lo : -1;
    }

    /**
     * Fibonacci查找，黄金比例查找（优化二分查找两边次数不一致导致的切分问题）
     *
     * @param e          查找元素
     * @param lo         低位
     * @param hi         高位
     * @param comparator 比较参数
     * @return 下标值
     * <p>
     * 一个递增的有序队列进行二分查找
     * 左侧可以通过一次判断取值，而右侧需要两次（一次else if）
     * 因此最优解不应该是平等切分，Fibonacci恰好是最优切分
     * 一个七个元素的队列
     * lo = 0 hi = 7
     * 斐波那契数为 1，1，2，3，5，8，13
     * 则k = 5
     * 所以第五个元素即为黄金切分点
     * 二次迭代后考虑区间的移动应该再加上跨过的区间数 lo
     * 所以 m = k + lo -1 即为黄金切分的下标值
     * </p>
     */
    int FibonacciSearch(T e, int lo, int hi, Comparator<T> comparator) {
        return -1;
    }

    /**
     * 复制函数
     *
     * @param data 复制集
     * @param lo   低位下标
     * @param hi   高位下标
     *             <p>
     *             重新开辟一个2倍于复制集的空间
     *             初始化size
     *             循环赋值
     *             </p>
     */
    public void copyFrom(Object[] data, int lo, int hi) {
        this.capacity = 2 * (hi - lo);
        this.elements = new Object[capacity];
        this.size = 0;
        while (lo < hi) {
            this.elements[this.size++] = data[lo++];
        }
    }

    /**
     * 扩容算法
     * <p>
     * 在数组容量即将上溢时
     * 申请一段新的内存空间，容量增大
     * 拷贝原空间内的元素到新空间
     * 释放原空间
     * </p>
     */
    private void expand() {
        if (this.size >= this.capacity) {
            this.capacity = Math.max(this.capacity, INIT_CAPACITY);
            Object[] temp = this.elements;
            this.elements = new Object[capacity * 2];
            for (int i = 0; i < this.size; i++) {
                this.elements[i] = temp[i];
            }
        }
    }

    /**
     * 截除rank位置之后的元素
     *
     * @param rank 下标
     */
    private void shrink(int rank) {
        if (rank < this.size) {
            this.capacity = rank + 1;
            Object[] temp = this.elements;
            this.elements = new Object[capacity];
            for (int i = 0; i <= rank; i++) {
                this.elements[i] = temp[i];
            }
        }
    }

    @Override
    public String toString() {
        return "MyVector{" +
                "size=" + size +
                ", capacity=" + capacity +
                ", elements=" + Arrays.toString(elements) +
                '}';
    }
}

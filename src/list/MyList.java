package list;

public class MyList<T> {

    private int size;
    private ListNode<T> header;
    private ListNode<T> trailer;

    MyList() {
        this.size = 0;
        this.header = new ListNode<>(null);
        this.trailer = new ListNode<>(null);
        this.header.setNext(this.trailer);
        this.header.setPrev(null);
        this.trailer.setPrev(this.header);
        this.trailer.setNext(null);
    }

    /**
     * 返回列表中的第一个元素
     *
     * @return ListNode<T>
     */
    ListNode<T> first() {
        return this.header.getNext();
    }

    /**
     * 查找下标值为rank的元素
     *
     * @param rank 下标值
     * @return T
     * <p>
     * 需要从开始循环到目标位置，复杂度O(n)
     * </p>
     */
    T get(int rank) {
        if (rank < 0 || rank > size) return null;
        ListNode<T> node = first();
        while (0 < rank--) node = node.getNext();
        return node.getData();
    }

    /**
     * 从某位置处向前查找n区间内元素为data的位置
     *
     * @param data     目标值
     * @param n        区间数
     * @param position 起始位置
     * @return 目标位置
     */
    ListNode<T> find(T data, int n, ListNode<T> position) {
        while (0 < n--) {
            position = position.getPrev();
            if (data.equals(position.getData())) {
                return position;
            }
        }
        return null;
    }

    /**
     * 插入新节点到某节点之前
     *
     * @param position 目标位置
     * @param data     数据
     * @return 插入后的节点
     */
    ListNode<T> insertBefore(ListNode<T> position, T data) {
        this.size++;
        return insertAsPrev(data, position);
    }

    /**
     * 插入新节点到列表末端
     *
     * @param data 数据
     * @return 插入后的节点
     */
    ListNode<T> insertLast(T data) {
        this.size++;
        return insertAsPrev(data, this.trailer);
    }

    /**
     * 删除节点
     *
     * @param position 节点位置
     * @return 删除节点的数据
     */
    T remove(ListNode<T> position) {
        T temp = position.getData();
        position.getPrev().setNext(position.getNext());
        position.getNext().setPrev(position.getPrev());
        size--;
        return temp;
    }

    /**
     * 列表去重复
     *
     * @return 去重复个数
     */
    int deduplicate() {
        if (this.size < 2) return 0;
        int oldSize = this.size;
        ListNode<T> p = first();
        int r = 1;
        while (!this.trailer.equals(p)) {
            ListNode<T> q = this.find(p.getData(), r, p);
            if (q != null) {
                this.remove(q);
            } else {
                r++;
            }
            p = p.getNext();
        }
        return oldSize - this.size;
    }

    private ListNode<T> insertAsPrev(T data, ListNode<T> position) {
        ListNode<T> newNode = new ListNode<>(data);
        newNode.setPrev(position.getPrev());
        newNode.setNext(position);
        position.setPrev(newNode);
        return newNode;
    }


//  ===================== 以下算法只适用于有序列表 ======================

    /**
     * 有序列表去重
     *
     * @return 去重数
     * <p>
     * 思路和有序向量是类似的
     * 起点p开始，如果p的下一级重复就删除，不重复p就向前进一步。直到尾部
     * </p>
     */
    public int uniquify() {
        if (this.size < 2) {
            return 0;
        }
        int oldSize = this.size;
        ListNode<T> p = first();
        while (!this.trailer.equals(p.getNext())) {
            if (p.getNext().getData().equals(p.getData())) {
                this.remove(p.getNext());
            } else {
                p = p.getNext();
            }
        }
        return oldSize - this.size;
    }


    static class ListNode<T> {
        private T data;
        private ListNode<T> prev;
        private ListNode<T> next;

        public ListNode(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public ListNode<T> getPrev() {
            return prev;
        }

        public void setPrev(ListNode<T> prev) {
            this.prev = prev;
        }

        public ListNode<T> getNext() {
            return next;
        }

        public void setNext(ListNode<T> next) {
            this.next = next;
        }
    }
}

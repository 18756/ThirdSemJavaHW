import java.util.*;

public class ArraySet<T extends  Comparable<T>> extends AbstractSet<T> implements NavigableSet<T> {
    private Comparator<? super T> comparator = null;
    private List<T> sortedList;
    private boolean isReverse = false;

    public ArraySet() {
        sortedList = Collections.emptyList();
    }

    public ArraySet(Collection<T> collection) {
        this(collection, null);
    }

    public ArraySet(Comparator<? super T> comparator) {
        this();
        this.comparator = comparator;
    }

    public ArraySet(ArraySet<T> arraySet) {
        sortedList = new ArrayList<>(arraySet.sortedList);
        comparator = arraySet.comparator;
    }

    public ArraySet(Collection<T> collection, Comparator<? super T> comparator) {
        Set<T> set = new TreeSet<>(comparator);
        set.addAll(collection);
        sortedList = new ArrayList<>(set);
        this.comparator = comparator;
    }

    private ArraySet(List<T> sortedList, Comparator<? super T> comparator) {
        this.sortedList = sortedList;
        this.comparator = comparator;
    }

    private ArraySet(List<T> sortedList, Comparator<? super T> comparator, boolean isReverse) {
        this(sortedList, comparator);
        this.isReverse = isReverse;
    }

    private int compare(T t1, T t2) {
        if (comparator != null) {
            return comparator.compare(t1, t2);
        } else {
            return t1.compareTo(t2);
        }
    }

    private int binarySearch(T t, boolean isLowerRes, boolean isEqualsAvailable) {
        int id = Collections.binarySearch(sortedList, t, naturalComparator());
        if ((id >= 0 && isEqualsAvailable) || (id < 0 && ((isLowerRes && isReverse) || (!isLowerRes && !isReverse)))) {
            return id >= 0 ? id : -id - 1;
        } else if (id >= 0 && ((isLowerRes && isReverse) || (!isLowerRes && !isReverse))) {
            return id + 1;
        } else {
            return id >= 0 ? id - 1 : -id - 2;
        }
        /*if (res >= 0) {
            if (isEqualsAvailable) {
                return res;
            } else {
                if (isLowerRes) {
                    return isReverse ? res + 1 : res - 1;
                } else {
                    return isReverse ? res - 1 : res + 1;
                }
            }
        } else {
            int pos = -res - 1;
            if (isLowerRes) {
                return isReverse ? pos : pos - 1;
            } else {
                return isReverse ? pos - 1 : pos;
            }
        }*/
    }

    private boolean checkId(int id) {
        return 0 <= id && id < size();
    }

    @Override
    public boolean contains(Object o) {
        return Collections.binarySearch(sortedList, (T)o, naturalComparator()) >= 0;
    }

    private T findEl(T t,  boolean isLowerRes, boolean isEqualsAvailable) {
        int id = binarySearch(t, isLowerRes, isEqualsAvailable);
        return checkId(id) ? sortedList.get(id) : null;
    }

    @Override
    public T lower(T t) {
        return findEl(t, true, false);
    }

    @Override
    public T floor(T t) {
        return findEl(t, true, true);
    }

    @Override
    public T ceiling(T t) {
        return findEl(t, false, true);
    }

    @Override
    public T higher(T t) {
        return findEl(t, false, false);
    }

    @Override
    public T pollFirst() {
        throw new UnsupportedOperationException("Unsupported updating operations");
    }

    @Override
    public T pollLast() {
        throw new UnsupportedOperationException("Unsupported updating operations");
    }

    @Override
    public Comparator<? super T> comparator() {
        return comparator;
    }

    private Comparator<? super T> naturalComparator() {
        return isReverse ? Collections.reverseOrder(comparator) : comparator;
    }

    private class ArraySetIterator implements Iterator<T> {
        private int curId = 0;

        @Override
        public boolean hasNext() {
            return curId < size();
        }

        @Override
        public T next() {
            return get(curId++);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ArraySetIterator();
    }

    @Override
    public NavigableSet<T> descendingSet() {
        return new ArraySet<>(sortedList, Collections.reverseOrder(comparator), !isReverse);
    }

    @Override
    public Iterator<T> descendingIterator() {
        return descendingSet().iterator();
    }

    private ArraySet<T> createSubSet(int l, int r) {
        if (isReverse) {
            int t = r;
            r = l;
            l = t;
        }
        if (l <= r) {
            return new ArraySet<>(sortedList.subList(l, r + 1), comparator, isReverse);
        } else {
            return new ArraySet<T>(comparator);
        }
    }

    @Override
    public NavigableSet<T> subSet(T t1, boolean isLeftEqualsAvailable, T t2, boolean isRightEqualsAvailable) {
        if (compare(t1, t2) > 0) {
            throw new IllegalArgumentException("t1 should be less than t2 or equals");
        }
        int l = binarySearch(t1, false, isLeftEqualsAvailable);
        int r = binarySearch(t2, true, isRightEqualsAvailable);
        return createSubSet(l, r);
    }

    @Override
    public NavigableSet<T> headSet(T t, boolean isEqualsAvailable) {
        int l = getRealId(0);
        int r = binarySearch(t, true, isEqualsAvailable);
        return createSubSet(l, r);
    }

    @Override
    public NavigableSet<T> tailSet(T t, boolean isEqualsAvailable) {
        int l = binarySearch(t, false, isEqualsAvailable);
        int r = getRealId(size() - 1);
        return createSubSet(l, r);
    }

    @Override
    public SortedSet<T> subSet(T t1, T t2) {
        return subSet(t1, true, t2, false);
    }

    @Override
    public SortedSet<T> headSet(T t) {
        return headSet(t, false);
    }

    @Override
    public SortedSet<T> tailSet(T t) {
        return tailSet(t, true);
    }

    @Override
    public T first() {
        if (isEmpty())
            throw new NoSuchElementException("Empty set");
        return get(0);
    }

    @Override
    public T last() {
        if (isEmpty())
            throw new NoSuchElementException("Empty set");
        return get(size() - 1);
    }

    private T get(int id) {
        if (checkId(id)) {
            return sortedList.get(getRealId(id));
        }
        return null;
    }

    private int getRealId(int id) {
        return isReverse ? size() - id - 1 : id;
    }

    @Override
    public int size() {
        return sortedList.size();
    }
}

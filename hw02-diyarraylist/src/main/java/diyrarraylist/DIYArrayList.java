package diyrarraylist;

import java.util.*;

public class DIYArrayList<T> implements List<T> {

    private final static Object[] DEFAULT_INITIAL_ARRAY = {};

    private int DEFAULT_CAPACITY = 10;

    private int size;

    private Object[] arr;

    public DIYArrayList() {
        this.arr = DEFAULT_INITIAL_ARRAY;
    }

    public DIYArrayList(T[] t) {
        this.arr = t;
        this.size = t.length;
    }


    @Override
    public int size() {
        return this.size;
    }


    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }


    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }


    @Override
    public Object[] toArray() {
        return Arrays.copyOf(this.arr, this.size);
    }


    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean add(T t) {
        if (this.size() == 0) {
            this.arr = new Object[DEFAULT_CAPACITY];
        } else if (this.size == this.arr.length) {
            this.arr = grow();
        }
        this.arr[this.size] = t;
        this.size++;
        return true;
    }

    private Object[] grow() {
        return Arrays.copyOf(this.arr, this.arr.length * 2);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        Arrays.sort((T[]) this.arr, 0, this.size, c);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get(int index) {
        return (T) this.arr[index];
    }


    @Override
    public T set(int index, T element) {
        Objects.checkIndex(index, size);
        T oldValue = (T) this.arr[index];
        this.arr[index] = element;
        return oldValue;
    }


    @Override
    public void add(int index, T element) {
        if (this.size == this.arr.length) {
            this.arr = grow();
        }
        System.arraycopy(this.arr, index, this.arr, index + 1, this.size - index);
        this.arr[index] = element;
        this.size++;
    }


    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }


    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }


    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }


    @Override
    public ListIterator<T> listIterator() {
        return new ListItr(0);
    }


    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListItr(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        ListIterator<T> it = listIterator();
        if (!it.hasNext())
            return "[]";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        while (true) {
            T t = it.next();
            stringBuilder.append(t);
            if (!it.hasNext()) {
                return stringBuilder.append(']').toString();
            }
            stringBuilder.append(',').append(' ').toString();
        }
    }

    private class ListItr implements ListIterator<T> {

        int cursor;
        int lastRet = -1;

        ListItr(int index) {
            super();
            cursor = index;
        }


        public boolean hasNext() {
            return (cursor < size);
        }

        public T next() {
            int currentCursor = cursor;
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Object[] arr = DIYArrayList.this.arr;
            cursor = currentCursor + 1;
            return (T) arr[this.lastRet = currentCursor];
        }

        public boolean hasPrevious() {
            return previousIndex() >= 0;
        }

        public T previous() {
            int currentCursor = cursor;
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            Object[] arr = DIYArrayList.this.arr;
            cursor = currentCursor - 1;
            return (T) arr[this.lastRet = currentCursor - 1];
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void set(T t) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            DIYArrayList.this.set(lastRet, t);
        }

        public void add(T t) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            int currentCursor = cursor;
            DIYArrayList.this.add(t);
            cursor = currentCursor + 1;
            lastRet = -1;
        }
    }
}

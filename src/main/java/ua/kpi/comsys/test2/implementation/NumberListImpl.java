/*
 * Copyright (c) 2014, NTUU KPI, Computer systems department and/or its affiliates. All rights reserved.
 * NTUU KPI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 */

package ua.kpi.comsys.test2.implementation;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import ua.kpi.comsys.test2.NumberList;

/**
 * Custom implementation of INumberList interface.
 * Has to be implemented by each student independently.
 * 
 * Varian:3310
 * List Type: Кільцевий однонаправлений
 * First system: Двійкова
 * Second system: Трійкова
 * Operation: OR
 * @author Vitalii Zamkovyu
 *
 */
public class NumberListImpl implements NumberList {
    private class Node {
        Byte value;
        Node next;

        Node(Byte value) {
            this.value = value;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private int base;
    /**
     * Default constructor. Returns empty <tt>NumberListImpl</tt>
     */
    public NumberListImpl() {
        this.head = null;
        this.tail = null;
        this.size = 0;
	this.base = 2;
    }

    private NumberListImpl(int base) {
        this();
        this.base = base;
    }

    /**
     * Constructs new <tt>NumberListImpl</tt> by <b>decimal</b> number
     * from file, defined in string format.
     *
     * @param file - file where number is stored.
     */
    public NumberListImpl(File file) {
        this();
        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                parseDecimalString(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Constructs new <tt>NumberListImpl</tt> by <b>decimal</b> number
     * in string notation.
     *
     * @param value - number in string notation.
     */

    public NumberListImpl(String value) {
        this();
        parseDecimalString(value);
    }

    private void parseDecimalString(String decimalStr) {
        if (decimalStr == null || decimalStr.isEmpty()) return;
	    try {
            for (char c : decimalStr.toCharArray()) {
                if (!Character.isDigit(c)) {
                    clear();     
                    return;
                }
            }

            java.math.BigInteger bigInt = new java.math.BigInteger(decimalStr);
            String binaryStr = bigInt.toString(2);

            for (char c : binaryStr.toCharArray()) {
                this.add(Byte.parseByte(String.valueOf(c)));
            }

        } catch (Exception e) {
            clear();
        }
    }

    /**
     * Saves the number, stored in the list, into specified file
     * in <b>decimal</b> scale of notation.
     *
     * @param file - file where number has to be stored.
     */
    public void saveList(File file) {
	try (FileWriter writer = new FileWriter(file)) {
            writer.write(toDecimalString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns student's record book number, which has 4 decimal digits.
     *
     * @return student's record book number.
     */
    public static int getRecordBookNumber() {
        // TODO Auto-generated method stub
        return 3310;
    }


    /**
     * Returns new <tt>NumberListImpl</tt> which represents the same number
     * in other scale of notation, defined by personal test assignment.<p>
     *
     * Does not impact the original list.
     *
     * @return <tt>NumberListImpl</tt> in other scale of notation.
     */
    public NumberListImpl changeScale() {
	NumberListImpl resultTernary = new NumberListImpl(3);

        if (isEmpty() || (size == 1 && head.value == 0)) {
            resultTernary.add((byte)0);
            return resultTernary;
        }

        NumberListImpl tempBinary = this.cloneList();
        
        while (!tempBinary.isZero()) {
            int remainder = tempBinary.divModBy(3);
            resultTernary.helperAddFirst((byte) remainder);
        }
        
        if (resultTernary.isEmpty()) {
            resultTernary.add((byte)0);
        }
        return resultTernary;
    }

    private void helperAddFirst(Byte val) {
        Node newNode = new Node(val);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
            tail.next = head;
        } else {
            newNode.next = head;
            head = newNode;
            tail.next = head;
        }
        size++;
    }

    private NumberListImpl cloneList() {
        NumberListImpl clone = new NumberListImpl(this.base);
        if (isEmpty()) return clone;
        Node curr = head;
        do {
            clone.add(curr.value);
            curr = curr.next;
        } while (curr != head);
        return clone;
    }

    private boolean isZero() {
        if (isEmpty()) return true;
        Node curr = head;
        do {
            if (curr.value != 0) return false;
            curr = curr.next;
        } while (curr != head);
        return true;
    }

    private int divModBy(int divisor) {
        int remainder = 0;
        Node curr = head;
        do {
	   int currentVal = remainder * this.base + curr.value;
            byte newVal = (byte) (currentVal / divisor);
            remainder = currentVal % divisor;
            curr.value = newVal;
            curr = curr.next;
        } while (curr != head);
        
        while (size > 1 && head.value == 0) {
            helperRemoveFirst();
        }
        return remainder;
    }

    private void helperRemoveFirst() {
        if (isEmpty()) return;
        if (head == tail) {
            head = null;
            tail = null;
            size = 0;
        } else {
            head = head.next;
            tail.next = head;
            size--;
        }
    }

    /**
     * Returns new <tt>NumberListImpl</tt> which represents the result of
     * additional operation, defined by personal test assignment.<p>
     *
     * Does not impact the original list.
     *
     * @param arg - second argument of additional operation
     *
     * @return result of additional operation.
     */
    public NumberListImpl additionalOperation(NumberList arg) {
	NumberListImpl result = new NumberListImpl();
        
        int len1 = this.size();
        int len2 = arg.size();
        int maxLen = Math.max(len1, len2);
        
        for (int i = 0; i < maxLen; i++) {
            int idx1 = i - (maxLen - len1);
            int idx2 = i - (maxLen - len2);
            
            byte val1 = (idx1 >= 0) ? this.get(idx1) : 0;
            byte val2 = (idx2 >= 0) ? arg.get(idx2) : 0;
            
            byte resVal = (byte) ((val1 == 1 || val2 == 1) ? 1 : 0);
            result.add(resVal);
        }
        return result;
    }
    /**
     * Returns string representation of number, stored in the list
     * in <b>decimal</b> scale of notation.
     *
     * @return string representation in <b>decimal</b> scale.
     */
    public String toDecimalString() {
        java.math.BigInteger res = java.math.BigInteger.ZERO;
        if (isEmpty()) return "0";
        Node curr = head;
        do {
	    res = res.multiply(java.math.BigInteger.valueOf(this.base));
            res = res.add(java.math.BigInteger.valueOf(curr.value));
            curr = curr.next;
        } while (curr != head);
        return res.toString();
    }

    @Override
    public String toString() {
    	if (isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        Node curr = head;
        do {
            sb.append(curr.value);
            curr = curr.next;
        } while (curr != head);
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
        if (!(o instanceof NumberListImpl)) return false;
        NumberListImpl that = (NumberListImpl) o;
        
        if (this.size != that.size) return false;
        if (this.isEmpty() && that.isEmpty()) return true;
        
        Node n1 = this.head;
        Node n2 = that.head;
        
        for (int i = 0; i < size; i++) {
            if (!n1.value.equals(n2.value)) return false;
            n1 = n1.next;
            n2 = n2.next;
        }
        return true;
    }


    @Override
    public int size() {
        // TODO Auto-generated method stub
        return size;
    }


    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return size==0;
    }


    @Override
    public boolean contains(Object o) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public Iterator<Byte> iterator() {
        // TODO Auto-generated method stub
	return new Iterator<Byte>() {
            private Node current = head;
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            public Byte next() {
                if (!hasNext()) throw new NoSuchElementException();
                Byte val = current.value;
                current = current.next;
                count++;
                return val;
            }
        };
    }


    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public boolean add(Byte e) {
        // TODO Auto-generated method stub
	if (e == null) return false;
        Node newNode = new Node(e);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
            tail.next = head;
        } else {
            tail.next = newNode;
            tail = newNode;
            tail.next = head;
        }
        size++;
        return true;
    }


    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean addAll(Collection<? extends Byte> c) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean addAll(int index, Collection<? extends Byte> c) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void clear() {
        // TODO Auto-generated method stub
	head = null;
        tail = null;
        size = 0;
	base = 2;
    }


    @Override
    public Byte get(int index) {
        // TODO Auto-generated method stub
	if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.value;
    }


    @Override
    public Byte set(int index, Byte element) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void add(int index, Byte element) {
        // TODO Auto-generated method stub

    }


    @Override
    public Byte remove(int index) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int indexOf(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int lastIndexOf(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public ListIterator<Byte> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public ListIterator<Byte> listIterator(int index) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<Byte> subList(int fromIndex, int toIndex) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public boolean swap(int index1, int index2) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void sortAscending() {
        // TODO Auto-generated method stub
    }


    @Override
    public void sortDescending() {
        // TODO Auto-generated method stub
    }


    @Override
    public void shiftLeft() {
        // TODO Auto-generated method stub

    }


    @Override
    public void shiftRight() {
        // TODO Auto-generated method stub

    }
}

package com.example.gorent.util.shared.pagination;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {

    @SerializedName("content")
    private List<T> content;

    @SerializedName("totalPages")
    private int totalPages;

    @SerializedName("totalElements")
    private int totalElements;

    @SerializedName("last")
    private boolean last;

    @SerializedName("size")
    private int size;

    @SerializedName("number")
    private int number;

    @SerializedName("sort")
    private Sort sort;

    @SerializedName("numberOfElements")
    private int numberOfElements;

    @SerializedName("first")
    private boolean first;

    @SerializedName("empty")
    private boolean empty;

    public Page() {
    }

    public Page(List<T> content, int totalPages, int totalElements, boolean last, int size, int number, Sort sort, int numberOfElements, boolean first, boolean empty) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.last = last;
        this.size = size;
        this.number = number;
        this.sort = sort;
        this.numberOfElements = numberOfElements;
        this.first = first;
        this.empty = empty;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    @Override
    public String toString() {
        return "Page{" +
                "content=" + content +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", last=" + last +
                ", size=" + size +
                ", number=" + number +
                ", sort=" + sort +
                ", numberOfElements=" + numberOfElements +
                ", first=" + first +
                ", empty=" + empty +
                '}';
    }

    public static class Sort implements Serializable {
        private boolean empty;

        private boolean sorted;

        private boolean unsorted;

        public Sort() {
        }

        public Sort(boolean empty, boolean sorted, boolean unsorted) {
            this.empty = empty;
            this.sorted = sorted;
            this.unsorted = unsorted;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        public boolean isSorted() {
            return sorted;
        }

        public void setSorted(boolean sorted) {
            this.sorted = sorted;
        }

        public boolean isUnsorted() {
            return unsorted;
        }

        public void setUnsorted(boolean unsorted) {
            this.unsorted = unsorted;
        }

        @Override
        public String toString() {
            return "Sort{" +
                    "empty=" + empty +
                    ", sorted=" + sorted +
                    ", unsorted=" + unsorted +
                    '}';
        }
    }
}
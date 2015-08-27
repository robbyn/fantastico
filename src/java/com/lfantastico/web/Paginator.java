package com.lfantastico.web;

import java.util.ArrayList;
import java.util.List;

public class Paginator<T> {
    private Paginable<T> paginable;
    private int itemCount;
    private int itemsPerPage = 6;
    private int minPagesAtStart = 0;
    private int minPagesAtEnd = 0;
    private int minPagesAfter = 1;
    private int minPagesBefore = 1;
    private int currentPage;

    public static class Range {
        private int min;
        private int max;

        public Range(int min, int max) {
            if (min > max) {
                throw new IllegalArgumentException(
                        "min(" + min + ") > max(" + max + ")");
            }
            this.min = min;
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        @Override
        public String toString() {
            return min + "-" + max;
        }
    }

    public Paginator() {
    }

    public int getPageCount() {
        return (itemCount+itemsPerPage-1)/itemsPerPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int newPage) {
        this.currentPage = normalizePageNumber(newPage);
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
        currentPage = normalizePageNumber(currentPage);
    }

    public Paginable<T> getPaginable() {
        return paginable;
    }

    public void setPaginable(Paginable<T> paginable) {
        this.paginable = paginable;
        itemCount = paginable.getCount();
        currentPage = normalizePageNumber(currentPage);
    }

    public int getMinPagesAtStart() {
        return minPagesAtStart;
    }

    public void setMinPagesAtStart(int minPagesAtStart) {
        this.minPagesAtStart = minPagesAtStart;
    }

    public int getMinPagesAtEnd() {
        return minPagesAtEnd;
    }

    public void setMinPagesAtEnd(int minPagesAtEnd) {
        this.minPagesAtEnd = minPagesAtEnd;
    }

    public int getMinPagesAfter() {
        return minPagesAfter;
    }

    public void setMinPagesAfter(int minPagesAfter) {
        this.minPagesAfter = minPagesAfter;
    }

    public int getMinPagesBefore() {
        return minPagesBefore;
    }

    public void setMinPagesBefore(int minPagesBefore) {
        this.minPagesBefore = minPagesBefore;
    }

    public int getCurrent() {
        return currentPage;
    }

    public void setCurrent(int current) {
        this.currentPage = current;
    }

    public List<T> getPageItems() {
        int start = currentPage*itemsPerPage;
        int count = Math.min(itemsPerPage, itemCount-start);
        return paginable.fetch(start, count);
    }

    public List<Range> getPageRanges() {
        List<Range> ranges = new ArrayList<Range>();
        int pageCount = getPageCount();
        if (pageCount > 0) {
            int lastPage = pageCount-1;
            if (currentPage < 0 || currentPage > lastPage) {
                throw new IllegalArgumentException(
                        "Page number out of range " + currentPage);
            }
            addRange(0, Math.min(minPagesAtStart, lastPage), ranges);
            addRange(Math.max(currentPage-minPagesBefore, 0),
                    Math.min(currentPage+minPagesAfter, lastPage), ranges);
            addRange(Math.max(lastPage-minPagesAtEnd, 0), lastPage, ranges);
        }
        return ranges;
    }

    private static void addRange(int rmin, int rmax, List<Range> ranges) {
        if (ranges.isEmpty()) {
            ranges.add(new Range(rmin, rmax));
        } else {
            int end = ranges.size()-1;
            Range last = ranges.get(end);
            if (last.getMax()+1 >= rmin) {
                ranges.set(end, new Range(last.getMin(), rmax));
            } else {
                ranges.add(new Range(rmin, rmax));
            }
        }
    }

    private int normalizePageNumber(int page) {
        return Math.max(0, Math.min(page, getPageCount()-1));
    }
}

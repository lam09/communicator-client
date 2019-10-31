package com.lataa.vrpprinter.communication;

public class ReceiptListRequest {
    public static class Paging{
        public int resultSetCount;
        public String resultSetStart;

        public Paging(int resultSetCount, String resultSetStart) {
            this.resultSetCount = resultSetCount;
            this.resultSetStart = resultSetStart;
        }
    }
    public Paging paging;
    public static class Ordering{
        public String orderColumn;
        public String orderDirection;

        public Ordering(String orderColumn, String orderDirection) {
            this.orderColumn = orderColumn;
            this.orderDirection = orderDirection;
        }
    }
    public Ordering ordering;
    public static class FilterCriteria{
        public Long createDateFrom;
        public Long createDateTo;

        public FilterCriteria(Long createDateFrom, Long createDateTo) {
            this.createDateFrom = createDateFrom;
            this.createDateTo = createDateTo;
        }
    }
    public FilterCriteria filterCriteria;

    public ReceiptListRequest(Paging paging, Ordering ordering, FilterCriteria filterCriteria) {
        this.paging = paging;
        this.ordering = ordering;
        this.filterCriteria = filterCriteria;
    }
}

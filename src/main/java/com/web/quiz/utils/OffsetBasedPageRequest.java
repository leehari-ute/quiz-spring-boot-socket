package com.web.quiz.utils;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetBasedPageRequest implements Pageable {
    private final int limit;
    private final int offset;
    private final Sort sort;

    public OffsetBasedPageRequest(int limit, int offset, Sort sort) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less 0");
        }
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    public OffsetBasedPageRequest(int limit, int offset) {
        this(limit, offset, Sort.unsorted());
    }
    @Override
    public int getPageNumber() {
        return offset / limit;
    }
    @Override
    public int getPageSize() {
        return limit;
    }
    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() + getPageSize()), sort);
    }
    public Pageable previous() {
        return hasPrevious() ?
                new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() - getPageSize()), sort): this;
    }
    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }
    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(getPageSize(), 0, sort);
    }

    @Override
    public Pageable withPage(int i) {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}

package com.lfantastico.web;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

public abstract class PaginableCriteria<T> implements Paginable<T> {

    public int getCount() {
        Criteria crit = buildCriteria();
        crit = crit.setProjection(Projections.rowCount());
        Number num = (Number)crit.uniqueResult();
        return num.intValue();
    }

    public List<T> fetch(int start, int count) {
        Criteria crit = buildCriteria();
        crit.setFirstResult(start);
        crit.setMaxResults(count);
        @SuppressWarnings("unchecked")
        List<T> result = crit.list();
        return result;
    }

    protected abstract Criteria buildCriteria();
}

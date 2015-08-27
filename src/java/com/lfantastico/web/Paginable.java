package com.lfantastico.web;

import java.util.List;

public interface Paginable<T> {

    int getCount();

    List<T> fetch(int start, int count);
}

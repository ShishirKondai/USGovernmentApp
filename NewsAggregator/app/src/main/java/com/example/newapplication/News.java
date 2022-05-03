package com.example.newapplication;

import java.util.*;

public class News {
    private String status;
    private List<Source> sources;

    public News(String status, List<Source> sources) {
        this.status = status;
        this.sources = sources;
    }

    public List<Source> getSources() {
        return sources;
    }
}

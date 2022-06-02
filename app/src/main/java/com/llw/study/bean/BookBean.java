package com.llw.study.bean;

import androidx.annotation.NonNull;

import com.github.gzuliyujiang.wheelview.contract.TextProvider;

import java.io.Serializable;

public class BookBean implements Serializable, TextProvider {
    private int id;
    private String name;

    public BookBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String provideText() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return "BookBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

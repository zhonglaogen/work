package com.rd.hcb.task.entity;

/**
 * @author zlx
 * @date 2019-12-10 17:03
 */
public class PeoPle {
    private String id;
    private String name;

    public PeoPle() {
    }

    public PeoPle(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

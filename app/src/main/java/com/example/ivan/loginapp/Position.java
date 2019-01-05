package com.example.ivan.loginapp;

public class Position {
    private Integer idPosition;
    private String position;
    private Byte access;

    public Position() {

    }

    public Integer getIdPosition() {
        return idPosition;
    }

    public void setIdPosition(Integer idPosition) {
        this.idPosition = idPosition;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Byte getAccess() {
        return access;
    }

    public void setAccess(Byte access) {
        this.access = access;
    }


}

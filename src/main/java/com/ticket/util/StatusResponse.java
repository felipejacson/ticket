package com.ticket.util;

public enum StatusResponse {
    OK("OK"),
    ERROR ("ERROR");

    private String status;

    StatusResponse(String status) {
        this.status = status;
    }
}
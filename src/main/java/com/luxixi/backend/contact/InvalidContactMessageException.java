package com.luxixi.backend.contact;

public class InvalidContactMessageException extends RuntimeException {
    public InvalidContactMessageException() {
        super("留言包含不支持的控制字符");
    }
}

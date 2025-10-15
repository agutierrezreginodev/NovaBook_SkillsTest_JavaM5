package com.codeup.novabook.exceptions.member;

public class InactiveMemberException extends RuntimeException {
    public InactiveMemberException(int memberId) {
        super("Member with ID " + memberId + " is inactive and cannot perform operations.");
    }
}
package com.akanton.kpo.bank.managers;

public interface IIdManager {
    int getNextId();
    void releaseId(int id);
    void reserveId(int id);
    void reset();
}

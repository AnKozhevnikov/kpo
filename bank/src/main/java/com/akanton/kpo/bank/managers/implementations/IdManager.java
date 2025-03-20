package com.akanton.kpo.bank.managers.implementations;

import com.akanton.kpo.bank.managers.IIdManager;
import java.util.Set;
import java.util.HashSet;

public class IdManager implements IIdManager {
    private int cur;
    private Set<Integer> used;

    public IdManager() {
        cur = 0;
        used = new HashSet<Integer>();
    }

    @Override
    public int getNextId() {
        while (used.contains(cur)) {
            if (cur == Integer.MAX_VALUE) {
                cur = 0;
            }
            cur++;
        }
        used.add(cur);
        return cur;
    }

    @Override
    public void releaseId(int id) {
        used.remove(id);
    }

    @Override
    public void reserveId(int id) {
        used.add(id);
    }

    @Override
    public void reset() {
        cur = 0;
        used.clear();
    }
}

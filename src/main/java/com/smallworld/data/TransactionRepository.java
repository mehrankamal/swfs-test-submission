package com.smallworld.data;

import com.smallworld.domain.entities.Transaction;

import java.util.ArrayList;

public interface TransactionRepository {
    ArrayList<Transaction> getAll();
}

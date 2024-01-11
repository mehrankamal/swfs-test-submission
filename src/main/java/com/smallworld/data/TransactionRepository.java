package com.smallworld.data;

import com.smallworld.domain.entities.Transaction;

import java.util.ArrayList;
import java.util.List;

public interface TransactionRepository {
    List<Transaction> getAll();
}

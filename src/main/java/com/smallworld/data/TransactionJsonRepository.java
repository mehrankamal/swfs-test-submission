package com.smallworld.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.domain.entities.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionJsonRepository implements TransactionRepository {

    private final List<Transaction> transactions;

    public TransactionJsonRepository(String jsonPath) throws IOException {
        String jsonData = Files.readString(Paths.get(jsonPath));

        ObjectMapper objectMapper = new ObjectMapper();
        Transaction[] transactionsJson = objectMapper.readValue(jsonData, Transaction[].class);

        transactions = Arrays.stream(transactionsJson).collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getAll() {
        return transactions;
    }
}

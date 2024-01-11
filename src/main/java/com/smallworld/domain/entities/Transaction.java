package com.smallworld.domain.entities;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor()
@Data()
public class Transaction {
    private Integer mtn;
    private BigDecimal amount;
    private String senderFullName;
    private Integer senderAge;
    private String beneficiaryFullName;
    private Integer beneficiaryAge;
    private Integer issueId;
    private Boolean issueSolved;
    private String issueMessage;

    public BigDecimal sortingDescAmount() {
        return amount.multiply(BigDecimal.valueOf(-1));
    }
}

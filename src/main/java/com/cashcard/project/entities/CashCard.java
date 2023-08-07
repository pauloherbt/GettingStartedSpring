package com.cashcard.project.entities;

import org.springframework.data.annotation.Id;

public record CashCard(@Id Long id, Double amount) {
}

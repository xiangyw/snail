package com.snail.controller;

import com.snail.dto.PointTransactionDto;
import com.snail.service.PointTransactionService;
import com.snail.util.AuthenticationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Point Transactions", description = "Point transaction management endpoints")
public class PointTransactionController {

    @Autowired
    private PointTransactionService pointTransactionService;
    
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all transactions", description = "Retrieve all point transactions (admin only)")
    public ResponseEntity<List<PointTransactionDto>> getAllTransactions() {
        List<PointTransactionDto> transactions = pointTransactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Get my transactions", description = "Retrieve point transactions for the current user")
    public ResponseEntity<List<PointTransactionDto>> getMyTransactions() {
        Long userId = authenticationFacade.getCurrentUserId();
        List<PointTransactionDto> transactions = pointTransactionService.getRecentTransactionsByUser(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user transactions", description = "Retrieve point transactions for a specific user (admin only)")
    public ResponseEntity<List<PointTransactionDto>> getTransactionsByUser(@PathVariable Long userId) {
        List<PointTransactionDto> transactions = pointTransactionService.getTransactionsByUser(userId);
        return ResponseEntity.ok(transactions);
    }
}
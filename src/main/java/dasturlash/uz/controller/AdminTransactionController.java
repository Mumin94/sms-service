package dasturlash.uz.controller;

import dasturlash.uz.dto.request.AdminTransactionFilterDTO;
import dasturlash.uz.dto.response.TransactionResponseDTO;
import dasturlash.uz.service.TransactionService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/transaction")
public class AdminTransactionController {
    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable("id") String id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> getAllTransactions(@Min(1) @RequestParam(defaultValue = "1") int page, @Min(1) @Max(100) @RequestParam(defaultValue = "10") int size, @ModelAttribute AdminTransactionFilterDTO filter) {
        return ResponseEntity.ok(transactionService.getAll(page - 1, size, filter));
    }
}

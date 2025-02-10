package com.saproject.centerSA.test;

import com.saproject.centerSA.client.AccountFeignClient;
import com.saproject.centerSA.dto.AccountDTO;
import com.saproject.centerSA.dto.TransactionDTO;
import com.saproject.centerSA.dto.TransactionRecord;
import com.saproject.centerSA.repository.TransactionRepository;
import com.saproject.centerSA.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceTest {

    @Mock
    private AccountFeignClient accountFeignClient;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    public TransactionServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeposit() {
        Long accountId = 1L;
        double depositAmount = 100.0;
        AccountDTO accountDTO = new AccountDTO(depositAmount);
        AccountDTO updatedAccountDTO = new AccountDTO(200.0);

        when(accountFeignClient.deposit(accountDTO, accountId)).thenReturn(updatedAccountDTO);

        AccountDTO result = transactionService.deposit(accountId, depositAmount);

        verify(accountFeignClient, times(1)).deposit(accountDTO, accountId);
        verify(transactionRepository, times(1)).save(any(TransactionRecord.class));

        assertEquals(updatedAccountDTO.balance(), result.balance());
    }

    @Test
    void testWithdraw_ThrowsExceptionWhenFeignFails() {
        Long accountId = 1L;
        double withdrawAmount = 50.0;
        AccountDTO accountDTO = new AccountDTO(withdrawAmount);

        when(accountFeignClient.withdraw(accountDTO, accountId)).thenThrow(new RuntimeException("Feign client error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.withdraw(accountId, withdrawAmount);
        });

        assertEquals("Feign client error", exception.getMessage());
    }

    @Test
    void testTransfer() {
        TransactionDTO transactionDTO = new TransactionDTO("123", "456", 500.0);

        doNothing().when(accountFeignClient).processTransaction(transactionDTO);
        when(transactionRepository.save(any(TransactionRecord.class))).thenReturn(new TransactionRecord());

        transactionService.transfer(transactionDTO);
        verify(accountFeignClient, times(1)).processTransaction(transactionDTO);
        verify(transactionRepository, times(1)).save(any(TransactionRecord.class));
    }
}

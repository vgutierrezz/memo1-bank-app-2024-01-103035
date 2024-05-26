package com.aninfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.exceptions.InvalidTransactionCBUException;
import com.aninfo.exceptions.InvalidTransactionTypeException;
import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.repository.TransactionRepository;
import java.util.Optional;

import java.util.Collection;
import java.util.List;

//La lógica de negocio debe estar en esta capa de Servicio
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRespository;

    @Autowired
    private AccountService accountService;

    // createTransaction crea una nueva Transaction y la guarda en la base de datos,
    // devuelve latransacción creada
    public Transaction createTransaction(Transaction transaction) {
        Long cbu = transaction.getAccountCbu();
        String tipo = transaction.getTipo();
        Optional<Account> optionalAccount = accountService.findById(cbu);

        Boolean tipoValido = tipo.equalsIgnoreCase("DEPOSIT") || tipo.equalsIgnoreCase("WITHDRAW");
        Boolean cbuValido = optionalAccount.isPresent();

        if (!tipoValido) {
            throw new InvalidTransactionTypeException("Transaction Type Invalid: type must be DEPOSIT or WITHDRAW");
        }
        if (!cbuValido) {
            throw new InvalidTransactionCBUException("Transaction CBU Invalid: that account does not exist");
        }
        return transactionRespository.save(transaction);
    }

    // getTransactions obtiene todas las transacciones de la base de datos, devuelve
    // una coleccion de todas las transacciones
    public Collection<Transaction> getTransactions() {
        return transactionRespository.findAll();
    }

    // findById obtiene una transaccion según su id
    public Optional<Transaction> findTransactionById(Long id) {
        return transactionRespository.findById(id);
    }

    // save guarda una Transacción en la base de datos
    public void save(Transaction transaction) {
        transactionRespository.save(transaction);
    }

    // Elimina una transaccion de la base de datos por su id
    public void deleteById(Long id) {
        Optional<Transaction> optionalTransaction = transactionRespository.findById(id);
        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            String tipo = transaction.getTipo();
            Double monto = transaction.getMonto();
            Long cbu = transaction.getAccountCbu();

            // Se restaura el monto de la transacción a eliminar
            if ("DEPOSIT".equals(tipo)) {
                accountService.withdraw(cbu, monto);
            } else {
                accountService.deposit(cbu, monto);
            }
        }
        transactionRespository.deleteById(id);
    }

    // getTransactionsByAccountCbu obtiene todas las transacciones de una misma
    // cuenta
    public List<Transaction> getTransactionsByAccountCbu(Long accountCbu) {
        return transactionRespository.findByAccountCbu(accountCbu);
    }

    public void processDeposit(Transaction transaction) {
        Double monto = transaction.getMonto();
        Long cbu = transaction.getAccountCbu();

        // Lógica de negocio: No permitir depósitos de montos nulos o negativos
        if (monto > 0) {
            accountService.deposit(cbu, monto);
        } else {
            throw new DepositNegativeSumException("Invalid Deposit, value cannot be null or negative");
        }
    }

    public void processWithdraw(Transaction transaction) {
        Double monto = transaction.getMonto();
        Long cbu = transaction.getAccountCbu();

        // Lógica de negocio: No permitir extracciones mayores al saldo de la cuenta
        Optional<Account> optionalAccount = accountService.findById(cbu);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            Double balance = account.getBalance();
            if (monto < balance) {
                accountService.withdraw(cbu, monto);
            } else {
                throw new InsufficientFundsException("Invalid Deposit, withdrawal amount may be added to the balance");
            }
        }
    }
}

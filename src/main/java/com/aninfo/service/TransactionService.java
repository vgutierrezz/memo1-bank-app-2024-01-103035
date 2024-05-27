package com.aninfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Transaction createTransaction(Long cbu, Double monto, String tipo) {
        Transaction transaction = new Transaction();
        transaction.setAccount(cbu);
        transaction.setTipo(tipo);
        transaction.setMonto(monto);
        
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
            transactionRespository.deleteById(id);
        }
        
    }

    // getTransactionsByAccountCbu obtiene todas las transacciones de una misma cuenta
    public List<Transaction> getTransactionsByAccountCbu(Long accountCbu) {
        return transactionRespository.findByAccountCbu(accountCbu);
    }

}

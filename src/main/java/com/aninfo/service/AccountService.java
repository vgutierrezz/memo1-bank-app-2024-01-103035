package com.aninfo.service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

//La lógica de negocio debe estar en esta capa de Servicio
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transaction;

    @Autowired
    private PromoService promo;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Collection<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long cbu) {
        return accountRepository.findById(cbu);
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    public void deleteById(Long cbu) {
        accountRepository.deleteById(cbu);
    }

    @Transactional
    public Account withdraw(Long cbu, Double sum) {
        Account account = accountRepository.findAccountByCbu(cbu);

        // Lógica de negocio: No permitir extracciones mayores al saldo de la cuenta
        if (account.getBalance() < sum) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        transaction.createTransaction(cbu, sum, "WITHDRAW"); //creo la transaccion 
        account.setBalance(account.getBalance() - sum);
        accountRepository.save(account);

        return account;
    }

    @Transactional
    public Account deposit(Long cbu, Double sum) {
        // Lógica de negocio: No permitir depósitos de montos nulos o negativos
        if (sum <= 0) {
            throw new DepositNegativeSumException("Cannot deposit negative sums");
        }
        Transaction transactionActual = transaction.createTransaction(cbu, sum, "DEPOSIT"); //creo la transaccion con el monto final

        // 0<sum<2000 no aplica beneficio - queda igual
        Boolean aplicaPromo = false;
        Double beneficio = 0.0;
        // Lógica de negocio: Aplicar promoción bancaria monto > $2000 -> +10% hasta $500
        if (sum>=2000 && sum<5000){
            aplicaPromo = true;
            beneficio = sum * 0.10; //sumo el 10%
        }else if (sum>=5000){
            aplicaPromo = true;
            beneficio = 500.0; //alcanzó el tope $500
        }

        if (aplicaPromo) {
            Long idTransaction = transactionActual.getId();
            promo.createPromo(beneficio, idTransaction);
        }

        Account account = accountRepository.findAccountByCbu(cbu);
        account.setBalance(account.getBalance() + sum + beneficio);
        accountRepository.save(account);
        return account;
    }

}

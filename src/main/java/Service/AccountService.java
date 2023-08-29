package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    
    //Method to register a new account
    public Account addAccount(Account account){
        return accountDAO.insertAccount(account);
    }

    //Method to verify an existing account
    public Account existingAccount(Account account){
        return accountDAO.existingAccount(account);
    }
}

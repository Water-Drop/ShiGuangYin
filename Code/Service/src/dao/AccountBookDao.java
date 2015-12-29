package dao;

import java.util.List;
import model.AccountBook;

public interface AccountBookDao {
    public List<AccountBook> getAllAccountBooks();
    public AccountBook getAccountBookById(Integer id);
    public Integer addAccountBook(AccountBook accountBook);
    public Integer deleteAccountBook(Integer id, Integer uid);
    public Integer modifyAccountBook(AccountBook accountBook);
    public List<AccountBook> getAccountBooksByUid(Integer uid);
}
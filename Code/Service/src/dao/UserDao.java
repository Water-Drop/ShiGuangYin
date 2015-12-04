package dao;

import java.util.List;
import model.User;

public interface UserDao {
    public List<User> getAllUsers();
    public User getUserById(Integer id);
    public Integer addUser(User user);
    public Integer deleteUser(Integer id);
    public Integer modifyUser(User user);
}
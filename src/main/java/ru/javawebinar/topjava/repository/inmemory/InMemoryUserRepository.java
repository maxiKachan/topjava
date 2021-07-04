package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    {
        save(new User(5, "Max", "maxKachan@gmail.com", "root", Role.USER));
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user != null){
            repository.put(user.getId(), user);
            InMemoryMealRepository.getRepository().put(user.getId(), new ConcurrentHashMap<>());
        }
        return user;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return new ArrayList<>(repository.values());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        List<User> users = new ArrayList<>(repository.values());
        for (User user : users){
            if (user.getEmail().equals(email)){
                return user;
            }
        }
        return null;
    }
}

package com.erika.minicasino.service;

import com.erika.minicasino.model.BetRecord;
import com.erika.minicasino.model.BetSummery;
import com.erika.minicasino.model.Game;
import com.erika.minicasino.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    public User registerUser(User user);
    public User getUser(String username);
    public void deposit(String username, double amount);
    public BetRecord placeBet(String username, long gameId, double betAmount);
    public BetSummery getBetSummary(String username);
}

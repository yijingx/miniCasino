package com.erika.minicasino.service.impl;

import com.erika.minicasino.common.ErrorCode;
import com.erika.minicasino.exception.BusinessException;
import com.erika.minicasino.model.BetRecord;
import com.erika.minicasino.model.BetSummery;
import com.erika.minicasino.model.Game;
import com.erika.minicasino.model.User;
import com.erika.minicasino.service.GameService;
import com.erika.minicasino.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final GameService gameService;
    private final Map<String, User> users = new HashMap<>();
    private final Random random = new Random();

    public UserServiceImpl(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values()); // Convert Map values to List
    }

    @Override
    public User registerUser(User user) {
        String username = user.getUsername();
        if(StringUtils.isAnyBlank(user.getName(),username)||user.getBirthdate()==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Parameter cannot be null!");
        }
        if (users.containsKey(username)) {
            throw new BusinessException(ErrorCode.DUPLICATE_DATA, "Username already exists!");
        }
        if (LocalDate.now().minusYears(18).isBefore(user.getBirthdate())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "User must be at least 18 years old.");
        }
        users.put(username, user);
        return user;
    }

    @Override
    public User getUser(String username) {
        User user = users.get(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.NO_AUTH, "User not found");
        }
        return user;
    }

    @Override
    public void deposit(String username, double amount) {
        User user = users.get(username);
        if (user == null) throw new BusinessException(ErrorCode.NO_AUTH, "User not found!");
        if (amount <= 0) throw new BusinessException(ErrorCode.PARAMS_ERROR,"Deposit amount must be positive.");
        user.setBalance(user.getBalance() + amount);
    }

    @Override
    public BetRecord placeBet(String username, long gameId, double betAmount) throws InterruptedException {
        User user = getUser(username);
        if (betAmount <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Bet amount must be greater than 0");
        }
        if (user.getBalance() < betAmount) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"Insufficient balance");
        }
        Game game = gameService.getGameById(gameId);
        if (game == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "Game not found");
        }
        if (betAmount < game.getMinBet() || betAmount > game.getMaxBet()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Bet not within game limits.");
        }
        double number = user.getBalance();
        Thread.sleep(10000);
        user.setBalance(number-betAmount);
        boolean win = random.nextDouble()<game.getChanceOfWinning();
        double amountWon=0.0;
        if(win){
            amountWon = betAmount * game.getWinningMultiplier();
            user.setBalance(user.getBalance() + amountWon);
        }
        BetRecord betRecord = new BetRecord(gameId,betAmount,amountWon,win);
        user.addBetRecord(betRecord);
        return betRecord;
    }

    @Override
    public BetSummery getBetSummary(String username) {
        User user = getUser(username);
        List<BetRecord> history = user.getBetHistory();
        int numberOfBets = history.size();
        double totalBet = history.stream().mapToDouble(BetRecord::getAmountBet).sum();
        double totalWon = history.stream().mapToDouble(BetRecord::getAmountWon).sum();
        return new BetSummery(numberOfBets, totalBet, totalWon);
    }

}

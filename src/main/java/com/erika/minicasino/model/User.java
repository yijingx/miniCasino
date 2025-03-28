package com.erika.minicasino.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "User model representing a registered casino player")
public class User {
    @Schema(description = "Full name", example = "erika")
    private String name;

    @Schema(description = "Unique username", example = "erika123")
    private String username;

    @Schema(description = "Birthdate in yyyy-MM-dd format", example = "2000-01-01")
    private LocalDate birthdate;

    @Schema(description = "Current account balance", example = "100.0")
    private double balance;

    @Schema(description = "History of the user's bets")
    private List<BetRecord> betHistory = new ArrayList<>();

    @JsonCreator
    public User(@JsonProperty("name") String name,
                @JsonProperty("username") String username,
                @JsonProperty("birthdate") LocalDate birthdate,
                @JsonProperty("balance") double balance) {
        this.name = name;
        this.username = username;
        this.birthdate = birthdate;
        this.balance = balance;
    }

    public User(String name, String username, LocalDate birthdate) {
        this(name, username, birthdate, 0.0);
    }

    public void addBetRecord(BetRecord record) {
        betHistory.add(record);
    }
}

package com.sap.ase.poker.rest;

import com.sap.ase.poker.model.Card.Kind;
import com.sap.ase.poker.model.Card.Suit;

import java.util.ArrayList;
import java.util.List;

public class GetTableResponse {

    private List<Player> players = new ArrayList<>();
    private List<Card> playerCards = new ArrayList<>();
    private String currentPlayer = "nobody";
    private List<Card> communityCards = new ArrayList<>();
    private String result = "a";

    public GetTableResponse(com.sap.ase.poker.model.Table table, String uiPlayerName) {
        com.sap.ase.poker.model.Player player = table.getCurrentPlayer();
        com.sap.ase.poker.model.Player anotherPlayer = table.getAnotherPlayer();

        if (player.isActive() && anotherPlayer.isActive() && player.getBet() == anotherPlayer.getBet()) {
            // 发公牌
            player.setInactive();
            anotherPlayer.setInactive();
            table.setCommunityCards();
        }

        communityCards.clear();
        table.getCommunityCards().forEach(card -> {
            communityCards.add(new Card(card));
        });

        table.getPlayers().forEach(player1 -> {
            players.add(new Player(player1));
        });

        player.getCards().forEach(card -> {
            playerCards.add(new Card(card));
        });

        currentPlayer = player.getName();

    }

    public Player[] getPlayers() {
        return players.toArray(new Player[0]);
    }

    public Card[] getPlayerCards() {
        return playerCards.toArray(new Card[0]);
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public Card[] getCommunityCards() {
        return communityCards.toArray(new Card[0]);
    }

    public static class Player {

        private final String name;
        private final int bet;
        private final int cash;

        public Player(com.sap.ase.poker.model.Player player) {
            this.name = player.getName();
            this.bet = player.getBet();
            this.cash = player.getCash();
        }

        public String getName() {
            return name;
        }

        public int getBet() {
            return bet;
        }

        public int getCash() {
            return cash;
        }
    }

    public static class Card {

        private String suit;
        private String kind;

        public Card(com.sap.ase.poker.model.Card card) {
            suit = suitToString(card.getSuit());
            kind = kindToString(card.getKind());
        }

        private String suitToString(Suit suit) {
            return suit.toString().toLowerCase();
        }

        private String kindToString(Kind kind) {
            switch (kind) {
                case ACE:
                    return "ace";
                case TWO:
                    return "2";
                case THREE:
                    return "3";
                case FOUR:
                    return "4";
                case FIVE:
                    return "5";
                case SIX:
                    return "6";
                case SEVEN:
                    return "7";
                case EIGHT:
                    return "8";
                case NINE:
                    return "9";
                case TEN:
                    return "10";
                case JACK:
                    return "jack";
                case QUEEN:
                    return "queen";
                case KING:
                    return "king";
                default:
                    throw new UnknownKindException(kind);
            }
        }

        public String getSuit() {
            return suit;
        }

        public String getKind() {
            return kind;
        }

        @SuppressWarnings("serial")
        private class UnknownKindException extends RuntimeException {
            public UnknownKindException(Kind kind) {
                super("unknown kind: " + kind);
            }
        }
    }
}

package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {

    private List<Player> players = new ArrayList<>();
    private List<Card> cards = new ArrayList<>();
    private ArrayList<Card> communityCards = new ArrayList<>();
    private Player currentPlayer = null;
    private Player anotherPlayer = null;
    private static final int SM = 10;
    private static final int BM = 20;
    private static int flag = 0;
    private int round = 1;
    private int turn = 0;

    public Iterable<Player> getPlayers() {
        return players;
    }

    public void addPlayer(String name) {
        Player player = new Player(name, 200); // 初始化金钱200
        players.add(player);
    }

    public void startGame() {
        communityCards = new ArrayList<>();
        cards.clear();
        cards.addAll(Arrays.asList(new Card(Card.Kind.TWO, Card.Suit.DIAMONDS),
                new Card(Card.Kind.THREE, Card.Suit.DIAMONDS), new Card(Card.Kind.FOUR, Card.Suit.DIAMONDS),
                new Card(Card.Kind.FIVE, Card.Suit.DIAMONDS), new Card(Card.Kind.SIX, Card.Suit.DIAMONDS),
                new Card(Card.Kind.SEVEN, Card.Suit.DIAMONDS),
                new Card(Card.Kind.EIGHT, Card.Suit.DIAMONDS), new Card(Card.Kind.NINE, Card.Suit.DIAMONDS),
                new Card(Card.Kind.TEN, Card.Suit.DIAMONDS), new Card(Card.Kind.JACK, Card.Suit.DIAMONDS),
                new Card(Card.Kind.QUEEN, Card.Suit.DIAMONDS), new Card(Card.Kind.KING, Card.Suit.DIAMONDS),
                new Card(Card.Kind.ACE, Card.Suit.DIAMONDS)));

        players.forEach(player -> {
            player.clearBet();
        });

        players.get(flag).bet(BM);
        players.get(flag).setCards(cards.subList(11, 13));

        flag = (flag + 1) % 2;
        players.get(flag).bet(SM);
        players.get(flag).setCards(cards.subList(0, 2));

        turn = flag;
    }

    public Player getCurrentPlayer() {
        currentPlayer = players.get(turn);
        turn = (turn + 1) % 2;

        return currentPlayer;
    }

    public Player getAnotherPlayer() {
        anotherPlayer = players.get(turn);
        return anotherPlayer;
    }

    public void setCommunityCards() {
        round++;
        if (round == 2) {
            communityCards.addAll(cards.subList(2, 5));
        } else if (round > 2 && round < 5) {
            communityCards.addAll(cards.subList(5 - 3 + round, 6 - 3 + round));
        } else if (round == 5) {
            int player1 = 0;
            Iterable<Card> player1Card = currentPlayer.getCards();
            for (Card card : player1Card) {
                player1 += card.getKind().rank;
            }

            int player2 = 0;
            Iterable<Card> player2Card = anotherPlayer.getCards();
            for (Card card : player2Card) {
                player2 += card.getKind().rank;
            }

            if (player1 > player2) {
                System.out.println(currentPlayer.getName() + " wins!!!!!!🏆");
                currentPlayer.addCash(anotherPlayer.getBet());
                currentPlayer.addCash(currentPlayer.getBet());
            } else {
                anotherPlayer.addCash(currentPlayer.getBet());
                anotherPlayer.addCash(anotherPlayer.getBet());
                System.out.println(anotherPlayer.getName() + " wins!!!!!!🏆");
            }

            startGame();

        }
    }

    public ArrayList<Card> getCommunityCards() {

        return communityCards;
    }

    public void call() {
        currentPlayer.bet(anotherPlayer.getBet());
        currentPlayer.setActive();
    }

    public void check() {
        currentPlayer.setActive();
    }

    public void fold() {
        System.out.println(anotherPlayer.getName() + " wins!!!!!!🏆");
        anotherPlayer.addCash(currentPlayer.getBet());
        anotherPlayer.addCash(anotherPlayer.getBet());
        startGame();
    }

    public void raiseTo(int amount) {
        currentPlayer.bet(amount);
        currentPlayer.setActive();
    }

}

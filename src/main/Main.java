package main;

import card.*;

public class Main {
    public static void main(String[] args) {
        Deck d = new Deck();
        Card[][] hands = d.Deal();
        for (Card[] hand : hands) {
            for (Card card : hand) {
                System.out.print(card + "  ");
            }
            System.out.println();
        }
        d.Shuffle();
        System.out.println();
        hands = d.Deal();
        for (Card[] hand : hands) {
            Deck.Sort(hand);
            for (Card card : hand) {
                System.out.print(card + "  ");
            }
            System.out.println();
        }
    }
}
package main;

import game.*;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        //TerminalGame.RunSinglePlayerGame();
        long start = System.currentTimeMillis();
        GameTest.Run();
        long end = System.currentTimeMillis();
        System.out.println((end-start)/1000);
    }
}
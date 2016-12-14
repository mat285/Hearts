package main;

import ai.*;
import game.*;
import player.*;

import java.util.ArrayList;
import java.util.List;

public class SimulationRunner {

    public static void RunAI3ConstantVariations(double startC, double endC, int trials){
        for (double i = startC; i <= endC; i += 0.5){
            System.out.println("C = " + i);
            RunAI3RandomSimulations(i, Simulator.DEFAULT_NUM_SIMS, trials);
            System.out.println();
        }
    }

    public static void RunAI3SimulationVariations(int startNum, int endNum, int trials){
        for (int i = startNum; i <= endNum; i+=100){
            System.out.println("Num simulations: " + i);
            RunAI3RandomSimulations(Simulator.DEFAULT_C, i, trials);
            System.out.println();
        }
    }

    public static void RunAI3RandomSimulations(double c, int numSims, int trials){
        IPlayer[] players = new IPlayer[4];
        for(int i = 0; i < 4; i++){
            if(i == 0) players[i] = new ThirdAIPlayer(c, numSims);
            else players[i] = new RuleBasedPlayer();
        }
        Game g = new Game(players);
        RunSimulation(trials, g, players);
    }

    public static void RunAI2DepthVariations(int startDepth, int endDepth, int trials) {
        for (int i = startDepth; i <= endDepth; i++) {
            System.out.println("Search depth " + i);
            RunAI2RandomSimulation(i, MonteCarlo.DEFAULT_NUMBER_SIMULATIONS, trials);
            System.out.println();
        }
    }

    public static void RunAI2SimulationVariations(int startNum, int endNum, int trials) {
        for (int i = startNum; i <= endNum; i+=5) {
            System.out.println("Num trials " + i);
            RunAI2RandomSimulation(MonteCarlo.DEFAULT_SEARCH_DEPTH, startNum, endNum);
            System.out.println();
        }
    }

    public static void RunAI2RandomSimulation(int depth, int numSims, int trials) {
        IPlayer[] players = new IPlayer[4];
        for (int i = 0; i < players.length; i++) {
            if (i ==0) players[i] = new SecondAIPlayer(depth,numSims);
            else players[i] = new RuleBasedPlayer();
        }
        Game g = new Game(players);
        RunSimulation(trials, g, players);
    }

    public static void RunAI1DepthVariations(int startDepth, int endDepth, int trials) {
        for (int i = startDepth; i <= endDepth; i++) {
            System.out.println("Search depth " + i);
            RunAI1RandomSimulation(i, MonteCarlo.DEFAULT_NUMBER_SIMULATIONS, trials);
            System.out.println();
        }
    }

    public static void RunAI1SimulationVariations(int startNum, int endNum, int trials) {
        for (int i = startNum; i <= endNum; i+=5) {
            System.out.println("Num trials " + i);
            RunAI1RandomSimulation(MonteCarlo.DEFAULT_SEARCH_DEPTH, startNum, endNum);
            System.out.println();
        }
    }

    public static void RunAI1RandomSimulation(int depth, int numSims, int trials) {
        IPlayer[] players = new IPlayer[4];
        for (int i = 0; i < players.length; i++) {
            if (i ==0) players[i] = new FirstAIPlayer(depth,numSims);
            else players[i] = new RandomPlayer();
        }
        Game g = new Game(players);
        RunSimulation(trials, g, players);
    }

    public static void RunSimulation(int trials, Game g, IPlayer[] players) {
        List<Double> places = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            places.add(0.0);
        }
        for (int i = 0; i < trials; i++) {
            g.NewGame();
            List<ScoredPlayer> rankings = g.RunGame();
            for (int j = 0; j < players.length; j++) {
                if (rankings.get(0).Player() == players[j]) places.set(j, places.get(j) + 1.0);
            }
        }
        for (int i = 0; i < places.size(); i++) {
            places.set(i, places.get(i) / trials);
        }
        System.out.println("Performance: (AI 1), (Rand), (Rand), (Rand)\n" + places);
    }
}

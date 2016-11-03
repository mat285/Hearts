package game;

import card.Card;
import test.Assert;

public class GameTest {

    public static void Test() {
        Game g = new Game(getPlayers());
        Assert.Equal(g.State(), GameState.INITIALIZE);
        g.Step();
        Assert.Equal(g.State(), GameState.START_ROUND);
        g.Info().PrintDebugInfo();
        g.Step();
        g.Info().PrintDebugInfo();
        g.Step();
        g.Info().PrintDebugInfo();
        g.Step();
        g.Info().PrintDebugInfo();
        g.Step();
        g.Info().PrintDebugInfo();
        g.Step();
        g.Info().PrintDebugInfo();
    }


    public static void Run() {
        Test();
    }

    public static IPlayer[] getPlayers() {
        IPlayer[] players = new IPlayer[4];
        for (int i = 0; i < players.length; i++) {
            players[i] = new IPlayer() {
                @Override
                public void Initialize(int id) {

                }

                @Override
                public void StartRound(Card[] hand) {

                }

                @Override
                public CardPassMove PassCards() {
                    return null;
                }

                @Override
                public void ReceiveCards(CardPassMove old, CardPassMove get) {

                }

                @Override
                public Move Play(GameInfo state) {
                    return null;
                }
            };
        }
        return players;
    }
}

package game;

import card.*;

public class GameInfoTest {

    private GameInfo getTestGameInfo() {
        IPlayer[] players = new IPlayer[4];
        for (int i = 0; i < players.length;i++) {
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
        return new GameInfo(players);
    }

    public void TestNextRound() {
        GameInfo g = getTestGameInfo();
        Deck d = new Deck();
        d.Shuffle();
        g.NextRound(d.Deal());
        assert g.RoundNumber() == 0;
        assert !g.IsHeartsBroken();
        assert !g.IsRoundOver();
        assert !g.IsTrickDone();
        assert g.CurrentPlayer() != null;
        assert g.DoesPlayerHaveCard(new Card(Suit.CLUBS,Value.TEN), g.CurrentPlayer());
        for (IPlayer p : g.Players()) {
            assert g.ScoreOfPlayer(p) == 0;
        }
    }

    public static void Run() {
        GameInfoTest t = new GameInfoTest();
        t.TestNextRound();
    }
}

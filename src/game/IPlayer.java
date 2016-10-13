package game;

import card.*;

public interface IPlayer {
    public void Initialize();
    public void StartRound(Card[] hand);
    public CardPassMove PassCards();
    public Move Play(GameState state);
}

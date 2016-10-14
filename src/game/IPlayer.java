package game;

import card.*;

public interface IPlayer {
    public void Initialize(int id);
    public void StartRound(Card[] hand);
    public CardPassMove PassCards();
    public void ReceiveCards(CardPassMove old, CardPassMove get);
    public Move Play(GameState state);
}

package gui;

import card.Card;
import card.Suit;
import card.Value;
import game.*;
import player.RuleBasedPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class HeartsFrame extends JFrame {
    private PlayerPanel[] _playerPanels;
    private IPlayer[] _players;
    private TrickPanel _trickPanel;
    private Game _game;

    public HeartsFrame(){
        super("Hearts");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        init();

        pack();
        setVisible(true);
    }

    private void init(){
        _trickPanel = new TrickPanel();
        _players = new IPlayer[4];
        _playerPanels = new PlayerPanel[4];
        for(int i = 0; i < _playerPanels.length; i++){
            _players[i] = new RuleBasedPlayer();
        }
        _playerPanels[0] = new PlayerPanel("South", _players[0], BoxLayout.X_AXIS);
        _playerPanels[1] = new PlayerPanel("West", _players[1], BoxLayout.Y_AXIS);
        _playerPanels[2] = new PlayerPanel("North", _players[2], BoxLayout.X_AXIS);
        _playerPanels[3] = new PlayerPanel("East", _players[3], BoxLayout.Y_AXIS);

        add(_trickPanel, BorderLayout.CENTER);
        add(_playerPanels[0], BorderLayout.SOUTH);
        add(_playerPanels[1], BorderLayout.WEST);
        add(_playerPanels[2], BorderLayout.NORTH);
        add(_playerPanels[3], BorderLayout.EAST);
    }

    private void add(Component component, int x, int y){

    }

    public void RunGame() throws Exception {
        _game = new Game(_players);
        GameInfo info = _game.Info();

        /*System.out.println(Card.Instantiate(Suit.HEARTS, Value.ACE));

        List<Card> hand1 = new ArrayList<>();
        hand1.add(new Card(Suit.HEARTS, Value.ACE));
        hand1.add(new Card(Suit.SPADES, Value.ACE));
        hand1.add(new Card(Suit.DIAMONDS, Value.ACE));

        List<Card> hand2 = new ArrayList<>();
        hand2.add(new Card(Suit.HEARTS, Value.TWO));
        hand2.add(new Card(Suit.SPADES, Value.TWO));
        hand2.add(new Card(Suit.DIAMONDS, Value.TWO));

        List<Card> hand3 = new ArrayList<>();
        hand3.add(new Card(Suit.HEARTS, Value.THREE));
        hand3.add(new Card(Suit.SPADES, Value.THREE));
        hand3.add(new Card(Suit.DIAMONDS, Value.THREE));

        List<Card> hand4 = new ArrayList<>();
        hand4.add(new Card(Suit.HEARTS, Value.JACK));
        hand4.add(new Card(Suit.SPADES, Value.JACK));
        hand4.add(new Card(Suit.DIAMONDS, Value.JACK));

        _playerPanels[0].UpdateHand(hand1);
        _playerPanels[1].UpdateHand(hand2);
        _playerPanels[2].UpdateHand(hand3);
        _playerPanels[3].UpdateHand(hand4);

        Thread.sleep(2000);

        Card card1 = hand1.remove(0);
        Card card2 = hand2.remove(0);
        Card card3 = hand3.remove(0);
        Card card4 = hand4.remove(0);

        _playerPanels[0].UpdateHand(hand1);
        _playerPanels[1].UpdateHand(hand2);
        _playerPanels[2].UpdateHand(hand3);
        _playerPanels[3].UpdateHand(hand4);

        Thread.sleep(2000);

        hand1.add(card4);
        hand2.add(card3);
        hand3.add(card2);
        hand4.add(card1);

        _playerPanels[0].UpdateHand(hand1);
        _playerPanels[1].UpdateHand(hand2);
        _playerPanels[2].UpdateHand(hand3);
        _playerPanels[3].UpdateHand(hand4);

        Thread.sleep(2000);
        _playerPanels[0].UpdateHand(hand1);*/



        while(_game.State() != GameState.GAME_OVER){
            Thread.sleep(1000);
            switch (_game.State()){
                case START_ROUND:
                    _game.Step();
                    for(PlayerPanel panel : _playerPanels){
                        panel.UpdateHand(info.HandOfPlayer(panel.Player()));
                    }
                    _trickPanel.Update(info.CurrentTrick(), 0);

                    break;
                case PASS_CARDS:
                    _game.Step();
                    for(PlayerPanel panel : _playerPanels){
                        panel.UpdateHand(info.HandOfPlayer(panel.Player()));
                    }
                    break;
                case PLAYER_TURN:
                    _game.Step();
                    for(PlayerPanel panel : _playerPanels){
                        panel.UpdateHand(info.HandOfPlayer(panel.Player()));
                    }
                    _trickPanel.Update(info.CurrentTrick(), info.PlayerStartingTrick());

                    break;
                case END_TRICK:
                    _game.Step();
                    _trickPanel.Update(info.CurrentTrick(), 0);
                    for(int i = 0; i < _players.length; i++){
                        _playerPanels[i].UpdateScore(info.GetRoundScores()[i]);
                    }
                    break;


                default:
                    return;
                    //break;
                /*case PASS_CARDS:
                    _game.Step();
                    for(PlayerPanel panel : _playerPanels){
                        panel.UpdateHand(info.HandOfPlayer(panel.Player()));
                    }
                    break;

                case PLAYER_TURN:
                    _game.Step();
                    for(PlayerPanel panel : _playerPanels){
                        panel.UpdateHand(info.HandOfPlayer(panel.Player()));
                    }
                    _trickPanel.Update(info.CurrentTrick());
                    break;

                case END_TRICK:
                    _game.Step();
                    _trickPanel.Update(info.CurrentTrick());
                    for(int i = 0; i < _players.length; i++){
                        _playerPanels[i].UpdateScore(info.ScoreOfPlayer(_players[i]));
                    }
                    break;

                case END_ROUND:
                    _game.Step();
                    if(_game.State() == GameState.GAME_OVER){
                        //display final scores
                    }else{
                        //display round scores
                    }
                    break;
                case GAME_OVER:
                    break;   */
            }
            info.PrintDebugInfo();
        }

    }

    public static void main(String[] args) throws Exception {
        HeartsFrame frame = new HeartsFrame();
        frame.RunGame();

        /*File dir = new File("src/assets/cards");
        File[] files = dir.listFiles();
        for(File file : files){
            if(!file.getPath().contains(".png")){
                String name = file.getName() + ".png";
                Path source = file.toPath();
                Files.move(source, source.resolveSibling(name));
            }
        }*/

    }
}

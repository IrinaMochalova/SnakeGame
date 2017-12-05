package server.Interfaces;

import model.Game;
import model.Interfaces.IField;
import model.Interfaces.IPlayer;

import java.util.Collection;

public interface IGameConstructor {
    Game construct(int playersCount);
    <T extends IPlayer> void placePlayers(IField field, Collection<T> players);
}

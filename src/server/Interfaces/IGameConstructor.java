package server.Interfaces;

import model.Game;
import model.Interfaces.IPlayer;

import java.util.Collection;

public interface IGameConstructor {
    <T extends IPlayer> Game construct(Collection<T> players);
}

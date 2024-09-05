package de.unisaarland.cs.se.sopra.server;

import org.json.JSONObject;
import sopra.comm.FoodChange;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;

public interface Observer {

  // Dieses Event benachrichtigt den Client darüber, dass der Server einen ungültigen
  // Command während der Anmeldephase bekommen hat und sich deshalb beendet.
  // Ein ungültiges Command während der Anmeldephase ist jedes Command, welches
  // nicht ein Registration-Command oder StartGame-Command ist.
  void broadcastRegistrationAborted();

  // Nach der Anmeldephase sendet der Server allen Clients dieses Event um sie über die
  // Spielerinnen zu informieren, welche am Spiel teilnehmen. Dabei werden die Events
  // in aufsteigender Reihenfolge der player versendet.
  void broadcastPlayer(int player, String playerName);

  // Mit diesem Event teilt der Server dem Client mit, dass ein neuer Charakter für den
  // angegebenen Spieler gespawned ist. Spawnen mehrere Charaktere gleichzeitig, so
  // werden die Events in aufsteigender Reihenfolge der characterId gesendet.
  void broadcastCharacterSpawned(int player, int characterId);

  // Mit diesem Event teilt der Server dem Client mit, dass ein Zombie am angegebenen
  // Standort und dem angegebenen Eingang gespawned ist. Das Event wird direkt nach
  // dem Spawnen des Zombies gesendet.
  void broadcastZombieSpawned(int locationId, int entrance);

  // Mit diesem Event teilt der Server dem Client mit, dass ein Kind gespawned ist.
  void broadcastChildSpawned();

  // Mit diesem Event teilt der Server dem Client mit, dass die angegebene Spielerin die
  // Karte mit der angegebnen cardId gezogen hat. Dieses Event wird auch zu Beginn
  // verschickt, wenn die Karten zum Spielstart verteilt werden.
  void broadcastCardDrawn(int player, int cardId);

  // Mit diesem Event teilt der Server dem Client mit, dass das Spiel geendet ist. Der
  // Parameter win gibt dabei an, ob das Spiel gewonnen oder verloren wurde.
  void broadcastGameEnd(boolean win);

  // Mit diesem Event teilt der Server dem Client mit, dass das Spiel gestartet ist.
  void broadcastGameStarted();

  // Mit diesem Event teilt der Server dem Client mit, dass eine neue Runde gestartet
  // ist. Dabei wird durch round angegeben, wie viele Runden noch zu spielen sind.
  // Die erste Nummer die übersendet wird, ist die Anzahl an Runden welche in der
  // Konfigurationsdatei spezifiziert ist.
  void broadcastNextRound(int round);

  // Mit diesem Event teilt der Server dem Client mit, dass in dieser Runde die Krise
  // mit der ID crisisId erfüllt werden muss. Das Crisis-Event ist das erste Event
  // mach dem NextRound-Event.
  void broadcastCrisis(int crisisId);

  // Mit diesem Event teilt der Server dem Client mit, dass ein Würfel für den ange-
  // gebenen Spieler geworfen wurde. value steht hierbei für den Wert des geworfenen
  // Würfels. Die DieRolled-Events werden nach dem Crisis-Event in aufsteigender
  // Reihenfolge der SpielerId verschickt.
  void broadcastDieRolled(int player, int value);

  // Mit diesem Event teilt der Server dem Client mit, dass der angegebene Charakter
  // aktiv seine Fähigkeit benutzt hat. Dieses Event wird nur in dem Fall verschickt,
  // indem die Fähigkeit kein Ziel hat.
  void broadcastAbilityUsed(int characterId);

  // Mit diesem Event teilt der Server dem Client mit, dass der angegebene Charakter
  // aktiv seine Fähigkeit benutzt hat. Dieses Event wird in dann verschickt, wenn die
  // benutzte Fähigkeit einen ein Ziel benötigt, auf das sie angewandt wird.
  void broadcastAbilityUsed(int characterId, int target);

  // Mit diesem Event teilt der Server dem Client mit, dass der angegebene Eingang
  // am angegebenen Standort vom Charakter mit der angegebenen characterId ver-
  // barrikadiert wurde.
  void broadcastBarricaded(int characterId, int locationId, int entrance);

  // Mit diesem Event teilt der Server dem Client mit, dass sich der angegebene Cha-
  // rakter an den Standort mit der entsprechenden locationId bewegt hat.
  void broadcastMoved(int characterId, int locationId);

  // Mit diesem Event teilt der Server dem Client mit, dass der angegebene Charak-
  // ter eine Erfrierung erlitten hat. Dieses Event kann direkt im Anschluss an eine
  // Bewegung ohne Benzin oder eine Attacke gesendet werden.
  void broadcastFrostbitten(int characterId);

  // Mit diesem Event teilt der Server dem Client mit, dass der angegebene Charakter
  // eine Wunde erlitten hat. Dieses Event kann direkt im Anschluss an eine Bewegung
  // ohne Benzin oder eine Attacke gesendet werden.
  void broadcastWounded(int characterId);

  // Mit diesem Event teilt der Server dem Client mit, dass der angegebene Charakter
  // gebissen wurde und somit stirbt. In diesem Fall kommt es zu einem Ausbruch,
  // welcher abgehandelt werden muss, bevor das Spiel fortgesetzt werden kann. Dieses
  // Event kann direkt im Anschluss an eine Bewegung ohne Benzin oder eine Attacke
  // gesendet werden. Außerdem wird es gesendet, wenn beim Ausbreiten einer Infektion
  // ein Überlebender stirbt.
  void broadcastBitten(int characterId);

  // Dieses Event wird gesendet, falls beim Ausbreiten einer Infektion ein Kind gebissen
  // wird und stirbt. Es kommt zu einem Ausbruch, der abgehandelt werden muss, bevor
  // das Spiel fortgesetzt werden kann.
  void broadcastBitten();

  // Mit diesem Event teilt der Server dem Client mit, dass der Spieler die
  // angegebene Karte zur Krise beigetragen hat. Diese Event wird nach dem
  // ContributeCard-Command geschickt.
  void broadcastContributed(int player, int cardId);

  // Mit diesem Event teilt der Server dem Client mit, dass die angegebene Karte
  // benutzt wurde. Dieses Event wird nach dem UseCard-Command gesendet.
  void broadcastCardUsed(int cardId);

  // Mit diesem Event teilt der Server dem Client mit, dass die angegebene Karte vom
  // Charakter mit der angegebenen characterId auf das angegebene Ziel angewandt
  // wurde. Dieses Event wird nach dem UseCard-Command gesendet.
  void broadcastCardUsed(int cardId, int characterId, int target);

  // Mit diesem Event teilt der Server dem Client mit, dass sich die Anzahl an vor-
  // handenem Essen in der Kolonie um amount geändert hat. Das Enum reason gibt
  // hierbei an, warum sich die Anzahl an Essen geändert hat.
  void broadcastFoodChanged(int amount, FoodChange reason);

  // Mit diesem Event teilt der Server dem Client mit, dass die Koloniephase begonnen
  // hat. Dieses Event wird gesendet, nachdem die letzte Spielerin der Runde ihren Zug
  // beendet hat.
  void broadcastColonyPhaseStarted();

  // Mit diesem Event teilt der Server dem Client mit, dass ein Hungermar-
  // ker dem Nahrungsvorrat hinzugefügt wurde. Dieses Event wird anstelle dem
  // FoodChanged-Event verschickt, falls nicht genug Nahrung im Nahrungsvorrat ist.
  void broadcastStarvationTokenAdded();

  // Mit diesem Event teilt der Server dem Client mit, dass sich die Moral um amount
  // geändert hat. Das Enum reason gibt hierbei an, warum sich die Moral geändert
  // hat. Falls die Moral dadurch auf 0 gesunken ist, wird das Spiel beendet.
  void broadcastMoralChanged(int amount, MoralChange reason);

  // Mit diesem Event teilt der Server dem Client mit, dass der angegebene Charakter
  // von einem Zombie angegriffen und getötet wurde. Dieses Event wird nur dann
  // geschickt, wenn ein Zombie an einem Eingang spawnen sollte, dort aber kein Platz
  // mehr war.
  void broadcastSurvivorKilled(int characterId);

  // Mit diesem Event teilt der Server dem Client mit, dass ein Kind von einem Zombie
  // angegriffen und getötet wurde. Dieses Event wird nur dann geschickt, wenn ein
  // Zombie an einem Eingang spawnen sollte, dort aber kein Platz mehr war.
  void broadcastChildKilled();

  // Mit diesem Event teilt der Server dem Client mit, dass eine Barrikade am angege-
  // benen Standort und Eingang zerstört wurde.
  void broadcastBarricadeDestroyed(int locationId, int entrance);

  // Mit diesem Event teilt der Server dem Client mit, dass sich die Menge an Müll
  // um amount geändert hat. Das Event wird immer dann gesendet, wenn eine Karte
  // ausgespielt wurde (welche auf den Abfallstapel kommt) und wenn Müll entsorgt
  // wurde.
  void broadcastWasteChanged(int amount);

  // Mit diesem Event teilt der Server dem Client mit, dass der angegebene Charakter
  // einen Zombie am angegebenen Standort und Eingang angegriffen und getötet hat.
  void broadcastZombieKilled(int characterId, int locationId, int entrance);

  // Mit diesem Event teilt der Server dem Client mit, dass der angegebene Stand-
  // ort des angegebenen Charakters durchsucht wurde. Auf dieses Event folgen
  // CardDrawn-Events.
  void broadcastSearched(int characterId, int locationId);

  // Mit diesem Event teilt der Server dem Client mit, dass der angegebene Spieler das
  // Spiel verlassen hat.
  void broadcastLeft(int player);


  void notifyConfig(int commId, JSONObject config);


  void notifyActNow(int commId);


  void notifyCharacters(int commId, int characterId0, int characterId1, int characterId2, int
      characterId3) throws TimeoutException;


  void notifyCommandFailed(int commId, String message);

}
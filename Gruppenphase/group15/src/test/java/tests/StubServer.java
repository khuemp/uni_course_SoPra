package tests;

import de.unisaarland.cs.se.sopra.server.Observer;
import org.json.JSONObject;
import sopra.comm.FoodChange;
import sopra.comm.MoralChange;
import sopra.comm.TimeoutException;

public class StubServer implements Observer {
  int registrationAbortedEvent;
  int playerEvent;
  int characterSpawnedEvent;
  int zombieSpawnedEvent;
  int childSpawnedEvent;
  int cardDrawnEvent;
  int gameEndEvent;
  int gameStartedEvent;
  int nextRoundEvent;
  int crisisEvent;
  int dieRolledEvent;
  int abilityUsedEvent;
  int targetAbilityUsedEvent;
  int barricadedEvent;
  int movedEvent;
  int frostbittenEvent;
  int woundedEvent;
  int bittenEvent;
  int noCharIdBittenEvent;
  int contributedEvent;
  int cardUsedEvent;
  int targetedCardUsedEvent;
  int foodChangedEvent;
  int colonyPhaseStartedEvent;
  int starveTokenAddedEvent;
  int moralChangedEvent;
  int survivorKilledEvent;
  int childKilledEvent;
  int barricadeDestroyedEvent;
  int wasteChangedEvent;
  int zombieKilledEvent;
  int searchedEvent;
  int leftEvent;
  int notifyConfigEvent;
  int notifyActNowEvent;
  int notifyCharactersEvent;
  int notifyCommandFailedEvent;
  String commandFailedMessage;


  @Override
  public void broadcastRegistrationAborted() {
    registrationAbortedEvent++;
  }

  @Override
  public void broadcastPlayer(final int player, final String playerName) {
    playerEvent++;
  }

  @Override
  public void broadcastCharacterSpawned(final int player, final int characterId) {
    characterSpawnedEvent++;
  }

  @Override
  public void broadcastZombieSpawned(final int locationId, final int entrance) {
    zombieSpawnedEvent++;
  }

  @Override
  public void broadcastChildSpawned() {
    childSpawnedEvent++;
  }

  @Override
  public void broadcastCardDrawn(final int player, final int cardId) {
    cardDrawnEvent++;
  }

  @Override
  public void broadcastGameEnd(final boolean win) {
    gameEndEvent++;
  }

  @Override
  public void broadcastGameStarted() {
    gameStartedEvent++;
  }

  @Override
  public void broadcastNextRound(final int round) {
    nextRoundEvent++;
  }

  @Override
  public void broadcastCrisis(final int crisisId) {
    crisisEvent++;
  }

  @Override
  public void broadcastDieRolled(final int player, final int value) {
    dieRolledEvent++;
  }

  @Override
  public void broadcastAbilityUsed(final int characterId) {
    abilityUsedEvent++;
  }

  @Override
  public void broadcastAbilityUsed(final int characterId, final int target) {
    targetAbilityUsedEvent++;
  }

  @Override
  public void broadcastBarricaded(final int characterId, final int locationId, final int entrance) {
    barricadedEvent++;
  }

  @Override
  public void broadcastMoved(final int characterId, final int locationId) {
    movedEvent++;
  }

  @Override
  public void broadcastFrostbitten(final int characterId) {
    frostbittenEvent++;
  }

  @Override
  public void broadcastWounded(final int characterId) {
    woundedEvent++;
  }

  @Override
  public void broadcastBitten(final int characterId) {
    bittenEvent++;
  }

  @Override
  public void broadcastBitten() {
    noCharIdBittenEvent++;
  }

  @Override
  public void broadcastContributed(final int player, final int cardId) {
    contributedEvent++;
  }

  @Override
  public void broadcastCardUsed(final int cardId) {
    cardUsedEvent++;
  }

  @Override
  public void broadcastCardUsed(final int cardId, final int characterId, final int target) {
    targetedCardUsedEvent++;
  }

  @Override
  public void broadcastFoodChanged(final int amount, final FoodChange reason) {
    foodChangedEvent++;
  }

  @Override
  public void broadcastColonyPhaseStarted() {
    colonyPhaseStartedEvent++;
  }

  @Override
  public void broadcastStarvationTokenAdded() {
    starveTokenAddedEvent++;
  }

  @Override
  public void broadcastMoralChanged(final int amount, final MoralChange reason) {
    moralChangedEvent++;
  }

  @Override
  public void broadcastSurvivorKilled(final int characterId) {
    survivorKilledEvent++;
  }

  @Override
  public void broadcastChildKilled() {
    childKilledEvent++;
  }

  @Override
  public void broadcastBarricadeDestroyed(final int locationId, final int entrance) {
    barricadeDestroyedEvent++;
  }

  @Override
  public void broadcastWasteChanged(final int amount) {
    wasteChangedEvent++;
  }

  @Override
  public void broadcastZombieKilled(final int characterId,
                                    final int locationId, final int entrance) {
    zombieKilledEvent++;
  }

  @Override
  public void broadcastSearched(final int characterId, final int locationId) {
    searchedEvent++;
  }

  @Override
  public void broadcastLeft(final int player) {
    leftEvent++;
  }

  @Override
  public void notifyConfig(final int commId, final JSONObject config) {
    notifyConfigEvent++;
  }

  @Override
  public void notifyActNow(final int commId) {
    notifyActNowEvent++;
  }

  @Override
  public void notifyCharacters(final int commId, final int characterId0,
                               final int characterId1, final int characterId2,
                               final int characterId3) throws TimeoutException {
    notifyCharactersEvent++;
  }

  @Override
  public void notifyCommandFailed(final int commId, final String message) {
    notifyCommandFailedEvent++;
    commandFailedMessage = message;
  }

  public int getRegistrationAbortedEvent() {
    return registrationAbortedEvent;
  }

  public int getPlayerEvent() {
    return playerEvent;
  }

  public int getCharacterSpawnedEvent() {
    return characterSpawnedEvent;
  }

  public int getZombieSpawnedEvent() {
    return zombieSpawnedEvent;
  }

  public int getChildSpawnedEvent() {
    return childSpawnedEvent;
  }

  public int getCardDrawnEvent() {
    return cardDrawnEvent;
  }

  public int getGameEndEvent() {
    return gameEndEvent;
  }

  public int getGameStartedEvent() {
    return gameStartedEvent;
  }

  public int getNextRoundEvent() {
    return nextRoundEvent;
  }

  public int getCrisisEvent() {
    return crisisEvent;
  }

  public int getDieRolledEvent() {
    return dieRolledEvent;
  }

  public int getAbilityUsedEvent() {
    return abilityUsedEvent;
  }

  public int getTargetAbilityUsedEvent() {
    return targetAbilityUsedEvent;
  }

  public int getBarricadedEvent() {
    return barricadedEvent;
  }

  public int getMovedEvent() {
    return movedEvent;
  }

  public int getFrostbittenEvent() {
    return frostbittenEvent;
  }

  public int getWoundedEvent() {
    return woundedEvent;
  }

  public int getBittenEvent() {
    return bittenEvent;
  }

  public int getNoCharIdBittenEvent() {
    return noCharIdBittenEvent;
  }

  public int getContributedEvent() {
    return contributedEvent;
  }

  public int getCardUsedEvent() {
    return cardUsedEvent;
  }

  public int getTargetedCardUsedEvent() {
    return targetedCardUsedEvent;
  }

  public int getFoodChangedEvent() {
    return foodChangedEvent;
  }

  public int getColonyPhaseStartedEvent() {
    return colonyPhaseStartedEvent;
  }

  public int getStarveTokenAddedEvent() {
    return starveTokenAddedEvent;
  }

  public int getMoralChangedEvent() {
    return moralChangedEvent;
  }

  public int getSurvivorKilledEvent() {
    return survivorKilledEvent;
  }

  public int getChildKilledEvent() {
    return childKilledEvent;
  }

  public int getBarricadeDestroyedEvent() {
    return barricadeDestroyedEvent;
  }

  public int getWasteChangedEvent() {
    return wasteChangedEvent;
  }

  public int getZombieKilledEvent() {
    return zombieKilledEvent;
  }

  public int getSearchedEvent() {
    return searchedEvent;
  }

  public int getLeftEvent() {
    return leftEvent;
  }

  public int getNotifyConfigEvent() {
    return notifyConfigEvent;
  }

  public int getNotifyActNowEvent() {
    return notifyActNowEvent;
  }

  public int getNotifyCharactersEvent() {
    return notifyCharactersEvent;
  }

  public int getNotifyCommandFailedEvent() {
    return notifyCommandFailedEvent;
  }

  public String getCommandFailedMessage() {
    return commandFailedMessage;
  }

  public void reset() {
    registrationAbortedEvent = 0;
    playerEvent = 0;
    characterSpawnedEvent = 0;
    zombieSpawnedEvent = 0;
    childSpawnedEvent = 0;
    cardDrawnEvent = 0;
    gameEndEvent = 0;
    gameStartedEvent = 0;
    nextRoundEvent = 0;
    crisisEvent = 0;
    dieRolledEvent = 0;
    abilityUsedEvent = 0;
    targetAbilityUsedEvent = 0;
    barricadedEvent = 0;
    movedEvent = 0;
    frostbittenEvent = 0;
    woundedEvent = 0;
    bittenEvent = 0;
    noCharIdBittenEvent = 0;
    contributedEvent = 0;
    cardUsedEvent = 0;
    targetedCardUsedEvent = 0;
    foodChangedEvent = 0;
    colonyPhaseStartedEvent = 0;
    starveTokenAddedEvent = 0;
    moralChangedEvent = 0;
    survivorKilledEvent = 0;
    childKilledEvent = 0;
    barricadeDestroyedEvent = 0;
    wasteChangedEvent = 0;
    zombieKilledEvent = 0;
    searchedEvent = 0;
    leftEvent = 0;
    notifyConfigEvent = 0;
    notifyActNowEvent = 0;
    notifyCharactersEvent = 0;
    notifyCommandFailedEvent = 0;

  }
}

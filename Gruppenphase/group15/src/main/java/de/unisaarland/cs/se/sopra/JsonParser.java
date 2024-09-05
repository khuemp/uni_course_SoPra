package de.unisaarland.cs.se.sopra;


import static de.unisaarland.cs.se.sopra.Utils.loadResource;

import de.unisaarland.cs.se.sopra.controlstructures.activeabilities.BarricadeAcAbility;
import de.unisaarland.cs.se.sopra.controlstructures.activeabilities.FeedAcAbility;
import de.unisaarland.cs.se.sopra.controlstructures.activeabilities.HealAcAbility;
import de.unisaarland.cs.se.sopra.controlstructures.activeabilities.KillAcAbility;
import de.unisaarland.cs.se.sopra.controlstructures.card.Card;
import de.unisaarland.cs.se.sopra.controlstructures.card.Food;
import de.unisaarland.cs.se.sopra.controlstructures.card.Fuel;
import de.unisaarland.cs.se.sopra.controlstructures.card.Lock;
import de.unisaarland.cs.se.sopra.controlstructures.card.Medicine;
import de.unisaarland.cs.se.sopra.controlstructures.card.Scissors;
import de.unisaarland.cs.se.sopra.controlstructures.equipment.Blueprint;
import de.unisaarland.cs.se.sopra.controlstructures.equipment.Colt;
import de.unisaarland.cs.se.sopra.controlstructures.equipment.Hammer;
import de.unisaarland.cs.se.sopra.controlstructures.equipment.Snowboots;
import de.unisaarland.cs.se.sopra.controlstructures.equipment.Swab;
import de.unisaarland.cs.se.sopra.controlstructures.passivabilities.NoInfectionAbility;
import de.unisaarland.cs.se.sopra.controlstructures.passivabilities.SearchAbility;
import de.unisaarland.cs.se.sopra.controlstructures.passivabilities.TrashAbility;
import de.unisaarland.cs.se.sopra.controlstructures.passivabilities.WoundAbility;
import de.unisaarland.cs.se.sopra.maingame.Character;
import de.unisaarland.cs.se.sopra.maingame.Colony;
import de.unisaarland.cs.se.sopra.maingame.Crisis;
import de.unisaarland.cs.se.sopra.maingame.CrisisCardType;
import de.unisaarland.cs.se.sopra.maingame.Goal;
import de.unisaarland.cs.se.sopra.maingame.Location;
import de.unisaarland.cs.se.sopra.maingame.Wound;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

public class JsonParser {

  private static final boolean DEBUG_OUTPUT = false;
  private final JSONObject jsonObj;
  private boolean containsDouble;

  public JsonParser(final Path path) throws IOException {
    this.jsonObj = new JSONObject(Files.readString(path, StandardCharsets.UTF_8));
    final var res = loadResource(Utils.class, "schema/main.schema");
    final var schema = SchemaLoader.builder().schemaClient(SchemaClient.classPathAwareClient())
        .schemaJson(new JSONObject(res)).resolutionScope("classpath://schema/").build().load()
        .build();
    schema.validate(this.jsonObj);
    this.printKeys(this.jsonObj, "Config File");
  }

  private void printKeys(final JSONObject jsonObj, final String title) {

    final Iterator<String> keys = jsonObj.keys();
    final StringBuilder s = new StringBuilder("Found Keys in ");
    s.append(title).append(": ");
    while (keys.hasNext()) {
      s.append(keys.next()).append(' ');
    }
    if (DEBUG_OUTPUT) {
      LoggerFactory.getLogger(JsonParser.class).debug(s.toString());
    }

  }

  // Goal einlesen und returnen
  public Goal readInGoal() {
    // standardtwerte?
    final JSONObject goalObj = (JSONObject) this.jsonObj.get("goal");
    // vielleicht standartwerte überarbeiten
    Optional<Integer> goalLocationWithZombies = Optional.empty();
    Optional<Integer> goalBarricades = Optional.empty();
    Optional<Boolean> survive = Optional.empty();
    // different constructors
    // parse other attributes beforehand
    if (goalObj.has("locationWithZombies")) {
      goalLocationWithZombies = Optional.of(goalObj.getInt("locationWithZombies"));
    } else if (goalObj.has("barricades")) {
      goalBarricades = Optional.of(goalObj.getInt("barricades"));
    } else if (goalObj.has("survive")) {
      survive = Optional.of(goalObj.getBoolean("survive"));
    }

    final int goalRounds = goalObj.getInt("rounds");
    final int goalMoral = goalObj.getInt("moral");
    final int goalZombiesColony = goalObj.getInt("zombiesColony");
    final int goalZombiesLocation = goalObj.getInt("zombiesLocations");
    final int goalChildrenInColony = goalObj.getInt("childrenInColony");

    return new Goal(goalLocationWithZombies, goalBarricades, survive, goalRounds, goalMoral,
        goalZombiesColony, goalZombiesLocation, goalChildrenInColony);
  }

  // read in the locations and save them in a hashmap
  // read in the colony separately
  public LocColTuple readInLocations(final Map<Integer, Card> cardHashMap) {

    final HashMap<Integer, Location> locationHashMap = new HashMap<>();
    // alle locations durchlaufen
    final JSONArray locationArray = this.jsonObj.getJSONArray("locations");
    Colony colony;
    colony = null;
    int totalNumEntrances = 0;
    // jedes Location objekt im array durchgehen
    for (int i = 0; i < locationArray.length(); i++) {
      final JSONObject currLoc = locationArray.getJSONObject(i);
      this.printKeys(currLoc, "Location");
      //final ArrayDeque<Card> locStack = new ArrayDeque<>();
      final ArrayList<Card> locStack = new ArrayList<>();
      final JSONArray cardStack;

      if (currLoc.has("colony")) {
        final JSONObject colonyJson = currLoc.getJSONObject("colony");
        this.printKeys(colonyJson, "Colony");
        final int entrances = colonyJson.getInt("entrances");
        totalNumEntrances += entrances;
        cardStack = colonyJson.getJSONArray("startCards");
        // vlt falsch hier durchzulaufen?
        for (final Object obj : cardStack) {
          final Integer id = (Integer) obj;
          if (!cardHashMap.containsKey(id)) {
            LoggerFactory.getLogger(JsonParser.class).error("could not find card Id: {}", id);
            throw new IllegalArgumentException();
          }
          locStack.add(cardHashMap.remove(id));
        }
        //LoggerFactory.getLogger(JsonParser.class).debug("location card stack: {}", locStack);
        final int identifier = colonyJson.getInt("identifier");
        colony = new Colony(identifier, entrances, locStack);
        continue;
      }

      cardStack = currLoc.getJSONArray("cards");
      // auslagern


      final int locationEntrances = currLoc.getInt("entrances");
      final int locationId = currLoc.getInt("identifier");
      final String locationName = currLoc.getString("name");
      final int locationSurvivorSpaces = currLoc.getInt("survivorSpaces");
      totalNumEntrances += locationEntrances;
      for (final Object o : cardStack) {
        final Integer id = (Integer) o;
        locStack.add(cardHashMap.get(id));
      }
      locationHashMap.put(locationId,
          new Location(locationId, locationEntrances, locationName, locationSurvivorSpaces,
              locStack));
    }


    return new LocColTuple(locationHashMap, colony, totalNumEntrances);
  }

  public CardTuple readInCards() {
    //final ArrayDeque<Card> cardArrayDeque = new ArrayDeque<>();


    final JSONArray cardArray = this.jsonObj.getJSONArray("cards");
    final List<Card> cardList = new ArrayList<>(cardArray.length());
    final HashMap<Integer, Card> cardHashMap = new HashMap<>(cardArray.length());
    this.containsDouble = false;
    for (int i = 0; i < cardArray.length(); i++) {
      final JSONObject currCard = cardArray.getJSONObject(i);
      if (currCard.has("blueprint")) {
        this.addBlueprint(currCard.getJSONObject("blueprint"), cardHashMap, cardList);
      } else if (currCard.has("colt")) {
        this.addColt(currCard.getJSONObject("colt"), cardHashMap, cardList);
      } else if (currCard.has("food")) {
        this.addFood(currCard.getJSONObject("food"), cardHashMap, cardList);
      } else if (currCard.has("fuel")) {
        this.addFuel(currCard.getJSONObject("fuel"), cardHashMap, cardList);
      } else if (currCard.has("hammer")) {
        this.addHammer(currCard.getJSONObject("hammer"), cardHashMap, cardList);
      } else if (currCard.has("lock")) {
        this.addLock(currCard.getJSONObject("lock"), cardHashMap, cardList);
      } else if (currCard.has("medicine")) {
        this.addMedicine(currCard.getJSONObject("medicine"), cardHashMap, cardList);
      } else if (currCard.has("scissors")) {
        this.addScissor(currCard.getJSONObject("scissors"), cardHashMap, cardList);
      } else if (currCard.has("snow_boots")) {
        this.addSnowBoots(currCard.getJSONObject("snow_boots"), cardHashMap, cardList);
      } else if (currCard.has("stuff")) {
        this.addStuff(currCard.getJSONObject("stuff"), cardHashMap, cardList);
      } else if (currCard.has("swab")) {
        this.addSwab(currCard.getJSONObject("swab"), cardHashMap, cardList);
      } else {
        LoggerFactory.getLogger(JsonParser.class).error(
            "could not process card: {}", currCard.keys());
      }


      this.checkUnique();
    }
    return new CardTuple(cardHashMap, cardList);
  }

  public void checkUnique() {
    if (this.containsDouble) {
      throw new IllegalArgumentException();
    }
  }

  public void addBlueprint(final JSONObject blueprintCard, final Map<Integer, Card> cardHashMap,
                           final java.util.Collection<Card> cardList) {
    final int id = blueprintCard.getInt("identifier");
    final int location = blueprintCard.getInt("location");
    cardList.add(new Blueprint(location, id));
    this.containsDouble = cardHashMap.containsKey(id);
    cardHashMap.put(id, new Blueprint(location, id));

  }

  public void addColt(final JSONObject coltCard, final Map<Integer, Card> cardHashMap,
                      final java.util.Collection<Card> cardList) {
    final int id = coltCard.getInt("identifier");
    cardList.add(new Colt(id));
    this.containsDouble = cardHashMap.containsKey(id);
    cardHashMap.put(id, new Colt(id));
  }

  public void addFood(final JSONObject foodCard, final Map<Integer, Card> cardHashMap,
                      final java.util.Collection<Card> cardList) {
    final int id = foodCard.getInt("identifier");
    final int amount = foodCard.getInt("amount");
    cardList.add(new Food(id, amount));
    this.containsDouble = cardHashMap.containsKey(id);
    cardHashMap.put(id, new Food(id, amount));
  }

  public void addFuel(final JSONObject fuelCard, final Map<Integer, Card> cardHashMap,
                      final java.util.Collection<Card> cardList) {
    final int id = fuelCard.getInt("identifier");
    cardList.add(new Fuel(id));
    this.containsDouble = cardHashMap.containsKey(id);
    cardHashMap.put(id, new Fuel(id));
  }

  public void addHammer(final JSONObject hammerCard, final Map<Integer, Card> cardHashMap,
                        final java.util.Collection<Card> cardList) {
    final int id = hammerCard.getInt("identifier");
    cardList.add(new Hammer(id));
    this.containsDouble = cardHashMap.containsKey(id);
    cardHashMap.put(id, new Hammer(id));
  }

  public void addLock(final JSONObject lockCard, final Map<Integer, Card> cardHashMap,
                      final java.util.Collection<Card> cardList) {
    final int id = lockCard.getInt("identifier");
    cardList.add(new Lock(id));
    this.containsDouble = cardHashMap.containsKey(id);
    cardHashMap.put(id, new Lock(id));
  }

  public void addMedicine(final JSONObject medicineCard, final Map<Integer, Card> cardHashMap,
                          final java.util.Collection<Card> cardList) {
    final int id = medicineCard.getInt("identifier");
    cardList.add(new Medicine(id));
    this.containsDouble = cardHashMap.containsKey(id);
    cardHashMap.put(id, new Medicine(id));
  }

  public void addScissor(final JSONObject scissorEquipment, final Map<Integer, Card> cardHashMap,
                         final java.util.Collection<Card> cardList) {
    final int id = scissorEquipment.getInt("identifier");
    this.containsDouble = cardHashMap.containsKey(id);
    cardList.add(new Scissors(id));
    cardHashMap.put(id, new Scissors(id));
  }

  public void addSnowBoots(final JSONObject snowBootsCard, final Map<Integer, Card> cardHashMap,
                           final java.util.Collection<Card> cardList) {
    final int id = snowBootsCard.getInt("identifier");
    this.containsDouble = cardHashMap.containsKey(id);
    cardList.add(new Snowboots(id));
    cardHashMap.put(id, new Snowboots(id));
  }

  public void addStuff(final JSONObject stuffCard, final Map<Integer, Card> cardHashMap,
                       final java.util.Collection<Card> cardList) {
    final int id = stuffCard.getInt("identifier");
    this.containsDouble = cardHashMap.containsKey(id);
    cardList.add(new Swab(id));
    cardHashMap.put(id, new Swab(id));
  }

  public void addSwab(final JSONObject swabCard, final Map<Integer, Card> cardHashMap,
                      final java.util.Collection<Card> cardList) {
    final int id = swabCard.getInt("identifier");
    this.containsDouble = cardHashMap.containsKey(id);
    cardList.add(new Swab(id));
    cardHashMap.put(id, new Swab(id));
  }

  // max Spiele auslesen & returnen
  public int readMaxPlayers() {
    return this.jsonObj.getInt("maxPlayers");
  }


  // Charaktere einlesen und in eine Hashmap geben, diese anschließend returnen
  //public ArrayDeque<Character> readInChars(final int colonyId) {
  public List<Character> readInChars(final int colonyId,
                                     final Map<Integer, Location> locationHashMap) {
    //final ArrayDeque<Character> characterArrayDeque = new ArrayDeque<>();
    //HashMap<Integer, Character> characterHashMap = new HashMap<>();
    final JSONArray charArray = this.jsonObj.getJSONArray("characters");
    final List<Character> characterList = new ArrayList<>(charArray.length());
    final Map<Integer, Integer> socialStatusMap = new HashMap<>(charArray.length());
    // loope durch das komplette charArray
    for (int i = 0; i < charArray.length(); i++) {
      final JSONObject character = charArray.getJSONObject(i);
      // generelle Char Informationen auslesen

      final int status = character.getInt("status");
      // der stöff ist so umständlich weil intellij sonst probleme gemacht hat
      // "for does not loop"
      // store all social status in a map and check if that status already exists
      final boolean checkCond = socialStatusMap.containsKey(status);
      socialStatusMap.put(status, status);
      if (checkCond) {
        throw new IllegalArgumentException();
      }
      final String name = character.getString("name");
      final int id = character.getInt("identifier");
      final int attack = character.getInt("attack");
      final int search = character.getInt("search");
      this.checkAttackSearch(search, attack);
      final JSONObject ability = character.getJSONObject("ability");
      // Ability checken
      if (ability.has("search")) {
        final SearchAbility charAbility = this.readSearch(ability);
        this.checkSearch(charAbility, colonyId, locationHashMap);
        characterList.add(new Character(id, status, charAbility, null,
            search, attack, colonyId, name));
      } else if (ability.has("kill")) {
        final KillAcAbility charAbility = this.readKill(ability, colonyId, locationHashMap);
        characterList.add(
            new Character(id, status, null, charAbility, search, attack, colonyId, name));
      } else if (ability.has("trash")) {
        final TrashAbility charAbility = this.readTrash(ability);
        characterList.add(
            new Character(id, status, charAbility, null, search, attack, colonyId, name));
      } else if (ability.has("heal")) {
        characterList.add(
            new Character(id, status, null, new HealAcAbility(), search, attack, colonyId, name));
      } else if (ability.has("feed")) {
        final FeedAcAbility charAbility = this.readFeed(ability);
        characterList.add(
            new Character(id, status, null, charAbility, search, attack, colonyId, name));
      } else if (ability.has("barricade")) {
        final BarricadeAcAbility charAbility = this.readBarr(ability);
        characterList.add(
            new Character(id, status, null, charAbility, search, attack, colonyId, name));
      } else if (ability.has("wound")) {
        final WoundAbility charAbility = this.readWound(ability);
        characterList.add(
            new Character(id, status, charAbility, null, search, attack, colonyId, name));
      } else if (ability.has("no_infection")) {
        characterList.add(
            new Character(id, status, new NoInfectionAbility(), null, search, attack, colonyId,
                name));
      }


    }
    return characterList;
  }

  private void checkSearch(final SearchAbility charAbility, final int colonyId,
                           final Map<Integer, Location> locationHashMap) {
    if (!locationHashMap.containsKey(charAbility.getLocationId())
        && charAbility.getLocationId() != colonyId) {
      throw new IllegalArgumentException();
    }
  }

  public void checkAttackSearch(final int search, final int attack) {
    if (search > 6 || attack > 6) {
      //todo ordentlich server beenden
      throw new IllegalArgumentException();
    }
  }

  // searchAbility auslesen und returnen
  public SearchAbility readSearch(final JSONObject ability) {
    final JSONObject searchAbility = ability.getJSONObject("search");
    final int maxActivations = searchAbility.getInt("maxActivations");
    final int location = searchAbility.getInt("location");
    final int numCards = searchAbility.getInt("numCards");
    return new SearchAbility(numCards, location, maxActivations);
  }

  // killAbility auslesen und returnen
  public KillAcAbility readKill(final JSONObject ability, final int colonyId,
                                final Map<Integer, Location> locationHashMap) {
    final JSONObject killAbility = ability.getJSONObject("kill");
    final int locationId = killAbility.getInt("locationId");
    // STANDARTWERTE UNKLAR
    if (locationId != colonyId
        && !locationHashMap.containsKey(locationId)) {
      throw new IllegalArgumentException();
    }
    final int dieValue = killAbility.getInt("dieValue");
    final int numZombies = killAbility.getInt("numZombies");
    final int maxActivations = killAbility.getInt("maxActivations");
    boolean infectionDie = true;
    boolean children = false;
    // Wie mit uninitialisierten Werten umgehen?
    if (killAbility.has("infectionDie")) {
      infectionDie = killAbility.getBoolean("infectionDie");
    }
    if (killAbility.has("children")) {
      children = killAbility.getBoolean("children");
    }
    return new KillAcAbility(locationId, infectionDie, dieValue, numZombies, children,
        maxActivations);

  }

  // WoundAbility auslesen und returnen
  public WoundAbility readWound(final JSONObject ability) {
    final JSONObject woundAbility = ability.getJSONObject("wound");
    final Wound before = this.toWound(woundAbility.getString("before"));
    final Wound after = this.toWound(woundAbility.getString("after"));
    return new WoundAbility(before, after);
  }

  private Wound toWound(final String s) {
    if ("wound".equals(s)) {
      return Wound.WOUND;
    }
    if ("frostBite".equals(s)) {
      return Wound.FROSTBITE;
    }
    if ("bite".equals(s)) {
      return Wound.BITE;
    }
    throw new IllegalArgumentException();
  }

  // feedAbility auslesen und returnen
  public FeedAcAbility readFeed(final JSONObject ability) {
    final JSONObject feedAbility = ability.getJSONObject("feed");
    return new FeedAcAbility(feedAbility.getInt("numFood"));
  }

  // barricadeAbility auslesen und returnen
  public BarricadeAcAbility readBarr(final JSONObject ability) {
    final JSONObject barrAbility = ability.getJSONObject("barricade");
    final int numBarricades = barrAbility.getInt("numBarricades");
    final int maxActivations = barrAbility.getInt("maxActivations");
    return new BarricadeAcAbility(numBarricades, maxActivations);
  }

  // trashAbility auslesen und returnen
  public TrashAbility readTrash(final JSONObject ability) {
    final JSONObject trashAbility = ability.getJSONObject("trash");
    return new TrashAbility(trashAbility.getInt("numCards"));
  }

  // alle Krisen einlesen und eine deQ returnen
  //@throws IllegalArgumentException
  //public ArrayDeque<Crisis> readCrisis() {
  public List<Crisis> readCrisis() {
    //final ArrayDeque<Crisis> crisisList = new ArrayDeque<>();
    final JSONArray crisisArray = this.jsonObj.getJSONArray("crises");
    CrisisCardType crisisCardTypeEnum;
    //
    JSONObject actualCrisis;

    final List<Crisis> crisisList = new ArrayList<>(crisisArray.length() + 5);
    for (int i = 0; i < crisisArray.length(); i++) {

      final JSONObject currCrisis = crisisArray.getJSONObject(i);
      if (currCrisis.has("food")) {
        actualCrisis = currCrisis.getJSONObject("food");
        crisisCardTypeEnum = CrisisCardType.FOOD;
      } else if (currCrisis.has("stuff")) {
        actualCrisis = currCrisis.getJSONObject("stuff");
        crisisCardTypeEnum = CrisisCardType.STUFF;
      } else if (currCrisis.has("medicine")) {
        actualCrisis = currCrisis.getJSONObject("medicine");
        crisisCardTypeEnum = CrisisCardType.MEDICINE;
      } else if (currCrisis.has("fuel")) {
        actualCrisis = currCrisis.getJSONObject("fuel");
        crisisCardTypeEnum = CrisisCardType.FUEL;
      } else {
        throw new IllegalArgumentException();
      }
      // eventuell auslagern
      final int identifier = actualCrisis.getInt("identifier");
      final int requiredCards = actualCrisis.getInt("requiredCards");
      final int moralChange = actualCrisis.getInt("moralChange");
      crisisList.add(new Crisis(moralChange, identifier, requiredCards, crisisCardTypeEnum));
    }
    return crisisList;

  }

  public JSONObject getJsonObj() {
    return this.jsonObj;
  }

}
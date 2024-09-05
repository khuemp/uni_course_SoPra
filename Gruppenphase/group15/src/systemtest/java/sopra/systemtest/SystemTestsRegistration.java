package sopra.systemtest;

import sopra.systemtest.advanced.BarricadeWinTest;
import sopra.systemtest.advanced.BluePrintWrongLocationTest;
import sopra.systemtest.advanced.CleanNotInColonyTest;
import sopra.systemtest.advanced.CleanWrongCharacterTest;
import sopra.systemtest.advanced.CrisisAceTest;
import sopra.systemtest.advanced.CrisisPassTest;
import sopra.systemtest.advanced.HealAbilityNoWoundTest;
import sopra.systemtest.advanced.MoveSameLocationTest;
import sopra.systemtest.advanced.NoSearchInColonyTest;
import sopra.systemtest.advanced.NotYourCardTest;
import sopra.systemtest.advanced.OrderZombieSpawnTest;
import sopra.systemtest.advanced.RegisterTwiceTest;
import sopra.systemtest.advanced.TooManyBarricadeAndNoMoreActivationTest;
import sopra.systemtest.api.SystemTestManager;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @version 1.0.0
 */
final class SystemTestsRegistration {

  private SystemTestsRegistration() {
    // empty
  }


  static void registerSystemTests(final SystemTestManager manager) {

    //Does not pass on example Implementation

    //manager.registerTest(new TryingInvalidInput());
    //pass
    manager.registerTest(new MoralLoseTest());
    //pass
    manager.registerTest(new AllPlayersLeftTest());
    //Does not pass on example Implementation
    //manager.registerTest(new TooManyRegisterTest());
    //pass
    manager.registerTest(new RegistrationAbortedTest2());
    //pass
    manager.registerTest(new TestingTestsTest());
    //pass
    manager.registerTest(new RegistrationAbortedTest());
    //Does not pass on example Implementation
    //manager.registerTest(new PreparationPhaseTest());
    //Does not pass on example Implementation
    //manager.registerTest(new MultiplePlayerTest());
    //pass
    manager.registerTest(new AllPlayersLeftTest());
    //Does not pass on example Implementation
    //manager.registerTest(new CardDrawnTest());
    //pass
    manager.registerTest(new StartGameEarlyTest());
    //Does not pass on example Implementation
    //manager.registerTest(new RegisterBasicTest());
    //Does not pass on example Implementation
    //manager.registerTest(new BaseTest());
    //Does not pass on example Implementation
    //manager.registerTest(new PickWrongCharacterTest());
    //Does not pass on example Implementation
    //manager.registerTest(new NotYourTurnTest());
    manager.registerTest(new DoesStartGameEarlySendRegistrationAbortedTest());
    manager.registerTest(new EndTurnButNotActivePlayerTest());
    //pass
    manager.registerTest(new BarricadeWinTest());
    //pass
    manager.registerTest(new MoveSameLocationTest());
    //pass
    manager.registerTest(new NoSearchInColonyTest());
    manager.registerTest(new CleanNotInColonyTest());
    //manager.registerTest(new NoMoveTwiceTest());
    //pass
    manager.registerTest(new TooManyBarricadeAndNoMoreActivationTest());
    //Does not pass on example Implementation
    //manager.registerTest(new ZombieSpawnTest());
    //pass
    manager.registerTest(new HealAbilityNoWoundTest());
    manager.registerTest(new CleanWrongCharacterTest());
    manager.registerTest(new CrisisPassTest());

    manager.registerTest(new CrisisAceTest());

    //Does not pass on example Implementation
    //manager.registerTest(new NoUseFuelTwiceTest());
    //pass
    //manager.registerTest(new LeaveTooEarlyTest());
    //Does not pass on example Implementation
    //manager.registerTest(new CharacterAndChildSpawnTest());
    manager.registerTest(new OrderZombieSpawnTest());
    //Does not pass on example Implementation
    //manager.registerTest(new RollInfectionTest());
    //Does not pass on example Implementation
    manager.registerTest(new RegisterTwiceTest());
    manager.registerTest(new NotYourCardTest());

    manager.registerTest(new BluePrintWrongLocationTest());
    //manager.registerTest(new WorkingBluePrintTest());

    //manager.registerTest(new OverrunTest());


  }
}

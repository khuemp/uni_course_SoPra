package sopra.systemtest;

import sopra.systemtest.api.SystemTestManager;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @version 1.0.0
 */
final class SystemTestsRegistration {

    private SystemTestsRegistration() {
        // empty
    }

    static void registerDailyTests(final SystemTestManager manager) {
        manager.registerTest(new RegistrationTest());
    }

    static void registerSecretTests(final SystemTestManager manager) {
        manager.registerTest(new RegistrationTest());
    }

    static void registerSystemTests(final SystemTestManager manager) {
        //manager.registerTest(new RegistrationTest());
        manager.registerTest(new RegistrationAbortedTest());
        manager.registerTest(new BarricadedFoodSearchedZombie());
        manager.registerTest(new SearchedNoLocZombieNoLocBarricadedZonmbieColony());
        manager.registerTest(new BarricadeZombieKillChildAndSurvivor());
        manager.registerTest(new ZombieKillAllSurvivor());
        manager.registerTest(new IfCrossroadStillRemain());
        manager.registerTest(new WatseConsequenceZeroMoralEndImmediately());
        manager.registerTest(new VoteZombieSpawnKillSurvivor());
        manager.registerTest(new OnePlayerVoteTwice());
        manager.registerTest(new AllPlayersLeft());
    }
}

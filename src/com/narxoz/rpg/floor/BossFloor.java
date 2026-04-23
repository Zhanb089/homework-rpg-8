package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import java.util.List;

public class BossFloor extends TowerFloor {

    private final String floorName;
    private final Monster boss;
    private final int goldReward;

    public BossFloor(String floorName, Monster boss, int goldReward) {
        this.floorName = floorName;
        this.boss = boss;
        this.goldReward = goldReward;
    }

    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void announce() {
        System.out.println();
        System.out.println("#############################################");
        System.out.println("  BOSS FLOOR: " + floorName);
        System.out.println("  " + boss.getName() + " blocks your path!");
        System.out.println("#############################################");
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("Setup: the ground trembles. " + boss.getName()
                + " (HP: " + boss.getHp() + ", ATK: " + boss.getAttackPower()
                + ") bares its fangs.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        int totalDamage = 0;
        int round = 1;

        while (boss.isAlive() && anyAlive(party)) {
            System.out.println("\n  === Round " + round++ + " ===");

            for (Hero h : party) {
                if (!h.isAlive()) continue;
                h.startTurn();
                if (!h.isAlive()) { h.endTurn(); continue; }

                if (h.canAct() && boss.isAlive()) {
                    int dmg = h.computeOutgoingDamage();
                    boss.takeDamage(dmg);
                    System.out.println("  " + h.getName() + " strikes " + boss.getName()
                            + " for " + dmg + " damage (" + boss.getName()
                            + " HP: " + boss.getHp() + ")");
                }
                h.endTurn();
            }

            if (boss.isAlive()) {
                System.out.println("  " + boss.getName() + " unleashes a sweeping attack!");
                for (Hero h : party) {
                    if (!h.isAlive()) continue;
                    int before = h.getHp();
                    h.receiveAttack(boss.getAttackPower());
                    int dealt = before - h.getHp();
                    totalDamage += dealt;
                    System.out.println("    " + h.getName() + " takes " + dealt
                            + " damage (HP: " + h.getHp() + ")");
                }
            }
        }

        boolean cleared = !boss.isAlive() && anyAlive(party);
        return new FloorResult(
                cleared,
                totalDamage,
                cleared ? boss.getName() + " has been vanquished!" : "The boss proved too mighty.");
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (result.isCleared()) {
            System.out.println("Loot awarded: " + goldReward + " gold and the Boss's Relic!");
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("Cleanup: the dust settles in the boss chamber.");
    }

    private boolean anyAlive(List<Hero> party) {
        for (Hero h : party) if (h.isAlive()) return true;
        return false;
    }
}
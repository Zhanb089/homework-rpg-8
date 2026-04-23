package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;

import java.util.List;

public class RestFloor extends TowerFloor {

    private final String floorName;
    private final int healAmount;

    public RestFloor(String floorName, int healAmount) {
        this.floorName = floorName;
        this.healAmount = healAmount;
    }

    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("Setup: a warm hearth crackles. A cauldron of stew simmers nearby.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        for (Hero h : party) {
            if (!h.isAlive()) continue;
            int before = h.getHp();
            h.heal(healAmount);
            int healed = h.getHp() - before;
            System.out.println("  " + h.getName() + " rests and recovers " + healed
                    + " HP (HP: " + h.getHp() + "/" + h.getMaxHp() + ")");
        }
        return new FloorResult(true, 0, "The party rested and recovered strength.");
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
    }
}
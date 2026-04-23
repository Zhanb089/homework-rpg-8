package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;

import java.util.List;

public class TrapFloor extends TowerFloor {

    public enum TrapType { POISON, STUN }

    private final String floorName;
    private final TrapType trapType;
    private final int trapDamage;

    public TrapFloor(String floorName, TrapType trapType, int trapDamage) {
        this.floorName = floorName;
        this.trapType = trapType;
        this.trapDamage = trapDamage;
    }

    @Override
    protected String getFloorName() { return floorName; }

    /** HOOK OVERRIDE: custom announce() banner. */
    @Override
    protected void announce() {
        System.out.println("\n~~~ " + floorName + " ~~~");
        System.out.println("A sinister " + trapType.toString().toLowerCase()
                + " trap lurks beneath the floor stones...");
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("Setup: the party steps cautiously forward, scanning the floor.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  *CLICK* The trap springs!");
        int totalDamage = 0;

        for (Hero h : party) {
            if (!h.isAlive()) continue;
            int before = h.getHp();
            h.receiveAttack(trapDamage);
            int dealt = before - h.getHp();
            totalDamage += dealt;
            System.out.println("  " + h.getName() + " takes " + dealt
                    + " trap damage (HP: " + h.getHp() + ")");
            if (h.isAlive()) {
                if (trapType == TrapType.POISON) {
                    h.setState(new PoisonedState());
                } else {
                    h.setState(new StunnedState());
                }
            }
        }

        boolean cleared = anyAlive(party);
        return new FloorResult(
                cleared,
                totalDamage,
                cleared ? "Survived the trap floor." : "Party wiped by the trap.");
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (result.isCleared()) {
            System.out.println("Loot awarded: a curio fragment found in the trap mechanism.");
        }
    }

    private boolean anyAlive(List<Hero> party) {
        for (Hero h : party) if (h.isAlive()) return true;
        return false;
    }
}
package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;

import java.util.ArrayList;
import java.util.List;

public class CombatFloor extends TowerFloor {

    private final String floorName;
    private final List<Monster> monsters;
    private final int goldReward;

    public CombatFloor(String floorName, List<Monster> monsters, int goldReward) {
        this.floorName = floorName;
        this.monsters = new ArrayList<>(monsters);
        this.goldReward = goldReward;
    }

    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("Setup: " + monsters.size() + " monster(s) emerge from the shadows.");
        for (Monster m : monsters) {
            System.out.println("  - " + m.getName() + " (HP: " + m.getHp()
                    + ", ATK: " + m.getAttackPower() + ")");
        }
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        int totalDamage = 0;
        int round = 1;

        while (anyMonsterAlive() && anyHeroAlive(party)) {
            System.out.println("\n  === Round " + round++ + " ===");

            // Heroes' turn
            for (Hero h : party) {
                if (!h.isAlive()) continue;
                h.startTurn();
                if (!h.isAlive()) { h.endTurn(); continue; }

                if (h.canAct()) {
                    Monster target = firstAliveMonster();
                    if (target != null) {
                        int dmg = h.computeOutgoingDamage();
                        target.takeDamage(dmg);
                        System.out.println("  " + h.getName() + " hits " + target.getName()
                                + " for " + dmg + " damage (" + target.getName()
                                + " HP: " + target.getHp() + ")");
                    }
                }
                h.endTurn();
            }

            // Monsters' turn - each monster targets a hero in round-robin
            int monsterAttackIndex = 0;
            for (Monster m : monsters) {
                if (!m.isAlive()) continue;
                List<Hero> alive = aliveHeroes(party);
                if (alive.isEmpty()) break;
                Hero target = alive.get(monsterAttackIndex % alive.size());
                int before = target.getHp();
                target.receiveAttack(m.getAttackPower());
                int dealt = before - target.getHp();
                totalDamage += dealt;
                System.out.println("  " + m.getName() + " strikes " + target.getName()
                        + " for " + dealt + " damage (" + target.getName()
                        + " HP: " + target.getHp() + ")");
                monsterAttackIndex++;
            }
        }

        boolean cleared = !anyMonsterAlive() && anyHeroAlive(party);
        String summary = cleared ? "All monsters defeated." : "Party fell in combat.";
        return new FloorResult(cleared, totalDamage, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (result.isCleared()) {
            System.out.println("Loot awarded: " + goldReward + " gold to the party.");
        }
    }

    private boolean anyMonsterAlive() {
        for (Monster m : monsters) if (m.isAlive()) return true;
        return false;
    }

    private boolean anyHeroAlive(List<Hero> party) {
        for (Hero h : party) if (h.isAlive()) return true;
        return false;
    }

    private Monster firstAliveMonster() {
        for (Monster m : monsters) if (m.isAlive()) return m;
        return null;
    }

    private List<Hero> aliveHeroes(List<Hero> party) {
        List<Hero> alive = new ArrayList<>();
        for (Hero h : party) if (h.isAlive()) alive.add(h);
        return alive;
    }
}
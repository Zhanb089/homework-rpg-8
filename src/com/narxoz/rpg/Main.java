package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.floor.BossFloor;
import com.narxoz.rpg.floor.CombatFloor;
import com.narxoz.rpg.floor.RestFloor;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.floor.TrapFloor;
import com.narxoz.rpg.state.NormalState;
import com.narxoz.rpg.state.StunnedState;
import com.narxoz.rpg.tower.TowerRunResult;
import com.narxoz.rpg.tower.TowerRunner;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Two heroes with different starting states.
        Hero valiant = new Hero("Valiant", 60, 12, 3, new NormalState());
        Hero shadow  = new Hero("Shadow",  45, 10, 4, new StunnedState());

        List<Hero> party = Arrays.asList(valiant, shadow);

        List<TowerFloor> floors = Arrays.asList(
                new CombatFloor("Floor 1: Skeleton Hall",
                        Arrays.asList(
                                new Monster("Skeleton A", 14, 6),
                                new Monster("Skeleton B", 14, 6)),
                        10),
                new TrapFloor("Floor 2: Poison Corridor",
                        TrapFloor.TrapType.POISON, 6),
                new RestFloor("Floor 3: Traveler's Sanctuary", 20),
                new BossFloor("Floor 4: Dragon's Lair",
                        new Monster("Ancient Wyrm", 80, 13), 100)
        );

        TowerRunner runner = new TowerRunner(floors);

        System.out.println("=============================================");
        System.out.println("  THE HAUNTED TOWER - ascent begins");
        System.out.println("  Party:");
        for (Hero h : party) {
            System.out.println("    - " + h.getName()
                    + " (HP: " + h.getHp() + "/" + h.getMaxHp()
                    + ", ATK: " + h.getAttackPower()
                    + ", DEF: " + h.getDefense()
                    + ", State: " + h.getState().getName() + ")");
        }
        System.out.println("=============================================");

        TowerRunResult result = runner.climb(party);

        System.out.println("\n====== TOWER RUN SUMMARY ======");
        System.out.println("Floors cleared   : " + result.getFloorsCleared()
                + " / " + floors.size());
        System.out.println("Heroes surviving : " + result.getHeroesSurviving()
                + " / " + party.size());
        System.out.println("Reached top      : " + result.isReachedTop());
        System.out.println("Final party status:");
        for (Hero h : party) {
            System.out.println("  - " + h.getName()
                    + " | HP: " + h.getHp() + "/" + h.getMaxHp()
                    + " | State: " + h.getState().getName()
                    + " | " + (h.isAlive() ? "ALIVE" : "FALLEN"));
        }
    }
}
package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;

import java.util.List;

public class TowerRunner {

    private final List<TowerFloor> floors;

    public TowerRunner(List<TowerFloor> floors) {
        this.floors = floors;
    }

    public TowerRunResult climb(List<Hero> party) {
        int cleared = 0;

        for (int i = 0; i < floors.size(); i++) {
            TowerFloor floor = floors.get(i);

            FloorResult r = floor.explore(party);

            System.out.println("Floor result: " + r.getSummary()
                    + " (cleared: " + r.isCleared()
                    + ", damage taken: " + r.getDamageTaken() + ")");

            if (!r.isCleared()) {
                System.out.println("\n!!! The tower has defeated the party on floor "
                        + (i + 1) + " !!!");
                return new TowerRunResult(cleared, countAlive(party), false);
            }

            cleared++;

            if (!anyAlive(party)) {
                return new TowerRunResult(cleared, 0, false);
            }
        }

        return new TowerRunResult(cleared, countAlive(party), true);
    }

    private boolean anyAlive(List<Hero> party) {
        for (Hero h : party) if (h.isAlive()) return true;
        return false;
    }

    private int countAlive(List<Hero> party) {
        int c = 0;
        for (Hero h : party) if (h.isAlive()) c++;
        return c;
    }
}
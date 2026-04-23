package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class StunnedState implements HeroState {

    private int turnsRemaining;

    public StunnedState() {
        this(1);
    }

    public StunnedState(int duration) {
        this.turnsRemaining = duration;
    }

    @Override
    public String getName() { return "Stunned"; }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return 0;
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) { return rawDamage; }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("  [" + hero.getName() + "] is stunned and cannot move");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0 && hero.isAlive()) {
            System.out.println("  [" + hero.getName() + "] shakes off the stun");
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() { return false; }
}
package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState {

    private int turnsRemaining;
    private final int poisonDamagePerTurn;

    public PoisonedState() {
        this(2, 3);
    }

    public PoisonedState(int duration, int poisonDamagePerTurn) {
        this.turnsRemaining = duration;
        this.poisonDamagePerTurn = poisonDamagePerTurn;
    }

    @Override
    public String getName() { return "Poisoned"; }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        // Sickly grip - weaker swings.
        return (int) (basePower * 0.7);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        // Poison itself does not alter physical defense.
        return rawDamage;
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("  [" + hero.getName() + "] suffers " + poisonDamagePerTurn
                + " poison damage (HP: " + Math.max(0, hero.getHp() - poisonDamagePerTurn) + ")");
        hero.takeDamage(poisonDamagePerTurn);
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0 && hero.isAlive()) {
            System.out.println("  [" + hero.getName() + "] has shaken off the poison");
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() { return true; }
}
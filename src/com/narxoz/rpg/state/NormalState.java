package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;


public class NormalState implements HeroState {

    private static final int BERSERK_HP_DIVISOR = 3;

    @Override
    public String getName() { return "Normal"; }

    @Override
    public int modifyOutgoingDamage(int basePower) { 
        return basePower; 
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) { 
        return rawDamage; 
    }

    @Override
    public void onTurnStart(Hero hero) {
        if (hero.isAlive() && hero.getHp() <= hero.getMaxHp() / BERSERK_HP_DIVISOR) {
            System.out.println("  [" + hero.getName() + "] is wounded deeply and erupts in BERSERKER rage!");
            hero.setState(new BerserkState());
        }
    }

    @Override
    public void onTurnEnd(Hero hero) {
    }

    @Override
    public boolean canAct() { return true; }
}
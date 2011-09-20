package com.killerappzz.spider.objects;

/**
 * Define a common contract for hitting walls :)
 * @author fbratu
 *
 */
public interface IBounceable {

    // specific behaviour when object touches the bounds
    public abstract void boundsTouchBehaviour(BounceAxis axis);
    // specific behaviour when object touches the already-claimed path
    public abstract void claimedPathTouch();
    
    public enum BounceAxis {
    	HORIZONTAL,
    	VERTICAL
    };
}

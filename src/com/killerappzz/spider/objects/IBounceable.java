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
    // the path is given as argument, as we might have to query it
    // for additional details in our behaviour
    public abstract void claimedPathTouch(ClaimedPath path);
    
    public enum BounceAxis {
    	HORIZONTAL,
    	VERTICAL
    };
}

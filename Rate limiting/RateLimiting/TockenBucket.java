public class TockenBucket {

    private double tockens;
    private  double capacity;
    private double refillRate;
    private  double lastRefillTimestamp;

    public TockenBucket(double capacity, double refillRate)
    {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tockens = capacity;
        this.lastRefillTimestamp = System.nanoTime();
    }

    public synchronized boolean allowRequest()
    {
        refill();
        if(tockens>1)
        {
            tockens--;
            return true;
        }
        return false;
    }

    public void refill()
    {
        long now = System.nanoTime();
        double tockensToAdd = (now- lastRefillTimestamp)*refillRate / 1e9;
        tockens = Math.min(capacity, tockens+ tockensToAdd);
        lastRefillTimestamp = now;
    }

    public double getTockens()
    {
        refill(); //so that if time has elapsed and client has not consumed any tockens then we will get the updated one
        return tockens;
    }

    public void updateConfig(double capacity, double refillRate)
    {
            refill();
            this.capacity = capacity;
            this.refillRate = refillRate;
            if(tockens>capacity)
                    tockens = capacity;
    }
}

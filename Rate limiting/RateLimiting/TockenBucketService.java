import java.util.concurrent.ConcurrentHashMap;

public class TockenBucketService {
    private final ConcurrentHashMap<String, TockenBucket> cache = new ConcurrentHashMap<>();

    public boolean checkAndAllowRequest(String clientId)
    {
        TockenBucket bucket = cache.get(clientId);
        if(bucket==null)
            return false;
        return bucket.allowRequest();
    }

    public void updateRule(String clientId, double capacity, double refillRate)
    {
        cache.compute(clientId, (id, bucket)->{
                if(bucket==null)
                {
                    return new TockenBucket(capacity, refillRate);
                }

                bucket.updateConfig(capacity, refillRate);
                return bucket;

        });
    }

    public double getTockenCount(String clientId)
    {
        TockenBucket bucket = cache.get(clientId);
        return bucket.getTockens();
    }

    public void refillAll()
    {
        cache.values().forEach(bucket->bucket.allowRequest());
    }
}

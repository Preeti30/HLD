import java.util.Map;
import java.util.ResourceBundle;

@RestController
public class RateLimiterController {

    @Autowired
    private TockenBucketService tockenBucketService;

    @PostMapping("/rate-limit/check")

    public ResponseEntity<String> checkRateLimitAndAllowRequest(@RequestHeader("Client-id") String clientId)
    {
        boolean allowed = tockenBucketService.checkAndAllowRequest(clientId);
        return allowed? ResponseEnity.ok("Allowed") : ResponseEnity.status(429).body("RAte limit exceeded");
    }

    @PostMapping("/admin/rules")
    public ResponseEntity<String> updateRule(@RequestBody Map<String, Object> rule)
    {
        String clientId = (String)rule.get("clientId");
        String refillRate = String.valueOf(((Number)rule.get("refillRate")).doubleValue());
        double capacity = ((Number)rule.get("capacity")).doubleValue();

        tockenBucketService.updateRule(clientId, capacity, Double.parseDouble(refillRate));
        return ResponseEntity.ok("Rule updated");
    }

    @GetMapping("/admin/rules/{clientId}")
    public ResponseEntity<String> getClientBucketTockens(@PathVariable String clientId)
    {
         double tockens = tockenBucketService.getTockenCount(clientId);
        if(bucket==null)
                return ResponseEntity.notFound().build;
        return ResponseEnity.ok(Map.of("tokens",tockens ));
    }

    @PostMapping("/admin/refill-now")
    public ResponseEntity<String> refillTockenBucket()
    {
         tockenBucketService.refillAll();
         return ResponseEnityt.ok("Refill triggered");
    }

}


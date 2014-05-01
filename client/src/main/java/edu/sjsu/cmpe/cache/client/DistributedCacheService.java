package edu.sjsu.cmpe.cache.client;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

/**
 * Distributed cache service
 */
public class DistributedCacheService implements CacheServiceInterface {
    @SuppressWarnings("rawtypes")
	private ConsistentHash consistentHash;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public DistributedCacheService(List<String> serverUrls) {
        // Create new ConsistentHash using MD5 hashing from the Guava Hashing class and the provided List of servers
        consistentHash = new ConsistentHash(Hashing.md5(), 1000, serverUrls);
    }

    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#get(long)
     */
    @Override
    public String get(long key) {
        HttpResponse<JsonNode> response = null;
        try {
            // Find appropriate server to get the value for given key using the ConsistentHash and MD5 hashing
            Object bucket = consistentHash.get(Hashing.md5().hashString(String.valueOf(key), Charsets.UTF_8));
            // Get the value from the appropriate server
            response = Unirest.get("http://" + bucket + "/cache/{key}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key)).asJson();
        } catch (UnirestException e) {
            System.err.println(e);
        }
        String value = response.getBody().getObject().getString("value");

        return value;
    }

    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#put(long,
     *      java.lang.String)
     */
    @Override
    public void put(long key, String value) {
        HttpResponse<JsonNode> response = null;
        try {
            // Find appropriate server to put the value for given key using the ConsistentHash and MD5 hashing
            Object bucket = consistentHash.get(Hashing.md5().hashString(String.valueOf(key), Charsets.UTF_8));
            // Put the value to the appropriate server
            response = Unirest
                    .put("http://" + bucket + "/cache/{key}/{value}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key))
                    .routeParam("value", value).asJson();
        } catch (UnirestException e) {
            System.err.println(e);
        }

        if (response.getCode() != 200) {
            System.out.println("Failed to add to the cache.");
        }
    }
}

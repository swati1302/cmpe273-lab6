package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;
import java.util.List;

public class Client {

    public static void main(String[] args) throws Exception {

        System.out.println("Starting Cache Client...");

        // Create list of available servers
        List<String> servers = new ArrayList<String>();
        servers.add("localhost:3000");
        servers.add("localhost:3001");
        servers.add("localhost:3002");

        // Create distributed Cache for storing key/value pairs
        CacheServiceInterface cache = new DistributedCacheService(servers);

        // Send 10 key/value pairs to be sharded amongst the list of servers using consistent hashing
        cache.put(1, "a");
        System.out.println("Inserting  {1:\"a\"} in Cache");
        cache.put(2, "b");
        System.out.println("Inserting {2:\"b\"} to Cache");
        cache.put(3, "c");
        System.out.println("Inserting {3:\"c\"} to Cache");
        cache.put(4, "d");
        System.out.println("Inserting {4:\"d\"} to Cache");
        cache.put(5, "e");
        System.out.println("Inserting {5:\"e\"} to Cache");
        cache.put(6, "f");
        System.out.println("Inserting {6:\"f\"} to Cache");
        cache.put(7, "g");
        System.out.println("Inserting {7:\"g\"} to Cache");
        cache.put(8, "h");
        System.out.println("Inserting {8:\"h\"} to Cache");
        cache.put(9, "i");
        System.out.println("Inserting {9:\"i\"} to Cache");
        cache.put(10, "j");
        System.out.println("Inserting {10:\"j\"} to Cache");

        // Get and print out the values from the sharded 10 key/value pairs
        for (int i=1; i<=10; i++) {
            System.out.println("Value received is \"" + cache.get(i) + "\" for  \"" + i + "\"");
        }

        System.out.println("Existing Cache Client...");
    }
}

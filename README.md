## Installation
Add this dependency:
```
<!-- CandyCraft repo -->
<repository>
    <id>candycraft-repo</id>
    <url>https://repo.morx.me</url>
</repository>

<!-- Proxy UUID-Fetcher -->
<dependency>
    <groupId>de.pauhull</groupId>
    <artifactId>uuidfetcher</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Remember to exclude the "bungee" packet if you're using BungeeCord.
```
<filters>
    <filter>
        <artifact>*:*</artifact>
        <excludes>
            <exclude>de/pauhull/uuidfetcher/bungee/*.*</exclude>
        </excludes>
    </filter>
</filters>
```
Add this to the shade build plugin in plugin/executions/execution/configuration

## Using the UUID Fetcher

### On Spigot
```
private SpigotUUIDFetcher uuidFetcher;

@Override
public void onEnable() {
    this.uuidFetcher = new UUIDFetcher(this);    
}
```

### On BungeeCord
```
private BungeeUUIDFetcher uuidFetcher;

@Override
public void onEnable() {
    this.uuidFetcher = new BungeeUUIDFetcher();
}
```

### Code examples
#### Getting a player's name from UUID
```
uuidFetcher.fetchNameAsync(myUUID, name -> {
    if(name == null) {
        System.out.println("There is no player with the UUID " + myUUID.toString() + "!");
        return;
    }

    System.out.println("Name of player with UUID " + myUUID.toString() + ": " + name);
});
```

#### Getting a player's UUID from name
```
uuidFetcher.fetchUUIDAsync(myName, uuid -> {
    if(uuid == null) {
        System.out.println("There is no player with the name " + myName + "!");
        return;
    }
    
    System.out.println("UUID of player with name " + myName + ": " + uuid.toString());
});
```
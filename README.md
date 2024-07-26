[![JavaDex Latest - 0.1.4.4](https://img.shields.io/badge/JavaDex_Latest-0.1.4.4-2ea44f?style=for-the-badge)](https://)
# JavaDex: MangaDex API for Java

JavaDex is a wrapper for managing and interacting with manga chapters and related entities. This project provides a comprehensive API to handle various operations such as retrieving manga information, managing chapters, and handling user interactions.


## Disclaimer
By using this wrapper, you agree to MangaDex's privacy policy and terms of service.

The developer(s) of this wrapper are not responsible for misuse and/or other ill-advised activites that affect others.

From MangaDex's API page:
 ```
Usage of our services implies acceptance of the following:

You MUST credit us
You MUST credit scanlation groups if you offer the ability to read chapters
You CANNOT run ads or paid services on your website and/or apps 
```
## Installation

### Maven
```xml
<dependency>
    <groupId>dev.kurumidisciples</groupId>
    <artifactId>javadex</artifactId>
    <version>0.1.4.4</version>
</dependency>
```
### Gradle
```gradle
implementation group: 'dev.kurumidisciples', name: 'javadex', version: '0.1.4.4'
```
    
## Usage/Examples

#### Authorize JavaDex
```java
import dev.kurumidisciples.javadex.api.core.*;

public class Main {
    public static void main(String[] args) throws LoginException {
        JavaDex javadex = JavaDexBuilder.createPersonal()
                .setClientId("your_client_id")
                .setClientSecret("your_client_secret")
                .setUsername("your_username")
                .setPassword("your_password")
                .build();
    }
}


```
#### Retrieving a Manga
```java
import dev.kurumidisciples.javadex.api.core.*;
import dev.kurumidisciples.javadex.api.entities.content.Manga;

public class Main{

    public static void main (String[] args) {
            JavaDex javadex = JavaDexBuilder.createGuest();

            List<Manga> manga = javadex.search("Dungeon Meshi")
            .setLimit(1)
            .complete();

            System.out.println("Manga:" + manga.get(0).getDefaultTitle());
    }
} 
```


## Useful Resources
 - [MangaDex API Reference Guide](https://api.mangadex.org/docs/swagger.html)
 - [MangaDex Discord Server](https://discord.gg/mangadex)

## Authors

- [@Hacking-Pancakez](https://github.com/Hacking-Pancakez)


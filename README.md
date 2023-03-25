# Hermes

Hermes is a light Event Bus library.
Written for fun, it is similar to what you can find in bigger libaries like [Google Guava](https://github.com/google/guava), 
Spring framework, and of course my first experience using an Event Bus, [jEdit](http://sourceforge.net/projects/jedit/).

## Dependency

Available through Maven central

```xml
<dependency>
  <groupId>com.kpouer</groupId>
  <artifactId>hermes</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Maven

```java
public class Test {
    public static void main(String[] args) {
        Hermes hermes = new Hermes();
        hermes.subscribe(new MyReceiver());
        hermes.publish(new Event("Hello"));
    }

    private static class MyReceiver {
        private String receivedMessage;
        
        @Listener
        public void onEvent(Event event) {
            receivedMessage = event.getMessage();
        }
    }

    private static class Event {
        private final String message;
        
        public Event(String message) {
            this.message = message;
        }
    }
}
```
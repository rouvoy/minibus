# MiniBus Library

Lightweight implementation of a publish/subscribe library inspired by [Otto](http://square.github.io/otto)

## Maven artefact

### Release
The latest released version of the Minibus library artefact is available as:

``` xml
<dependency>
    <groupId>fr.inria.minibus</groupId>
    <artifactId>minibus-library</artifactId>
    <version>1.0</version>
</dependency>
```

### Snapshot
The currently developed version of the MiniBus library artefact is available as:

``` xml
<dependency>
    <groupId>fr.inria.minibus</groupId>
    <artifactId>minibus-library</artifactId>
    <version>1.1-SNAPSHOT</version>
</dependency>
```

## Maven compilation

MiniBus is a [Maven](http://maven.apache.org "Maven") managed project. All you have to do is to invoke the `install` command from the root directory (`MINIBUS_DIR`):

``` bash
cd $MINIBUS_DIR
mvn install
```


## API Usage

### Declaring an event
MiniBus supports the usage of POJO events as long as the filtered fields are exposed:
``` java
public class Event {
    public final int value;
    
    public Event(int v) {
        this.value = v;
    }
}
```

### Subscribing to an event
Enlistment of event handlers is achieved through the interface Subscriber and the annotation Subscribe:
``` java
public class SubscribingObject {
    public SubscribingObject(Subscriber bus) {
        bus.subscribe(this);
    }
  
    @Subscribe
    public String onAnyValue(Event e) {
        return "Received event with value " + e.value;
    }
  
    @Subscribe("value>0")
    public String onStrictPositiveValue(Event e) {
        return "Event "+e+" is strictly positive ";
    }
    
    @Subscribe
    public void print(String msg) {
        System.out.println(msg);
    }
}
```

### Publishing an event
Delivery of events is ensured by the interface Publisher, which can be used to asynchronously post events:
``` java
public class PublishingObject {
    public PublishingObject(Publisher bus) {
        bus.publish(new Event(10))
        bus.publish(new Event(-10))
    }
}
```

### Configuring the event bus
``` java
public class Application {
    public static void main (String[] args) {
    	Bus bus = new EventBus(10);
    	new SubscribingObject(bus);
    	new PublishingObject(bus);
    }
}
```

## Licence

    Copyright (C) 2014 University Lille 1, Inria

    This library is free software; you can redistribute it and/or modify
    it under the terms of the GNU Library General Public License as published
    by the Free Software Foundation; either version 2 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Library General Public
    License for more details.

    You should have received a copy of the GNU Library General Public License
    along with this library; if not, write to the Free Software Foundation,
    Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301, USA.
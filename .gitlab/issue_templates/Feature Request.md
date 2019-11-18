# Feature Request
<!--
  This is for requesting new features, or improvements/additions
  to existing features.

  This is pre-filled with example values, feel free to
  remove them before populating the template.
  
  If you feel a heading is irrelevent, just remove it.
-->

## Sub-Project
<!-- 
  Can you specify which sub-project(s) this is relevant to? If you aren't sure,
  or this is a proposal for a new sub-project, you can delete this.
-->
This is regarding:
* core

## Description
<!-- 
  Explain what this is about, try to use full sentences, and make your point clear.
-->
As a developer I would like to have configuration to be handled for me, where the object
and values from the configuration are mapped, and I just have to say how via
configuration, or annotations rather than implementing and using the configuration
abstraction in my configuration objects constructor which leaves a lot of room for error.

## Motivation
<!--
  Why is this a feature that should be implemented in Commandler
  rather than in your own project using it?
  What makes this fundamental, or reusable, or of demand to 
  other developers?
-->
This is more similar to what other larger frameworks do, and leaves less room for
error, also by doing it this way, it's easier to standardise how configuration is loaded
between applications. As a result Commandler would be able to use the javax.validation
implemention on the configuration as well to further validate the configuration
before starting up all integrations.

## Interface
<!--
  In some cases you may wish to propose an interface or method names
  to help describe how you'd want to use this, or for others to discuss
  and improve ahead of time before final implementation.
-->
```java
@Configuration("myapp.database")
public class DatabaseConfig {
    
    /** myapp.database.username */
    @NotNull
    private String username;
    
    /** myapp.database.port */
    @Min(Short.MIN_VALUE)
    @Max(Short.MAX_VALUE)
    private int port;   

    public String getUsername() {
        return username;
    }  

    public int getPort() {
        return port;
    }
}
```

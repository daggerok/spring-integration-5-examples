# spring-integration-5-examples [![Build Status](https://travis-ci.org/daggerok/spring-integration-5-examples.svg?branch=master)](https://travis-ci.org/daggerok/spring-integration-5-examples)

this repository is containing spring-integration 5 examples

#### build, run, test

```bash
bash gradle clean build bootRun
http :8080/sse
# output:
data:foo

data:bar

data:baz
```

## inportant

this will works (at lease right now) only with these versions:

```gradle
buildscript {
  ext {
    springBootVersion = "2.0.0.M3"
    springIntegrationVersion = "5.0.0.M6"
  }
  // ...
```

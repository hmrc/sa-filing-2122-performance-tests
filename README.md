
# sa-filing-2122-performance-tests

Running the simulation:

to run a test against STAGING:-

sbt -Dperftest.runSmokeTest=false -DrunLocal=false -Dperftest.loadPercentage=10 -Dperftest.labels=Core gatling:test



to run a smoke test against STAGING:-

sbt -Dperftest.runSmokeTest=true -DrunLocal=false -Dperftest.loadPercentage=10 -Dperftest.labels=Core gatling:test
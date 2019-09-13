;[]// Example of a query that only retrieves five results at each depth.
;[]// Note that a groupCount step with a label parameter acts as a
;[]// side effect rather than a barrier.
;[]//
;[]// This example is designed to be run from the Gremlin console.

g.V().has('code','SFO').
       repeat(out().groupCount('airports').by(loops()).
              where(select('airports').select(loops()).is(lte(5)))).
       emit().
       times(3).
       path().
         by('code') 

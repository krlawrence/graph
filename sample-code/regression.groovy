;[] // Regression tests designed to be run from the Gremlin Console with the
;[] // air routes graph loaded. This script is tuned for the version of the graphml
;[] // file shipped with the book (0.77). The Gremlin version should be 3.3.1 or
;[] // higher. This script is intended to be run each time a new Apache TinkerPop
;[] // version is released to make sure the examples in the book are still working.
;[] // This script will be added to as time permits to increase coverage.

println "-------------";[]
println "Running tests";[]
println "-------------";[]

println "Checking Gremlin version";[]
ver=Gremlin.version();
assert ver[0]=='3' && ver[2]=='3' && ver[4].toInteger() >= 1;[]

println "Checking air-routes version";[]
ver=g.V().hasLabel('version').values('code').next();
assert ver=='0.77';[]

;[] //-------------------------------------------------------------------------
println "Checking simple traversals";[]
d=g.V().has('code','DFW').
        outE().as('a').inV().
        has('code','AUS').
        select('a').values('dist').next();[]
assert d == 190;[]       

;[] //-------------------------------------------------------------------------
println "Checking 'order' and 'path'";[]
a=g.V().has('airport','code','LCY').outE().inV().
        order().by('code').path().by('code').by('dist').by('code').toList();[]
assert a[0][0]=='LCY';[] 
assert a[0][1]==404;[] 
assert a[0][2]=='ABZ';[] 

;[] //-------------------------------------------------------------------------
println "Checking 'group'";[]
m=g.V().hasLabel('continent').group().by('code').by(out().count()).next();[]
assert m['EU']==583;[]

println "Checking 'groupCount' and 'select'";[]
m=g.V().hasLabel('airport').
        groupCount().by('country').select('FR','GR','BE').next();[]
assert m['GR']==39;[]

println "Checking 'group' and 'order' using 'by(keys,incr)'";[]
m=g.V().has('airport','country','US').
        group().by('code').by('runways').
        order(local).by(keys,incr).next();[]

assert m.size()==579;[]
assert m['ABE'][0]==2;[]
assert m['DFW'][0]==7;[]

;[] //-------------------------------------------------------------------------
println "Checking 'where' with 'by'";[]
c=g.V().has('airport','code','AUS').as('a').
        out().where(eq('a')).by('runways').count().next();[]
assert c==9;[]

println "Checking 'where' with 'is', 'gt' and inline 'and'" ;[]
c=g.V().has('airport','code','AUS').out().
        where(values('runways').is(gt(4).and(neq(6)))).
        valueMap('code','runways').count().next();[]
assert c==6;[]

println "Checking 'where' with 'is', 'gt' and inline 'or'" ;[]
c=g.V().has('airport','code','AUS').out().
        where(values('runways').is(gt(6)).or().values('runways').is(4)).
        valueMap('code','runways').count().next();[]
assert c==24;[]

println "Checking 'filter' and a 'where'  with two parameters" ;[]
c=g.V().has('code','AUS').as('a').out().as('b').
        filter(select('a','b').by('runways').where('a',eq('b'))).
        valueMap('code','runways').count().next();[]
assert c==9;[]

println "Checking 'where' with two 'by' steps and 3 parameter 'select'";[]
c=g.V().has('airport','city','London').as('a','r').
        in('contains').as('b').
        where('a',eq('b')).by('country').by('code').
        select('a','r','b').by('code').by('region').count().next();[]
assert c==6;[]

;[] //-------------------------------------------------------------------------
println "Checking two V steps and 'select' inside a 'local' step";[]
c=g.V().has('code','NCE').values('region').as('r').
    V().hasLabel('airport').as('a').
        values('region').where(eq('r')).by().
        local(select('a').values('city','code','region').fold()).
        count().next();[]
assert c==4;[]

;[] //-------------------------------------------------------------------------
println "Checking 'choose' with two 'choices'";[]
a= g.V().hasLabel('airport').has('country','US').limit(15).
         choose(values('code').is(within('AUS','DFW')),
            values('city'),
            values('region')).order().toList();[]

assert a[0]=='Austin';[]
assert a[1]=='Dallas';[]
assert a[2]=='US-AK';[]

;[] //-------------------------------------------------------------------------
println "Checking 'match'";[]
c= g.V().hasLabel('airport').
         match(__.as('s').out().as('d'),
               __.not(__.as('d').out().as('s'))).
         select('s','d').by('code').count().next();[]
assert c==238;[]

;[] //-------------------------------------------------------------------------
println "Checking 'union'";[]
a=g.V().has('city','London').has('region','GB-ENG').
        local(union(__.values('code'),
                    out('route').has('city','Paris').values('code'),
                    out('route').has('city','Berlin').values('code')).
        fold()).toList();[]

assert a.flatten() - ['LHR','ORY','CDG','TXL','LGW','LCY','STN','LTN','SXF'] == [];[]

;[] //-------------------------------------------------------------------------
println "Checking 'math' addition";[]
d=g.V().has('airport','code','DFW').as('a').
        out().has('code','SFO').as('b').
        math('a + b').by('runways').next();[]
assert d==11.0;[]

println "Checking 'math' sine";[]
x=g.inject(60*(Math.PI/180)).math('sin(_)').next();[]
assert x == 0.8660254037844386;[]

;[] //-------------------------------------------------------------------------
println "Checking 'aggregate' and 'without'";[]
c=g.V().has('code','AUS').out().aggregate('nonstop').
     out().where(without('nonstop')).dedup().count().next();[]
assert c==812;[]

;[] //-------------------------------------------------------------------------
println "Checking 'coalesce'";[]
s=g.V().has('code','AUS').
        coalesce(out().has('code','SYD'),identity()).values('city').next();[]
assert s=='Austin';[]

s=g.V().has('code','AUS').
        coalesce(out().has('code','DFW'),identity()).values('city').next();[]
assert s=='Dallas';[]

;[] //-------------------------------------------------------------------------
println "Checking vertex and edge creation";[]
graph2=TinkerGraph.open();[]
g2=graph2.traversal();[]
g2.addV('root').property('data',9).as('root').
   addV('node').property('data',5).as('b').
   addV('node').property('data',2).as('c').
   addV('node').property('data',11).as('d').
   addV('node').property('data',15).as('e').
   addV('node').property('data',10).as('f').
   addV('node').property('data',1).as('g').
   addV('node').property('data',8).as('h').
   addV('node').property('data',22).as('i').
   addV('node').property('data',16).as('j').
   addE('left').from('root').to('b').
   addE('left').from('b').to('c').
   addE('right').from('root').to('d').
   addE('right').from('d').to('e').
   addE('right').from('e').to('i').
   addE('left').from('i').to('j').
   addE('left').from('d').to('f').
   addE('right').from('b').to('h').
   addE('left').from('c').to('g').iterate();[]

println graph2.toString();[]
cv = g2.V().count().next();[]
assert cv == 10;[]

ce = g2.E().count().next();[]
assert ce == 9;[]

;[] //-------------------------------------------------------------------------
println "Checking 'repeat'";[]
c=g2.V().hasLabel('root').
         repeat(out('right')).
           until(out('right').count().is(0)).
         values('data').next();[]
assert c == 22;[]

c=g2.V().hasLabel('root').
         repeat(out('left')).
           until(__.not(out('left'))).
         values('data').next();[]
assert c == 1;[]

a=g2.V().hasLabel('root').
         repeat(out('left')).
           until(__.not(out('left'))).
         path().by('data').next().toList();[]
assert a.flatten() == [9,5,2,1];[]

a=g.V().has('airport','region','US-CA').
        order().by('city').limit(5).
        group().by('code').by('city').
        select(values).
        repeat(unfold()).
          until(count(local).is(1)).
        unfold().toList();[]
assert a[1] == 'Bakersfield';[]

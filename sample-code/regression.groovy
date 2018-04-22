;[] // Regression tests designed to be run from the Gremlin Console with the
;[] // air routes graph loaded. This script is tuned for the version of the graphml
;[] // file shipped with the book (0.77). The Gremlin version should be 3.3.1 or
;[] // higher. This script is intended to be run each time a new Apache TinkerPop
;[] // version is released to make sure the examples in the book are still working.
;[] // This script will be added to as time permits to increase coverage.
;[] // This script assumes the 'graph' and 'g' variables have been defined.

println "\n\n-------------";[]
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

c=g.V().has('code','DEN').out().
        has('country','MX').count().next();[]

assert c == 7;[]

a=g.V().has('code','DEL').out().as('a').in("contains").
        has('code','EU').select('a').by('city').toList();[]

assert a.size() == 17;[]
assert a.contains('Helsinki');[]
assert a.sort().subList(0,3).flatten() == ['Amsterdam','Birmingham','Brussels'];[]


c=g.V().outE().has('dist',within(100..200)).count().next();[]

assert c == 3029;[]

c=g.V().has('airport','country','US').
        outE().has('dist',within(100..200)).
        inV().has('country','US').
        count().next();[]

assert c == 583;[]        


;[] //-------------------------------------------------------------------------
println "Checking simple 'has' steps";[]
c=g.V().has('region','US-TX').has('longest',gte(12000)).count().next();[]
assert c == 6;[]       

c=g.V().hasLabel('airport').count().next();[]
assert c == 3374;[]

c=g.V().has('region','US-CA').count().next();[]
assert c==29;[]

;[] //-------------------------------------------------------------------------
println "Checking 'order' and 'path'";[]
a=g.V().has('airport','code','LCY').outE().inV().
        order().by('code').path().by('code').by('dist').by('code').toList();[]
assert a[0][0]=='LCY';[] 
assert a[0][1]==404;[] 
assert a[0][2]=='ABZ';[] 
assert a[0] instanceof org.apache.tinkerpop.gremlin.process.traversal.step.util.MutablePath;[]

println "Checking 'path'";[]
p=g.V().has('code','AUS').outE().inV().
        has('code','MEX').
        path().by('code').by('dist').next();[]

assert p instanceof org.apache.tinkerpop.gremlin.process.traversal.step.util.MutablePath;[]
assert p.isSimple();[]
assert p.head() == 'MEX';[]
assert p[1] == 748;[]

println "Checking 'cyclicPath'";[]
p=g.V().has('code','AUS').repeat(out()).until(cyclicPath()).
        limit(1).path().by('code').next();[]
assert !p.isSimple();[]
assert p[0] == p[-1];[]

;[] //-------------------------------------------------------------------------
println "Checking 'group'";[]
m=g.V().hasLabel('continent').group().by('code').by(out().count()).next();[]
assert m instanceof  java.util.HashMap;[]
assert m['EU']==583;[]

println "Checking 'groupCount'";[]
m=g.V().hasLabel('airport').groupCount().by('runways').next();[]
assert m[1] == 2316;[]

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

println "Checking 'group' with 'count(local)'";[]
c=g.V().hasLabel('airport').limit(5).
      group().by('code').by('city').
      count(local).next();[]
assert c == 5;[]

println "Checking 'group' with 'unfold' and 'select(keys)'";[]
a=g.V().has('airport','country','IE').
      group().by('code').by('runways').select(keys).
      unfold().order().fold().next();[]
assert a.size() == 7;[]
assert a[0] == 'CFN';[]
assert a == ['CFN','DUB','KIR','NOC','ORK','SNN','WAT'];[]

println "Checking 'order' with 'valueMap' and 'select'";[]
a=g.V().hasLabel('airport').
        order().by('longest',decr).valueMap().
        select('code','longest').limit(10).toList();[]

assert a[0]['code'][0] == 'BPX';[]      
assert a[0]['longest'][0] == 18045;[]      


;[] //-------------------------------------------------------------------------
println "Checking 'project'";[]
a=g.V().has('airport','region','GB-ENG').
        project('IATA','Routes').
          by('code').by(out().count()).
        order().by(select('Routes'),decr).toList();[]

assert a[0]['IATA'] == 'LGW';[]
assert a[0]['Routes'] == 200;[]
assert a.size==27;[]

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

println "Checking 'where' with 'count(local)'" ;[]
c=g.V().hasLabel('airport').
        where(out('route').count().is(gt(180))).
        values('code').fold().count(local).next();[]
assert c == 25;[]

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
println "Checking 'choose' with two choices";[]
a= g.V().hasLabel('airport').has('country','US').limit(15).
         choose(values('code').is(within('AUS','DFW')),
            values('city'),
            values('region')).order().toList();[]

assert a[0]=='Austin';[]
assert a[1]=='Dallas';[]
assert a[2]=='US-AK';[]

println "Checking 'choose' with  three 'options'";[]
a=g.V().hasLabel('airport').
        choose(values('code')).
           option('DFW',values('icao')).
           option('AUS',values('region')).
           option('LAX',values('code')).fold().next();[]

assert a.sort() == ['KDFW','LAX','US-TX'];[]                            

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
println "Checking 'without'";[]
a=g.V().has('airport','code','AUS').
        out().has('code',without('DFW','LAX')).
        out().has('code','SYD').path().by('code').toList();[]
assert a.size() == 1;[]
assert a[0].flatten() == ['AUS','SFO','SYD'];[]

c=g.V().has('runways',without(3..6)).values('code','runways').fold().count(local).next();[]
assert c == 6160;[]

println "Checking 'aggregate' and 'without'";[]
c=g.V().has('code','AUS').out().aggregate('nonstop').
     out().where(without('nonstop')).dedup().count().next();[]
assert c==812;[]

println "Checking 'within'";[]
c=g.V().has('runways',within(3..6)).values('code','runways').count().next();[]
assert c == 588;[]

c=g.V().has('runways',within(1,2,3)).values('code','runways').count().next();[]
assert c == 6606;[]

a=g.V().has('airport','code','AUS').
       out().has('code',within('DFW','DAL','IAH','HOU','SAT')).
       out().has('code','LAS').path().by('code').toList();[]
assert a.size() == 4;[]

println "Checking 'between'";[]
c=g.V().has('runways',between(5,8)).values('code','runways').fold().count(local).next();[]
assert c == 38;[]

println "Checking 'inside'";[]
c=g.V().has('runways',inside(3,6)).values('code','runways').count().next();[]
assert c == 130;[]

println "Checking 'outside'";[]
c=g.V().has('lat',outside(-50,77)).order().by('lat',incr).count().next();[]
assert c == 10;[]

;[] //-------------------------------------------------------------------------
println "Checking boolean 'or', 'and' and 'not' operators";[]
c=g.V().hasLabel('airport').
        or(has('region','US-TX'),                                 
           has('region','US-LA'),                                 
           has('region','US-AZ'),                                 
           has('region','US-OK')).                                
        order().by('region',incr).                               
        valueMap().select('code','region').count().next();[]    
assert c == 48;[]

c=g.V().hasLabel('airport').
        and(has('region','US-TX'),
            has('longest',gte(12000))).
        values('code').count().next();[]
assert c == 6;[]

a=g.V().has('airport','code','AUS').
        out().and(has('code',neq('DFW')),has('code',neq('LAX'))).
        out().has('code','SYD').path().by('code').toList();[]
assert a.size() == 1;[]
assert a[0].flatten() == ['AUS','SFO','SYD'];[]

c=g.V().not(inE()).count().next();[]
assert c == 245;[]

c=g.V().not(bothE()).count().next();[]
assert c == 7;[]

c=g.V().not(hasLabel('airport')).count().next();[]
assert c == 245;[]

;[] //-------------------------------------------------------------------------
println "Checking 'coalesce'";[]
s=g.V().has('code','AUS').
        coalesce(out().has('code','SYD'),identity()).values('city').next();[]
assert s=='Austin';[]

s=g.V().has('code','AUS').
        coalesce(out().has('code','DFW'),identity()).values('city').next();[]
assert s=='Dallas';[]

s=g.V().has('code','AUS').
        coalesce(out().has('code','SYD'),constant('no route')).next();[]
assert s == 'no route';[]


;[] //-------------------------------------------------------------------------
println "Checking 'withSideEffect', 'store' and a Set";[]
s=g.withSideEffect('s', [] as Set).
  V().hasLabel('airport').values('runways').
      store('s').cap('s').order(local).next();[]
assert s instanceof java.util.ArrayList;[]
assert s.size == 8;[]
assert s == [1,2,3,4,5,6,7,8];[]

;[] //-------------------------------------------------------------------------
println "Checking 'BulkSet' from 'store'";[]
s=g.V().has('region','US-MA').store('r').by('runways').cap('r').next();[]
assert s instanceof org.apache.tinkerpop.gremlin.process.traversal.step.util.BulkSet;[]
assert s.size() == 7;[]
assert s.flatten().size() == 4;[]
assert s.uniqueSize() == 4;[]
assert s.contains(6);[]
assert s.asBulk()[2] == 3;[]
assert s.asBulk()[3] == 2;[]

println "Checking 'BulkSet' from 'aggregate'";[]
s=g.V().has('airport','country','IE').aggregate('ireland').cap('ireland').next();[]
assert s instanceof org.apache.tinkerpop.gremlin.process.traversal.step.util.BulkSet;[]
assert s.size() == 7;[]
assert s.uniqueSize() == 7;[]

;[] //-------------------------------------------------------------------------
println "Checking property keys";[]
a=g.V().has('airport','code','DFW').properties().key().fold().next();[]
assert a.size() == 12;[]
assert a == ['country','code','longest','city','elev','icao','lon','type','region','runways','lat','desc'];[]

println "Checking property cardinality of LIST";[]
g.addV('test').property('a',1).property('a',2).property('a',3).iterate();[]
g.V().hasLabel('test').property(list,'a',4).iterate();[]
a=g.V().hasLabel('test').valueMap().select('a').next();[]
assert a.getClass() == java.util.ArrayList;[]
assert a == [1,2,3,4];[]
g.V().hasLabel('test').drop().iterate();[]

println "Checking property cardinality of SET";[]
g.addV('test').property(set,'s',1).iterate();[]
g.V().hasLabel('test').property(set,'s',2).property(set,'s',1).iterate();[]
g.V().hasLabel('test').property(set,'s',2).property(set,'s',4).iterate();[]
s=g.V().hasLabel('test').valueMap().select('s').next();[]
assert s.sort() == [1,2,4];[]
g.V().hasLabel('test').drop().iterate();[]

println "Checking meta properties";[]
g.V().has('code','AUS').
      properties().hasValue('AUS').
      property('date','6/6/2017').iterate();[]
mp=g.V(3).properties('code').properties('date').next();[]
assert mp instanceof org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerProperty;[]
assert mp.value() == '6/6/2017';[]
g.V(3).properties('code').properties('date').drop().iterate();[]
mp=g.V(3).properties('code').properties().fold().next();[]
assert mp==[];[]

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

cv = g2.V().count().next();[]
assert cv == 10;[]

ce = g2.E().count().next();[]
assert ce == 9;[]

println "Checking vertex label creation using a traversal";[]
v=g.addV(V().has('code','AUS').label()).property('code','XYZ').next();[]
assert v.label == 'airport';[] 
g.V(v).drop();

;[] //-------------------------------------------------------------------------
println "Checking 'repeat until'";[]
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

println "Checking 'repeat' with 'unfold'";[]
a=g.V().has('airport','region','US-CA').
        order().by('city').limit(5).
        group().by('code').by('city').
        select(values).
        repeat(unfold()).
          until(count(local).is(1)).
        unfold().toList();[]

assert a[1] == 'Bakersfield';[]
assert a[4] == 'Burbank';[]

println "Checking 'repeat' with 'emit'";[]
p= g.V().has('airport','code','AUS').
      repeat(out()).emit().times(3).has('code','MIA').
      limit(5).path().by('code').next();[]

assert p instanceof org.apache.tinkerpop.gremlin.process.traversal.step.util.MutablePath;[]
assert p.isSimple();[]
assert p[0] == 'AUS';[]
assert p.head() == 'MIA';[]
assert p.size() == 2;[]

;[] //-------------------------------------------------------------------------
println "Checking vertex and edge deletion";[]

g2.V().has('data',15).drop().iterate();[]
cv = g2.V().count().next();[]
assert cv == 9;[]

ce = g2.E().count().next();[]
assert ce == 7;[]                    


;[] //-------------------------------------------------------------------------
println "Checking 'sack' with 'assign' using a 'constant'";[]
a=g.V().sack(assign).by(constant(1)).
        has('code','SAF').
        out().values('runways').
        sack(sum).sack().fold().next();[]
assert a.flatten() == [8,5,4,7];[]

println "Checking 'sack' with 'assign' using a property value'";[]
a=g.V().has('code','AUS').
        sack(assign).by('runways').
    V().has('code','SAF').
        out().
        sack(mult).by('runways').
        sack().fold().next();[]
assert a.flatten() == [14,8,6,12];[]

println "Checking 'sack' with 'addAll'";[]
a=g.withSack([]).
    V().has('code','SAF').
        out().values('runways').fold().
        sack(addAll).sack().next();[]
assert a.flatten() == [7,4,3,6];[]

println "Checking 'sack' with zero base and 'sum'";[]
a=g.withSack(0).
    V().has('code','AUS').
        outE().
        sack(sum).by('dist').
        inV().
        outE().
        sack(sum).by('dist').
        inV().has('code','LHR').
        sack().
        order().by(incr).limit(10).
        path().
          by('code').
          by('dist').
          by('code').
          by('dist').
          by('code').
          by().toList();[]
assert a[0][5] == 4893;[]

println "Checking 'sack' with 'assign' using a 'constant' and 'min'";[]
a=g.V().sack(assign).by(constant(400)).has('code','SAF').
      outE().sack(min).by('dist').sack().fold().next();[]
assert a.size == 4;[]
assert a == [400,400,369,303];[]

println "Checking 'sack' with 'assign' using a 'constant' and 'max'";[]
a=g.V().sack(assign).by(constant(400)).has('code','SAF').
      outE().sack(max).by('dist').sack().fold().next();[]
assert a.size == 4;[]
assert a ==[549,708,400,400];[]

;[] //-------------------------------------------------------------------------
println "Checking side effects and lambdas";[]
c = 0;[]
g.V().has('region','US-OR').sideEffect{ c += 1 }.values('code').fold().next();[]
assert c == 7;[]

c=g.V().hasLabel('airport').
        filter{it.get().property('city').value() =="London"}.
        count().next();[]
assert c == 6;[]      

c=g.V().hasLabel('airport').as('a').values('desc').
       filter{it.toString().contains('F.')}.select('a').
       local(values('code','desc').fold()).count().next();[]
assert c == 4;[]

c=g.V().has('airport','type','airport').
        filter{it.get().property('city').value ==~/Dallas|Austin/}.
        values('code').count().next();[]
assert c == 3;[]

c=g.V().has('airport','type','airport').
        filter{it.get().property('city').value()==~/^Dal\w*/}.
        values('city').count().next();[]
assert c == 7;[]

println "Checking 'map' and a lambda";[]
a=g.V().has('airport','region','GB-ENG').
        map{it.get().value('code')+" "+it.get().value('city')}.toList();[]
assert a.sort()[0] == 'BHX Birmingham';[]


;[] //-------------------------------------------------------------------------
println "Checking custom predicates";[]
a = g.V().hasLabel('airport').values('longest').mean().next();[]
f = {x,y -> x > y*2};[]
c=g.V().hasLabel('airport').has('longest',test(f,a)).values('code').count().next();[]
assert c == 9;[]

bp = new java.util.function.BiPredicate<String, String>() {
         boolean test(String val, String pattern) {
           return val ==~ pattern  }};[]
regex = {new P(bp, it)};[]
c= g.V().has('desc', regex(/^Dal.*/)).count().next();[]
assert c == 5;[]


;[] //-------------------------------------------------------------------------
println "Checking classes and types";[]
assert graph instanceof org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;[]

cls = g.V().has('airport','code','DFW').next();[]
assert cls instanceof org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerVertex;[]

edge = g.V().has('airport','code','DFW').outE().limit(1).next();[]
assert edge instanceof org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerEdge;[]

cls = g.V().has('code','LHR').valueMap().next();[]
assert cls instanceof java.util.HashMap;[]

assert Pop.values() == [first,last,all,mixed];[]

assert Operator.values() == [sum,minus,mult,div,min,max,assign,and,or,addAll,sumLong];[]

assert T.values() == [label,id,key,value];[]

assert Column.values() == [keys,values];[]

assert Scope.values() == [global,local];[]


;[] //-------------------------------------------------------------------------
println "Checking user defined Groovy methods";[]
def dist(g,from,to) {
  d=g.V().has('code',from).outE().as('a').inV().has('code',to)
         .select('a').values('dist').next()
  return d };[]
assert dist(g,'AUS','LHR') ==  4901;[]



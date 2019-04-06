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

n = 0;[]

def status(msg,ctr) {
  printf("%4d : %s\n",ctr,msg);
  return ctr+1; } ;[]

;[] //-------------------------------------------------------------------------

n=status("Checking Gremlin version",n);[]
ver=Gremlin.version();[]
println "Reported version is $ver";[]

assert ver[0]=='3' && ((ver[2]=='3' && ver[4].toInteger() >= 1) || (ver[2]=='4' && ver[4].toInteger() >= 0));[]

n=status( "Checking air-routes version",n);[]
ver=g.V().hasLabel('version').values('code').next();[]
println "Reported version is $ver";[]
assert ver=='0.77';[]

;[] //-------------------------------------------------------------------------
n=status( "Checking simple traversals with 'outE' and 'inV'",n);[]
d=g.V().has('code','DFW').
        outE().as('a').inV().
        has('code','AUS').
        select('a').values('dist').next();[]
assert d == 190;[] 

n=status( "Checking traversal of form 'has().out().has()'",n);[]
c=g.V().has('code','DEN').out().
        has('country','MX').count().next();[]

assert c == 7;[]

n=status( "Checking traversal 'has().out().as()' and 'select'",n);[]
a=g.V().has('code','DEL').out().as('a').in("contains").
        has('code','EU').select('a').by('city').toList();[]

assert a.size() == 17;[]
assert a.contains('Helsinki');[]
assert a.sort().subList(0,3).flatten() == ['Amsterdam','Birmingham','Brussels'];[]

n=status( "Checking edge dist property 'within(100..200)'",n);[]
c=g.V().outE().has('dist',within(100..200)).count().next();[]

assert c == 3029;[]

c=g.V().has('airport','country','US').
        outE().has('dist',within(100..200)).
        inV().has('country','US').
        count().next();[]

assert c == 583;[]        

;[] //-------------------------------------------------------------------------
n=status( "Checking 'hasNext' and 'tryNext'",n);[]
assert g.V().has('code','AUS').out('route').hasNext();[]
assert g.V().has('code','AUS').out('route').next();[]
assert g.V(3).out('route').tryNext();[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'optional' when test fails",n);[]
s=g.V().has('code','AUS').optional(out().has('code','SYD')).values('city').next();[]
assert s == 'Austin';[]

n=status( "Checking 'optional' when test matches",n);[]
s=g.V().has('code','DFW').optional(out().has('code','SYD')).values('city').next();[]
assert s == 'Sydney';[]

;[] //-------------------------------------------------------------------------
n=status( "Checking simple 'has' steps",n);[]
c=g.V().has('region','US-TX').has('longest',gte(12000)).count().next();[]
assert c == 6;[]       


n=status( "Checking 'hasLabel' and 'count'",n);[]
c=g.V().hasLabel('airport').count().next();[]
assert c == 3374;[]

n=status( "Checking 'has('region','US-CA')' and 'count'" ,n);[]
c=g.V().has('region','US-CA').count().next();[]
assert c==29;[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'order' and 'path'",n);[]
a=g.V().has('airport','code','LCY').outE().inV().
        order().by('code').path().by('code').by('dist').by('code').toList();[]
assert a[0][0]=='LCY';[] 
assert a[0][1]==404;[] 
assert a[0][2]=='ABZ';[] 
assert a[0] instanceof org.apache.tinkerpop.gremlin.process.traversal.step.util.MutablePath;[]

n=status( "Checking 'path'",n);[]
p=g.V().has('code','AUS').outE().inV().
        has('code','MEX').
        path().by('code').by('dist').next();[]

assert p instanceof org.apache.tinkerpop.gremlin.process.traversal.step.util.MutablePath;[]
assert p.isSimple();[]
assert p.head() == 'MEX';[]
assert p[1] == 748;[]

n=status( "Checking 'path' with 'count(local)'",n);[]
c=g.V().has('code','AUS').out().out().out().limit(1).
        path().count(local).next();[]
assert c == 4;[]

n=status( "Checking 'cyclicPath'",n);[]
p=g.V().has('code','AUS').repeat(out()).until(cyclicPath()).
        limit(1).path().by('code').next();[]
assert !p.isSimple();[]
assert p[0] == p[-1];[]


;[] //-------------------------------------------------------------------------
n=status( "Checking 'group'",n);[]
m=g.V().hasLabel('continent').group().by('code').by(out().count()).next();[]
assert m instanceof  java.util.HashMap;[]
assert m['EU']==583;[]

n=status( "Checking 'group' for London airports",n);[]
m=g.V().hasLabel('continent').group().by('code').by(out().count()).next();[]
m = g.V().has('region','GB-ENG').has('city','London').
          group().by('code').by(out().count()).next();[]
assert m['LCY']==42;[]          
assert m['LHR']==191;[]          
assert m['LTN']==104;[]          
assert m['STN']==186;[]          
assert m['LGW']==200;[]          

n=status( "Checking 'groupCount'",n);[]
m=g.V().hasLabel('airport').groupCount().by('runways').next();[]
assert m[1] == 2316;[]

n=status( "Checking 'groupCount' and 'select'",n);[]
m=g.V().hasLabel('airport').
        groupCount().by('country').select('FR','GR','BE').next();[]
assert m['GR']==39;[]

n=status( "Checking 'group' and 'order' using 'by(keys,incr)'",n);[]
m=g.V().has('airport','country','US').
        group().by('code').by('runways').
        order(local).by(keys,incr).next();[]

assert m.size()==579;[]
assert m['ABE'][0]==2;[]
assert m['DFW'][0]==7;[]

n=status( "Checking 'group' with 'count(local)'",n);[]
c=g.V().hasLabel('airport').limit(5).
      group().by('code').by('city').
      count(local).next();[]
assert c == 5;[]

n=status( "Checking 'group' with 'unfold' and 'select(keys)'",n);[]
a=g.V().has('airport','country','IE').
      group().by('code').by('runways').select(keys).
      unfold().order().fold().next();[]
assert a.size() == 7;[]
assert a[0] == 'CFN';[]
assert a == ['CFN','DUB','KIR','NOC','ORK','SNN','WAT'];[]

n=status( "Checking nested 'group' steps",n);[]
a=g.V().hasLabel("airport").limit(5).
        group().
          by('code').
          by(out("route").limit(5).
             group().
               by('code').
               by(out("route").count())).
        select('ANC').
        select('IAH','LAX').next();[]
assert a['IAH'] == 192;[]
assert a ['LAX'] == 195;[]


n=status( "Checking 'order' with 'valueMap' and 'select'",n);[]
a=g.V().hasLabel('airport').
        order().by('longest',decr).valueMap().
        select('code','longest').limit(10).toList();[]

assert a[0]['code'][0] == 'BPX';[]      
assert a[0]['longest'][0] == 18045;[]

n=status( "Checking 'group' with 'select(values)'",n);[]
a=g.V().has('code','DFW').project('dfw','route_count').
        by().by(outE().count()).select(values).next();[]

assert a[0] == g.V().has('code','DFW').next();[]
assert a[1] == 221;[]

n=status( "Checking 'order by('lobgest',decr)' with 'valueMap' and 'select''",n);[]
a=g.V().hasLabel('airport').
        order().by('longest',decr).valueMap().
        select('code','longest').limit(10).toList();[]

assert a.size() == 10;[]
assert a[0]['longest'][0] == 18045;[]
assert a[9]['longest'][0] == 15092;[]
;[] //-------------------------------------------------------------------------
n=status( "Checking 'project'",n);[]
a=g.V().has('airport','region','GB-ENG').
        project('IATA','Routes').
          by('code').by(out().count()).
        order().by(select('Routes'),decr).toList();[]

assert a[0]['IATA'] == 'LGW';[]
assert a[0]['Routes'] == 200;[]
assert a.size==27;[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'where' with 'by'",n);[]
c=g.V().has('airport','code','AUS').as('a').
        out().where(eq('a')).by('runways').count().next();[]
assert c==9;[]

n=status( "Checking 'where' with 'is', 'gt' and inline 'and'",n) ;[]
c=g.V().has('airport','code','AUS').out().
        where(values('runways').is(gt(4).and(neq(6)))).
        valueMap('code','runways').count().next();[]
assert c==6;[]

n=status( "Checking 'where' with 'is', 'gt' and inline 'or'",n) ;[]
c=g.V().has('airport','code','AUS').out().
        where(values('runways').is(gt(6)).or().values('runways').is(4)).
        valueMap('code','runways').count().next();[]

assert c==24;[]

n=status( "Checking 'where' with 'count(local)'",n) ;[]
c=g.V().hasLabel('airport').
        where(out('route').count().is(gt(180))).
        values('code').fold().count(local).next();[]

assert c == 25;[]

n=status( "Checking 'filter' and a 'where'  with two parameters",n) ;[]
c=g.V().has('code','AUS').as('a').out().as('b').
        filter(select('a','b').by('runways').where('a',eq('b'))).
        valueMap('code','runways').count().next();[]

assert c==9;[]

n=status( "Checking 'where' with two 'by' steps and 3 parameter 'select'",n);[]
c=g.V().has('airport','city','London').as('a','r').
        in('contains').as('b').
        where('a',eq('b')).by('country').by('code').
        select('a','r','b').by('code').by('region').count().next();[]

assert c==6;[]

n=status( "Checking 'where('a',lt('b'))'",n);[]
a=g.V().has('region','GB-ENG').order().by('code').as('a').
        out().has('region','GB-ENG').as('b').
        where('a',lt('b')).by('code').
        path().by('code').toList();[]

assert a.size() == 22;[]
assert a[0][0] == 'BHX';[]
assert a[0][1] == 'NCL';[]
assert a[-1][0] == 'NCL';[]
assert a[-1][1] == 'SOU';[]


;[] //-------------------------------------------------------------------------
n=status( "Checking two V steps and 'select' inside a 'local' step",n);[]
c=g.V().has('code','NCE').values('region').as('r').
    V().hasLabel('airport').as('a').
        values('region').where(eq('r')).by().
        local(select('a').values('city','code','region').fold()).
        count().next();[]
assert c==4;[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'choose' with two choices",n);[]
a= g.V().hasLabel('airport').has('country','US').limit(15).
         choose(values('code').is(within('AUS','DFW')),
            values('city'),
            values('region')).order().toList();[]

assert a[0]=='Austin';[]
assert a[1]=='Dallas';[]
assert a[2]=='US-AK';[]

n=status( "Checking 'choose' with  three 'options'",n);[]
a=g.V().hasLabel('airport').
        choose(values('code')).
           option('DFW',values('icao')).
           option('AUS',values('region')).
           option('LAX',values('code')).fold().next();[]

assert a.sort() == ['KDFW','LAX','US-TX'];[]                            

n=status( "Checking 'choose' as a side effect with labels",n);[]
a=g.V().hasLabel('airport').
        choose(has('runways',4), groupCount('a').by(constant('four'))).
        choose(has('runways',lte(2)), groupCount('a').by(constant('low'))).
        choose(has('runways',gte(6)), groupCount('a').by(constant('high'))).
        choose(has('country','FR'), groupCount('a').by(constant('France'))).
        groupCount('a').by(constant('total')).cap('a').next();[]
assert a['total'] == 3374;[]
assert a['low'] == 3078;[]
assert a['four'] == 51;[]
assert a['France'] == 58;[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'match'",n);[]
c= g.V().hasLabel('airport').
         match(__.as('s').out().as('d'),
               __.not(__.as('d').out().as('s'))).
         select('s','d').by('code').count().next();[]
assert c==238;[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'union'",n);[]
a=g.V().has('city','London').has('region','GB-ENG').
        local(union(__.values('code'),
                    out('route').has('city','Paris').values('code'),
                    out('route').has('city','Berlin').values('code')).
        fold()).toList();[]

assert a.flatten() - ['LHR','ORY','CDG','TXL','LGW','LCY','STN','LTN','SXF'] == [];[]

n=status( "Checking 'union' with 'identity' and 'constant'",n);[]
a=g.V().has('airport','code','LAX').
        union(identity(),constant(1)).toList();[]
assert a[0] == g.V().has('airport','code','LAX').next();[]
assert a[1] == 1;[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'math' addition",n);[]
d=g.V().has('airport','code','DFW').as('a').
        out().has('code','SFO').as('b').
        math('a + b').by('runways').next();[]
assert d==11.0;[]

n=status( "Checking 'math' sine",n);[]
x=g.inject(60*(Math.PI/180)).math('sin(_)').next();[]
assert x == 0.8660254037844386;[]

n=status( "Checking 'max' on 'values('longest')",n);[]
c=g.V().hasLabel('airport').values('longest').max().next();[]
assert c == 18045;[]

n=status( "Checking 'min' on 'values('longest')",n);[]
c=g.V().hasLabel('airport').values('longest').min().next();[]
assert c == 1300;[]

n=status( "Checking 'mean' on 'values('longest')",n);[]
c=g.V().hasLabel('airport').values('longest').mean().next();[]
assert c == 7570.862477771191;[]

n=status( "Checking 'sum' on 'values('longest')",n);[]
c=g.V().hasLabel('airport').values('longest').sum().next();[]
assert c == 25544090;[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'has' with 'eq'",n);[]
c=g.V().has('runways',eq(3)).count().next();[]
assert c == 225;[]

n=status( "Checking 'has' with 'neq'",n);[]
c=g.V().has('runways',neq(1)).count().next();[]
assert c == 1058;[]

n=status( "Checking 'has' with 'gt'",n);[]
c=g.V().has('runways',gt(3)).count().next();[]
assert c == 71;[]

n=status( "Checking 'has' with 'gte'",n);[]
c=g.V().has('runways',gte(3)).count().next();[]
assert c == 296;[]

n=status( "Checking 'has' with 'lt'",n);[]
c=g.V().has('runways',lt(3)).count().next();[]
assert c == 3078;[]

n=status( "Checking 'has' with 'lte'",n);[]
c=g.V().has('runways',lte(3)).count().next();[]
assert c == 3303;[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'without('DFW','LAX')'",n);[]
a=g.V().has('airport','code','AUS').
        out().has('code',without('DFW','LAX')).
        out().has('code','SYD').path().by('code').toList();[]
assert a.size() == 1;[]
assert a[0].flatten() == ['AUS','SFO','SYD'];[]


n=status( "Checking 'aggregate' and 'without'",n);[]
c=g.V().has('code','AUS').out().aggregate('nonstop').
     out().where(without('nonstop')).dedup().count().next();[]
assert c==812;[]

n=status( "Checking 'within(3..6)'",n);[]
c=g.V().has('runways',within(3..6)).values('code','runways').count().next();[]
assert c == 588;[]

n=status( "Checking 'without(3..6)'",n);[]
c=g.V().has('runways',without(3..6)).values('code','runways').count().next();[]
assert c == 6160;[]

n=status( "Checking 'without(3..6)' with 'fold' and 'count(local)'",n);[]
c=g.V().has('runways',without(3..6)).values('code','runways').fold().count(local).next();[]
assert c == 6160;[]

n=status( "Checking 'within(1,2,3)'",n);[]
c=g.V().has('runways',within(1,2,3)).values('code','runways').count().next();[]
assert c == 6606;[]

n=status( "Checking 'without(1,2,3)'",n);[]
c=g.V().has('runways',without(1,2,3)).values('code','runways').count().next();[]
assert c == 142;[]

n=status( "Checking 'within(('DFW','DAL','IAH','HOU','SAT')'",n);[]
a=g.V().has('airport','code','AUS').
       out().has('code',within('DFW','DAL','IAH','HOU','SAT')).
       out().has('code','LAS').path().by('code').toList();[]
assert a.size() == 4;[]

n=status( "Checking 'witout(('DFW','DAL','IAH','HOU','SAT')'",n);[]
a=g.V().has('airport','code','AUS').
       out().has('code',without('DFW','DAL','IAH','HOU','SAT')).
       out().has('code','LAS').path().by('code').toList();[]
assert a.size() == 48;[]

n=status( "Checking 'hasId(within(1..46)'",n);[]
c=g.V().hasId(within(1..46)).out().hasId(between(1,47)).count().next();[]
assert c == 1326;[]

n=status( "Checking 'between(5,8)'",n);[]
c=g.V().has('runways',between(5,8)).values('code','runways').fold().count(local).next();[]
assert c == 38;[]

n=status( "Checking 'between('Dal','Dam')'",n);[]
a=g.V().hasLabel('airport').
        has('city',between('Dal','Dam')).
        values('city').order().dedup().toList();[]
assert a.size() == 6;[]
assert a[0] == 'Dalaman';[]
assert a[5] == 'Dallas';[]

n=status( "Checking 'inside(3,6)'",n);[]
c=g.V().has('runways',inside(3,6)).values('code','runways').count().next();[]
assert c == 130;[]

n=status( "Checking 'outside(-50,77)'",n);[]
c=g.V().has('lat',outside(-50,77)).order().by('lat',incr).count().next();[]
assert c == 10;[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'range(local,3,5)'",n);[]
a=g.V().has('airport','country','IE').
        group().by('code').by('runways').
        order(local).by(keys).select(keys).
        range(local,3,5).next();[]
assert a.size() == 2;[]
assert a.contains('NOC');[]
assert a.contains('ORK');[]

n=status( "Checking 'range(local,0,-1)'",n);[]
a=g.V().has('airport','country','IE').
        group().by('code').by('runways').
        order(local).by(keys).select(keys).
        range(local,0,-1).next();[]  
assert a.size() == 7;[]
assert a.toString() == "[CFN, DUB, KIR, NOC, ORK, SNN, WAT]";[]

n=status( "Checking 'skip(local)'",n);[]
a=g.V().has('airport','country','IE').
        group().by('code').by('runways').
        order(local).by(keys).select(keys).
        skip(local,3).next();[]  
assert a.size() == 4;[]
assert a.toString()  == "[NOC, ORK, SNN, WAT]";[]

n=status( "Checking 'limit(3)'",n);[]
a=g.V().has('country','IE').
        order().by('code').limit(3).
        values('code').fold().next();[]
assert a.size() == 3;[]
assert a == ['CFN','DUB','KIR'];[]

n=status( "Checking 'tail(2)'",n);[]
a=g.V().has('country','IE').
        order().by('code').tail(2).
        values('code').fold().next();[]
assert a.size() == 2;[]
assert a == ['SNN','WAT'];[]

n=status( "Checking 'skip(5)'",n);[]
a=g.V().has('country','IE').
        order().by('code').skip(5).
        values('code').fold().next();[]
assert a.size() == 2;[]
assert a == ['SNN','WAT'];[] 

;[] //-------------------------------------------------------------------------
n=status( "Checking boolean 'or' operator",n);[]
c=g.V().hasLabel('airport').
        or(has('region','US-TX'),                                 
           has('region','US-LA'),                                 
           has('region','US-AZ'),                                 
           has('region','US-OK')).                                
        order().by('region',incr).                               
        valueMap().select('code','region').count().next();[]    
assert c == 48;[]

n=status( "Checking boolean 'and' operator",n);[]
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

n=status( "Checking 'is(gt(3).and(lt(6))'",n);[]
c=g.V().hasLabel('airport').
      where(values('runways').is(gt(3).and(lt(6)))).count().next();[]
assert c == 65;[]

c2=g.V().hasLabel('airport').has('runways',between(4,6)).count().next();[]
assert c == c2;[]

c=status( "Checking 'is(lt(3).and(gt(6))'",n);[]
c=g.V().hasLabel('airport').
        where(values('runways').is(lt(3).or(gt(6)))).count().next();[]
assert c == 3080;[]

n=status( "Checking boolean 'not' operator",n);[]
c=g.V().not(inE()).count().next();[]
assert c == 245;[]

c=g.V().not(bothE()).count().next();[]
assert c == 7;[]

c=g.V().not(hasLabel('airport')).count().next();[]
assert c == 245;[]

n=status( "Checking 'hasNot('runways')'",n);[]
c=g.V().hasNot('runways').count().next();[]
assert c == 245;[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'coalesce' where first param is false",n);[]
s=g.V().has('code','AUS').
        coalesce(out().has('code','SYD'),identity()).values('city').next();[]
assert s=='Austin';[]

n=status( "Checking 'coalesce' where first param is true",n);[]
s=g.V().has('code','AUS').
        coalesce(out().has('code','DFW'),identity()).values('city').next();[]
assert s=='Dallas';[]

n=status( "Checking 'coalesce' with a 'constant' for secon param",n);[]
s=g.V().has('code','AUS').
        coalesce(out().has('code','SYD'),constant('no route')).next();[]
assert s == 'no route';[]


;[] //-------------------------------------------------------------------------
n=status( "Checking 'withSideEffect', 'store' and a Set",n);[]
s=g.withSideEffect('s', [] as Set).
  V().hasLabel('airport').values('runways').
      store('s').cap('s').order(local).next();[]
assert s instanceof java.util.ArrayList;[]
assert s.size == 8;[]
assert s == [1,2,3,4,5,6,7,8];[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'BulkSet' from 'store'",n);[]
s=g.V().has('region','US-MA').store('r').by('runways').cap('r').next();[]
assert s instanceof org.apache.tinkerpop.gremlin.process.traversal.step.util.BulkSet;[]
assert s.size() == 7;[]
assert s.flatten().size() == 4;[]
assert s.uniqueSize() == 4;[]
assert s.contains(6);[]
assert s.asBulk()[2] == 3;[]
assert s.asBulk()[3] == 2;[]

n=status( "Checking 'BulkSet' from 'aggregate'",n);[]
s=g.V().has('airport','country','IE').aggregate('ireland').cap('ireland').next();[]
assert s instanceof org.apache.tinkerpop.gremlin.process.traversal.step.util.BulkSet;[]
assert s.size() == 7;[]
assert s.uniqueSize() == 7;[]

n=status( "Checking 'BulkSet' using 'aggregate' with a 'by' modulator",n);[]
a=g.V().has('airport','country','IE').
      aggregate('ireland').by('runways').cap('ireland').next();[] 
assert a.size() == 7;[]
assert a.uniqueSize() == 3;[]
assert a.get(1) == 3;[]
assert a.get(2) == 3;[]
assert a.get(4) == 0;[]
assert a.get(5) == 1;[]

a=g.V().has('region','US-TX').aggregate('c').by('code').cap('c').next();[]
assert a.size() == 26;[]
assert a.uniqueSize() == 26;[]
assert a.sort()[0] == 'ABI';[]

;[] //-------------------------------------------------------------------------

n=status( "Checking 'dedup(local)'",n);[]
a=g.V().has('country','US').values('region').
      order().fold().dedup(local).next();[]
assert a.size() == 51;[]
assert a[1] == 'US-AL';[]
assert a[50] == 'US-WY';[]

;[] //-------------------------------------------------------------------------
n=status( "Checking property keys",n);[]
a=g.V().has('airport','code','DFW').properties().key().fold().next();[]
assert a.size() == 12;[]
assert a == ['country','code','longest','city','elev','icao','lon','type','region','runways','lat','desc'];[]

n=status( "Checking property cardinality of LIST",n);[]
g.addV('test').property('a',1).property('a',2).property('a',3).iterate();[]
g.V().hasLabel('test').property(list,'a',4).iterate();[]
a=g.V().hasLabel('test').valueMap().select('a').next();[]
assert a.getClass() == java.util.ArrayList;[]
assert a == [1,2,3,4];[]
g.V().hasLabel('test').drop().iterate();[]

n=status( "Checking property cardinality of SET",n);[]
g.addV('test').property(set,'s',1).iterate();[]
g.V().hasLabel('test').property(set,'s',2).property(set,'s',1).iterate();[]
g.V().hasLabel('test').property(set,'s',2).property(set,'s',4).iterate();[]
s=g.V().hasLabel('test').valueMap().select('s').next();[]
assert s.sort() == [1,2,4];[]
g.V().hasLabel('test').drop().iterate();[]

n=status( "Checking meta properties",n);[]
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
n=status( "Checking vertex and edge creation",n);[]
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

n=status( "Checking vertex label creation using a traversal",n);[]
v=g.addV(V().has('code','AUS').label()).property('code','XYZ').next();[]
assert v.label == 'airport';[] 
g.V(v).drop();

;[] //-------------------------------------------------------------------------
n=status( "Checking 'repeat until'",n);[]
c=g2.V().hasLabel('root').
         repeat(out('right')).
           until(out('right').count().is(0)).
         values('data').next();[]

assert c == 22;[]

n=status( "Checking 'repeat until not'",n);[]
c=g2.V().hasLabel('root').
         repeat(out('left')).
           until(__.not(out('left'))).
         values('data').next();[]

assert c == 1;[]

n=status( "Checking 'repeat until not' with 'path'",n);[]
a=g2.V().hasLabel('root').
         repeat(out('left')).
           until(__.not(out('left'))).
         path().by('data').next().toList();[]

assert a.flatten() == [9,5,2,1];[]

n=status( "Checking 'repeat' with 'unfold'",n);[]
a=g.V().has('airport','region','US-CA').
        order().by('city').limit(5).
        group().by('code').by('city').
        select(values).
        repeat(unfold()).
          until(count(local).is(1)).
        unfold().toList();[]

assert a[1] == 'Bakersfield';[]
assert a[4] == 'Burbank';[]

n=status( "Checking 'repeat' with 'emit()'",n);[]
p= g.V().has('airport','code','AUS').
      repeat(out()).emit().times(3).has('code','MIA').
      limit(5).path().by('code').next();[]


n=status( "Checking 'repeat' with 'emit(has(...))'",n);[]
c=g.V(3).repeat(out()).times(3).
         emit(has('region','GB-ENG')).
         has('region','GB-ENG').
         path().by('city').count().next();[]

assert c == 8502;[]

assert p instanceof org.apache.tinkerpop.gremlin.process.traversal.step.util.MutablePath;[]
assert p.isSimple();[]
assert p[0] == 'AUS';[]
assert p.head() == 'MIA';[]
assert p.size() == 2;[]

;[] //-------------------------------------------------------------------------
n=status( "Checking vertex and implicit edge deletion",n);[]

g2.V().has('data',15).drop().iterate();[]
cv = g2.V().count().next();[]
assert cv == 9;[]

ce = g2.E().count().next();[]
assert ce == 7;[]                    


;[] //-------------------------------------------------------------------------
n=status( "Checking 'sack' with 'assign' using a 'constant'",n);[]
a=g.V().sack(assign).by(constant(1)).
        has('code','SAF').
        out().values('runways').
        sack(sum).sack().fold().next();[]
assert a.flatten() == [8,5,4,7];[]

n=status( "Checking 'sack' with 'assign' using a property value'",n);[]
a=g.V().has('code','AUS').
        sack(assign).by('runways'). 
    V().has('code','SAF').
        out().
        sack(mult).by('runways').
        sack().fold().next();[]
assert a.flatten() == [14,8,6,12];[]

n=status( "Checking 'sack' with 'addAll'",n);[]
a=g.withSack([]).
    V().has('code','SAF').
        out().values('runways').fold().
        sack(addAll).sack().next();[]
assert a.flatten() == [7,4,3,6];[]

n=status( "Checking 'sack' with zero base and 'sum'",n);[]
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

n=status( "Checking 'sack' with 'assign' using a 'constant' and 'min'",n);[]
a=g.V().sack(assign).by(constant(400)).has('code','SAF').
      outE().sack(min).by('dist').sack().fold().next();[]
assert a.size == 4;[]
assert a == [400,400,369,303];[]

n=status( "Checking 'sack' with 'assign' using a 'constant' and 'max'",n);[]
a=g.V().sack(assign).by(constant(400)).has('code','SAF').
      outE().sack(max).by('dist').sack().fold().next();[]
assert a.size == 4;[]
assert a ==[549,708,400,400];[]

;[] //-------------------------------------------------------------------------
n=status( "Checking side effects and lambdas",n);[]
c = 0;[]
n=status( "Checking 'filter { c += 1 }'",n);[]
g.V().has('region','US-OR').sideEffect{ c += 1 }.values('code').fold().next();[]
assert c == 7;[]

n=status( "Checking 'filter with property check in lambda'",n);[]
c=g.V().hasLabel('airport').
        filter{it.get().property('city').value() =="London"}.
        count().next();[]
assert c == 6;[]      

n=status( "Checking 'filter with a 'contains()' in a lambda'",n);[]
c=g.V().hasLabel('airport').as('a').values('desc').
       filter{it.toString().contains('F.')}.select('a').
       local(values('code','desc').fold()).count().next();[]
assert c == 4;[]

n=status( "Checking 'filter with regex in a lambda'",n);[]
c=g.V().has('airport','type','airport').
        filter{it.get().property('city').value ==~/Dallas|Austin/}.
        values('code').count().next();[]
assert c == 3;[]

n=status( "Checking 'filter with more complex regex in a lambda'",n);[]
c=g.V().has('airport','type','airport').
        filter{it.get().property('city').value()==~/^Dal\w*/}.
        values('city').count().next();[]
assert c == 7;[]

n=status( "Checking 'map' and a lambda",n);[]
a=g.V().has('airport','region','GB-ENG').
        map{it.get().value('code')+" "+it.get().value('city')}.toList();[]

assert a.sort()[0] == 'BHX Birmingham';[]

n=status( "Checking 'map' and a multi statement lambda",n);[]
a=g.V().hasLabel('airport').limit(10).
      map{a=1;b=2;c=a+b;it.get().id() + c}.fold().next();[]

assert a == [4,5,6,7,8,9,10,11,12,13];[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'map' containing a traversal",n);[]
a=g.V().hasLabel('airport').has('region','US-NM').
      order().by('code'). 
      map(properties('city').group().by(key()).by(value())).toList();[]

assert a.size() == 9;[]
assert a[0]['city'] == 'Albuquerque';[]
assert a[5]['city'] == 'Los Alamos';[]

;[] //-------------------------------------------------------------------------
n=status( "Checking custom predicates",n);[]
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
n=status( "Checking 'tree' of routes from AUS",n);[]
tree=g.V().has('code','AUS').
           repeat(out()).
             times(2).tree().by('code').next();[]

assert tree instanceof org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree;[]
items = tree.getObjectsAtDepth(1).toList();[]
assert items.size() == 1;[]
items = tree.getObjectsAtDepth(2).toList();[]
assert items.size() == 59;[]
items = tree.getObjectsAtDepth(3).toList();[]
assert items.size() == 5942;[]
assert tree['AUS']['DFW'].size() == 221;[]

;[] //-------------------------------------------------------------------------
n=status( "Checking 'subgraph' of first 46 airports.",n);[]

subg=g.V(1..46).outE().
       filter(inV().hasId(within(1L..46L))).
       subgraph('a').cap('a').next();[]

sgt = subg.traversal();[]

assert sgt.E().count().next() == 1326;[]
assert sgt.V().count().next() == 46;[]
c = sgt.V().has('code','LAX').out().count().next();[]
assert c == 40;[]


n=status( "Checking 'subgraph' of European airports.",n);[]
subg = g.V().hasLabel('continent').has('code','EU').
             outE('contains').subgraph('eu-air-routes').inV().as('a').
             inE('contains').subgraph('eu-air-routes').
             outV().hasLabel('country').
             select('a').outE().as('r').
             inV().hasLabel('airport').in().hasLabel('continent').
             has('code','EU').select('r').subgraph('eu-air-routes').
             cap('eu-air-routes').next();[]

sgt = subg.traversal();[]

assert sgt.E().hasLabel('route').count().next() == 12499;[]  
assert sgt.E().count().next() == 13665;[]
assert sgt.V().count().next() == 630;[]
m = sgt.V().groupCount().by(label).next();[]
assert m['airport'] == 583;[]
assert m['country'] == 46;[]
assert m['continent'] == 1;[]


;[] //-------------------------------------------------------------------------
n=status( "Checking classes and types",n);[]
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
n=status( "Checking user defined Groovy methods",n);[]
def dist(g,from,to) {
  d=g.V().has('code',from).outE().as('a').inV().has('code',to)
         .select('a').values('dist').next()
  return d };[]
assert dist(g,'AUS','LHR') ==  4901;[]



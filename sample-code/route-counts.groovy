// Explore the distribution of routes in the graph
// This code is intended to be :load-ed and run inside the Gremlin console

EQ  = [0,1,2,3,4,5,10];[]
LTE = [5,10,15,20,25,30,40,50,75,100,150,200,400];[]
GTE = [5,10,15,20,25,30,35,40,50,75,100,125,150,200,250,300];[]
BTW = [0,1,0,2,0,5,0,10,1,2,1,5,1,10,1,25,1,50,1,100,5,10,10,15,10,20,20,25,20,30,25,50,
       30,50,50,100,100,150,100,175,100,200,150,200,175,200,200,250,200,300,250,300];[]

for (n in EQ)
{
  print "EQ $n : " +
  g.V().hasLabel('airport').where(out('route').count().is(eq(n))).count().next() + "\n";[]
//  printf( "EQ %4d : %4d\n",n, 
//  g.V().hasLabel('airport').where(out('route').count().is(eq(n))).count().next());[]

};[]

for (n in LTE)
{
  print "LTE $n : " +
  g.V().hasLabel('airport').where(out('route').count().is(lte(n))).count().next() + "\n";[]

};[]

for (n in GTE)
{
  print "GTE $n : " +
  g.V().hasLabel('airport').where(out('route').count().is(gte(n))).count().next() + "\n";[]

};[]

for (i=0; i < BTW.size; i+=2)
{
  a = BTW[i]; b = BTW[i+1];
  print "BTW $a,$b : " +
  g.V().hasLabel('airport').where(out('route').count().is(between(a,b+1))).count().next() + "\n";[]
};[]

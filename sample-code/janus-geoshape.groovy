// Script that experiments with the JanusGraph GeoSpatial API
//
// This script is intended to be run from within the Gremlin Console
// attached to a JanusGraph instance with the air-routes data loaded.

// Create a 100km radius circle with LHR at the center

lon_lhr = g.V().has('code','LHR').values('lon').next()
lat_lhr = g.V().has('code','LHR').values('lat').next()

lhr_circ = Geoshape.circle(lat_lhr,lon_lhr,100)

// Create a 100km radius circle with MAN at the center

lat_man = g.V().has('code','MAN').values('lat').next()
lon_man = g.V().has('code','MAN').values('lon').next()

man_circ = Geoshape.circle(lat_man,lon_man,100)

// Find other airports that within 100km of LHR
g.V().has('airport','region','GB-ENG').
      where(map{a=it.get().value('lat');
                b=it.get().value('lon');
                Geoshape.point(a,b).within(lhr_circ)}.is(true)).
                valueMap('code','lat','lon')  

// Do any points in the two LHR and MAN circles intersect?

lhr_circ.intersect(man_circ)

// Create a 100km radius circle with LPL at the center

lat_lpl = g.V().has('code','LPL').values('lat').next()
lon_lpl = g.V().has('code','LPL').values('lon').next()

lpl_circ = Geoshape.circle(lat_lpl,lon_lpl,100)


// Do any points in the MAN and LPL circles intersect
lpl_circ.intersect(man_circ)

// Define a box around LHR with opposite diagonal corners 
// each one degree from LHR.
box = Geoshape.box(lat_lhr-1,lon_lhr-1,lat_lhr+1,lon_lhr+1)

// Find other airports that within the box
g.V().has('airport','region','GB-ENG').
      where(map{a=it.get().value('lat');
                b=it.get().value('lon');
                Geoshape.point(a,b).within(box)}.is(true)).
                valueMap('code','lat','lon')   




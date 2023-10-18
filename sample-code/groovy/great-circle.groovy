start = 'SFO'
stop = 'NRT'

g.withSideEffect("rdeg", 0.017453293).
        withSideEffect("gcmiles", 3956).
        V().has('code', start).as('src').
        V().has('code', stop).as('dst').
        select('src', 'dst').
        by(project('lat', 'lon').
                by('lat').
                by('lon')).
        as('grp').
        project('ladiff', 'lgdiff', 'lat1', 'lon1', 'lat2', 'lon2').
        by(project('la1', 'la2').
                by(select('grp').select('src').select('lat')).
                by(select('grp').select('dst').select('lat')).
                math('(la2 - la1) * rdeg')).
        by(project('lg1', 'lg2').
                by(select('grp').select('src').select('lon')).
                by(select('grp').select('dst').select('lon')).
                math('(lg2 - lg1) * rdeg')).
        by(select('grp').select('src').select('lat')).
        by(select('grp').select('src').select('lon')).
        by(select('grp').select('dst').select('lat')).
        by(select('grp').select('dst').select('lon')).
        math('(sin(ladiff/2))^2 + cos(lat1*rdeg) * cos(lat2*rdeg) * (sin(lgdiff/2))^2').
        math('gcmiles * (2 * asin(sqrt(_)))')

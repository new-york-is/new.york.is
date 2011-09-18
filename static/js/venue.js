var central_park = '40.77924157014336,-73.96734237670898';
var times_square =  '40.756489872673846,-73.98626804351807';
var world_trade = '40.7114673282201,-74.01328325271606';
var union_square = '40.735226248609976, -73.99068832397461';

$(document).ready(function() {
        printCategoryStats();
        setInterval(printCategoryStats, 10000);   	
    });

function printCategoryStats(){
    $.when(getVenue(times_square),getVenue(central_park),getVenue(world_trade), getVenue(union_square)).done(function(result1,  result2, result3, result4){
            var results = _.union(result1[0].response.venues, result2[0].response.venues, result3[0].response.venues, result4[0].response.venues);
            groupCategories(results);
        })
    .fail(function(){
            console.log('Failed getting trends.' );
        });
}

function groupCategories(results){

    var categoryMap = new Object();
    var sortedCategoryMap = new Object();

    $.each(results, function(index, venue) { 
            var category = venue.categories[0].parents[0];
            var checkinCount = venue.hereNow.count;

            if (categoryMap[category] == null){categoryMap[category] = checkinCount;}
            else {categoryMap[category] += checkinCount;}	 
        });
    var categoryArray = _.map(categoryMap,function(v,k){var temp = {};temp[k] = v;return temp;})
    categoryArray.sort(function(a,b){return _.values(a)[0] < _.values(b)[0]});

    var container = $('#categoryContainer');
    for(key in categoryArray){
        var categoryName =_.keys(categoryArray[key])[0];
        var checkinsForCategory = _.values(categoryArray[key])[0];
        var event = new CategoryEvent({category:categoryName, checkins:checkinsForCategory});
        window.events.add(event);
        //container.append('<li>New York is ' + _.keys(categoryArray[key])[0] + ' '+ _.values(categoryArray[key])[0] + ' </li>');
    }	


    /*		var categories = _.uniq(_.flatten(_.pluck(_.flatten(_.pluck(results,"categories")),"parents")));	
     var groupedCategories = _.groupBy(_.flatten(_.pluck(_.flatten(categories),"parents")),function(s){ return s;});
     var sortedCategories = _.sortBy(groupedCategories, function(num){ return -num.length; }) ;
     $.each(sortedCategories, function(index, value) { 
     container.append('<li>New York is ' + value[0] + ' '+ value.length + ' </li>');
 });*/

}

function getVenue(latlong){
    return $.ajax({
            url: 'https://api.foursquare.com/v2/venues/trending?ll='+latlong+'&radius=500&client_id=L2Y45KQZ5ZC1N2QCZI11ZOOZPPXO4SSKK2GLKU2WKX2FXDUI&client_secret=A12OXNUK0RO3HYKDTH51LOI002DDYEGBFNV3I40BEI5IWI4G',
            crossDomain:true, 
            dataType: 'jsonp'
        });
}




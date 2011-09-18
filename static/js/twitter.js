var twitterapiurl = "http://search.twitter.com/search.json?q=foursquare&rpp=100&callback=?";

$.getJSON(twitterapiurl, function(ob) {
    console.log(ob)
});


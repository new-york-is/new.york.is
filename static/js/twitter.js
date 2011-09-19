(function($,window,undefined){
        
    var twitterUrl = "http://search.twitter.com/search.json?q=SEARCH_TERM&rpp=100&callback=?";
    var getUrlForSearchTerm = function(searchTerm){
        return twitterUrl.replace("SEARCH_TERM",searchTerm);
    }

    var addTextEvent = function(text,highPriority){
        highPriority = highPriority || false;
        var event = new Event({name:text, highPriority:highPriority});
        window.events.add(event);
    };
    
    var alreadyDisplayedTweets = {};
    setInterval(function(){
            $.getJSON(getUrlForSearchTerm(escape("#newyorkis")))
        .success(function(obj){
            var texts = _.pluck(obj.results,"text");    
            _.each(texts, function(text){
                    if(typeof alreadyDisplayedTweets[text] == "undefined"){
                        alreadyDisplayedTweets[text] = true;
                        addTextEvent(text, true);
                    }
                });
        });
    },10000);
})(window.jQuery,window);




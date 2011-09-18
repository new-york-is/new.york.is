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

    $.getJSON(getUrlForSearchTerm("\"new york is\""))
    .success(function(obj){
        var texts = _.pluck(obj.results,"text");    
        var interval = setInterval(function(){
                if(texts.length > 0){
                    addTextEvent(texts.pop());
                }
                else{
                    clearInterval(interval);
                }
            }, 10000);
    });
    
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
    },700);
})(window.jQuery,window);




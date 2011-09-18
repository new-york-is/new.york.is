(function($,window,undefined){
        
    var twitterUrl = "http://search.twitter.com/search.json?q=SEARCH_TERM&rpp=100&callback=?";
    var getUrlForSearchTerm = function(searchTerm){
        return twitterUrl.replace("SEARCH_TERM",searchTerm);
    }

    $.getJSON(getUrlForSearchTerm("\"new york is\""))
    .success(function(obj){
        var texts = _.pluck(obj.results,"text");    
        var addTextEvent = function(text){
            console.log(text);
            var event = new Event({name:text});
            window.events.add(event);
        };
        var interval = setInterval(function(){
                if(texts.length > 0){
                    addTextEvent(texts.pop());
                }
                else{
                    clearInterval(interval);
                }
            }, 1000);
    })
})(window.jQuery,window);




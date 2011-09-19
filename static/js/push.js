(function($,window,undefined){        
  var alreadyDisplayed = [];
  setInterval(function(){
    $.getJSON("/api/push?secret=O1XQSYRINI3Y3P30",function(data){
      if(!_.contains(alreadyDisplayed, data.response[0].id)){
        alreadyDisplayed.push(data.response[0].id);
        var event = new VenuePushEvent({
          highPriority:true,
          firstName:data.response[0].firstName,
          userImage:data.response[0].photo
        });
        window.events.add(event);
      };
    };
  },1000);
})(window.jQuery,window);




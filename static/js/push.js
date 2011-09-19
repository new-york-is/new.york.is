(function($,window,undefined){
        
        var getNewVenuePush = function(){
           var users = [
               {
                   "firstName":"Christina",
                   "userImage":"https://playfoursquare.s3.amazonaws.com/userpix/5BRBSC0JE45O0GEX.jpg",
                   "highPriority":true
               },
               {
                   "firstName":"Paul",
                   "userImage":"https://playfoursquare.s3.amazonaws.com/userpix_thumbs/U2KBUTFLUEVTI4N4.jpg",
                   "highPriority":true
               },
               {
                   "firstName":"John",
                   "userImage":"https://playfoursquare.s3.amazonaws.com/userpix/FPJJCUBBOEEFQWFQ.jpg",
                   "highPriority":true
               },
               {
                   "firstName":"Jorge",
                   "userImage":"https://playfoursquare.s3.amazonaws.com/userpix/4a5e36cdcc36b.jpg",
                   "highPriority":true
               },
               {
                   "firstName":"Ben",
                   "userImage":"https://playfoursquare.s3.amazonaws.com/userpix/YPIJJHORR20P30HD.png",
                   "highPriority":true
               },
               {
                   "firstName":"Echo",
                   "userImage":"https://playfoursquare.s3.amazonaws.com/userpix/ABX4VUNHL3212GAN.jpg",
                   "highPriority":true
               },
               {
                   "firstName":"Joe",
                   "userImage":"https://playfoursquare.s3.amazonaws.com/userpix/BU0IJQDPMZ2JL3OQ.jpg",
                   "highPriority":true
               }              
           ];


            var random = Math.floor(Math.random()*users.length);
            return new VenuePushEvent(users[random]);
        }
        setInterval(function(){
                window.events.add(getNewVenuePush())
            },10000);

})(window.jQuery,window);



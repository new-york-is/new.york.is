var Event = Backbone.Model.extend({ 
        defaults:{
            name:"Test"
        }
    });

var CategoryEvent = Event.extend({
        defaults:{
            category:"testCategory",
            checkins:"1319"
        }
    });

var EventCollection = Backbone.Collection.extend({
        model: Event,
        initialize:function(){
            _.bindAll(this,"removeEvent");
        },
        add:function(event){
            var random = Math.floor(Math.random()*6000);
            var self = this;
            setTimeout(function(){
                        Backbone.Collection.prototype.add.call(self, event);
                },random);
            event.bind("animationFinished", this.removeEvent);
            return this;
        },
        removeEvent:function(event){
            this.remove(event);
            this.trigger("eventRemoved", event);
        }
    }); 

var EventView = Backbone.View.extend({
        tagName:"li",
        className:"event",
        events:{
            "mouseover":"handleMouseover",
            "mouseout":"handleMouseout"
        },
        initialize:function(){
            _.bindAll(this,"render","remove");
            this.model.bind("change",this.render);
            this.model.bind("animationFinished",this.remove);
            this.template = _.template($("#event-view").html());
        },
        render:function(height){
            var el = $(this.el),
                self = this,
                getRandomEasing = function(){
                    var easings = ["linear","easeOut","easeIn"];
                    var random = Math.floor(Math.random()*easings.length);
                    return easings[random];
                };
            el.html(this.template(this.model.toJSON()));

            
            var dimensions = this.getElDimensions();
            var scaleFactor = height/dimensions.height
            var easing = getRandomEasing();
            var startingLeft, finalLeft;

            el.children().eq(0).css("transform","scale("+scaleFactor+")");
            if(new Date().getTime() % 2 == 0){
                startingLeft = $(window).width()+200;
                finalLeft = -1*dimensions.width*scaleFactor-200;
            }else{
                startingLeft  = -1*dimensions.width*scaleFactor-200;
                finalLeft = $(window).width()+200;
            }
            this.startingLeft = startingLeft;
            this.finalLeft = finalLeft;
            el.css("left",startingLeft);
            el.animate({"left":finalLeft},12000,easing, function(){
                   self.model.trigger("animationFinished", self.model);
                });

            return this;
        },
        handleMouseover:function(){
            if($(this.el).is(':animated') ){
                $(this.el).stop();
            }
        },
        handleMouseout:function(){
            var self = this;
            if(!$(this.el).is(':animated')){
                $(this.el).animate({"left":this.finalLeft},7000,"linear",function(){
                    self.model.trigger("animationFinished", self.model);
                });
            }
        },
        getElDimensions:function(){
            var newEl = this.el.cloneNode(true);
            newEl.style.visibility="hidden";
            document.body.appendChild(newEl);
            var width = $(newEl).children(0).width();
            var height = $(newEl).children(0).height();
            newEl.parentNode.removeChild(newEl);
            return {width:width,height:height};
        },
        remove:function(){
            this.el.parentNode.removeChild(this.el);
        }
});

var CategoryEventView = EventView.extend({
        initialize:function(){
            EventView.prototype.initialize.apply(this,arguments);
            this.template = _.template($("#category-event-view").html());
        }
});

var RowView = Backbone.View.extend({
        tagName:"ul",
        initialize:function(){
            _.bindAll(this,"render","renderEvent",
                        "removedEvent","getNextAvailableSlot",
                        "scheduleToSlot");
            this.collection.bind("add",this.renderEvent);
            this.takenSlots = [false,false,false,false,false];
        },
        render:function(){
            this.collection.each(this.renderEvent); 
            return this;
        },
        getNextAvailableSlot:function(){
            return _.indexOf(this.takenSlots,false);
        },
        scheduleToSlot:function(event){
            var slot = this.getNextAvailableSlot();
            if(slot != -1){
                this.takenSlots[slot] = true;
                event.slot = slot;
                return slot;
            }
            else{
                return false;
            }
        },
        renderEvent:function(event){
            var slot = this.scheduleToSlot(event),
                viewClass,
                height,
                eventViewEl,
                eventView;

            if(slot === false){
                setTimeout(_.bind(this.renderEvent,this,event),3000);
                return;
            }

            if(event instanceof CategoryEvent){
                viewClass = CategoryEventView; 
            }else{
                viewClass = EventView; 
            } 

            eventView = new viewClass({
                    model:event
            }); 

            height = $(window).height()/5;
            eventViewEl = eventView.render(height).el
            $(eventViewEl).css("top",height*slot);

            $(this.el).append(eventViewEl);
            event.bind("animationFinished", this.removedEvent);
        },
        removedEvent:function(event){
            this.takenSlots[event.slot] = false;
        }
    });

var AppRouter = Backbone.Router.extend({
        routes: {
            "*actions": "defaultRoute" 
        },
        defaultRoute: function( actions ){
            window.events = window.collection = new EventCollection();
            for(var i =0;i<20;i++){
                event = new Event({name:"hello"});
                event2 = new Event({name:"echo"});
                event3 = new Event({name:"and"});
                event4 = new Event({name:"friend"});
                event5 = new Event({name:"world"});
                event6 = new Event({name:"aspartame"});
               // collection.add(event).add(event2).add(event3).add(event4).add(event5).add(event6)
            }
            view = new RowView({collection:collection});
            $("#container").append(view.render().el);
        }
    });

var app_router = new AppRouter;
Backbone.history.start();

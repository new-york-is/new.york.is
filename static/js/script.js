var Event = Backbone.Model.extend({ 
        defaults:{
            name:"Test"
        }
    });

var EventCollection = Backbone.Collection.extend({
        model: Event,
        initialize:function(){
            _.bindAll(this,"removeEvent");
        },
        add:function(event){
            Backbone.Collection.prototype.add.call(this, event);
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
        initialize:function(){
            _.bindAll(this,"render","remove");
            this.model.bind("change",this.render);
            this.model.bind("animationFinished",this.remove);
            this.template = _.template($("#event-view").html());
        },
        render:function(height){
            var el = $(this.el),
                self = this;
            el.html(this.template(this.model.toJSON()));

            el.css("left",$(window).width());
            
            var dimensions = this.getElDimensions();
            var scaleFactor = height/dimensions.height

            el.children().eq(0).css("transform","scale("+scaleFactor+")");

            el.animate({"left":-1*dimensions.width*scaleFactor-100},1000,"linear", function(){
               self.model.trigger("animationFinished", self.model);
            });

            return this;
        },
        getElDimensions:function(){
            var newEl = this.el.cloneNode(true);
            newEl.style.visibility="hidden";
            document.body.appendChild(newEl);
            var width = $(newEl).width();
            var height = $(newEl).height();
            newEl.parentNode.removeChild(newEl);
            return {width:width,height:height};
        },
        remove:function(){
            this.el.parentNode.removeChild(this.el);
        }
});

var RowView = Backbone.View.extend({
        tagName:"ul",
        initialize:function(){
            _.bindAll(this,"render","renderEvent",
                        "removedEvent","getNextAvailableSlot",
                        "scheduleToSlot");
            this.template = _.template($("#row-view").html());
            this.collection.bind("add",this.renderEvent);
            this.takenSlots = [false,false,false,false,false];
        },
        render:function(){
            $(this.el).html(this.template()); 
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
            var slot = this.scheduleToSlot(event);
            if(slot === false){
                setTimeout(_.bind(this.renderEvent,this,event),3000);
                return;
            }

            var eventView = new EventView({
                    model:event
            }); 
            var height = $(window).height()/5;
            var eventViewEl = eventView.render(height).el
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
            event = new Event({name:"hello"});
            event2 = new Event({name:"echo"});
            event3 = new Event({name:"and"});
            event4 = new Event({name:"friend"});
            event5 = new Event({name:"world"});
            event6 = new Event({name:"aspartame"});
            window.events = window.collection = new EventCollection();
            collection.add(event).add(event2).add(event3).add(event4).add(event5).add(event6)
            view = new RowView({collection:collection});
            $("#container").append(view.render().el);
        }
    });

var app_router = new AppRouter;
Backbone.history.start();

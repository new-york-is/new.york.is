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
        }
    }); 

var EventView = Backbone.View.extend({
        tagName:"li",
        className:"event",
        initialize:function(){
            _.bindAll(this,"render");
            this.model.bind("change",this.render);
            this.template = _.template($("#event-view").html());
        },
        render:function(){
            var el = $(this.el),
                self = this;
            el.html(this.template(this.model.toJSON()));

            el.css("left",$(window).width());
            el.css("top",100);
            
            var elWidth = this.getElWidth();
            el.animate({"left":-elWidth},4000,"easeInOutSine", function(){
               self.model.trigger("animationFinished");
            });

            return this;
        },
        getElWidth:function(){
            var newEl = this.el.cloneNode(true);
            newEl.style.visibility="hidden";
            document.body.appendChild(newEl);
            var width = $(newEl).width();
            newEl.parentNode.removeChild(newEl);
            return width;
        }
});

var RowView = Backbone.View.extend({
        tagName:"ul",
        initialize:function(){
            _.bindAll(this,"render","renderEvent");
            this.template = _.template($("#row-view").html());
            this.collection.bind("add",this.renderEvent);
        },
        render:function(){
            $(this.el).html(this.template()); 
            this.collection.each(this.renderEvent); 
            return this;
        },
        renderEvent:function(event){
           var eventView = new EventView({
                    model:event
            }); 
            $(this.el).append(eventView.render().el);
        }
    });

var AppRouter = Backbone.Router.extend({
        routes: {
            "*actions": "defaultRoute" 
        },
        defaultRoute: function( actions ){
            event = new Event({name:"hello"});
            event2 = new Event({name:"world"});
            collection = new EventCollection();
            collection.add(event).add(event2);
            view = new RowView({collection:collection});
            $("#container").append(view.render().el);
        }
    });

var app_router = new AppRouter;
Backbone.history.start();

var Event = Backbone.Model.extend({ 
        defaults:{
            name:"Test"
        }
    });

var EventView = Backbone.View.extend({
        className:"event",
        initialize:function(){
            _.bindAll(this,"render");
            this.model.bind("change",this.render);
            this.template = _.template($("#event-view").html());
        },
        render:function(){
            var getDropHeight = function(){
                return $(window).height()+el.height();
            };
            var getRandomInRangeOfHeight = function(){
                Math.floor(Math.random() * $(window).height()+$(this.el).height());
            }


            var el = $(this.el);
            el.html(this.template(this.model.toJSON()));
            el.css("left",$(window).width());
            var randomHeightInViewport = getRandomInRangeOfHeight();
            el.css("top",$(window).height()-randomHeightInViewport);
            el.animate({"left":-2*$(this.el).width()},2000,function(){
                    el.animate({"top":"+="+getDropHeight()}, 2000);
            });

            return this;
        }
});

var AppRouter = Backbone.Router.extend({
        routes: {
            "*actions": "defaultRoute" 
        },
        defaultRoute: function( actions ){
            event = new Event();
            view = new EventView({model:event});
            $("#container").append(view.render().el);
        }
    });

var app_router = new AppRouter;
Backbone.history.start();

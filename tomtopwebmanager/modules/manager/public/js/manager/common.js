require.config({
    baseUrl: '/assets/js',
    paths: {
        // the left side is the module ID,
        // the right side is the path to
        // the jQuery file, relative to baseUrl.
        // Also, the path should NOT include
        // the '.js' file extension. This example
        // is using jquery-1.11.2.min.js located at
        // js/lib/jquery-1.11.2.min.js, relative to
        // the HTML page.
        jquery: './jquery-1.10.1.min',
        jqueryjson: '../js/jquery.json.min',
        jvalidate: '../js/jquery.validate.min',
        jmetadata: '../js/jquery.metadata',
        jqueryform: '../js/jquery.form',
        app: './manager'
    },
    shim:{
    	"jqueryjson":{
    		deps:["jquery"],
    		exports:"jqueryjson"
    	},
    	"jvalidate":{
    		deps:["jquery"],
    		exports:"jvalidate"
    	},
    	"jmetadata":{
    		deps:["jquery","jvalidate"],
    		exports:"jmetadata"
    	}
    }

});


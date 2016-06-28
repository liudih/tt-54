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
        jquery: '../lib/jquery/jquery.min',
        jqueryjson: '../js/lib/jquery.json.min',
        app: './product'
    },
    shim:{
    	"jqueryjson":{
    		deps:["jquery"],
    		exports:"jqueryjson"
    	}
    }

});
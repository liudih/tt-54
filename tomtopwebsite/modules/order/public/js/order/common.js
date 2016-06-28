require.config({
    baseUrl: 'assets/js',
    paths: {
        jquery: '../lib/jquery/jquery.min',
        jqueryjson: '../js/lib/jquery.json.min',
        app: './order'
    },
    shim:{
    	"jqueryjson":{
    		deps:["jquery"],
    		exports:"jqueryjson"
    	}
    }

});
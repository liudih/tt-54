//Load common code that includes config, then load the app logic for this page.
require(['./common'], function (common) {
	require(['./app/category/category']);
	require(['./app/attribute/attribute']);
	require(['./app/category/category_attribute']);
	require(['./app/uploadfile/uploadfile']);
});
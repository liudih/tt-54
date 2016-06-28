//Load common code that includes config, then load the app logic for this page.
require(['./common'], function (common) {
    require(['./app/controller/category_product']);
    require(['./app/controller/detail']);
});
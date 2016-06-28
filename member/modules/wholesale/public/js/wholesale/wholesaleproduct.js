//Load common code that includes config, then load the app logic for this page.
require(['../product/common'], function (common) {
    require(['./wholesale/controller/wholesaleproduct']);
});
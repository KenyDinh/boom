(function() {
	var body = document.body, html = document.documentElement;
	var nodesjs = new NodesJs({
        id: 'canvas',
        width: body.scrollWidth,
        height: Math.max(body.scrollHeight, body.offsetHeight, html.clientHeight, html.scrollHeight, html.offsetHeight),
        backgroundFrom: [3, 3, 3],
        backgroundTo: [25, 25, 25],
        backgroundDuration: 4000,
        nobg: false,
        number: window.hasOwnProperty('orientation') ? 30: 100,
        speed: 20,
        pointerCircleRadius: 150
    });
	window.addEventListener('resize', function () {
		nodesjs.setWidth(body.scrollWidth);
		nodesjs.setHeight(Math.max(body.scrollHeight, body.offsetHeight, html.clientHeight, html.scrollHeight, html.offsetHeight));
	});
})();
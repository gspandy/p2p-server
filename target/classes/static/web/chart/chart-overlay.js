(function($) {
	var $window = this;
	var ChartWithOverlay = function(ctx, doughnutData, options) {
		var o = new Overlay(ctx);
		new Chart(ctx.getContext("2d")).Doughnut(doughnutData, options);
		o.create();
		o.fill(options);
		return this;
	};

	var Overlay = function(obj) {
		this.parentCanvas = obj;
		this.canvas = $(obj).clone();
	};

	Overlay.prototype.create = function() {
//		var left = $(this.parentCanvas).offset().left;
//		this.canvas = $(this.parentCanvas).clone();
//		alert(left);
		var canvas = $(this.canvas);
		var parent = $(this.parentCanvas).offset();
		canvas.attr("style", "position:absolute;");
		canvas.css("top", parent.top);
		canvas.css("left", parent.left);
		var id = new Date().getTime();
		this.canvasId = id;
		canvas.attr("id", id);
		$("body").append(canvas);
	};
	Overlay.prototype.fill = function(options) {
		if (options && options.innerText) {
			var canvas = document.getElementById(this.canvasId).getContext("2d");
			canvas.font = options.innerText.font;
			var texts = options.innerText.text;
			for(var i=0;i<texts.length;i++) {
				var t = texts[i];
				canvas.fillText(t.content,t.position.x, t.position.y);
			}
		}
	};

	$window.ChartWithOverlay = ChartWithOverlay;
}).call(this, jQuery);
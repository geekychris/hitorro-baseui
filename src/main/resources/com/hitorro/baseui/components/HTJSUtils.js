function HtSpider(spec) {
    holder = spec.canvas;
    width = spec.width;
    height = spec.height;
    radius = spec.radius;
    dataPoints = spec.points;
    maxValue = spec.maxValue;
    points = dataPoints.length;
    valNames = spec.names;
    var color = "#F00";
    var paper = Raphael(holder, width, height);
    paper.clear();
    paper.rect(0, 0, width, height, 10).attr({fill: "#fff", stroke: "none"});

    var deg = 360 / points;
    var xC = width / 2;
    var yC = height / 2;


    // draw the spokes
    var p = this.drawSpokes(points, deg, radius, xC, yC, paper, dataPoints);
    this.drawRings(paper, radius, xC, yC, maxValue);
    for (p = 0; p < valNames.length; p++) {
        // do each entity
        var col = valNames[p].color;
        var plotMe = this.getCoords(points, deg, radius, xC, yC, valNames[p].value, maxValue);
        var pathString = plotMe.join(",");
        //.attr({fill:"#060", opacity:".5"});
        var shapeTitle = valNames[p].name;
        paper.path(pathString).attr({stroke: col, fill: col, opacity: ".2", title: shapeTitle, stroke: "#000"});
    }
}


HtSpider.prototype.ra_de = function (radians) {
    return ((radians) * (180 / Math.PI));
};


HtSpider.prototype.de_ra = function (degrees) {
    return ((degrees) * (Math.PI / 180));
},

    HtSpider.prototype.drawCircles = function (holder, width, height) {
        var paper = Raphael(holder, width, height);

        paper.clear();
        paper.rect(0, 0, width, height, 10).attr({fill: "#fff", stroke: "none"});
        var circ = paper.circle(width / 2, height / 2, 60);
        circ.animate({fill: "#223fa3", stroke: "#000", "stroke-width": 80, "stroke-opacity": 0.5}, 2000);
    },

    HtSpider.prototype.getCoords = function (points, deg, radius, xC, yC, scale, maxValue) {
        var p = [];
        var startX;
        var startY;
        for (i = 1; i <= points; i++) {
            var newRadius = Math.round(radius * (scale[i - 1] / maxValue))
            var ang = deg * i;
            var x = Math.round(newRadius * Math.cos(this.de_ra(ang))) + xC;
            var y = Math.round(newRadius * Math.sin(this.de_ra(ang))) + yC;
            if (i == 1) {
                p = p.concat(["M", x, y]);
                startX = x;
                startY = y;
            } else {
                p = p.concat(["L", x, y]);
            }

        }
        p = p.concat(["L", startX, startY]);
        p.concat(["C"]);
        return p;
    },

    HtSpider.prototype.drawRings = function (paper, radius, xC, yC, scaleCount) {
        for (var i = 1; i <= scaleCount; i++) {
            var thisRad = Math.round(radius * (i / scaleCount));
            paper.circle(xC, yC, thisRad).attr({stroke: "#777", "stroke-width": 1});
        }
    },

    HtSpider.prototype.drawSpokes = function (points, deg, radius, xC, yC, paper, data) {
        var p = [];
        for (i = 1; i <= points; i++) {
            var ang = deg * i;
            var x = Math.round(radius * Math.cos(this.de_ra(ang))) + xC;
            var y = Math.round(radius * Math.sin(this.de_ra(ang))) + yC;
            p = p.concat(["M", xC, yC, "L", x, y]);

            // plot the labels for the data series
            x = Math.round((radius * 1.1) * Math.cos(this.de_ra(ang))) + xC;
            y = Math.round((radius * 1.1) * Math.sin(this.de_ra(ang))) + yC;
            paper.text(x, y, data[i - 1].name);
        }
        paper.path(p.join(",")).attr({stroke: "#777", "stroke-width": 1});
        return p;
    };

(function ($) {

    T5.extendInitializers(function () {
        function init(specs) {
            if (specs.type == 'radial') {
                initRadial(specs);
            } else {
                initExpander(specs);
            }
        }

        function initExpander(specs) {
            var jsonauth = specs.jsonauth;
            var width = specs.width;
            var height = specs.height;
            var idElem = specs.id;
            var m = [20, 120, 20, 120], w = width - m[1] - m[3], h = height - m[0] - m[2], i = 0, duration = 500, root;

            var tree = d3.layout.tree()
                .size([h, w]);

            var diagonal = d3.svg.diagonal()
                .projection(function (d) {
                    return [d.y, d.x];
                });

            var vis = d3.select("#" + idElem).append("svg")
                .attr("width", w + m[1] + m[3])
                .attr("height", h + m[0] + m[2])
                .append("g")
                .attr("transform", "translate(" + m[3] + "," + m[0] + ")");

            d3.json(jsonauth, function (json) {
                root = json;
                root.x0 = h / 2;
                root.y0 = 0;

                function collapse(d) {
                    if (d.children) {
                        d._children = d.children;
                        d._children.forEach(collapse);
                        d.children = null;
                    }
                }

                root.children.forEach(collapse);
                update(root);
            });

            function update(source) {

                // Compute the new tree layout.
                var nodes = tree.nodes(root).reverse();

                // Normalize for fixed-depth.
                nodes.forEach(function (d) {
                    d.y = d.depth * 180;
                });

                // Update the nodes…
                var node = vis.selectAll("g.node")
                    .data(nodes, function (d) {
                        return d.id || (d.id = ++i);
                    });

                // Enter any new nodes at the parent's previous position.
                var nodeEnter = node.enter().append("g")
                    .attr("class", "node")
                    .attr("transform", function (d) {
                        return "translate(" + source.y0 + "," + source.x0 + ")";
                    })
                    .on("click", click);

                nodeEnter.append("circle")
                    .attr("r", 1e-6)
                    .style("fill", function (d) {
                        return d._children ? "lightsteelblue" : "#fff";
                    });

                nodeEnter.append("text")
                    .attr("x", function (d) {
                        return d.children || d._children ? -10 : 10;
                    })
                    .attr("dy", ".35em")
                    .attr("text-anchor", function (d) {
                        return d.children || d._children ? "end" : "start";
                    })
                    .text(function (d) {
                        return d.name;
                    })
                    .style("fill-opacity", 1e-6);

                // Transition nodes to their new position.
                var nodeUpdate = node.transition()
                    .duration(duration)
                    .attr("transform", function (d) {
                        return "translate(" + d.y + "," + d.x + ")";
                    });

                nodeUpdate.select("circle")
                    .attr("r", 4.5)
                    .style("fill", function (d) {
                        return d._children ? "lightsteelblue" : "#fff";
                    });

                nodeUpdate.select("text")
                    .style("fill-opacity", 1);

                // Transition exiting nodes to the parent's new position.
                var nodeExit = node.exit().transition()
                    .duration(duration)
                    .attr("transform", function (d) {
                        return "translate(" + source.y + "," + source.x + ")";
                    })
                    .remove();

                nodeExit.select("circle")
                    .attr("r", 1e-6);

                nodeExit.select("text")
                    .style("fill-opacity", 1e-6);

                // Update the links…
                var link = vis.selectAll("path.link")
                    .data(tree.links(nodes), function (d) {
                        return d.target.id;
                    });

                // Enter any new links at the parent's previous position.
                link.enter().insert("path", "g")
                    .attr("class", "link")
                    .attr("d", function (d) {
                        var o = {x: source.x0, y: source.y0};
                        return diagonal({source: o, target: o});
                    })
                    .transition()
                    .duration(duration)
                    .attr("d", diagonal);

                // Transition links to their new position.
                link.transition()
                    .duration(duration)
                    .attr("d", diagonal);

                // Transition exiting nodes to the parent's new position.
                link.exit().transition()
                    .duration(duration)
                    .attr("d", function (d) {
                        var o = {x: source.x, y: source.y};
                        return diagonal({source: o, target: o});
                    })
                    .remove();

                // Stash the old positions for transition.
                nodes.forEach(function (d) {
                    d.x0 = d.x;
                    d.y0 = d.y;
                });
            }

// Toggle children on click.
            function click(d) {
                if (d.children) {
                    d._children = d.children;
                    d.children = null;
                } else {
                    d.children = d._children;
                    d._children = null;
                }
                update(d);
            }
        }

        function initRadial(specs) {
            //d3.select("#chart").src = "";
            var width = specs.width;
            var jsonauth = specs.jsonauth;
            var idElem = specs.id;
            var r = width / 2;

            var tree = d3.layout.tree()
                .size([360, r - 120])
                .separation(function (a, b) {
                    return (a.parent == b.parent ? 1 : 2) / a.depth;
                });

            var diagonal = d3.svg.diagonal.radial()
                .projection(function (d) {
                    return [d.y, d.x / 180 * Math.PI];
                });

            var vis = d3.select("#" + idElem).append("svg:svg")
                .attr("width", r * 4)
                .attr("height", r * 2 - 150)
                .append("svg:g")
                .attr("transform", "translate(" + r + "," + r + ")");

            d3.json(jsonauth, function (json) {
                var nodes = tree.nodes(json);

                var link = vis.selectAll("path.link")
                    .data(tree.links(nodes))
                    .enter().append("svg:path")
                    .attr("class", "link")
                    .attr("d", diagonal);

                var node = vis.selectAll("g.node")
                    .data(nodes)
                    .enter().append("svg:g")
                    .attr("class", "node")
                    .attr("transform", function (d) {
                        return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")";
                    })

                node.append("svg:circle")
                    .attr("r", 4.5);

                node.append("svg:text")
                    .attr("dx", function (d) {
                        return d.x < 180 ? 8 : -8;
                    })
                    .attr("dy", ".31em")
                    .attr("text-anchor", function (d) {
                        return d.x < 180 ? "start" : "end";
                    })
                    .attr("transform", function (d) {
                        return d.x < 180 ? null : "rotate(180)";
                    })
                    .text(function (d) {
                        return d.name;
                    });
            });
        }

        return {
            d3Node: init
        }
    });

})(jQuery);